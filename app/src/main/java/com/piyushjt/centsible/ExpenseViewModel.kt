// This is ViewModel used to update the state

package com.piyushjt.centsible

import android.util.Log
import android.widget.Toast
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

                Log.d("HEh", data.id.toString())
                Log.d("HEh", data.title)
                Log.d("HEh", data.description.toString())
                Log.d("HEh", data.date.toString())
                Log.d("HEh", data.amount.toString())
                Log.d("HEh", data.type)

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

        }
    }
}