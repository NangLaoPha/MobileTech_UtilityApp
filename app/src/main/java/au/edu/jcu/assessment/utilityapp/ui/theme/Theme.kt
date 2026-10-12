// File: app/src/main/java/au/edu/jcu/assessment/utilityapp/ui/theme/Theme.kt
package au.edu.jcu.assessment.utilityapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

/**
 * Ollama-inspired Dark Color Scheme:
 * Deep obsidian background, zinc surfaces, crisp white typography, and emerald success accents.
 */
private val DarkColorScheme = darkColorScheme(
    primary = OllamaDarkPrimary,
    onPrimary = OllamaDarkOnPrimary,
    primaryContainer = OllamaDarkSurfaceVariant,
    onPrimaryContainer = OllamaDarkPrimary,
    secondary = OllamaZinc400,
    onSecondary = OllamaBlack,
    secondaryContainer = OllamaDarkSurfaceVariant,
    onSecondaryContainer = OllamaDarkPrimary,
    tertiary = OllamaSuccess,
    onTertiary = Color.White,
    tertiaryContainer = OllamaSuccessContainerDark,
    onTertiaryContainer = Color.White,
    background = OllamaBlack,
    onBackground = Color.White,
    surface = OllamaDarkSurface,
    onSurface = Color.White,
    surfaceVariant = OllamaDarkSurfaceVariant,
    onSurfaceVariant = OllamaZinc400,
    outline = OllamaDarkBorder
)

/**
 * Ollama-inspired Light Color Scheme:
 * Clean off-white background, pure white card surfaces, deep slate typography, and emerald accents.
 */
private val LightColorScheme = lightColorScheme(
    primary = OllamaLightPrimary,
    onPrimary = OllamaLightOnPrimary,
    primaryContainer = OllamaLightSurfaceVariant,
    onPrimaryContainer = OllamaLightPrimary,
    secondary = OllamaZinc500,
    onSecondary = Color.White,
    secondaryContainer = OllamaLightSurfaceVariant,
    onSecondaryContainer = OllamaLightPrimary,
    tertiary = OllamaSuccess,
    onTertiary = Color.White,
    tertiaryContainer = OllamaSuccessContainerLight,
    onTertiaryContainer = OllamaLightPrimary,
    background = OllamaLightBackground,
    onBackground = OllamaLightPrimary,
    surface = OllamaLightSurface,
    onSurface = OllamaLightPrimary,
    surfaceVariant = OllamaLightSurfaceVariant,
    onSurfaceVariant = OllamaZinc500,
    outline = OllamaLightBorder
)

@Composable
fun MobileTech_UtilityAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Set to false by default to ensure the custom Ollama theme is applied
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}