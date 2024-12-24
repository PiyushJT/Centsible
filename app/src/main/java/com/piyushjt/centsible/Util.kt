package com.piyushjt.centsible

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar
import java.util.Date



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



    // Get Start and End date for Weekly Stats
    fun getWeekDates(date: Long): Pair<Long, Long> {


        // Parse the input date (yyyyMMdd format) to a Date object
        val inputFormatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val parsedDate = inputFormatter.parse(date.toString()) ?: throw IllegalArgumentException("Invalid date format")

        val calendar = Calendar.getInstance()
        calendar.time = parsedDate

        // Set Monday as the first day of the week
        calendar.firstDayOfWeek = Calendar.MONDAY

        // Move to the start of the week (Monday)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfWeek = calendar.time

        // Move to the end of the week (Sunday)
        calendar.add(Calendar.DAY_OF_WEEK, 6)
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endOfWeek = calendar.time

        // Format the start and end dates back to yyyyMMdd and convert to Long
        val outputFormatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val formattedStart = outputFormatter.format(startOfWeek).toLong()
        val formattedEnd = outputFormatter.format(endOfWeek).toLong()

        return Pair(formattedStart, formattedEnd)

    }


    // Get Start and End date for Monthly Stats
    @SuppressLint("DefaultLocale")
    fun getMonthDates(date: Long): Pair<Long, Long> {

        val month = ((date / 100) % 100).toInt()
        val year = (date / 10000).toInt()

        // Define the days in each month
        val daysInMonth = intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

        // Check for leap year and update February days
        if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
            daysInMonth[1] = 29
        }

        // Format start and end dates
        val startDate = String.format("%04d%02d01", year, month).toLong()
        val endDate = String.format("%04d%02d%02d", year, month, daysInMonth[month - 1]).toLong()

        return Pair(startDate, endDate)
    }

}