package com.piyushjt.centsible.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import com.piyushjt.centsible.Expense
import com.piyushjt.centsible.ExpenseEvent
import com.piyushjt.centsible.R
import com.piyushjt.centsible.Types
import com.piyushjt.centsible.UI
import com.piyushjt.centsible.UI.readexPro
import com.piyushjt.centsible.Util
import com.piyushjt.centsible.Util.types
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.abs


val newExpense = Expense(
    title = "",
    description = "",
    type = Types.MISC.type,
    amount = 0f,
    date = Util.getCurrentDate(),
    id = -1
)


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

    newExpense.title = title
    newExpense.description = description
    newExpense.type = type
    newExpense.amount = abs(amount)
    newExpense.date = date
    newExpense.id = id


    val newAmount = remember { mutableFloatStateOf(newExpense.amount) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = CenterHorizontally
    ) {


        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current

        val typeBoxExpanded = remember { mutableStateOf(false) }


        EditExpenseHeader(
            onEvent = onEvent,
            navController = navController
        )

        EditTitle(
            title = title,
            focusRequester = focusRequester,
            focusManager = focusManager
        )

        EditDescription(
            description = description?: "",
            focusRequester = focusRequester,
            focusManager = focusManager
        )

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            EditAmount(
                newAmount = newAmount,
                focusRequester = focusRequester,
                focusManager = focusManager,
                typeBoxExpanded = typeBoxExpanded
            )

            // Date Picker
            EditDatePickerUI(
                date = date
            )

        }



        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {

            EditTypeSelector(
                modifier = Modifier,
                isExpanded = typeBoxExpanded
            )

        }

        // Update Button
        UpdateButton(
            onEvent = onEvent,
            navController = navController
        )

    }
}


@Composable
fun EditExpenseHeader(
    onEvent: (ExpenseEvent) -> Unit,
    navController: NavController
) {

    Row (
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = CenterVertically

    ) {

        BackButton(
            navController = navController
        )

        // Header
        Text(
            text = "Edit Transaction",
            color = UI.colors("main_text"),
            fontSize = 18.sp,
            fontFamily = readexPro
        )

        DeleteButton(
            expense = newExpense,
            onEvent = onEvent,
            navController = navController
        )

    }
}



