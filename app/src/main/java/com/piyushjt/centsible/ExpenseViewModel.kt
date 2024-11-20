package com.piyushjt.centsible

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.sql.Date

@OptIn(ExperimentalCoroutinesApi::class)
class ExpenseViewModel(
    // dao
    private val dao: ExpenseDao

) : ViewModel() {

    // Sort Type
    private val _sortType = MutableStateFlow(SortType.DATE)

    private val _expenses = _sortType
        .flatMapLatest { sortType ->
            when (sortType) {
                SortType.DATE -> dao.getAllExpense()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())


    private val _state = MutableStateFlow(ExpenseState())

    val state = combine(_state, _sortType, _expenses) { state, sortType, expenses ->
        state.copy(
            expenses = expenses,
            sortType = sortType,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ExpenseState())


    fun onEvent(event: ExpenseEvent) {
        when (event) {

            // Delete a Task
            is ExpenseEvent.DeleteExpense -> {
                viewModelScope.launch {
                    dao.deleteExpense(event.expense)
                }
            }

            // Save or Update a Task
            is ExpenseEvent.SaveExpense -> {
                val title = state.value.title
                val description = state.value.description
                val type = state.value.type
                val date = state.value.date
                val amount = state.value.amount
                val amountToShow = state.value.amountToShow
                val id = state.value.id

                if (title.isBlank() || amountToShow.isBlank()) {
                    return
                }

                val updateExpense = Expense(
                    id = id,
                    title = title,
                    description = description,
                    type = type,
                    date = date,
                    amount = amount
                )

                val newExpense = Expense(
                    title = title,
                    description = description,
                    type = type,
                    date = date,
                    amount = amount
                )

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

                _state.update {
                    it.copy(
                        id = -1,
                        title = "",
                        description = "",
                        type = "ent",
                        date = 30072007,
                        amount = -100.0f,
                    )
                }
            }

            // Setting the Title
            is ExpenseEvent.SetTitle -> {
                _state.update {
                    it.copy(
                        title = event.title
                    )
                }
            }

            // Setting the amount
            is ExpenseEvent.SetAmount -> {

                _state.update {
                    it.copy(
                        amountToShow = event.amount,
                        amount = event.amount.toFloat()
                    )
                }
            }


            // Setting the Id
            is ExpenseEvent.SetID -> {
                _state.update {
                    it.copy(
                        id = event.id
                    )
                }
            }

            // Setting the Description
            is ExpenseEvent.SetDescription -> {
                _state.update {
                    it.copy(
                        description = event.description
                    )
                }
            }

            // Sort the list
            is ExpenseEvent.SortExpense -> {
                _sortType.value = event.sortType
            }

            // Setting the Type
            is ExpenseEvent.ChangeNavState -> {
                _state.update {
                    it.copy(
                        navFilled = event.navFilled
                    )
                }
            }

        }
    }
}