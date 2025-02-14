package com.piyushjt.centsible.Screens

import android.util.Log
import android.widget.EditText
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
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
    state: ExpenseState,
    title: MutableState<String>,
    description: MutableState<String>,
    amount: MutableState<Float>,
    type: MutableState<String>,
    typeBoxExpanded: MutableState<Boolean>,
    onEvent: (ExpenseEvent) -> Unit,
    navFilled: MutableState<String>
) {

    val todayDate = Util.getCurrentDate()

    val date = remember { mutableLongStateOf(todayDate) }

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

        Title()

        Description()

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Amount()

            // Date Picker
            DatePickerUI(
                date = date,
                onEvent = onEvent
            )

        }



        // Drop Down Type Selector
        TypeSelector(
            type = type,
            typeBoxExpanded = typeBoxExpanded,
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

        CustomDropdownMenu(
            list = listOf("as", "aif"),
            defaultSelected = "as",
            color = Color.Blue,
            modifier = Modifier,
            onSelected = {}
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

            focusedContainerColor = colorResource(id = R.color.card_background),
            unfocusedContainerColor = colorResource(id = R.color.card_background),

            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,

            focusedTextColor = colorResource(id = R.color.text),
            unfocusedTextColor = colorResource(id = R.color.text),

            cursorColor = colorResource(id = R.color.text)

        ),
        textStyle = TextStyle(
            fontSize = 24.sp
        ),
        shape = RoundedCornerShape(20.dp),
        onValueChange = { value = it },
        placeholder = {
            Text(
                text = "Title",
                fontSize = 24.sp,
                color = colorResource(id = R.color.hint_text)
            )
        },
        maxLines = 1,
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
            .border(0.3.dp, colorResource(id = R.color.hint_text), RoundedCornerShape(20.dp))
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

            focusedContainerColor = colorResource(id = R.color.card_background),
            unfocusedContainerColor = colorResource(id = R.color.card_background),

            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,

            focusedTextColor = colorResource(id = R.color.text),
            unfocusedTextColor = colorResource(id = R.color.text),

            cursorColor = colorResource(id = R.color.text)

        ),
        textStyle = TextStyle(
            fontSize = 18.sp
        ),
        shape = RoundedCornerShape(20.dp),
        onValueChange = { value = it },
        placeholder = {
            Text(
                text = "Description",
                fontSize = 18.sp,
                color = colorResource(id = R.color.hint_text)
            )
        },
        maxLines = 1,
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxWidth()
            .border(0.3.dp, colorResource(id = R.color.hint_text), RoundedCornerShape(20.dp))
    )

}



@Composable
fun Amount(
    modifier: Modifier = Modifier
) {

    var value by remember { mutableStateOf("") }

    TextField(
        value = value,
        colors = TextFieldDefaults.colors(

            focusedContainerColor = colorResource(id = R.color.card_background),
            unfocusedContainerColor = colorResource(id = R.color.card_background),

            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,

            focusedTextColor = colorResource(id = R.color.text),
            unfocusedTextColor = colorResource(id = R.color.text),

            cursorColor = colorResource(id = R.color.text)

        ),
        textStyle = TextStyle(
            fontSize = 18.sp
        ),
        shape = RoundedCornerShape(20.dp),
        onValueChange = { newValue ->
            // Valid Float Input with two decimal places

            // Allows optional '-' at the start, one decimal point, and up to 2 decimal places
            val regex = "^-?\\d*(\\.\\d{0,2})?$".toRegex()


            if (regex.matches(newValue) && !newValue.contains(" ") && !newValue.contains(",")) {

                value = newValue

                value = if(newValue in arrayOf("", " ", "."))
                    ""
                else
                    newValue

            }

            },
        placeholder = {
            Text(
                text = "Amount",
                fontSize = 16.sp,
                color = colorResource(id = R.color.hint_text)
            )
        },

        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),

        maxLines = 1,
        modifier = Modifier
            .fillMaxWidth(0.4f)
            .padding(top = 20.dp)
            .border(0.3.dp, colorResource(id = R.color.hint_text), RoundedCornerShape(20.dp))
    )

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


    // Background color
    val bgColor = bgColors[(1..5).random()]!!


    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = CenterVertically
    ) {


        Box(
            modifier = Modifier
                .width(80.dp)
        ) {

            Row(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .border(
                        0.5.dp, colorResource(id = R.color.hint_text), RoundedCornerShape(20.dp)
                    )
                    .height(80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(colorResource(id = R.color.card_background))
            ) {

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .aspectRatio(1f)
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
                            .aspectRatio(1f)
                            .padding(10.dp)
                            .background(bgColor)
                    )

                }
            }

        }

        // Giving animation and expand and shrink
        AnimatedVisibility(
            visible = typeBoxExpanded.value,
            enter = expandHorizontally(),
            exit = shrinkHorizontally()
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(top = 20.dp)
                        .border(
                            0.5.dp, colorResource(id = R.color.hint_text), RoundedCornerShape(20.dp)
                        )
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(colorResource(id = R.color.card_background))
                        .horizontalScroll(rememberScrollState())
                ) {

                    // Showing all types (logos only)
                    for (item in list) {


                        // Background color
                        val bgColor = bgColors[(1..5).random()]!!

                        Box(
                            modifier = Modifier
                                .padding(8.dp)
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
                                    .fillMaxHeight()
                                    .aspectRatio(1f)
                                    .padding(10.dp)
                            )

                        }

                    }


                    // Spacer for another type
                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                }
            }
        }
    }
}


@Composable
fun CustomDropdownMenu(
    list: List<String>, // Menu Options
    defaultSelected: String, // Default Selected Option on load
    color: Color, // Color
    modifier: Modifier, //
    onSelected: (Int) -> Unit, // Pass the Selected Option
) {
    var selectedIndex by remember { mutableStateOf(0) }
    var expand by remember { mutableStateOf(false) }
    var stroke by remember { mutableStateOf(1) }
    Box(
        modifier
            .padding(8.dp)
            .border(
                border = BorderStroke(stroke.dp, color),
                shape = RoundedCornerShape(4.dp)
            )
            .clickable {
                expand = true
                stroke = if (expand) 2 else 1
            },
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = defaultSelected,
            color = color,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )

        DropdownMenu(
            expanded = expand,
            onDismissRequest = {
                expand = false
                stroke = if (expand) 2 else 1
            },
            properties = PopupProperties(
                focusable = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
            ),
            modifier = Modifier
                .background(Color.White)
                .padding(2.dp)
                .fillMaxWidth(.4f)
        ) {
            list.forEachIndexed { index, item ->
                DropdownMenuItem(
                    onClick = {
                        selectedIndex = index
                        expand = false
                        stroke = if (expand) 2 else 1
                        onSelected(selectedIndex)
                    },
                    text = {
                        Text(
                            text = "hi"
                        )
                    },
                    modifier = Modifier,
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
                .border(0.3.dp, colorResource(id = R.color.hint_text), RoundedCornerShape(20.dp))
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
                            Instant.ofEpochMilli(datePickerState.selectedDateMillis?: System.currentTimeMillis())
                                .atZone(ZoneId.systemDefault())
                                .format(formatter)

                        // Set the new date to State
                        date.value = anotherFormattedDate.toLong()

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


    @Preview
    @Composable
    private fun AddScreenPreview() {

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
                val navFilled = remember { mutableStateOf("add") }
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