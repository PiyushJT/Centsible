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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
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
import com.piyushjt.centsible.R
import com.piyushjt.centsible.Util
import com.piyushjt.centsible.readexPro
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

/*

        TextButton(
            onClick = {
                navController.navigate(
                    com.piyushjt.centsible.EditExpenseScreen(
                        title = "",
                        description = "da",
                        type = "ent",
                        amount = 9f,
                        date = 20240101L,
                        id = 1
                    )
                )
            }
        ) {
            Text(
                text = "Click"
            )
        }

*/


        Header()

        ListOfExpenses(
            onEvent = onEvent,
            state = state,
            navController = navController
        )
    }
}


// List of All Expenses
@Composable
fun ListOfExpenses(
    onEvent: (ExpenseEvent) -> Unit,
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
                onEvent = onEvent,
                expense = expense,
                navController = navController
            )

        }

    }

}


// Header for All Expenses
@Composable
fun Header() {

    Column {

        DayDate()

        TotalBalance()

    }

}


// Day & Date at the Top in header
@Composable
fun DayDate() {

    Column {

        // Day
        Text(
            text = SimpleDateFormat("EEEE,", Locale.getDefault()).format(Date()),
            color = colorResource(id = R.color.light_text),
            fontSize = 14.sp,
            fontFamily = readexPro
        )

        // Date
        Text(
            text = SimpleDateFormat("dd MMMM", Locale.getDefault()).format(Date()),
            color = colorResource(id = R.color.main_text),
            fontSize = 18.sp,
            fontFamily = readexPro
        )

    }

}


// Total Balance in the Header
@Composable
fun TotalBalance() {

    Column(
        modifier = Modifier
            .padding(top = 24.dp, bottom = 12.dp)
    ) {

        Text(
            text = "Total Balance",
            color = colorResource(id = R.color.light_text),
            fontSize = 18.sp,
            fontFamily = readexPro
        )

        // Balance
        Text(
            text = "₹58,21,985.70",
            color = colorResource(id = R.color.lime),
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
                .background(colorResource(id = R.color.background))
                .fillMaxSize()
                .padding(top = 42.dp)
        ) {


            val expenses = remember { mutableStateOf(emptyList<Expense>()) }
            val title = remember { mutableStateOf("Title") }
            val description = remember { mutableStateOf("Desc") }
            val type = remember { mutableStateOf("ent") }
            val amount = remember { mutableFloatStateOf(-100.0f) }
            val date = remember { mutableLongStateOf(20241231L) }
            val id = remember { mutableIntStateOf(-1) }
            val navFilled = remember { mutableStateOf("home") }
            val typeBoxExpanded = remember { mutableStateOf(false) }

            MainScreen(
                state = ExpenseState(

                    expenses = listOf(

                        Expense(
                            title = "Title",
                            description = "Desc",
                            type = "ent",
                            amount = -100.0f,
                            date = 20241231L,
                            id = 1
                        ),

                        Expense(
                            title = "Title",
                            description = "Desc",
                            type = "ent",
                            amount = -100.0f,
                            date = 20241231L,
                            id = 1
                        ),

                        Expense(
                            title = "Title",
                            description = "Desc",
                            type = "ent",
                            amount = -100.0f,
                            date = 20241231L,
                            id = 1
                        ),

                        Expense(
                            title = "Title",
                            description = "Desc",
                            type = "ent",
                            amount = -100.0f,
                            date = 20241231L,
                            id = 1
                        ),

                        Expense(
                            title = "Title",
                            description = "Desc",
                            type = "ent",
                            amount = -100.0f,
                            date = 20241231L,
                            id = 1
                        ),

                        Expense(
                            title = "Title",
                            description = "Desc",
                            type = "ent",
                            amount = -100.0f,
                            date = 20241231L,
                            id = 1
                        ),

                        Expense(
                            title = "Title",
                            description = "Desc",
                            type = "ent",
                            amount = -100.0f,
                            date = 20241231L,
                            id = 1
                        ),

                        Expense(
                            title = "Title",
                            description = "Desc",
                            type = "ent",
                            amount = -100.0f,
                            date = 20241231L,
                            id = 1
                        ),

                        Expense(
                            title = "Title",
                            description = "Desc",
                            type = "ent",
                            amount = -100.0f,
                            date = 20241231L,
                            id = 1
                        ),

                        Expense(
                            title = "Title",
                            description = "Desc",
                            type = "ent",
                            amount = -100.0f,
                            date = 20241231L,
                            id = 1
                        ),

                        Expense(
                            title = "Title",
                            description = "Desc",
                            type = "ent",
                            amount = -100.0f,
                            date = 20241231L,
                            id = 1
                        )

                    ),
                ),
                expenses = expenses,
                title = title,
                description = description,
                type = type,
                amount = amount,
                date = date,
                id = id,
                typeBoxExpanded = typeBoxExpanded,
                navFilled = navFilled,
                onEvent = {},
            )
        }

    }
}