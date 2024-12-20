package com.piyushjt.centsible

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {


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

}