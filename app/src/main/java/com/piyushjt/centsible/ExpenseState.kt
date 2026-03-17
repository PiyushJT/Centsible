package com.piyushjt.centsible

enum class PeriodType {
    WEEKLY, MONTHLY
}

data class ExpenseState(

    val expenses: List<Expense> = emptyList(),

    val expensesInPeriod: List<Expense> = emptyList(),
    val dateForPeriod: Long = Util.getCurrentDate(),
    val amountsInPeriod: List<Float> = emptyList(),
    val amountsInLastPeriod: List<Float> = emptyList(),

    val totalAmount: Float = 0f,
    val weeklyAverage: Float = 0f,
    val monthlyAverage: Float = 0f,

    val periodType: PeriodType = PeriodType.WEEKLY

)