package com.appTareas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = green,
    onPrimary = white,
    background = black,
    surface = backgroundDialogDarkMode,
    surfaceVariant = surfaceDarkMode
)

private val LightColorScheme = lightColorScheme(
    primary = green,
    onPrimary = black,
    background = white,
    surface = backgroundDialog,
    surfaceVariant = surface


    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */

    /*
    primary = Color(0xFF1DB954), // Botones, barra nav, etc
    primaryVariant = Color(0xFF1AA34A), // Variante más oscura
    secondary = Color(0xFFBB86FC), // Un color morado de ejemplo
    background = Color.Black, // Fondo general
    surface = Color.DarkGray, // Fondo de tarjetas o diálogos
    error = Color.Red, // Color de errores
    onPrimary = Color.White, // Texto o íconos sobre el verde
    onSecondary = Color.Black, // Texto sobre el morado
    onBackground = Color.White, // Texto sobre el fondo negro
    onSurface = Color.White, // Texto sobre superficies grises
    onError = Color.White // Texto sobre el color de error
     */
)



@Composable
fun Ej2_LoginSpotifyTheme(
    darkTheme: Boolean = false, // Recibe el estado del tema oscuro
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}


/*  Funcion por default:

@Composable
fun Ej2_LoginSpotifyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
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
*/