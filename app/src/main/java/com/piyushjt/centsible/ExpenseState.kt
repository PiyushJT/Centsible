package com.piyushjt.centsible

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.text.toLong

data class ExpenseState(
    val expenses: List<Expense> = emptyList(),
    val id : Int = -1,
    val title: String = "",
    val description: String? = null,
    val date: Long = getTodayDateInYYYYMMDDFormat(),
    val type: String = "ent",
    val amount: Float = -100.0f,
    val amountToShow: String = "-100",
    val sortType: SortType = SortType.DATE,
    val navFilled: String = "home",
    val typeBoxExpanded: Boolean = false
)

fun getTodayDateInYYYYMMDDFormat(): Long {
    val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.US) // Explicitly using Locale.US
    return dateFormat.format(Date()).toLong()
}