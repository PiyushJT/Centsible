package com.piyushjt.centsible

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.room.Room
import com.piyushjt.centsible.ui.theme.CentsibleTheme
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    // Initializing Database
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            ExpenseDatabase::class.java,
            "expense.db"
        ).build()
    }

    // Defining ViewModel and dao
    private val viewModel by viewModels<ExpenseViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return ExpenseViewModel(db.dao) as T
                }
            }
        }
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Edge to Edge -> No Status Bar
        enableEdgeToEdge()

        // Removing action bar if visible
        actionBar?.hide()

        setContent {
            CentsibleTheme {


                // State
                val state by viewModel.state.collectAsState()

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = MainScreen
                ) {

                    composable<MainScreen> {

                        Surface(
                            modifier = Modifier
                                .background(colorResource(id = R.color.background))
                                .fillMaxSize()
                                .padding(top = 42.dp)
                        ) {

                            MainScreen(
                                state = state,
                                onEvent = viewModel::onEvent,
                                navController = navController
                            )

                        }

                    }
                    composable<EditExpenseScreen> {
                        val args = it.toRoute<EditExpenseScreen>()

                        Surface(
                            modifier = Modifier
                                .background(colorResource(id = R.color.background))
                                .fillMaxSize()
                                .padding(top = 42.dp)
                        ) {

                            Log.d("passed id", args.id.toString())

                            EditExpenseScreen(
                                state = state,
                                onEvent = viewModel::onEvent,
                                navController = navController,
                                id = args.id
                            )

                        }

                    }
                }
            }
        }
    }
}



@Serializable
object MainScreen

@Serializable
data class EditExpenseScreen(
    val id: Int
)


// Custom font
val readexPro = FontFamily(
    Font(R.font.readex_pro, FontWeight.Medium)
)



@Composable
fun MainScreen(
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit,
    navController: NavController? = null
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {


        // Different screens for different navigation items
        when (state.navFilled) {

            "home" -> ALlExpenses(
                state = state,
            )

            "stats" -> Stats()

            else -> AddExpense(
                state = state,
                onEvent = onEvent,
            )

        }


        // Bottom Navigation bar
        NavBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            state = state,
            onEvent = onEvent,
            enabled = true
        )

        Button(
            modifier = Modifier,
            onClick = {
                navController?.navigate(EditExpenseScreen(id = 190))
            }
        ) {
            Text(
                text = "Edit"
            )
        }

    }
}


@Composable
fun EditExpenseScreen(
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit,
    navController: NavController? = null,
    id: Int
) {
    Button(
        modifier = Modifier,
        onClick = {
            navController?.popBackStack()
        }
    ) {
        Text(
            text = "$id"
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


// Header for All Expenses
@Composable
fun Header() {

    Column {

        DayDate()

        TotalBalance()

    }

}


// Heading for All Expenses
@Composable
fun Heading() {

    Text(
        modifier = Modifier
            .padding(top = 12.dp, bottom = 18.dp),
        text = "Recent Transactions",
        color = colorResource(id = R.color.heading_text),
        fontSize = 18.sp,
        fontFamily = readexPro
    )

}


// List of All Expenses
@Composable
fun ListOfExpenses(
    state : ExpenseState
) {

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
    ) {

        // Heading as above Composable
        Heading()


        // All Expenses
        for(expense in state.expenses){

            Expense(
                title = expense.title,
                description = expense.description,
                amount = expense.amount,
                type = expense.type
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


// Expense Card
@Composable
fun Expense(
    title: String,
    description: String?,
    amount: Float,
    type: String
) {

    // Image and Background Color
    val image = when (type) {
        "misc" -> painterResource(id = R.drawable.misc)
        "food" -> painterResource(id = R.drawable.food)
        "shopping" -> painterResource(id = R.drawable.shopping_cart)
        "travel" -> painterResource(id = R.drawable.travel)
        "ent" -> painterResource(id = R.drawable.ent_netflix)
        "grocery" -> painterResource(id = R.drawable.grocery)
        "everyday" -> painterResource(id = R.drawable.everyday)
        "skill" -> painterResource(id = R.drawable.skill)
        else -> painterResource(id = R.drawable.shopping_cart)
    }

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
    ) {

        /*

        Row with two children as
            1) Logo & title / Desc
            2) Amount

        */

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(colorResource(id = R.color.card_background))
                .height(80.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = CenterVertically
        ) {


            // Image Logo and Title / Desc
            Row {

                // BG color for logo
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxWidth()
                        .padding(12.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(bgColor)
                ) {

                    // Logo
                    Image(
                        painter = image,
                        contentDescription = type,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .padding(10.dp)

                    )


                }


                // Title and Description
                Column(
                    modifier = Modifier
                        .height(90.dp),
                    verticalArrangement = Arrangement.Center

                ) {

                    // Title
                    Text(
                        text = title,
                        color = colorResource(id = R.color.text),
                        fontSize = 12.sp,
                        fontFamily = readexPro
                    )

                    // Description
                    Text(
                        text = if(description.isNullOrEmpty()) type else description,
                        color = colorResource(id = R.color.light_text),
                        fontSize = 10.sp,
                        fontFamily = readexPro
                    )

                }
            }


            val amountToShow = if(amount < 0) "-₹${-amount}" else "₹$amount"

            // Amount
            Text(
                modifier = Modifier
                    .padding(14.dp),
                text = amountToShow,
                color = colorResource(id = R.color.main_text),
                fontSize = 14.sp,
                fontFamily = readexPro
            )

        }


        // Spacer for another Expense
        Spacer(
            modifier = Modifier
                .height(10.dp)
        )

    }

}


// All Expenses to be shown in a Nav Item
@Composable
fun ALlExpenses(
    state : ExpenseState
) {

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
    ) {

        Header()

        ListOfExpenses(
            state = state
        )
    }
}


// Statistics Screen
@Composable
fun Stats() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    )
}


// Add Expense Screen
@Composable
fun AddExpense(
    state : ExpenseState,
    onEvent: (ExpenseEvent) -> Unit
) {


    val isExpense = if (state.amount > 0) false else true


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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
            state = state,
            onEvent = onEvent
        )


        // Drop Down Type Selector
        TypeSelector(
            state = state,
            onEvent = onEvent
        )


        // Date Picker
        DatePickerUI(
            state = state,
            onEvent = onEvent
        )


        // Save Button
        SaveButton(
            state = state,
            onEvent = onEvent
        )

    }
}


