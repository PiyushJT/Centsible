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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale


object Util {


    // Current Date
    fun getCurrentDate(): Long {

        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.US)
        return dateFormat.format(Date()).toLong()

    }


    fun getPreviousWeekDate(
        dateForPeriod: Long
    ): Long {

        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val currentDate = LocalDate.parse(dateForPeriod.toString(), formatter)
        val previousDate = currentDate.minusDays(7)

        return previousDate.format(formatter).toLong()

    }


    fun getNextWeekDate(
        dateForPeriod: Long
    ): Long {

        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val currentDate = LocalDate.parse(dateForPeriod.toString(), formatter)
        val nextDate = currentDate.plusDays(7)

        return nextDate.format(formatter).toLong()

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


    fun getWeekDates(
        date: Long
    ): List<Long> {
        val inputFormatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val parsedDate = inputFormatter.parse(date.toString()) ?: throw IllegalArgumentException("Invalid date format")

        val calendar = Calendar.getInstance()
        calendar.time = parsedDate
        calendar.firstDayOfWeek = Calendar.MONDAY

        // Move to the start of the week (Monday)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        val outputFormatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val weekDates = mutableListOf<Long>()

        // Add all 7 days (Monday to Sunday)
        for (i in 0..6) {
            weekDates.add(outputFormatter.format(calendar.time).toLong())
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return weekDates
    }


    fun dayOfWeek(dateLong: Long): Int {
        val dateStr = dateLong.toString()
        val year = dateStr.substring(0, 4).toInt()
        val month = dateStr.substring(4, 6).toInt()
        val day = dateStr.substring(6, 8).toInt()

        return LocalDate.of(year, month, day).dayOfWeek.value

    }



    val types = listOf("misc", "food", "shopping", "travel", "ent", "grocery", "everyday", "skill")


    fun createBackup(
        state: ExpenseState
    ) : String {

        var backup = ""

        for(expense in state.expenses){

            backup = "$backup\n${expense.title}__${expense.description}__${expense.type}__${expense.amount}__${expense.date}__${expense.id}"

        }

        return backup

    }


    fun saveBackup(
        backup: String,
        onEvent: (ExpenseEvent) -> Unit
    ){

        val rawExpenses = backup.split("\n")
        val expenses = emptyList<Expense>().toMutableList()


        for(value in rawExpenses){

            val expenseValues = value.split("__")

            val expense = Expense(
                title = expenseValues[0],
                description = if (expenseValues[1].isEmpty() || expenseValues[1] == "null") "" else expenseValues[1],
                type = expenseValues[2],
                amount = expenseValues[3].toFloat(),
                date = expenseValues[4].toLong(),
            )
            expenses.add(expense)
        }

        for (expense in expenses){

            onEvent(ExpenseEvent.SaveExpense(expense))

        }

    }

}