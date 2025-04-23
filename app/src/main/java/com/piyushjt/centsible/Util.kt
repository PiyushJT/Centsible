package com.piyushjt.centsible

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.piyushjt.centsible.UI.readexPro
import com.piyushjt.centsible.screens.expense
import org.json.JSONArray
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Util {

    // Expense Types
    val types = Types.entries.toList()


    // Current Date
    fun getCurrentDate(): Long {

        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.US)
        return dateFormat.format(Date()).toLong()

    }


    // Gives date from previous week (same dayOfWeek)
    fun getPreviousWeekDate(
        dateForPeriod: Long
    ): Long {

        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val currentDate = LocalDate.parse(dateForPeriod.toString(), formatter)
        val previousDate = currentDate.minusDays(7)

        return previousDate.format(formatter).toLong()

    }


    // Gives date from next week (same dayOfWeek)
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


    // Image by expense type
    @Composable
    fun image(expenseType: Types) : Painter{

        return when (expenseType) {
            Types.MISC -> painterResource(id = R.drawable.misc)
            Types.FOOD -> painterResource(id = R.drawable.food)
            Types.GROCERIES -> painterResource(id = R.drawable.grocery)
            Types.TRAVEL -> painterResource(id = R.drawable.travel)
            Types.ENT -> painterResource(id = R.drawable.ent_netflix)
            Types.SHOPPING -> painterResource(id = R.drawable.shopping_cart)
            Types.SKILL -> painterResource(id = R.drawable.skill)
            Types.BILL -> painterResource(id = R.drawable.bill)
            Types.EMI -> painterResource(id = R.drawable.emi)
            Types.MEDICINE -> painterResource(id = R.drawable.medicine)
            Types.INVESTMENT -> painterResource(id = R.drawable.invest)
        }

    }


    // Gives random color
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


    // Gives Device Height in dp
    @Composable
    fun getDeviceHeightInDp(): Dp {
        val configuration = LocalConfiguration.current
        val screenHeightPx = configuration.screenHeightDp
        return screenHeightPx.dp
    }


    // Gives Bottom Padding
    @Composable
    fun getBottomPadding(): Dp {
        return WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    }


    // Gives dates of week
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
        (0..6).forEach { i ->
            weekDates.add(outputFormatter.format(calendar.time).toLong())
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return weekDates
    }


    // Gives day of week
    fun dayOfWeek(dateLong: Long): Int {
        val dateStr = dateLong.toString()
        val year = dateStr.substring(0, 4).toInt()
        val month = dateStr.substring(4, 6).toInt()
        val day = dateStr.substring(6, 8).toInt()

        return LocalDate.of(year, month, day).dayOfWeek.value

    }


    // Formats any amount in Indian Currency System
    fun formatInIndianSystem(amount: Float): String {

        // format in Indian system.
        val formatter = DecimalFormat("#,##,##0.00")
        return "₹${formatter.format(amount)}"

    }


    // Removes scientific notation from amount
    @SuppressLint("DefaultLocale")
    fun removeScientificNotation(amount: Float): String {

        return if (amount.toString().contains("E"))
            String.format("%.10f", amount).trimEnd('0').trimEnd('.')
        else
            amount.toString()

    }


    // Gives type by string
    fun getTypeByString(type: String): Types {

        return when(type){
            "misc" -> Types.MISC
            "food" -> Types.FOOD
            "grocery" -> Types.GROCERIES
            "travel" -> Types.TRAVEL
            "ent" -> Types.ENT
            "shopping" -> Types.SHOPPING
            "skill" -> Types.SKILL
            "bills" -> Types.BILL
            "emi" -> Types.EMI
            "medicine" -> Types.MEDICINE
            "investment" -> Types.INVESTMENT
            else -> Types.MISC
        }

    }

    // Clears expense data
    fun clearExpenseData() {

        expense.title = ""
        expense.description = ""
        expense.type = "misc"
        expense.amount = 0f
        expense.date = getCurrentDate()
        expense.id = -1
    }


    // Confirmation Dialog
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun DialogBox(
        isDialogVisible: MutableState<Boolean>,
        title: String,
        message: String = "",
        posBtnText: String,
        negBtnText: String,
        onPosBtnClick: () -> Unit,
        onNegBtnClick: () -> Unit = {  },
        isPosBtnPositive: Boolean = false
    ) {

        if(isDialogVisible.value) {

            BasicAlertDialog(
                onDismissRequest = {
                    isDialogVisible.value = false
                },

                // Content
                content = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(UI.colors("card_background"))
                            .border(
                                0.5.dp,
                                color = UI.colors("hint_text"),
                                RoundedCornerShape(20.dp)
                            )
                    ) {

                        Column(
                            modifier = Modifier
                                .padding(vertical = 24.dp),
                            horizontalAlignment = CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceAround
                        ) {

                            // Dialog title
                            Text(
                                modifier = Modifier
                                    .padding(vertical = 10.dp),
                                text = title,
                                color = UI.colors("text"),
                                fontSize = 26.sp,
                                fontFamily = readexPro
                            )


                            // Dialog message
                            Text(
                                modifier = Modifier
                                    .padding(vertical = 10.dp)
                                    .fillMaxWidth(0.8f),
                                text = message,
                                color =
                                    if (isPosBtnPositive)
                                        UI.colors("main_text")
                                    else
                                        UI.colors("red"),
                                fontSize = 18.sp,
                                fontFamily = readexPro,
                                textAlign = TextAlign.Center
                            )

                            // Dialog buttons
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 10.dp),
                                verticalAlignment = CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {

                                // Negative button
                                TextButton(
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp)
                                        .clip(RoundedCornerShape(15.dp))
                                        .background(UI.colors("trans"))
                                        .border(
                                            1.dp,
                                            UI.colors("text"),
                                            RoundedCornerShape(15.dp)
                                        )
                                        .padding(horizontal = 5.dp),
                                    onClick = {
                                        onNegBtnClick()
                                        isDialogVisible.value = false
                                    }
                                ) {
                                    Text(
                                        text = negBtnText,
                                        color = UI.colors("text"),
                                        fontSize = 20.sp,
                                        fontFamily = readexPro
                                    )
                                }

                                // Positive button
                                TextButton(
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp)
                                        .clip(RoundedCornerShape(15.dp))
                                        .background(
                                            if (isPosBtnPositive)
                                                UI.colors("lime")
                                            else
                                                UI.colors("red")
                                        )
                                        .padding(horizontal = 5.dp),
                                    onClick = {
                                        onPosBtnClick()
                                        isDialogVisible.value = false
                                    }
                                ) {
                                    Text(
                                        text = posBtnText,
                                        color = UI.colors("black"),
                                        fontSize = 20.sp,
                                        fontFamily = readexPro
                                    )
                                }

                            }
                        }

                    }
                }
            )
        }
    }


    // Converts JSON string to list of expenses
    fun parseExpensesFromJson(json: String): List<Expense> {

        // Base Expense list
        val expenseList = mutableListOf<Expense>()

        //
        val jsonArray = JSONArray(json)

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)

            // Expense Object .. using this because of minified enabled and caused severe bug in last update.
            val expense = Expense(
                title = obj.optString("title"),
                description = obj.optString("description"),
                type = obj.optString("type"),
                amount = obj.optDouble("amount").toFloat(),
                date = obj.optLong("date"),
                id = obj.optInt("id")
            )

            // Adding object to list
            expenseList.add(expense)

        }

        return expenseList

    }

}