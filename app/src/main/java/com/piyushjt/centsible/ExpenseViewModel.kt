// This is ViewModel used to update the state

package com.piyushjt.centsible

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ExpenseViewModel(
    // dao
    private val dao: ExpenseDao

) : ViewModel() {

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