package com.piyushjt.centsible.Screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.piyushjt.centsible.EditExpenseScreen
import com.piyushjt.centsible.Expense
import com.piyushjt.centsible.ExpenseEvent
import com.piyushjt.centsible.MainScreen
import com.piyushjt.centsible.R
import com.piyushjt.centsible.Util
import com.piyushjt.centsible.readexPro
import com.piyushjt.centsible.ui.theme.CentsibleTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


// Add Expense Screen
@Composable
fun AddExpense(
    title: MutableState<String>,
    description: MutableState<String>,
    amount: MutableState<Float>,
    amountToShow: MutableState<String>,
    type: MutableState<String>,
    date: MutableState<Long>,
    typeBoxExpanded: MutableState<Boolean>,
    onEvent: (ExpenseEvent) -> Unit,
    navFilled: MutableState<String>
) {


    val isExpense = if (amount.value > 0) false else true


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = CenterHorizontally
    ) {

        // Header
        Text(
            text = if (isExpense) "Add an Expense" else "Add an Earning",
            color = colorResource(id = R.color.main_text),
            fontSize = 18.sp,
            fontFamily = readexPro
        )


        // Edit Expense -> Similar to Expense Card
        EditExpense(
            title = title,
            description = description,
            amount = amount,
            amountToShow = amountToShow,
            type = type,
            typeBoxExpanded = typeBoxExpanded,
            onEvent = onEvent
        )


        // Drop Down Type Selector
        TypeSelector(
            type = type,
            typeBoxExpanded = typeBoxExpanded,
            onEvent = onEvent
        )


        // Date Picker
        DatePickerUI(
            date = date,
            onEvent = onEvent
        )


        // Save Button
        SaveButton(
            title = title,
            description = description,
            type = type,
            date = date,
            amount = amount,
            navFilled = navFilled,
            onEvent = onEvent
        )

    }
}




// Edit Expense fields (Inputs)
@Composable
fun EditExpense(
    title: MutableState<String>,
    description: MutableState<String>,
    amount: MutableState<Float>,
    amountToShow: MutableState<String>,
    type: MutableState<String>,
    typeBoxExpanded: MutableState<Boolean>,
    onEvent: (ExpenseEvent) -> Unit
) {

    val bgColors = mapOf(
        1 to colorResource(id = R.color.green_bg),
        2 to colorResource(id = R.color.red_bg),
        3 to colorResource(id = R.color.pink_bg),
        4 to colorResource(id = R.color.gray_bg),
        5 to colorResource(id = R.color.cream_bg)
    )

    val bgColor = bgColors[(1..5).random()]!!


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {

        // Row with two children as logo, title / desc  and amount
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(colorResource(id = R.color.card_background))
                .height(80.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = CenterVertically
        ) {

            // Logo and title / Desc
            Row {

                // Logo background
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxWidth()
                        .padding(12.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(bgColor)
                        .clickable {
                            typeBoxExpanded.value = !typeBoxExpanded.value
                        }
                ) {

                    // Logo
                    Image(
                        painter = Util.image(type.value),
                        contentDescription = type.value,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .padding(10.dp)

                    )

                }


                // Title and Description
                Column(
                    modifier = Modifier
                        .height(110.dp),
                    verticalArrangement = Arrangement.Center

                ) {


                    // Title text Field (custom)
                    BasicTextField(
                        value = title.value,

                        // Cursor color
                        cursorBrush = SolidColor(Color.Blue),

                        onValueChange = {

                            title.value = it

                        },

                        textStyle = TextStyle(
                            color = colorResource(id = R.color.text),
                            fontFamily = readexPro,
                            fontSize = 12.sp,
                        ),

                        singleLine = true,

                        // Placeholder (Hint)
                        decorationBox = { innerTextField ->

                            // Placeholder Text
                            if (title.value.isEmpty()) {
                                Text(
                                    text = "Enter Title",
                                    fontFamily = readexPro,
                                    fontSize = 12.sp,
                                    color = colorResource(id = R.color.hint_text),
                                )
                            } else {
                                innerTextField()
                            }

                        }

                    )


                    // Description text Field (custom)
                    BasicTextField(
                        value = description.value ?: "",

                        cursorBrush = SolidColor(Color.Blue),

                        onValueChange = {

                            description.value = it

                        },

                        textStyle = TextStyle(
                            color = colorResource(id = R.color.light_text),
                            fontFamily = readexPro,
                            fontSize = 10.sp,
                        ),

                        singleLine = true,

                        // Placeholder (Hint)
                        decorationBox = { innerTextField ->

                            // Placeholder Text
                            if (description.value.isNullOrEmpty()) {
                                Text(
                                    text = "Description (optional)",
                                    fontFamily = readexPro,
                                    fontSize = 10.sp,
                                    color = colorResource(id = R.color.hint_light_text),
                                )
                            } else {
                                innerTextField()
                            }

                        }

                    )

                }
            }



            // Amount Text Field
            BasicTextField(

                modifier = Modifier
                    .padding(end = 24.dp),

                value = amountToShow.value,

                cursorBrush = SolidColor(Color.Blue),

                onValueChange = { newValue ->

                    // Valid Float Input with two decimal places

                    // Allows optional '-' at the start, one decimal point, and up to 2 decimal places
                    val regex = "^-?\\d*(\\.\\d{0,2})?$".toRegex()

                    if (regex.matches(newValue) && !newValue.contains(" ") && !newValue.contains(",")) {

                        amountToShow.value = newValue
                        amount.value = if(newValue in arrayOf("", " ", ".", "-"))
                            -100.0f
                        else
                            newValue.toFloat()

                    }

                },

                textStyle = TextStyle(
                    color = colorResource(id = R.color.main_text),
                    fontFamily = readexPro,
                    fontSize = 14.sp,
                    textAlign = TextAlign.End
                ),

                // Placeholder (Hint)
                decorationBox = { innerTextField ->

                    // Placeholder Text
                    if (amount.value == 0f) {
                        Text(
                            text = "Amount",
                            fontFamily = readexPro,
                            fontSize = 14.sp,
                            color = colorResource(id = R.color.hint_main_text),
                        )
                    } else {
                        innerTextField()
                    }

                },

                singleLine = true,

                // Show numerical keyboard only
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )

            )


        }

    }
}




