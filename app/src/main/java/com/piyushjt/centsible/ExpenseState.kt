// This is State of all values

package com.piyushjt.centsible

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.text.toLong

data class ExpenseState(

    val expenses: List<Expense> = emptyList(),

    val title: String = "",
    val description: String? = null,
    val type: String = "ent",
    val amount: Float = -100.0f,
    val date: Long = getTodayDateInYYYYMMDDFormat(),
    val id : Int = -1,

    val typeBoxExpanded: Boolean = false,
    val navFilled: String = "home",

    val amountToShow: String = "-100",
    val sortType: SortType = SortType.DATE

)



fun getTodayDateInYYYYMMDDFormat(): Long {

    val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.US)
    return dateFormat.format(Date()).toLong()

}