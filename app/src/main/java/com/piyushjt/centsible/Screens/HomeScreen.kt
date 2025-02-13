package com.piyushjt.centsible.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.piyushjt.centsible.Expense
import com.piyushjt.centsible.ExpenseState
import com.piyushjt.centsible.Heading
import com.piyushjt.centsible.MainScreen
import com.piyushjt.centsible.R
import com.piyushjt.centsible.readexPro
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


// All Expenses to be shown in a Nav Item
@Composable
fun ALlExpenses(
    state: ExpenseState,
    navController: NavController
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
            state = state,
            navController = navController
        )
    }
}


// List of All Expenses
@Composable
fun ListOfExpenses(
    state: ExpenseState,
    navController: NavController
) {

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
    ) {

        // Heading as above Composable
        Heading(text = "Recent Transactions")


        // All Expenses
        for(expense in state.expenses){

            Expense(
                id = expense.id,
                title = expense.title,
                description = expense.description,
                amount = expense.amount,
                type = expense.type,
                navController = navController
            )

        }


        // Spacer for bottom navigation bar
        Spacer(
            modifier = Modifier
                .height(
                    WindowInsets.navigationBars
                        .asPaddingValues()
                        .calculateBottomPadding()
                            + 110.dp
                )
        )

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
            text = "₹5,21,985.00",
            color = colorResource(id = R.color.lime),
            fontSize = 34.sp,
            fontFamily = readexPro
        )

    }

}


