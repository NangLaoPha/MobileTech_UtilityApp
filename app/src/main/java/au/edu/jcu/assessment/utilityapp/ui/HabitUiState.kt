// File: app/src/main/java/au/edu/jcu/assessment/utilityapp/ui/HabitUiState.kt
package au.edu.jcu.assessment.utilityapp.ui

import au.edu.jcu.assessment.utilityapp.data.model.Habit
import au.edu.jcu.assessment.utilityapp.data.model.Quote

/**
 * UI State representing all the data needed to render UtilityScreen and SettingsScreen.
 */
data class HabitUiState(
    val habit: Habit = Habit(),
    val quote: Quote = Quote(),
    val isQuoteLoading: Boolean = false,
    val quoteErrorMessage: String? = null
)
