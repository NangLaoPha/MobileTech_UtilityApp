// File: app/src/main/java/au/edu/jcu/assessment/utilityapp/ui/HabitViewModel.kt
package au.edu.jcu.assessment.utilityapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import au.edu.jcu.assessment.utilityapp.data.repository.HabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel managing UI-related state for the habit utility app.
 * Follows the Android Architecture Components pattern.
 */
class HabitViewModel(
    private val repository: HabitRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HabitUiState())
    val uiState: StateFlow<HabitUiState> = _uiState.asStateFlow()

    init {
        // Observe repository in-memory habit changes
        viewModelScope.launch {
            repository.habitState.collect { habit ->
                _uiState.update { it.copy(habit = habit) }
            }
        }

        // Fetch quote on launch
        loadDailyQuote()
    }

    /**
     * Toggles the daily habit completion state.
     */
    fun toggleHabitCompletion() {
        repository.toggleCompletion()
    }

    /**
     * Updates the habit name and streak target goal.
     */
    fun updateHabitSettings(name: String, goal: Int) {
        repository.updateHabitSettings(name, goal)
    }

    /**
     * Loads the initial daily quote on app launch.
     */
    fun loadDailyQuote() {
        viewModelScope.launch {
            _uiState.update { it.copy(isQuoteLoading = true, quoteErrorMessage = null) }
            try {
                val quote = repository.fetchDailyQuote()
                _uiState.update {
                    it.copy(
                        quote = quote,
                        isQuoteLoading = false,
                        quoteErrorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isQuoteLoading = false,
                        quoteErrorMessage = "Could not load quote. Tap reload to try again."
                    )
                }
            }
        }
    }

    /**
     * Fetches a brand new random quote from the API.
     * Triggered when the user taps the reload button.
     */
    fun refreshQuote() {
        viewModelScope.launch {
            _uiState.update { it.copy(isQuoteLoading = true, quoteErrorMessage = null) }
            try {
                val quote = repository.fetchRandomQuote()
                _uiState.update {
                    it.copy(
                        quote = quote,
                        isQuoteLoading = false,
                        quoteErrorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isQuoteLoading = false,
                        quoteErrorMessage = "Could not refresh quote. Tap reload to try again."
                    )
                }
            }
        }
    }
}

/**
 * Manual Dependency Injection Factory for HabitViewModel.
 * Instantiates the ViewModel by supplying the repository dependency.
 */
class HabitViewModelFactory(
    private val repository: HabitRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitViewModel::class.java)) {
            return HabitViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
