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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.piyushjt.centsible.Expense
import com.piyushjt.centsible.ExpenseEvent
import com.piyushjt.centsible.R
import com.piyushjt.centsible.UI
import com.piyushjt.centsible.UI.readexPro


// Edit Expense Screen
@Composable
fun EditExpenseScreen(
    title: String,
    description: String?,
    type: String,
    amount: Float,
    date: Long,
    id: Int,
    navController: NavController,
    onEvent: (ExpenseEvent) -> Unit
) {


    Column (

        modifier = Modifier
            .fillMaxSize(),

        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround

    ) {


        TextButton(
            onClick = {
                onEvent(ExpenseEvent.DeleteExpense(
                    expense = Expense(
                        title = title,
                        description = description,
                        type = type,
                        amount = amount,
                        date = date,
                        id = id
                    )
                ))

                navController.popBackStack()
            }
        ) {
            Text(
                text = "Delete"
            )
        }

        Text(
            text =  "$title \n$description \n$type \n$amount \n$date \n$id"
        )

    }

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
            contentColor = UI.colors("main_text"),
            disabledContentColor = UI.colors("card_background"),
            containerColor = UI.colors("card_background"),
            disabledContainerColor = UI.colors("card_background")
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
            contentColor = UI.colors("red"),
            disabledContentColor = UI.colors("red50"),
            containerColor = UI.colors("card_background"),
            disabledContainerColor = UI.colors("card_background")
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

                        .background(UI.colors("card_background"))
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {

                        Text(
                            text = "Confirm Delete?",
                            color = UI.colors("text"),
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
                                    .background(UI.colors("trans"))
                                    .border(
                                        1.dp,
                                        UI.colors("text"),
                                        RoundedCornerShape(15.dp)
                                    )
                                    .padding(horizontal = 5.dp),
                                onClick = {
                                    isDialogVisible.value = false
                                }

                            ) {
                                Text(
                                    text = "Cancel",
                                    color = UI.colors("text"),
                                    fontSize = 20.sp,
                                    fontFamily = readexPro
                                )
                            }



                            TextButton(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp)
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(UI.colors("red"))
                                    .padding(horizontal = 5.dp),
                                onClick = onDelete

                            ) {
                                Text(
                                    text = "Delete",
                                    color = UI.colors("black"),
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