// Back button
@Composable
fun BackButton(
    navController: NavController
) {

    IconButton(
        modifier = Modifier
            .width(50.dp)
            .height(50.dp),

        onClick = {

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
    expense: Expense,
    onEvent: (ExpenseEvent) -> Unit,
    navController: NavController
) {

    val isDialogVisible = remember { mutableStateOf(false) }

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
        isDialogVisible = isDialogVisible,
        onDelete = {

            onEvent(ExpenseEvent.DeleteExpense(expense = expense))

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
                        .fillMaxWidth(0.9f)
                        .aspectRatio(1.8f)
                        .clip(RoundedCornerShape(20.dp))
                        .background(UI.colors("card_background"))
                        .border(
                            0.5.dp,
                            color = UI.colors("hint_text"),
                            RoundedCornerShape(20.dp)
                        )
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




@Composable
fun EditTitle(
    title: String,
    focusRequester: FocusRequester,
    focusManager: FocusManager
) {

    var value by remember { mutableStateOf(title) }

    TextField(
        value = value,
        colors = TextFieldDefaults.colors(

            focusedContainerColor = UI.colors("card_background"),
            unfocusedContainerColor = UI.colors("card_background"),

            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,

            focusedTextColor = UI.colors("text"),
            unfocusedTextColor = UI.colors("text"),

            cursorColor = UI.colors("text")

        ),
        textStyle = TextStyle(
            fontSize = 24.sp
        ),
        shape = RoundedCornerShape(20.dp),
        onValueChange = {
            value = it
            newExpense.title = it
        },
        placeholder = {
            Text(
                text = "Title",
                fontSize = 24.sp,
                color = UI.colors("hint_text")
            )
        },
        singleLine = true,
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
            .border(
                0.3.dp,
                color = UI.colors("hint_text"),
                RoundedCornerShape(20.dp)
            )
            .onKeyEvent { event ->
                when (event.key) {
                    Key.DirectionUp -> {
                        focusManager.clearFocus()
                        true
                    }
                    Key.DirectionDown -> {
                        focusManager.moveFocus(FocusDirection.Down)
                        true
                    }
                    else -> false
                }
            }
            .focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        )
    )

}



@Composable
fun EditDescription(
    description: String,
    focusRequester: FocusRequester,
    focusManager: FocusManager
) {

    var value by remember { mutableStateOf(description) }

    TextField(
        value = value,
        colors = TextFieldDefaults.colors(

            focusedContainerColor = UI.colors("card_background"),
            unfocusedContainerColor = UI.colors("card_background"),

            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,

            focusedTextColor = UI.colors("text"),
            unfocusedTextColor = UI.colors("text"),

            cursorColor = UI.colors("text")

        ),
        textStyle = TextStyle(
            fontSize = 18.sp
        ),
        shape = RoundedCornerShape(20.dp),
        onValueChange = {
            value = it
            newExpense.description = it
        },
        placeholder = {
            Text(
                text = "Description (optional)",
                fontSize = 18.sp,
                color = UI.colors("hint_text")
            )
        },
        singleLine = true,
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
            .border(
                0.3.dp,
                color = UI.colors("hint_text"),
                RoundedCornerShape(20.dp)
            )
            .onKeyEvent { event ->
                when (event.key) {
                    Key.DirectionUp -> {
                        focusManager.moveFocus(FocusDirection.Up)
                        true
                    }
                    Key.DirectionDown -> {
                        focusManager.moveFocus(FocusDirection.Down)
                        true
                    }
                    else -> false
                }
            }
            .focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }
        )
    )

}



@Composable
fun EditAmount(
    newAmount: MutableFloatState,
    focusRequester: FocusRequester,
    focusManager: FocusManager,
    typeBoxExpanded: MutableState<Boolean>
) {

    val context = LocalContext.current
    var toast: Toast? by remember { mutableStateOf(null) }

    var value by remember { mutableStateOf(Util.removeScientificNotation(newAmount.floatValue)) }


    TextField(
        value = value,
        colors = TextFieldDefaults.colors(

            focusedContainerColor = UI.colors("card_background"),
            unfocusedContainerColor = UI.colors("card_background"),

            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,

            focusedTextColor = UI.colors("text"),
            unfocusedTextColor = UI.colors("text"),

            cursorColor = UI.colors("text")

        ),
        textStyle = TextStyle(
            fontSize = 18.sp
        ),
        shape = RoundedCornerShape(20.dp),
        onValueChange = { newValue ->

            if(
                (newValue.length > 8
                &&
                !newValue.contains("."))
                ||
                (newValue.length > 9
                &&
                newValue.contains("."))
            ) {

                // Cancel the existing toast if it's showing
                toast?.cancel()

                // Show a new toast for invalid input
                toast = Toast.makeText(
                    context,
                    "This app doesn't support such big values",
                    Toast.LENGTH_LONG,
                )
                toast?.show()

            }
            else {
                // Allows optional '-' at the start, one decimal point, and up to 2 decimal places
                val regex = "^-?\\d*(\\.\\d{0,2})?$".toRegex()

                if (regex.matches(newValue) && !newValue.contains(" ") && !newValue.contains(",") && !newValue.startsWith(
                        "-."
                    )
                ) {

                    value = newValue

                    value = if (newValue in arrayOf("", " ", "."))
                        ""
                    else
                        newValue

                    newExpense.amount = if (value != "-" && value.isNotEmpty() && value != "-.")
                        value.toFloat()
                    else
                        0f

                    newAmount.floatValue = newExpense.amount

                }
            }

        },
        placeholder = {
            Text(
                text = "Amount",
                fontSize = 16.sp,
                color = UI.colors("hint_text")
            )
        },

        singleLine = true,
        modifier = Modifier
            .fillMaxWidth(0.4f)
            .padding(top = 20.dp)
            .border(
                0.3.dp,
                color = UI.colors("hint_text"),
                RoundedCornerShape(20.dp)
            )
            .onKeyEvent { event ->
                when (event.key) {
                    Key.DirectionUp -> {
                        focusManager.moveFocus(FocusDirection.Up)
                        true
                    }
                    Key.DirectionDown -> {
                        focusManager.clearFocus()
                        typeBoxExpanded.value = true
                        true
                    }
                    else -> false
                }
            }
            .focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                focusManager.clearFocus()
                typeBoxExpanded.value = true
            }
        )
    )

}




@Composable
fun EditTypeSelector(
    modifier: Modifier,
    isExpanded: MutableState<Boolean>
) {

    // Background color
    val bgColor = Util.getRandomColor()


    Box(
        modifier
            .width(80.dp),
        contentAlignment = Alignment.Center
    ) {


        Row(
            modifier = Modifier
                .padding(top = 20.dp)
                .border(
                    0.5.dp,
                    color = UI.colors("hint_text"),
                    RoundedCornerShape(20.dp)
                )
                .height(80.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(color = UI.colors("card_background"))
                .clickable {
                    isExpanded.value = true
                }
        ) {

            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(15.dp))
                    .background(bgColor)

            ) {

                // Logo
                Image(
                    painter = Util.image(
                        Util.getTypeByString(newExpense.type)
                    ),
                    contentDescription = newExpense.type,
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(10.dp)
                        .background(bgColor)
                )

            }
        }

        DropdownMenu(

            modifier = Modifier
                .background(color = UI.colors("card_background"))
                .width(80.dp)
                .fillMaxHeight(0.4f),

            expanded = isExpanded.value,

            onDismissRequest = {
                isExpanded.value = false
            },

            properties = PopupProperties(
                focusable = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),

            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(
                0.5.dp,
                color = UI.colors("hint_text")
            )

        ) {

            types.forEach{ item ->

                DropdownMenuItem(

                    onClick = {
                        newExpense.type = item.type
                        isExpanded.value = false
                    },

                    text = {

                        Box(
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(15.dp))
                                .background(bgColor)
                        ) {

                            // Logo
                            Image(
                                painter = Util.image(item),
                                contentDescription = item.type,
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .padding(10.dp)
                            )

                        }
                    }
                )
            }
        }
    }
}


