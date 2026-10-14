// File: app/src/main/java/au/edu/jcu/assessment/utilityapp/data/model/Habit.kt
package au.edu.jcu.assessment.utilityapp.data.model

/**
 * Data class representing the tracked core habit.
 * Maintains the habit name, current streak count, goal target, and today's completion status.
 */
data class Habit(
    val name: String = "Read 20 Mins",
    val streakGoal: Int = 7,
    val currentStreak: Int = 3,
    val isCompletedToday: Boolean = false
)
