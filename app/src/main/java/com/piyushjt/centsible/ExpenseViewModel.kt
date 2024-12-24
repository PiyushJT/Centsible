// This is ViewModel used to update the state

package com.piyushjt.centsible

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExpenseViewModel(
    // dao
    private val dao: ExpenseDao

) : ViewModel() {

    private val _expenses = dao.getAllExpense()


    // State
    private val _state = MutableStateFlow(ExpenseState())

    val state = combine(_state, _expenses) { state, expenses ->
        state.copy(
            expenses = expenses
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExpenseState())

    // Main Events
    fun onEvent(event: ExpenseEvent) {


        when (event) {


            // Setting the Title
            is ExpenseEvent.SetTitle -> {
                _state.update {
                    it.copy(
                        title = event.title
                    )
                }

                Log.d("Title", state.value.title)
            }


            // Setting the Description
            is ExpenseEvent.SetDescription -> {
                _state.update {
                    it.copy(
                        description = event.description
                    )
                }

                Log.d("Description", state.value.description?: "")
            }


            // Setting the Type
            is ExpenseEvent.SetType -> {
                _state.update {
                    it.copy(
                        type = event.type
                    )
                }

                Log.d("Type", state.value.type)
            }


            // Setting the amount
            is ExpenseEvent.SetAmount -> {
                _state.update {
                    it.copy(
                        amountToShow = event.amount,

                        amount = if(event.amount in arrayOf("", " ", ".", "-"))
                            -100.0f
                        else
                            event.amount.toFloat()
                    )
                }

                Log.d("Amount", state.value.amount.toString())
                Log.d("Amount To Show", state.value.amountToShow)
            }


            // Setting the date
            is ExpenseEvent.SetDate -> {
                _state.update {
                    it.copy(
                        date = event.date
                    )
                }

                Log.d("Date", state.value.date.toString())
            }


            // Setting the Id
            is ExpenseEvent.SetID -> {
                _state.update {
                    it.copy(
                        id = event.id
                    )
                }

                Log.d("ID", state.value.id.toString())
            }





            // Setting Constrained Expenses
            is ExpenseEvent.SetConstrainedExpenses -> {
                viewModelScope.launch {
                    val constrainedExpenses = withContext(Dispatchers.IO) {
                        dao.getConstrainedExpenses(
                            startDate = event.startDate,
                            endDate = event.endDate
                        ).firstOrNull() ?: emptyList()
                    }

                    _state.update {
                        it.copy(
                            constrainedExpenses = constrainedExpenses
                        )
                    }

                    Log.d("Constrained Expense", "Constraints Set from ${event.startDate} to ${event.endDate}")
                }
            }


            // Setting Stats Duration
            is ExpenseEvent.SetStatsDuration -> {
                _state.update {
                    it.copy(
                        statsDuration = event.duration
                    )
                }

                Log.d("Stats Duration", state.value.statsDuration)
            }


            // Setting Stats Date
            is ExpenseEvent.SetStatsDate -> {
                _state.update {
                    it.copy(
                        statsDate = event.date
                    )
                }

                Log.d("Stats Date", state.value.statsDate.toString())
            }






            // Save or Update an Expense
            is ExpenseEvent.SaveExpense -> {


                // Declaring the values
                val title = state.value.title
                val description = state.value.description
                val type = state.value.type
                val amount = state.value.amount
                val amountToShow = state.value.amountToShow
                val date = state.value.date
                val id = state.value.id


                if (title.isBlank() || amountToShow.isBlank()) {
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


                // if id is -1 -> new Expense needs to be created
                if (updateExpense.id != -1) {
                    viewModelScope.launch {
                        dao.upsertExpense(updateExpense)
                    }
                }
                else {
                    viewModelScope.launch {
                        dao.upsertExpense(newExpense)
                    }
                }


                // Updating the State
                _state.update {
                    it.copy(
                        title = "",
                        description = "",
                        type = "ent",
                        amount = -100.0f,
                        amountToShow = "-100",
                        date = Util.getCurrentDate(),
                        id = -1,
                    )
                }

                Log.d("Saved", "Saved the Expense and updated the state as below")
                Log.d("Title", state.value.title)
                Log.d("Description", state.value.description?: "")
                Log.d("Type", state.value.type)
                Log.d("Amount", state.value.amount.toString())
                Log.d("Amount To Show", state.value.amountToShow)
                Log.d("Date", state.value.date.toString())
                Log.d("ID", state.value.id.toString())

            }


            // Delete an Expense
            is ExpenseEvent.DeleteExpense -> {
                viewModelScope.launch {
                    dao.deleteExpense(event.expense)
                }
            }





            // Setting Type box expand state
            is ExpenseEvent.SetTypeBoxExpanded -> {
                _state.update {
                    it.copy(
                        typeBoxExpanded = event.expanded
                    )
                }

                Log.d("Type Box Expanded", state.value.typeBoxExpanded.toString())
            }


            // Setting dialog visibility
            is ExpenseEvent.SetIsDialogVisible -> {
                _state.update {
                    it.copy(
                        isDialogVisible = event.visible
                    )
                }

                Log.d("Type Box Expanded", state.value.isDialogVisible.toString())
            }


            // Setting the Navigation State
            is ExpenseEvent.ChangeNavState -> {
                _state.update {
                    it.copy(
                        navFilled = event.navFilled
                    )
                }

                Log.d("Nav", state.value.navFilled)
            }


            // Clearing the state to default
            is ExpenseEvent.ClearState -> {
                _state.update {
                    it.copy(
                        title = "",
                        description = "",
                        type = "ent",
                        amount = -100.0f,
                        amountToShow = "-100",
                        date = Util.getCurrentDate(),
                        id = -1,
                        typeBoxExpanded = false,
                    )
                }
                Log.d("Cleared", "State changed to default")
                Log.d("Title", state.value.title)
                Log.d("Description", state.value.description?: "")
                Log.d("Type", state.value.type)
                Log.d("Amount", state.value.amount.toString())
                Log.d("Amount To Show", state.value.amountToShow)
                Log.d("Date", state.value.date.toString())
                Log.d("ID", state.value.id.toString())
            }


        }
    }
}