// Drop down Type Selector
@Composable
fun TypeSelector(
    type: MutableState<String>,
    typeBoxExpanded: MutableState<Boolean>,
    onEvent: (ExpenseEvent) -> Unit
) {

    // List of Types
    val list = listOf("misc", "food", "shopping", "travel", "ent", "grocery", "everyday", "skill")

    val bgColors = mapOf(
        1 to colorResource(id = R.color.green_bg),
        2 to colorResource(id = R.color.red_bg),
        3 to colorResource(id = R.color.pink_bg),
        4 to colorResource(id = R.color.gray_bg),
        5 to colorResource(id = R.color.cream_bg)
    )


    // Giving animation and expand and shrink
    AnimatedVisibility(
        visible = typeBoxExpanded.value,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {


        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .width(80.dp)
                    .height(283.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(colorResource(id = R.color.card_background))
                    .verticalScroll(rememberScrollState()),
            ) {

                // Showing all types (logos only)
                for (item in list) {


                    // Background color
                    val bgColor = bgColors[(1..5).random()]!!

                    Box(
                        modifier = Modifier
                            .padding(top = 12.dp, start = 12.dp, end = 12.dp)
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(15.dp))
                            .background(bgColor)

                            .clickable {
                                // Update state of type and close the selector
                                type.value = item
                                typeBoxExpanded.value = false
                            }

                    ) {

                        // Logo
                        Image(
                            painter = Util.image(item),
                            contentDescription = type.value,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .padding(10.dp)
                        )

                    }

                }


                // Spacer for another type
                Spacer(
                    modifier = Modifier
                        .height(12.dp)
                )

            }
        }
    }
}



// Date Picker
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerUI(
    date: MutableState<Long>,
    onEvent: (ExpenseEvent) -> Unit
) {


    // Some temp States
    val datePickerState = rememberDatePickerState()
    val showDialog = remember { mutableStateOf(false) }


    // Formatted date
    val formattedDate = Util.formatToMonthDayYear(date.value)


    Row(
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {

        // Box to open date picker
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(colorResource(id = R.color.card_background))
                .clickable { showDialog.value = true }
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .align(CenterVertically)
        ) {

            // Showing the selected date
            Text(
                text = formattedDate,
                color = colorResource(id = R.color.text),
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
                            Instant.ofEpochMilli(datePickerState.selectedDateMillis!!)
                                .atZone(ZoneId.systemDefault())
                                .format(formatter)

                        // Set the new date to State
                        date.value = anotherFormattedDate.toLong()

                    }
                ) {
                    Text("OK")
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
    title: MutableState<String>,
    description: MutableState<String>,
    type: MutableState<String>,
    amount: MutableState<Float>,
    date: MutableState<Long>,
    navFilled: MutableState<String>,
    onEvent: (ExpenseEvent) -> Unit
) {

    val isExpense = if (amount.value > 0f) false else true

    // The button
    TextButton(

        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
            .height(60.dp),

        onClick = {

            // Save the Expense if title and amount are not empty
            if (!(title.value.isBlank() || amount.value == 0f)) {


                Log.d("Yes", "Condition satisfies for blank values")
                Log.d("Yes", title.value)

                onEvent(
                    ExpenseEvent.SaveExpense(
                        Expense(
                            title = title.value,
                            description = description.value,
                            type = type.value,
                            amount = amount.value,
                            date = date.value,
                            id = 1
                        )
                    )

                )
                navFilled.value = "home"

            }

        },

        shape = RoundedCornerShape(20.dp),
        colors = ButtonColors(
            containerColor = if (isExpense) colorResource(id = R.color.red)
            else colorResource(id = R.color.lime),
            contentColor = colorResource(id = R.color.black),
            disabledContainerColor = colorResource(id = R.color.red),
            disabledContentColor = colorResource(id = R.color.black)
        )

    ) {

        // Text on the button
        Text(
            text = if (isExpense) "Save Expense" else "Save Earning",
            color = colorResource(id = R.color.black),
            fontSize = 20.sp,
            fontFamily = readexPro
        )

    }
}