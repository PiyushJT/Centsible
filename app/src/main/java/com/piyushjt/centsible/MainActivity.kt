package com.piyushjt.centsible

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.math.exp

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

            var expenses = remember { mutableStateOf(emptyList<Expense>()) }
            var title = remember { mutableStateOf("Title") }
            var description = remember { mutableStateOf("Desc") }
            var type = remember { mutableStateOf("ent") }
            var amount = remember { mutableFloatStateOf(-100.0f) }
            var date = remember { mutableStateOf(20241231L) }
            var id = remember { mutableStateOf(-1) }

            var navFilled = remember { mutableStateOf("add") }

            var typeBoxExpanded = remember { mutableStateOf(false) }

            var amountToShow = remember { mutableStateOf("-100") }

            var statsDate = remember { mutableLongStateOf(Util.getCurrentDate()) }
            var statsDuration = remember { mutableStateOf("Weekly") }
            var map = remember { mutableStateOf(mutableMapOf<Long, Float>()) }
            var startDate = remember { mutableLongStateOf(20241223) }
            var endDate = remember { mutableLongStateOf(20241229) }

            CentsibleTheme {
                
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = MainScreen
                ) {

                    // Main screen (set content)
                    composable<MainScreen> {

                        Surface(
                            modifier = Modifier
                                .background(colorResource(id = R.color.background))
                                .fillMaxSize()
                                .padding(top = 42.dp)
                        ) {

                            MainScreen(
                                expenses = expenses,
                                title = title,
                                description = description,
                                type = type,
                                amount = amount,
                                amountToShow = amountToShow,
                                date = date,
                                id = id,
                                startDate = startDate,
                                endDate = endDate,
                                typeBoxExpanded = typeBoxExpanded,
                                statsDuration = statsDuration,
                                navFilled = navFilled,
                                onEvent = viewModel::onEvent,
                                navController = navController
                            )

                        }

                    }

                    // Edit Expense Screen
                    composable<EditExpenseScreen> {
                        val args = it.toRoute<EditExpenseScreen>()

                        Surface(
                            modifier = Modifier
                                .background(colorResource(id = R.color.background))
                                .fillMaxSize()
                                .padding(top = 42.dp)
                        ) {

                            EditExpenseScreen(
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
    expenses: MutableState<List<Expense>>,
    title: MutableState<String>,
    description: MutableState<String>,
    type: MutableState<String>,
    amount: MutableState<Float>,
    amountToShow: MutableState<String>,
    date: MutableState<Long>,
    id: MutableState<Int>,
    navFilled: MutableState<String>,

    typeBoxExpanded: MutableState<Boolean>,

    startDate: MutableState<Long>,
    endDate: MutableState<Long>,
    statsDuration: MutableState<String>,

    onEvent: (ExpenseEvent) -> Unit,
    navController: NavController
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {


        // Different screens for different navigation items
        when (navFilled.value) {

            "home" -> ALlExpenses(
                expenses = expenses,
                navController = navController
            )

            "stats" -> Stats(
                startDate = startDate,
                endDate = endDate,
                expenses = expenses,
                statsDuration = statsDuration,
                onEvent = onEvent
            )

            else -> AddExpense(
                title = title,
                description = description,
                type = type,
                date = date,
                typeBoxExpanded = typeBoxExpanded,
                amount = amount,
                amountToShow = amountToShow,
                navFilled = navFilled,
                onEvent = onEvent
            )

        }


        // Bottom Navigation bar
        NavBar(
            navFilled = navFilled,
            modifier = Modifier.align(Alignment.BottomCenter),
            onEvent = onEvent,
            enabled = true
        )

    }
}


// Edit Expense Screen
@Composable
fun EditExpenseScreen(
    title: MutableState<String>,
    description: MutableState<String>,
    type: MutableState<String>,
    amount: MutableState<Float>,
    amountToShow: MutableState<String>,
    date: MutableState<Long>,
    id: MutableState<Int>,
    expenses: MutableState<List<Expense>>,

    isDialogVisible: MutableState<Boolean>,
    navFilled: MutableState<String>,

    typeBoxExpanded: MutableState<Boolean>,

    onEvent: (ExpenseEvent) -> Unit,
    navController: NavController? = null,
    idToEdit: Int
) {

    LaunchedEffect(key1 = idToEdit) {
        expenses.value.find { it.id == idToEdit }?.let { expense ->

            title.value = expense.title
            description.value = expense.description ?: ""
            type.value = expense.type
            amount.value = expense.amount
            amountToShow.value = expense.amount.toString()
            date.value = expense.date
            id.value = idToEdit

        }
    }


    val isExpense = if (amount.value > 0) false else true


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = CenterVertically
        ) {

            BackButton(
                onEvent = onEvent,
                navController = navController
            )

            // Header
            Text(
                text = if (isExpense) "Edit Expense" else "Edit Earning",
                color = colorResource(id = R.color.main_text),
                fontSize = 18.sp,
                fontFamily = readexPro
            )

            DeleteButton(
                expenses = expenses,
                id = idToEdit,
                isDialogVisible = isDialogVisible,
                onEvent = onEvent,
                navController = navController
            )

        }

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


// Heading for Expenses
@Composable
fun Heading(
    text: String
) {

    Text(
        modifier = Modifier
            .padding(top = 12.dp, bottom = 18.dp),
        text = text,
        color = colorResource(id = R.color.heading_text),
        fontSize = 18.sp,
        fontFamily = readexPro
    )

}


// List of All Expenses
@Composable
fun ListOfExpenses(
    expenses: MutableState<List<Expense>>,
    navController: NavController
) {

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
    ) {

        // Heading as above Composable
        Heading(text = "Recent Transactions")


        // All Expenses
        for(expense in expenses.value){

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


// List of All Expenses
@Composable
fun ListOfConstrainedExpenses(
    startDate: MutableState<Long>,
    endDate: MutableState<Long>,
    expenses: MutableState<List<Expense>>,
    onEvent: (ExpenseEvent) -> Unit
) {

    Column {

        // Heading as above Composable
        Heading(text = "Categories")

        val list = listOf("misc", "food", "shopping", "travel", "ent", "grocery", "everyday", "skill")

        for(type in list){

            val typeExpense = mutableListOf<Expense>()
            var totalAmount = 0.0f

            for(expense in expenses.value) {

                if (expense.date in startDate.value .. endDate.value && expense.type == type && expense.amount < 0.0f) {

                    typeExpense.add(expense)
                    totalAmount += expense.amount

                }

            }


            if(totalAmount != 0.0f) {
                Expense(
                    id = -2,
                    title = type,
                    description = "${typeExpense.size} expenses",
                    amount = totalAmount,
                    type = type
                )

            }
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
    id: Int,
    title: String,
    description: String?,
    amount: Float,
    type: String,
    navController: NavController? = null
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
                .height(80.dp)
                .clickable {

                    navController?.navigate(EditExpenseScreen(id))

                },
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
                        painter = Util.image(type),
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



// Navigation Items

// All Expenses to be shown in a Nav Item
@Composable
fun ALlExpenses(
    expenses: MutableState<List<Expense>>,
    navController: NavController
) {

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
    ) {

        Header()

        ListOfExpenses(
            expenses = expenses,
            navController = navController
        )
    }
}


// Statistics Screen
@Composable
fun Stats(
    startDate: MutableState<Long>,
    endDate: MutableState<Long>,
    expenses: MutableState<List<Expense>>,
    statsDuration: MutableState<String>,
    onEvent: (ExpenseEvent) -> Unit
) {

    val map = mutableMapOf<Long, Float>()

    for (date in startDate.value .. endDate.value){

        var amount = 0f

        for(expense in expenses.value){

            if(expense.date == date){
                amount -= expense.amount
            }

            map[date] = amount
        }
    }

    // TODO: onEvent(ExpenseEvent.SetMap(map))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        // Header
        Text(
            text = "Statistics",
            color = colorResource(id = R.color.main_text),
            fontSize = 18.sp,
            fontFamily = readexPro
        )

        ExpensesAverage()

        if(statsDuration.value == "Weekly") {

            WeeklyStatsCard(
                onEvent = onEvent,
                map = map
            )
        }
        else {
            MonthlyStatsCard(
                onEvent = onEvent,
                map = map
            )
        }

        AnimatedSelector(
            statsDuration = statsDuration,
            onEvent = onEvent
        )

        ListOfConstrainedExpenses(
            startDate = startDate,
            endDate = endDate,
            expenses = expenses,
            onEvent = onEvent
        )

    }

}


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


@Composable
fun ExpensesAverage() {

    Row (
        modifier = Modifier
            .padding(top = 34.dp)
            .fillMaxWidth()
            .background(colorResource(id = R.color.trans)),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {

        Column {
            Text(
                text = "Expenses Average",
                color = colorResource(id = R.color.light_text),
                fontSize = 14.sp,
                fontFamily = readexPro
            )

            Text(
                text = "₹33,285.00",
                color = colorResource(id = R.color.main_text),
                fontSize = 30.sp,
                fontFamily = readexPro
            )

        }

        Icon(
            modifier = Modifier
                .padding(start = 10.dp, bottom = 6.dp)
                .height(18.dp)
                .width(18.dp),

            tint = colorResource(id = R.color.main_text),
            painter = painterResource(id = R.drawable.decreased),

            contentDescription = "Decreased"
        )

        Text(
            modifier = Modifier
                .padding(start = 5.dp, bottom = 3.dp),
            text = "20%",
            color = colorResource(id = R.color.main_text),
            fontSize = 20.sp,
            fontFamily = readexPro
        )

    }

}


@Composable
fun WeeklyStatsCard(
    onEvent: (ExpenseEvent) -> Unit,
    map: MutableMap<Long, Float>
) {

    val highestVal = map.values.toList().maxOrNull() ?: 0f

    var heightDp by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current


    Box(
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(colorResource(id = R.color.card_background))
            .aspectRatio(1.374f)
            .onGloballyPositioned { coordinates ->
                heightDp = with(density) { coordinates.size.height.toDp() }
            }

    ) {

        Box(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                val days = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                var ind = 0;

                for (height in map.values) {

                    val fraction = height/ highestVal

                    Column(
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = CenterHorizontally
                    ) {

                        Box(
                            modifier = Modifier
                                .defaultMinSize(minHeight = 10.dp)
                                .width(18.dp)
                                .height((heightDp - 80.dp) * fraction)
                                .clip(RoundedCornerShape(26.dp))
                                .background(colorResource(id = R.color.main_text))
                        )

                        Text(
                            modifier = Modifier
                                .padding(top = 4.dp),
                            text = days[ind],
                            color = colorResource(id = R.color.light_text),
                            fontSize = 10.sp,
                            fontFamily = readexPro
                        )
                    }
                    ind++
                }
            }

        }

    }

}



@Composable
fun MonthlyStatsCard(
    onEvent: (ExpenseEvent) -> Unit,
    map: MutableMap<Long, Float>
) {

    val highestVal = map.values.toList().maxOrNull() ?: 0f

    var heightDp by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current


    Box(
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(colorResource(id = R.color.card_background))
            .aspectRatio(1.374f)
            .onGloballyPositioned { coordinates ->
                heightDp = with(density) { coordinates.size.height.toDp() }
            }

    ) {

        Box(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxSize()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {

                for (height in map.values) {

                    val fraction = height/ highestVal

                    Column(
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = CenterHorizontally
                    ) {

                        Box(
                            modifier = Modifier
                                .defaultMinSize(minHeight = 10.dp)
                                .width(18.dp)
                                .height((heightDp - 50.dp) * fraction)
                                .clip(RoundedCornerShape(26.dp))
                                .background(colorResource(id = R.color.main_text))
                        )

                    }
                }
            }

        }

    }

}





@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun AnimatedSelector(
    statsDuration: MutableState<String>,
    onEvent: (ExpenseEvent) -> Unit
) {

    var widthDP by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current


    Box(
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(colorResource(id = R.color.card_background))
            .aspectRatio(8.175f)
            .onGloballyPositioned { coordinates ->
                widthDP = with(density) { coordinates.size.width.toDp() }
            }

    ) {


        // Animation for the selector position
        val selectorPosition by animateDpAsState(
            targetValue = if (statsDuration.value == "Weekly") 0.dp else (widthDP / 2),
            animationSpec = tween(durationMillis = 150)
        )


        // Selector background
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
                .offset(x = selectorPosition)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxHeight(0.75f)
                    .fillMaxWidth(0.92f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(colorResource(id = R.color.main_text))
                    .align(Alignment.Center)
            )

        }

        // Options
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Weekly Button
            Text(
                text = "Weekly",
                color = if (statsDuration.value == "Weekly") colorResource(id = R.color.card_background) else colorResource(id = R.color.light_text),
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        indication = null, // Remove ripple effect
                        interactionSource = remember { MutableInteractionSource() } // Suppress interaction feedback
                    ) {
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(150)
                            statsDuration.value = "Weekly"
                        }
                    },
                textAlign = TextAlign.Center
            )

            // Monthly Button
            Text(
                text = "Monthly",
                color = if (statsDuration.value == "Monthly") colorResource(id = R.color.card_background) else colorResource(id = R.color.light_text),
                modifier = Modifier
                    .weight(1f)
                    .clickable(
                        indication = null, // Remove ripple effect
                        interactionSource = remember { MutableInteractionSource() } // Suppress interaction feedback
                    ) {
                        CoroutineScope(Dispatchers.Main).launch {
                            delay(150)
                            statsDuration.value = "Monthly"
                        }
                    },
                textAlign = TextAlign.Center
            )
        }
    }
}




// Back button
@Composable
fun BackButton(
    onEvent: (ExpenseEvent) -> Unit,
    navController: NavController? = null
) {

    IconButton(
        modifier = Modifier
            .width(50.dp)
            .height(50.dp),

        onClick = {

            // TODO: onEvent(ExpenseEvent.ClearState)

            navController?.popBackStack()

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
    navController: NavController? = null
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

            navController?.popBackStack()

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


// Navigation Bar Buttons
@Composable
fun NavBarButton(
    navFilled: MutableState<String>,
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

            navFilled.value = buttonLogo

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
    navFilled: MutableState<String>,
    modifier: Modifier = Modifier,
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
                navFilled = navFilled,
                buttonLogo = "home",
                filled = navFilled.value,
                onEvent = onEvent
            )

            // Statistics (Graph)
            NavBarButton(
                navFilled = navFilled,
                buttonLogo = "stats",
                filled = navFilled.value,
                onEvent = onEvent
            )

            // Add Expense
            NavBarButton(
                navFilled = navFilled,
                buttonLogo = "add",
                filled = navFilled.value,
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