package com.piyushjt.centsible

data class ExpenseState(

    val expenses: List<Expense> = emptyList(),

    val expensesInPeriod: List<Expense> = emptyList(),
    val dateForPeriod: Long = Util.getCurrentDate(),
    val amountsInPeriod: List<Float> = emptyList(),

    val totalAmount: Float = 0f,

)