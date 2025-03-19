package com.piyushjt.centsible.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.piyushjt.centsible.MainScreen
import com.piyushjt.centsible.Types
import com.piyushjt.centsible.UI
import com.piyushjt.centsible.UI.readexPro
import com.piyushjt.centsible.Util
import com.piyushjt.centsible.ui.theme.CentsibleTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


// All Expenses to be shown in a Nav Item
@Composable
fun ALlExpenses(
    onEvent: (ExpenseEvent) -> Unit,
    state: ExpenseState,
    navController: NavController?
) {

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
    ) {

        Header(
            state = state,
            onEvent = onEvent
        )

        ListOfExpenses(
            state = state,
            navController = navController
        )
    }
}


// List of All Expenses
@Composable
fun ListOfExpenses(
    state: ExpenseState,
    navController: NavController?
) {

    val bottomPadding = 90.dp + Util.getBottomPadding()

    Column(
        modifier = Modifier
            .padding(bottom = bottomPadding)
            .verticalScroll(rememberScrollState()),
    ) {

        // Heading as above Composable
        Heading(text = "Recent Transactions")


        // All Expenses
        for (expense in state.expenses) {

            ExpenseCard(
                expense = expense,
                navController = navController
            )

        }

    }

}


// Header for All Expenses
@Composable
fun Header(
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit
) {

    Column {

        DayDate()

        TotalBalance(
            state = state,
            onEvent = onEvent
        )

    }

}


// Day & Date at the Top in header
@Composable
fun DayDate() {

    Column {

        // Day
        Text(
            text = SimpleDateFormat("EEEE,", Locale.getDefault()).format(Date()),
            color = UI.colors("light_text"),
            fontSize = 14.sp,
            fontFamily = readexPro
        )

        // Date
        Text(
            text = SimpleDateFormat("dd MMMM", Locale.getDefault()).format(Date()),
            color = UI.colors("main_text"),
            fontSize = 18.sp,
            fontFamily = readexPro
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

        onEvent(ExpenseEvent.SetTotalAmount)

        Text(
            text = "Total Balance",
            color = UI.colors("light_text"),
            fontSize = 18.sp,
            fontFamily = readexPro
        )

        // Balance
        Text(
            text = Util.formatInIndianSystem(state.totalAmount),
            color = UI.colors("lime"),
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

            MainScreen(
                state = ExpenseState(
                    expenses = listOf(

                        Expense(
                            title = "Title",
                            description = "Desc",
                            type = Types.ENT.type,
                            amount = -100.0f,
                            date = 20241231L,
                            id = 1
                        ),

                        Expense(
                            title = "Title",
                            description = "Desc",
                            type = Types.ENT.type,
                            amount = -100.0f,
                            date = 20241231L,
                            id = 1
                        ),

                        Expense(
                            title = "Title",
                            description = "Desc",
                            type = Types.ENT.type,
                            amount = -100.0f,
                            date = 20241231L,
                            id = 1
                        ),

                        Expense(
                            title = "Title",
                            description = "Desc",
                            type = Types.ENT.type,
                            amount = -100.0f,
                            date = 20241231L,
                            id = 1
                        ),

                        Expense(
                            title = "Title",
                            description = "Desc",
                            type = Types.ENT.type,
                            amount = -100.0f,
                            date = 20241231L,
                            id = 1
                        ),

                        Expense(
                            title = "Title",
                            description = "Desc",
                            type = Types.ENT.type,
                            amount = -100.0f,
                            date = 20241231L,
                            id = 1
                        ),

                        Expense(
                            title = "Title",
                            description = "Desc",
                            type = Types.ENT.type,
                            amount = -100.0f,
                            date = 20241231L,
                            id = 1
                        ),

                        Expense(
                            title = "Title",
                            description = "Desc",
                            type = Types.ENT.type,
                            amount = -100.0f,
                            date = 20241231L,
                            id = 1
                        ),

                        Expense(
                            title = "Title",
                            description = "Desc",
                            type = Types.ENT.type,
                            amount = -100.0f,
                            date = 20241231L,
                            id = 1
                        ),

                        Expense(
                            title = "Title",
                            description = "Desc",
                            type = Types.ENT.type,
                            amount = -100.0f,
                            date = 20241231L,
                            id = 1
                        ),

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
                navFilled = navFilled,
                onEvent = {}
            )
        }

    }
}