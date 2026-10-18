// File: app/src/main/java/au/edu/jcu/assessment/utilityapp/data/repository/HabitRepository.kt
package au.edu.jcu.assessment.utilityapp.data.repository

import android.util.Log
import au.edu.jcu.assessment.utilityapp.data.model.Habit
import au.edu.jcu.assessment.utilityapp.data.model.Quote
import au.edu.jcu.assessment.utilityapp.data.network.RetrofitInstance
import au.edu.jcu.assessment.utilityapp.data.network.ZenQuotesApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Repository pattern: Coordinates in-memory habit state and remote quote data.
 * Acts as the single source of truth for the ViewModel.
 */
class HabitRepository(
    private val apiService: ZenQuotesApiService = RetrofitInstance.api
) {
    companion object {
        private const val TAG = "HabitRepository"
    }

    // --- In-Memory Habit State ---
    private val _habitState = MutableStateFlow(Habit())
    val habitState: StateFlow<Habit> = _habitState.asStateFlow()

    // Cache of live quotes successfully retrieved from the API this session
    private val liveQuotesCache = mutableListOf<Quote>()

    // Rotating fallback quote index for offline / rate-limited mode
    private var fallbackIndex = 0
    private val fallbackQuotes = listOf(
        Quote("Small daily improvements over time lead to stunning results.", "Robin Sharma"),
        Quote("The secret of getting ahead is getting started.", "Mark Twain"),
        Quote("We are what we repeatedly do. Excellence, then, is not an act, but a habit.", "Will Durant"),
        Quote("Action is the foundational key to all success.", "Pablo Picasso"),
        Quote("Focus on being productive instead of busy.", "Tim Ferriss"),
        Quote("It does not matter how slowly you go as long as you do not stop.", "Confucius")
    )

    /**
     * Toggles the completion state for today and updates the streak counter accordingly.
     */
    fun toggleCompletion() {
        val current = _habitState.value
        val willBeCompleted = !current.isCompletedToday
        val updatedStreak = if (willBeCompleted) {
            current.currentStreak + 1
        } else {
            maxOf(0, current.currentStreak - 1)
        }

        _habitState.value = current.copy(
            isCompletedToday = willBeCompleted,
            currentStreak = updatedStreak
        )
    }

    /**
     * Updates habit settings configured from the Settings screen.
     */
    fun updateHabitSettings(newName: String, newGoal: Int) {
        val current = _habitState.value
        _habitState.value = current.copy(
            name = newName.ifBlank { current.name },
            streakGoal = newGoal.coerceAtLeast(1)
        )
    }

    // --- Remote Quote Fetching ---
    /**
     * Fetches today's quote from the ZenQuotes API.
     */
    suspend fun fetchDailyQuote(): Quote {
        Log.d(TAG, "Fetching daily quote from ZenQuotes API...")
        return try {
            val response = apiService.getDailyQuote()
            if (response.isNotEmpty() && response.first().text.isNotBlank()) {
                val quote = response.first()
                Log.d(TAG, "Successfully fetched daily quote: \"${quote.text}\" by ${quote.author}")
                if (liveQuotesCache.none { it.text == quote.text }) {
                    liveQuotesCache.add(quote)
                }
                quote
            } else {
                Log.w(TAG, "API returned empty list, using fallback quote.")
                nextFallbackQuote()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching daily quote: ${e.message}", e)
            nextFallbackQuote()
        }
    }

    /**
     * Fetches a brand new random quote from the ZenQuotes API.
     * Called whenever the user clicks the reload/refresh button.
     */
    suspend fun fetchRandomQuote(): Quote {
        Log.d(TAG, "Fetching fresh random quote from ZenQuotes API...")
        return try {
            val response = apiService.getRandomQuote()
            if (response.isNotEmpty() && response.first().text.isNotBlank()) {
                val quote = response.first()
                Log.d(TAG, "Successfully fetched fresh random quote: \"${quote.text}\" by ${quote.author}")
                if (liveQuotesCache.none { it.text == quote.text }) {
                    liveQuotesCache.add(quote)
                }
                quote
            } else {
                Log.w(TAG, "Random API returned empty list, serving available quote.")
                nextAvailableQuote()
            }
        } catch (e: Exception) {
            if (e is retrofit2.HttpException && e.code() == 429) {
                Log.w(TAG, "ZenQuotes free tier rate limit (5 requests / 30s) reached. Serving available quote.")
            } else {
                Log.e(TAG, "Error fetching random quote: ${e.message}", e)
            }
            nextAvailableQuote()
        }
    }

    private fun nextAvailableQuote(): Quote {
        // If we collected live quotes from the API this session, cycle through them
        return if (liveQuotesCache.isNotEmpty()) {
            val quote = liveQuotesCache[fallbackIndex % liveQuotesCache.size]
            fallbackIndex++
            quote
        } else {
            nextFallbackQuote()
        }
    }

    private fun nextFallbackQuote(): Quote {
        val quote = fallbackQuotes[fallbackIndex % fallbackQuotes.size]
        fallbackIndex++
        return quote
    }
}