// Date Picker
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDatePickerUI(
    date: Long
) {

    val date = remember { mutableLongStateOf(date) }


    // Some temp States
    val datePickerState = rememberDatePickerState()
    val showDialog = remember { mutableStateOf(false) }


    // Formatted date
    val formattedDate = Util.formatToMonthDayYear(date.longValue)


    Row(
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {

        // Box to open date picker
        Box(
            modifier = Modifier
                .border(
                    0.3.dp,
                    color = UI.colors("hint_text"),
                    RoundedCornerShape(20.dp)
                )
                .clip(RoundedCornerShape(20.dp))
                .background(color = UI.colors("card_background"))
                .clickable { showDialog.value = true }
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .align(CenterVertically)
        ) {

            // Showing the selected date
            Text(
                text = formattedDate,
                color = UI.colors("text"),
                fontSize = 16.sp,
                fontFamily = readexPro
            )

        }
    }


    if (showDialog.value) {

        // Showing the dialog
        DatePickerDialog(

            onDismissRequest = {
                showDialog.value = false
            },

            confirmButton = {

                Button(

                    onClick = {

                        // Close the dialog
                        showDialog.value = false

                        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                        val anotherFormattedDate =
                            Instant.ofEpochMilli(datePickerState.selectedDateMillis?: System.currentTimeMillis())
                                .atZone(ZoneId.systemDefault())
                                .format(formatter)

                        // Set the new date to State
                        date.longValue = anotherFormattedDate.toLong()

                        newExpense.date = date.longValue

                    }
                ) {
                    Text("Select")
                }

            },


            dismissButton = {
                Button(

                    onClick = {
                        // close the dialog
                        showDialog.value = false
                    }

                ) {
                    Text("Cancel")
                }
            }

        ) {

            DatePicker(state = datePickerState)

        }
    }
}



// Save / Update Button
@Composable
fun UpdateButton(
    onEvent: (ExpenseEvent) -> Unit,
    navController: NavController
) {

    val context = LocalContext.current
    var toast: Toast? by remember { mutableStateOf(null) }


    Row(
        modifier = Modifier
            .padding(top = 24.dp)
    ) {

        // Update Earning button
        TextButton(

            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
                .height(60.dp),

            onClick = {


                // Save the Expense if title and amount are not empty
                if (!(newExpense.title.isBlank() || newExpense.amount == 0f)) {

                    onEvent(
                        ExpenseEvent.UpdateExpense(newExpense)
                    )

                    navController.popBackStack()

                } else {

                    // Cancel the existing toast if it's showing
                    toast?.cancel()

                    // Show a new toast for invalid input
                    toast = Toast.makeText(
                        context,
                        "Title and Amount cannot be empty",
                        Toast.LENGTH_SHORT,
                    )
                    toast?.show()

                }

            },

            shape = RoundedCornerShape(20.dp),
            colors = ButtonColors(

                containerColor = UI.colors("lime"),

                contentColor = UI.colors("black"),
                disabledContainerColor = UI.colors("red"),
                disabledContentColor = UI.colors("black")
            )

        ) {

            // Text on the button
            Text(
                text = "Save Earning",
                color = UI.colors("black"),
                fontSize = 16.sp,
                fontFamily = readexPro,
                textAlign = TextAlign.Center
            )

        }


        // Update Expense button
        TextButton(

            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
                .height(60.dp),

            onClick = {


                // Save the Expense if title and amount are not empty
                if (!(newExpense.title.isBlank() || newExpense.amount == 0f)) {

                    newExpense.amount = -newExpense.amount

                    onEvent(
                        ExpenseEvent.UpdateExpense(newExpense)
                    )

                    navController.popBackStack()

                } else {

                    // Cancel the existing toast if it's showing
                    toast?.cancel()

                    // Show a new toast for invalid input
                    toast = Toast.makeText(
                        context,
                        "Title and Amount cannot be empty",
                        Toast.LENGTH_SHORT
                    )
                    toast?.show()

                }

            },

            shape = RoundedCornerShape(20.dp),
            colors = ButtonColors(

                containerColor = UI.colors("red"),

                contentColor = UI.colors("black"),
                disabledContainerColor = UI.colors("red"),
                disabledContentColor = UI.colors("black")
            )

        ) {

            // Text on the button
            Text(
                text = "Save Expense",
                color = UI.colors("black"),
                fontSize = 16.sp,
                fontFamily = readexPro,
                textAlign = TextAlign.Center
            )

        }


    }
}