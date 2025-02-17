package com.piyushjt.centsible


import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

object UI {

    @Composable
    fun colors(color: String): Color {


        val colors = mapOf(
            "logo_theme" to colorResource(id = R.color.logo_theme),
            "background" to colorResource(id = R.color.background),
            "card_background" to colorResource(id = R.color.card_background),
            "text" to colorResource(id = R.color.text),
            "main_text" to colorResource(id = R.color.main_text),
            "light_text" to colorResource(id = R.color.light_text),
            "heading_text" to colorResource(id = R.color.heading_text),

            "hint_text" to colorResource(id = R.color.hint_text),
            "hint_main_text" to colorResource(id = R.color.hint_main_text),
            "hint_light_text" to colorResource(id = R.color.hint_light_text),

            "black" to colorResource(id = R.color.black),
            "white" to colorResource(id = R.color.white),
            "red" to colorResource(id = R.color.red),
            "red50" to colorResource(id = R.color.red50),
            "lime" to colorResource(id = R.color.lime),
            "blue" to colorResource(id = R.color.blue),
            "trans" to colorResource(id = R.color.trans),

            "green_bg" to colorResource(id = R.color.green_bg),
            "pink_bg" to colorResource(id = R.color.pink_bg),
            "gray_bg" to colorResource(id = R.color.gray_bg),
            "red_bg" to colorResource(id = R.color.red_bg),
            "cream_bg" to colorResource(id = R.color.cream_bg)
        )


        return colors[color] ?: Color.Transparent

    }

    // Custom font
    val readexPro = FontFamily(
        Font(R.font.readex_pro, FontWeight.Medium)
    )

}