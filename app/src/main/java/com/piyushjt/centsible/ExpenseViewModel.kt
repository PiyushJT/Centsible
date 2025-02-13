// This is ViewModel used to update the state

package com.piyushjt.centsible

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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
                val id = data.id

                Log.d("Yes", "Came to save expense iin view Model")
                Log.d("Yes", title)

                if (title.isBlank() || amount == 0f) {
                    return
                }


                // To Update the Expense
                val updateExpense = Expense(
                    title = title,
                    description = description,
                    type = type,
                    amount = amount,
                    date = date,
                    id = id
                )


                // To Create a new Expense
                val newExpense = Expense(
                    title = title,
                    description = description,
                    type = type,
                    amount = amount,
                    date = date
                )


                viewModelScope.launch {
                    Log.d("Yes", "Came to save expense iin view Model in new coroutine")
                    dao.upsertExpense(newExpense)
                }

            }


            // Delete an Expense
            is ExpenseEvent.DeleteExpense -> {
                viewModelScope.launch {
                    dao.deleteExpense(event.expense)
                }
            }

        }
    }
}