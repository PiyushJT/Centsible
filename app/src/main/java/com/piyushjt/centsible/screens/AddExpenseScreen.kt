package com.piyushjt.centsible.screens

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.piyushjt.centsible.Expense
import com.piyushjt.centsible.ExpenseEvent
import com.piyushjt.centsible.ExpenseState
import com.piyushjt.centsible.MainScreen
import com.piyushjt.centsible.UI
import com.piyushjt.centsible.UI.readexPro
import com.piyushjt.centsible.Util.types
import com.piyushjt.centsible.Util
import com.piyushjt.centsible.ui.theme.CentsibleTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


val expense = Expense(
    title = "",
    description = "",
    type = "ent",
    amount = 0f,
    date = Util.getCurrentDate(),
    id = -1,
)


// Add Expense Screen
@SuppressLint("AutoboxingStateValueProperty")
@Composable
fun AddExpense(
    onEvent: (ExpenseEvent) -> Unit,
    navFilled: MutableState<String>
) {

    val amount = remember { mutableFloatStateOf(expense.amount) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = CenterHorizontally
    ) {

        // Header
        Text(
            text = "New Transaction",
            color = UI.colors("main_text"),
            fontSize = 18.sp,
            fontFamily = readexPro
        )

        Title()

        Description()

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Amount(
                amount = amount
            )

            // Date Picker
            DatePickerUI(
                onEvent = onEvent
            )

        }



        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {

            TypeSelector(
                modifier = Modifier
            )

        }

        // Save Button
        SaveButton(
            amount = amount,
            expense = expense,
            navFilled = navFilled,
            onEvent = onEvent
        )

    }
}


@Composable
fun Title(
    modifier: Modifier = Modifier
) {

    var value by remember { mutableStateOf("") }

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
            expense.title = it
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
    )

}



@Composable
fun Description(
    modifier: Modifier = Modifier
) {

    var value by remember { mutableStateOf("") }

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
            expense.description = it
        },
        placeholder = {
            Text(
                text = "Description",
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
    )

}



@Composable
fun Amount(
    amount: MutableFloatState,
    modifier: Modifier = Modifier
) {

    var value by remember { mutableStateOf("") }

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
            // Valid Float Input with two decimal places

            // Allows only one decimal point, and up to 2 decimal places
            val regex = "^\\d*(\\.\\d{0,2})?$".toRegex()


            if (regex.matches(newValue) && !newValue.contains(" ") && !newValue.contains(",")) {

                value = newValue

                value = if (newValue in arrayOf("", " ", "."))
                    ""
                else
                    newValue

                expense.amount = if (value != "-" && value.isNotEmpty() && value != "-.")
                    value.toFloat()
                else
                    0f

                amount.floatValue = expense.amount

            }

        },
        placeholder = {
            Text(
                text = "Amount",
                fontSize = 16.sp,
                color = UI.colors("hint_text")
            )
        },

        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),

        singleLine = true,
        modifier = Modifier
            .fillMaxWidth(0.4f)
            .padding(top = 20.dp)
            .border(
                0.3.dp,
                color = UI.colors("hint_text"),
                RoundedCornerShape(20.dp)
            )
    )

}




@Composable
fun TypeSelector(
    modifier: Modifier
) {

    // Background color
    val bgColor = Util.getRandomColor()

    var expand by remember { mutableStateOf(false) }


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
                    expand = true
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
                    painter = Util.image(expense.type),
                    contentDescription = expense.type,
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

            expanded = expand,

            onDismissRequest = {
                expand = false
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
                        expense.type = item
                        expand = false
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
                                contentDescription = item,
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
fun DatePickerUI(
    onEvent: (ExpenseEvent) -> Unit
) {

    val date = remember { mutableLongStateOf(Util.getCurrentDate()) }


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

                        expense.date = date.longValue

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
fun SaveButton(
    amount: MutableFloatState,
    expense: Expense,
    navFilled: MutableState<String>,
    onEvent: (ExpenseEvent) -> Unit
) {

    val context = LocalContext.current
    var toast: Toast? by remember { mutableStateOf(null) }


    Row(
        modifier = Modifier
            .padding(top = 24.dp)
    ) {
        // Save Earning button
        TextButton(

            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
                .height(60.dp),

            onClick = {

                // Save the Expense if title and amount are not empty
                if (!(expense.title.isBlank() || expense.amount == 0f)) {

                    onEvent(
                        ExpenseEvent.SaveExpense(expense)
                    )

                    navFilled.value = "home"

                } else {

                    // Cancel the existing toast if it's showing
                    toast?.cancel()

                    // Show a new toast for invalid input
                    toast =
                        Toast.makeText(
                            context,
                            "Title and Amount cannot be empty",
                            Toast.LENGTH_SHORT
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
                fontSize = 18.sp,
                fontFamily = readexPro,
                textAlign = TextAlign.Center
            )

        }

        // Save Expense button
        TextButton(

            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
                .height(60.dp),

            onClick = {

                // Save the Expense if title and amount are not empty
                if (!(expense.title.isBlank() || expense.amount == 0f)) {

                    expense.amount = -expense.amount

                    onEvent(
                        ExpenseEvent.SaveExpense(expense)
                    )

                    navFilled.value = "home"

                } else {

                    // Cancel the existing toast if it's showing
                    toast?.cancel()

                    // Show a new toast for invalid input
                    toast =
                        Toast.makeText(
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
                fontSize = 18.sp,
                fontFamily = readexPro,
                textAlign = TextAlign.Center
            )

        }

    }
}


@Preview
@Composable
private fun AddScreenPreview() {

    CentsibleTheme {
        Surface(
            modifier = Modifier
                .background(UI.colors("background"))
                .fillMaxSize()
                .padding(top = 42.dp)
        ) {

            val navFilled = remember { mutableStateOf("add") }

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
                navFilled = navFilled,
                onEvent = {},
            )
        }

    }
}