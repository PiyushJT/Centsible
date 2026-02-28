package com.piyushjt.centsible.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.piyushjt.centsible.Expense
import com.piyushjt.centsible.ExpenseCard
import com.piyushjt.centsible.ExpenseEvent
import com.piyushjt.centsible.ExpenseState
import com.piyushjt.centsible.Heading
import com.piyushjt.centsible.Types
import com.piyushjt.centsible.UI
import com.piyushjt.centsible.UI.readexPro
import com.piyushjt.centsible.Util
import com.piyushjt.centsible.ui.theme.CentsibleTheme


// All Expenses to be shown in a Nav Item
@Composable
fun ALlExpenses(
    onEvent: (ExpenseEvent) -> Unit,
    state: ExpenseState,
    navController: NavController
) {

    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 24.dp)
    ) {

        item {
            TotalBalance(
                state = state,
                onEvent = onEvent
            )
        }

        ListOfExpenses(
            state = state,
            navController = navController
        )
    }
}


// List of All Expenses
fun androidx.compose.foundation.lazy.LazyListScope.ListOfExpenses(
    state: ExpenseState,
    navController: NavController
) {

    // Heading as above Composable
    item {
        Heading(text = UI.strings("recent_transactions"))
    }


    // All Expenses
    items(state.expenses) { expense ->

        ExpenseCard(
            expense = expense,
            navController = navController
        )

    }

}


// Total Balance in the Header
@Composable
fun TotalBalance(
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit
) {

    Column(
        modifier = Modifier
            .padding(top = 24.dp, bottom = 12.dp)
    ) {

        Text(
            text = UI.strings("total_balance"),
            color = UI.colors("light_text"),
            fontSize = 18.sp,
            fontFamily = readexPro
        )

        // Balance
        Text(
            text = Util.formatInIndianSystem(state.totalAmount),
            color =
                if (state.totalAmount >= 0)
                    UI.colors("lime")
                else
                    UI.colors("red"),
            fontSize = 34.sp,
            fontFamily = readexPro
        )

    }

}

@Preview
@Composable
private fun HomeScreenPreview() {

    CentsibleTheme {
        Surface(
            modifier = Modifier
                .background(UI.colors("background"))
                .fillMaxSize()
                .padding(top = 42.dp)
        ) {

            val navFilled = remember { mutableStateOf("home") }

            ALlExpenses(
                state = ExpenseState(
                    expenses = listOf(

                        Expense(
                            title = "Title",
                            description = "Desc",
                            type = Types.ENT.type,
                            amount = -100.0f,
                            date = 20241231L,
                            id = 1
                        )
                    ),
                    totalAmount = 521985f
                ),
                onEvent = {},
                navController = androidx.navigation.compose.rememberNavController()
            )
        }

    }
}