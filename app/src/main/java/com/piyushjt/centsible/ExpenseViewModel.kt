// This is ViewModel used to update the state

package com.piyushjt.centsible

import android.content.ContentValues
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
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


            // Update an Expense
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


            // Set total amount
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


            // Export data
            is ExpenseEvent.ExportData -> {
                viewModelScope.launch {

                    // Expenses to be exported
                    val expenses = state.value.expenses

                    try {

                        val fileName = "expenses.centsible"

                        val json = Gson().toJson(expenses)
                        val resolver = event.context.contentResolver


                        val contentValues = ContentValues().apply {
                            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                            put(MediaStore.Downloads.MIME_TYPE, "application/octet-stream")
                            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
                        }


                        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                            ?: throw Exception("Failed to create file in Downloads")


                        resolver.openOutputStream(uri)?.use { outputStream ->
                            outputStream.write(json.toByteArray())
                            outputStream.flush()
                        }

                        Log.d("Export", "Data exported successfully to $uri")


                        val dataSaved = R.string.data_saved_to_downloads
                        Toast.makeText(event.context, dataSaved, Toast.LENGTH_LONG).show()

                    }
                    catch (e: Exception) {

                        Log.e("Export", "Failed to export: ${e.message}", e)


                        val dataNotSaved = R.string.failed_to_save_data
                        Toast.makeText(event.context, dataNotSaved, Toast.LENGTH_SHORT).show()
                    }

                }
            }


            // Delete all expenses
            is ExpenseEvent.DeleteAllExpenses -> {
                viewModelScope.launch {

                    // Delete all expenses
                    for (expense in state.value.expenses) {
                        dao.deleteExpense(expense)
                    }
                }
                Log.d("Delete All Expenses", "All expenses deleted")
            }

        }
    }
}