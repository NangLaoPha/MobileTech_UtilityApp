// File: app/src/main/java/au/edu/jcu/assessment/utilityapp/ui/screens/UtilityScreen.kt
package au.edu.jcu.assessment.utilityapp.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import au.edu.jcu.assessment.utilityapp.ui.HabitUiState
import au.edu.jcu.assessment.utilityapp.ui.theme.OllamaSuccess

/**
 * UtilityScreen: The primary at-a-glance screen styled in the Ollama minimalist aesthetic.
 * Displays today's tracked habit, one-tap check-in, streak progress, and daily quote.
 */
@Composable
fun UtilityScreen(
    uiState: HabitUiState,
    onToggleCompletion: () -> Unit,
    onRefreshQuote: () -> Unit,
    modifier: Modifier = Modifier
) {
    val habit = uiState.habit
    val quote = uiState.quote
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- Ollama-styled Header ---
        Column {
            Text(
                text = "Daily Me",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "At-a-glance habit & streak tracker",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // --- Card 1: Today's Habit Check-in (Ollama Minimalist Card) ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            colors = CardDefaults.cardColors(
                containerColor = if (habit.isCompletedToday) {
                    MaterialTheme.colorScheme.tertiaryContainer
                } else {
                    MaterialTheme.colorScheme.surface
                }
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "TODAY'S HABIT",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = MaterialTheme.typography.labelMedium.letterSpacing,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = habit.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(20.dp))

                // High-Contrast Ollama Check-in Button
                val buttonColor by animateColorAsState(
                    targetValue = if (habit.isCompletedToday) {
                        OllamaSuccess
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                    label = "CheckInButtonColor"
                )

                val buttonContentColor by animateColorAsState(
                    targetValue = if (habit.isCompletedToday) {
                        Color.White
                    } else {
                        MaterialTheme.colorScheme.onPrimary
                    },
                    label = "CheckInContentColor"
                )

                Button(
                    onClick = onToggleCompletion,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColor,
                        contentColor = buttonContentColor
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (habit.isCompletedToday) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Done Checkmark",
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Completed Today",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        } else {
                            Text(
                                text = "Mark as Done",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // --- Card 2: Streak & Progress Tracker ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Current Streak",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${habit.currentStreak} Days",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Streak Goal",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${habit.streakGoal} Days",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Progress Bar toward streak goal (Emerald accent)
                val progress = (habit.currentStreak.toFloat() / habit.streakGoal.toFloat()).coerceIn(0f, 1f)
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = MaterialTheme.colorScheme.tertiary, // OllamaSuccess emerald
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = if (habit.currentStreak >= habit.streakGoal) {
                        "Goal achieved! Keep up the momentum!"
                    } else {
                        "${habit.streakGoal - habit.currentStreak} more day(s) to reach your goal"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // --- Card 3: Daily Motivational Quote (Retrofit Web API) ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Daily Focus Quote",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    IconButton(
                        onClick = onRefreshQuote,
                        enabled = !uiState.isQuoteLoading
                    ) {
                        if (uiState.isQuoteLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh Quote",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                if (quote.text.isNotBlank()) {
                    Text(
                        text = "\"${quote.text}\"",
                        style = MaterialTheme.typography.bodyLarge,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "- ${quote.author.ifBlank { "Unknown" }}",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.End)
                    )
                }

                if (uiState.quoteErrorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.quoteErrorMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                    OutlinedButton(
                        onClick = onRefreshQuote,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Retry")
                    }
                }
            }
        }
    }
}
