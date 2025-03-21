// This is ViewModel used to update the state

package com.piyushjt.centsible

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExpenseViewModel(
    // dao
    private val dao: ExpenseDao

) : ViewModel() {

    private val _expenses = dao.getAllExpense()
    private val _state = MutableStateFlow(ExpenseState())


    val state = combine(_state, _expenses) { state, expenses ->
        state.copy(
            expenses = expenses
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExpenseState())


    // Main Events
    fun onEvent(event: ExpenseEvent) {

        when (event) {

            // Save or Update an Expense
            is ExpenseEvent.SaveExpense -> {

                val data = event.expense

                // Declaring the values
                val title = data.title
                val description = data.description
                val type = data.type
                val amount = data.amount
                val date = data.date

                if (title.isBlank() || amount == 0f) {
                    return
                }


                // To Create a new Expense
                val newExpense = Expense(
                    title = title,
                    description = description,
                    type = type,
                    amount = amount,
                    date = date
                )


                viewModelScope.launch {
                    dao.upsertExpense(newExpense)
                }

            }


            // Delete an Expense
            is ExpenseEvent.UpdateExpense -> {


                val data = event.expense

                // To Update the Expense
                val updateExpense = Expense(
                    title = data.title,
                    description = data.description,
                    type = data.type,
                    amount = data.amount,
                    date = data.date,
                    id = data.id
                )

                viewModelScope.launch {
                    dao.upsertExpense(updateExpense)
                }

            }


            // Delete an Expense
            is ExpenseEvent.DeleteExpense -> {
                viewModelScope.launch {
                    dao.deleteExpense(event.expense)
                }
            }


            // Change period of expenseInPeriod
            is ExpenseEvent.ChangePeriod -> {

                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            expensesInPeriod = dao.getExpenseInPeriod(event.startDate, event.endDate)
                        )
                    }
                }

                Log.d("Period changed", "new size: ${state.value.expensesInPeriod.size}")
            }


            // Change date for period
            is ExpenseEvent.ChangeDateForPeriod -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            dateForPeriod = event.date
                        )
                    }
                    Log.d("Date changed", "new date: ${state.value.dateForPeriod}")
                }
            }


            // Set amounts
            is ExpenseEvent.SetAmounts -> {
                viewModelScope.launch {

                    val amounts = mutableListOf<Float>()

                    for (date in event.dates) {
                        amounts.add(dao.getAmount(date))
                        Log.d("Amount from dao", "amount: ${dao.getAmount(date)}")
                    }

                    _state.update {
                        it.copy(
                            amountsInPeriod = amounts
                        )
                    }

                    Log.d("Amounts set", "new amounts: ${state.value.amountsInPeriod}")
                }
            }


            // Set Last amounts
            is ExpenseEvent.SetLastAmounts -> {
                viewModelScope.launch {

                    val amounts = mutableListOf<Float>()

                    for (date in event.dates) {
                        amounts.add(dao.getAmount(date))
                        Log.d("Amount from dao", "amount: ${dao.getAmount(date)}")
                    }

                    _state.update {
                        it.copy(
                            amountsInLastPeriod = amounts
                        )
                    }

                    Log.d("Last Amounts set", "new amounts: ${state.value.amountsInLastPeriod}")
                }
            }




            is ExpenseEvent.SetTotalAmount -> {

                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            totalAmount = dao.getTotalAmount()
                        )
                    }

                    Log.d("Total Amount", "new amount: ${state.value.totalAmount}")
                }
            }
        }
    }
}