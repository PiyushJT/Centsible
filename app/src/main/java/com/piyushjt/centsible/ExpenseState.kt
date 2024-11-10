package com.piyushjt.centsible

import java.sql.Date

data class ExpenseState(
    val expenses: List<Expense> = emptyList(),
    val id : Int = -1,
    val title: String = "tit",
    val description: String = "des",
    val date: Long = 30072007,
    val type: String = "good",
    val amount: Float = 100.0f,
    val sortType: SortType = SortType.DATE
)