@Composable
fun TopButton(
    onEvent: (ExpenseEvent) -> Unit,
    isDeleteBtn: Boolean,
    add: Boolean,
    navController: NavController? = null
) {

    IconButton(
        modifier = Modifier
            .width(50.dp)
            .height(50.dp),

        onClick = {

            if(isDeleteBtn){

            } else {
                onEvent(ExpenseEvent.ClearState)

                navController?.popBackStack()

            }

        },
        colors = IconButtonColors(

            contentColor =
            if(isDeleteBtn)
                colorResource(id = R.color.red)
            else
                colorResource(id = R.color.main_text),


            disabledContentColor =
            if(isDeleteBtn)
                colorResource(id = R.color.red50)
            else
                colorResource(id = R.color.card_background),

            containerColor = colorResource(id = R.color.card_background),
            disabledContainerColor = colorResource(id = R.color.card_background)

        ),
        enabled = !add

    ) {

        // Icon
        Icon(
            modifier = Modifier
                .height(30.dp)
                .width(30.dp),

            painter =
            if (isDeleteBtn)
                painterResource(id = R.drawable.delete)
            else
                painterResource(id = R.drawable.back),

            contentDescription = ""
        )

    }

}


@Composable
fun EditExpense(
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit
) {

    // Image and Background Color
    val image = when (state.type) {
        "misc" -> painterResource(id = R.drawable.misc)
        "food" -> painterResource(id = R.drawable.food)
        "shopping" -> painterResource(id = R.drawable.shopping_cart)
        "travel" -> painterResource(id = R.drawable.travel)
        "ent" -> painterResource(id = R.drawable.ent_netflix)
        "grocery" -> painterResource(id = R.drawable.grocery)
        "everyday" -> painterResource(id = R.drawable.everyday)
        "skill" -> painterResource(id = R.drawable.skill)
        else -> painterResource(id = R.drawable.shopping_cart)
    }

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
                            onEvent(ExpenseEvent.SetTypeBoxExpanded(!state.typeBoxExpanded))
                        }
                ) {

                    // Logo
                    Image(
                        painter = image,
                        contentDescription = state.type,
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
                        value = state.title,

                        // Cursor color
                        cursorBrush = SolidColor(Color.Blue),

                        onValueChange = {

                            onEvent(ExpenseEvent.SetTitle(it))

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
                            if (state.title.isEmpty()) {
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
                        value = state.description ?: "",

                        cursorBrush = SolidColor(Color.Blue),

                        onValueChange = {

                            onEvent(ExpenseEvent.SetDescription(it))

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
                            if (state.description.isNullOrEmpty()) {
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

                value = state.amountToShow,

                cursorBrush = SolidColor(Color.Blue),

                onValueChange = { newValue ->

                    // Valid Float Input with two decimal places

                    // Allows optional '-' at the start, one decimal point, and up to 2 decimal places
                    val regex = "^-?\\d*(\\.\\d{0,2})?$".toRegex()

                    if (regex.matches(newValue) && !newValue.contains(" ") && !newValue.contains(",")) {
                        onEvent(ExpenseEvent.SetAmount(newValue))
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
                    if (state.amountToShow.isEmpty()) {
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
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit
) {

    // List of Types
    val list = listOf("misc", "food", "shopping", "travel", "ent", "grocery", "everyday", "skill")


    // Images and Background Colors
    val images = mapOf(
        "misc" to painterResource(id = R.drawable.misc),
        "food" to painterResource(id = R.drawable.food),
        "shopping" to painterResource(id = R.drawable.shopping_cart),
        "travel" to painterResource(id = R.drawable.travel),
        "ent" to painterResource(id = R.drawable.ent_netflix),
        "grocery" to painterResource(id = R.drawable.grocery),
        "everyday" to painterResource(id = R.drawable.everyday),
        "skill" to painterResource(id = R.drawable.skill)
    )

    val bgColors = mapOf(
        1 to colorResource(id = R.color.green_bg),
        2 to colorResource(id = R.color.red_bg),
        3 to colorResource(id = R.color.pink_bg),
        4 to colorResource(id = R.color.gray_bg),
        5 to colorResource(id = R.color.cream_bg)
    )


    // Giving animation and expand and shrink
    AnimatedVisibility(
        visible = state.typeBoxExpanded,
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
                                onEvent(ExpenseEvent.SetType(item))
                                onEvent(ExpenseEvent.SetTypeBoxExpanded(false))
                            }

                    ) {

                        // Logo
                        Image(
                            painter = images[item]?: painterResource(id = R.drawable.shopping_cart),
                            contentDescription = state.type,
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
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit
) {


    // Some temp States
    val datePickerState = rememberDatePickerState()
    val showDialog = remember { mutableStateOf(false) }


    // Formatted date
    val formattedDate = Utils.formatToMonthDayYear(state.date)


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

                androidx.compose.material3.Button(

                    onClick = {

                        // Close the dialog
                        showDialog.value = false

                        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                        val anotherFormattedDate =
                            Instant.ofEpochMilli(datePickerState.selectedDateMillis!!)
                                .atZone(ZoneId.systemDefault())
                                .format(formatter)

                        // Set the new date to State
                        onEvent(ExpenseEvent.SetDate(anotherFormattedDate.toLong()))

                    }
                ) {
                    Text("OK")
                }

            },


            dismissButton = {
                androidx.compose.material3.Button(

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
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit
) {

    val isExpense = if (state.amount > 0) false else true

    // The button
    TextButton(

        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
            .height(60.dp),

        onClick = {

            // Save the Expense if title and amount are not empty
            if (!(state.title.isBlank() || state.amountToShow.isBlank())) {

                onEvent(ExpenseEvent.SaveExpense)

                onEvent(ExpenseEvent.ChangeNavState("home"))

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


// Navigation Bar Buttons
@Composable
fun NavBarButton(
    buttonLogo: String,
    filled: String,
    onEvent: (ExpenseEvent) -> Unit
) {


    // Setting the icon (filled/unfilled)
    val icon =

        if(buttonLogo == "home")
            if(buttonLogo == filled)
                 painterResource(id = R.drawable.home_filled)
            else
                painterResource(id = R.drawable.home)

        else if(buttonLogo == "stats")
            if(buttonLogo == filled)
                painterResource(id = R.drawable.stats_filled)
            else
                painterResource(id = R.drawable.stats)

        else
            if(buttonLogo == filled)
                painterResource(id = R.drawable.add_filled)
            else
                painterResource(id = R.drawable.add)


    IconButton(
        modifier = Modifier
            .width(100.dp)
            .height(60.dp),

        onClick = {

            onEvent(ExpenseEvent.ChangeNavState(buttonLogo))

        }

    ) {

        // Icon
        Icon(
            modifier = Modifier
                .height(25.dp)
                .width(25.dp),

            tint = colorResource(id = R.color.main_text),
            painter = icon,

            contentDescription = buttonLogo
        )

    }
}


// Bottom Navigation Bar
@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit,
    enabled: Boolean
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            .background(colorResource(id = R.color.card_background)),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = CenterVertically
        ) {

            // Home
            NavBarButton(
                buttonLogo = "home",
                filled = state.navFilled,
                onEvent = onEvent
            )

            // Statistics (Graph)
            NavBarButton(
                buttonLogo = "stats",
                filled = state.navFilled,
                onEvent = onEvent
            )

            // Add Expense
            NavBarButton(
                buttonLogo = "add",
                filled = state.navFilled,
                onEvent = onEvent
            )

        }


        // Bottom Space for System Navigation
        Box(
            modifier = Modifier
                .height(
                    WindowInsets.navigationBars
                        .asPaddingValues()
                        .calculateBottomPadding()
                )
                .background(Color.Transparent)
        )

    }
}


// Preview
@Preview
@Composable
fun CentsiblePreview() {
    CentsibleTheme {

        Surface(
            modifier = Modifier
                .background(colorResource(id = R.color.background))
                .fillMaxSize()
                .padding(top = 42.dp)
        ) {

            MainScreen(
                state = ExpenseState(
                    expenses = emptyList(),
                    id = -1,
                    title = "title",
                    description = "des",
                    date = 20241206L,
                    type = "good",
                    amount = 100.0f,
                    amountToShow = "100",
                    sortType = SortType.DATE,
                    navFilled = "home",
                    typeBoxExpanded= true
                ),

                onEvent = {},

            )

        }
    }
}