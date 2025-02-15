package com.piyushjt.centsible.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.piyushjt.centsible.Expense
import com.piyushjt.centsible.ExpenseEvent
import com.piyushjt.centsible.R
import com.piyushjt.centsible.readexPro


// Edit Expense Screen
@Composable
fun EditExpenseScreen(
    title: String,
    description: String?,
    type: String,
    amount: Float,
    date: Long,
    id: Int,
    navController: NavController
) {

/*
    TextButton(
        onClick = {
            navController.navigate(
                MainScreen
            )
        }
    ) {
        Text(
            text = "Click"
        )
    }

*/

}



// Back button
@Composable
fun BackButton(
    onEvent: (ExpenseEvent) -> Unit,
    navController: NavController
) {

    IconButton(
        modifier = Modifier
            .width(50.dp)
            .height(50.dp),

        onClick = {

            // TODO: onEvent(ExpenseEvent.ClearState)

            navController.popBackStack()

        },

        colors = IconButtonColors(
            contentColor = colorResource(id = R.color.main_text),
            disabledContentColor = colorResource(id = R.color.card_background),
            containerColor = colorResource(id = R.color.card_background),
            disabledContainerColor = colorResource(id = R.color.card_background)
        )

    ) {

        // Icon
        Icon(
            modifier = Modifier
                .height(30.dp)
                .width(30.dp),

            painter =  painterResource(id = R.drawable.back),

            contentDescription = "Back"
        )

    }

}





// Delete button
@Composable
fun DeleteButton(
    expenses: MutableState<List<Expense>>,
    id: Int,
    isDialogVisible: MutableState<Boolean>,
    onEvent: (ExpenseEvent) -> Unit,
    navController: NavController
) {

    IconButton(
        modifier = Modifier
            .width(50.dp)
            .height(50.dp),

        onClick = {

            isDialogVisible.value = true

        },
        colors = IconButtonColors(
            contentColor = colorResource(id = R.color.red),
            disabledContentColor = colorResource(id = R.color.red50),
            containerColor = colorResource(id = R.color.card_background),
            disabledContainerColor = colorResource(id = R.color.card_background)
        ),

        ) {

        // Icon
        Icon(
            modifier = Modifier
                .height(30.dp)
                .width(30.dp),

            painter =  painterResource(id = R.drawable.delete),

            contentDescription = "Delete Expense"
        )

    }

    DeleteDialog(
        onEvent = onEvent,
        isDialogVisible = isDialogVisible,
        onDelete = {

            onEvent(ExpenseEvent.DeleteExpense(expense = expenses.value.find { it.id == id }!!))
            // TODO: onEvent(ExpenseEvent.ClearState)

            navController.popBackStack()

            isDialogVisible.value = false

        }
    )

}




// Confirmation Dialog
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteDialog(
    isDialogVisible: MutableState<Boolean>,
    onEvent: (ExpenseEvent) -> Unit,
    onDelete: () -> Unit
) {
    if(isDialogVisible.value) {
        BasicAlertDialog(
            onDismissRequest = {
                isDialogVisible.value = false
            },
            content = {
                Box(
                    modifier = Modifier
                        .height(160.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))

                        .background(colorResource(id = R.color.card_background))
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {

                        Text(
                            text = "Confirm Delete?",
                            color = colorResource(id = R.color.text),
                            fontSize = 26.sp,
                            fontFamily = readexPro
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {

                            TextButton(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(colorResource(id = R.color.trans))
                                    .border(
                                        1.dp,
                                        colorResource(id = R.color.text),
                                        RoundedCornerShape(15.dp)
                                    )
                                    .padding(horizontal = 5.dp),
                                onClick = {
                                    isDialogVisible.value = false
                                }

                            ) {
                                Text(
                                    text = "Cancel",
                                    color = colorResource(id = R.color.text),
                                    fontSize = 20.sp,
                                    fontFamily = readexPro
                                )
                            }



                            TextButton(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(colorResource(id = R.color.red))
                                    .padding(horizontal = 5.dp),
                                onClick = onDelete

                            ) {
                                Text(
                                    text = "Delete",
                                    color = colorResource(id = R.color.black),
                                    fontSize = 20.sp,
                                    fontFamily = readexPro
                                )
                            }


                        }

                    }

                }
            }
        )
    }
}