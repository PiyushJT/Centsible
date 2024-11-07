package com.piyushjt.centsible.ui.theme

import android.annotation.SuppressLint
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.piyushjt.centsible.R


@SuppressLint("ResourceAsColor")
@Composable
private fun getColorPalette() = darkColorScheme(
    background = colorResource(id = R.color.background),
    surface = colorResource(id = R.color.background)
)


@Composable
fun CentsibleTheme(
    content: @Composable () -> Unit
) {

    val colors = getColorPalette()


    MaterialTheme(
        shapes = Shapes(),
        content = content,
        colorScheme = colors
    )
}