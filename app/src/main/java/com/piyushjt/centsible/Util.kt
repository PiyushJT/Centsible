package com.piyushjt.centsible

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object Util {


    // Current Date
    fun getCurrentDate(): Long {

        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.US)
        return dateFormat.format(Date()).toLong()

    }



    // Date Formatter
    fun formatToMonthDayYear(dateInLong: Long): String {

        // Ensure the input is valid for yyyyMMdd format
        if (dateInLong.toString().length != 8) {
            throw IllegalArgumentException("Invalid date format. Expected yyyyMMdd, but got $dateInLong")
        }

        // Getting day, month and year
        val day = (dateInLong % 100).toInt()
        val month = ((dateInLong / 100) % 100).toInt()
        val year = (dateInLong / 10000).toInt()


        // Setting month name
        val monthNames =  arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        val monthName = monthNames[month - 1]


        return "$monthName $day, $year"

    }



    // Image and Background Color
    @Composable
    fun image(type: String) : Painter{

        return when (type) {
            "misc" -> painterResource(id = R.drawable.misc)
            "food" -> painterResource(id = R.drawable.food)
            "shopping" -> painterResource(id = R.drawable.shopping_cart)
            "travel" -> painterResource(id = R.drawable.travel)
            "ent" -> painterResource(id = R.drawable.ent_netflix)
            "grocery" -> painterResource(id = R.drawable.grocery)
            "everyday" -> painterResource(id = R.drawable.everyday)
            "skill" -> painterResource(id = R.drawable.skill)
            else -> painterResource(id = R.drawable.shopping_cart)
        }

    }



    @Composable
    fun getRandomColor() : Color {

        val bgColors = mapOf(
            1 to UI.colors("green_bg"),
            2 to UI.colors("red_bg"),
            3 to UI.colors("pink_bg"),
            4 to UI.colors("gray_bg"),
            5 to UI.colors("cream_bg")
        )


        // Background color
        return bgColors[(1..5).random()]!!

    }


    @Composable
    fun getDeviceHeightInDp(): Dp {
        val configuration = LocalConfiguration.current
        val screenHeightPx = configuration.screenHeightDp
        return screenHeightPx.dp
    }


    @Composable
    fun getBottomPadding(): Dp {
        return WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    }


    val types = listOf("misc", "food", "shopping", "travel", "ent", "grocery", "everyday", "skill")


}