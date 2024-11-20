package com.piyushjt.centsible

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import androidx.room.Room
import com.piyushjt.centsible.ui.theme.CentsibleTheme
import java.text.SimpleDateFormat
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
        enableEdgeToEdge()

        setContent {
            CentsibleTheme {


                val state by viewModel.state.collectAsState()


                Surface(
                    modifier = Modifier
                        .background(colorResource(id = R.color.background))
                        .fillMaxSize()
                        .padding(top = 42.dp)
                ) {

                    MainScreen(
                        state = state,
                        onEvent = viewModel::onEvent
                    )

                }
            }
        }
    }
}


// Custom font
val readexPro = FontFamily(
    Font(R.font.readex_pro, FontWeight.Medium)
)


@Composable
fun MainScreen(
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit
) {

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        when (state.navFilled) {
            "home" -> ALlExpenses(
                state = state
            )
            "stats" -> Stats()
            else -> AddExpense(
                state = state,
                onEvent = onEvent
            )
        }


        NavBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            state = state,
            onEvent = onEvent
        )

    }
}


@Composable
fun DayDate() {

    Column {

        Text(
            text = SimpleDateFormat("EEEE,", Locale.getDefault()).format(Date()),
            color = colorResource(id = R.color.light_text),
            fontSize = 14.sp,
            fontFamily = readexPro
        )

        Text(
            text = SimpleDateFormat("dd MMMM", Locale.getDefault()).format(Date()),
            color = colorResource(id = R.color.main_text),
            fontSize = 18.sp,
            fontFamily = readexPro
        )
    }


}


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

        Text(
            text = "\$521,985.00",
            color = colorResource(id = R.color.lime),
            fontSize = 34.sp,
            fontFamily = readexPro
        )
    }


}


@Composable
fun Header() {

    Column {

        DayDate()

        TotalBalance()

    }

}


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


@Composable
fun ListOfExpenses(
    state : ExpenseState
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
    ) {

        Heading()

        for(expense in state.expenses){

            Expense(
                title = expense.title,
                description = expense.description,
                amount = expense.amount,
                type = expense.type,
                )

        }

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


@Composable
fun Expense(
    title: String,
    description: String?,
    amount: Float,
    type: String
) {

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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(colorResource(id = R.color.card_background))
                .height(80.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row {
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxWidth()
                        .padding(12.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(bgColor)
                ) {


                    Image(
                        painter = image,
                        contentDescription = type,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .padding(10.dp)

                    )


                }

                Column(
                    modifier = Modifier
                        .height(90.dp),
                    verticalArrangement = Arrangement.Center

                ) {

                    Text(
                        text = title,
                        color = colorResource(id = R.color.text),
                        fontSize = 14.sp,
                        fontFamily = readexPro
                    )

                    Text(
                        text = if(description.isNullOrEmpty()) type else description,
                        color = colorResource(id = R.color.light_text),
                        fontSize = 12.sp,
                        fontFamily = readexPro
                    )
                }
            }


            Text(
                modifier = Modifier
                    .padding(14.dp),
                text = "₹${amount}",
                color = colorResource(id = R.color.main_text),
                fontSize = 16.sp,
                fontFamily = readexPro
            )

        }

        Spacer(
            modifier = Modifier
                .height(10.dp)
        )

    }

}


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


@Composable
fun Stats() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan)
    )
}


@Composable
fun AddExpense(
    state : ExpenseState,
    onEvent: (ExpenseEvent) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Header
        Text(
            text = "Add an Expense",
            color = colorResource(id = R.color.main_text),
            fontSize = 18.sp,
            fontFamily = readexPro
        )

        EditExpense(
            state = state,
            onEvent = onEvent
        )

    }
}


@Composable
fun EditExpense(
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit
) {

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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(colorResource(id = R.color.card_background))
                .height(80.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row {
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .fillMaxWidth()
                        .padding(12.dp)
                        .clip(RoundedCornerShape(15.dp))
                        .background(bgColor)
                ) {


                    Image(
                        painter = image,
                        contentDescription = state.type,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .padding(10.dp)

                    )


                }

                Column(
                    modifier = Modifier
                        .height(110.dp),
                    verticalArrangement = Arrangement.Center

                ) {

                    BasicTextField(
                        value = state.title,
                        onValueChange = {

                            onEvent(ExpenseEvent.SetTitle(it))

                        },

                        textStyle = TextStyle(
                            color = colorResource(id = R.color.text),
                            fontFamily = readexPro,
                            fontSize = 16.sp,
                        ),
                        singleLine = true,


                        decorationBox = { innerTextField ->
                            // Placeholder Text
                            if (state.title.isEmpty()) {
                                Text(
                                    text = "Enter Title",
                                    fontFamily = readexPro,
                                    fontSize = 16.sp,
                                    color = colorResource(id = R.color.hint_text),
                                )
                            } else {
                                innerTextField()
                            }

                        },

                        )


                    BasicTextField(
                        value = state.description ?: "",
                        onValueChange = {

                            onEvent(ExpenseEvent.SetDescription(it))

                        },

                        textStyle = TextStyle(
                            color = colorResource(id = R.color.light_text),
                            fontFamily = readexPro,
                            fontSize = 14.sp,
                        ),
                        singleLine = true,


                        decorationBox = { innerTextField ->
                            // Placeholder Text
                            if (state.description.isNullOrEmpty()) {
                                Text(
                                    text = "Description (optional)",
                                    fontFamily = readexPro,
                                    fontSize = 14.sp,
                                    color = colorResource(id = R.color.hint_light_text),
                                )
                            } else {
                                innerTextField()
                            }

                        },

                        )

                }
            }



            BasicTextField(
                modifier = Modifier
                    .padding(end = 24.dp),
                value = state.amountToShow,
                onValueChange = { newValue ->

                    // Validation for valid float input
                    val regex =
                        "^-?\\d*(\\.\\d{0,2})?$".toRegex() // Allows optional '-' at the start, one decimal point, and up to 2 decimal places
                    if (regex.matches(newValue) && !newValue.contains(" ") && !newValue.contains(",")) {
                        onEvent(ExpenseEvent.SetAmount(newValue))
                    }

                },
                textStyle = TextStyle(
                    color = colorResource(id = R.color.main_text),
                    fontFamily = readexPro,
                    fontSize = 16.sp,
                    textAlign = TextAlign.End
                ),

                decorationBox = { innerTextField ->
                    // Placeholder Text
                    if (state.title.isEmpty()) {
                        Text(
                            text = "Amount",
                            fontFamily = readexPro,
                            fontSize = 16.sp,
                            color = colorResource(id = R.color.hint_main_text),
                        )
                    } else {
                        innerTextField()
                    }

                },

                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )


        }

        Spacer(
            modifier = Modifier
                .height(30.dp)
        )


        TextButton(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            onClick = {
                onEvent(ExpenseEvent.SaveExpense)
            },
            shape = RoundedCornerShape(20.dp),
            colors = ButtonColors(
                containerColor = colorResource(id = R.color.logo_theme),
                contentColor = colorResource(id = R.color.lime),
                disabledContainerColor = colorResource(id = R.color.logo_theme),
                disabledContentColor = colorResource(id = R.color.lime)
            )
        ) {


            Text(
                text = "Save Expense",
                color = colorResource(id = R.color.lime),
                fontSize = 20.sp,
                fontFamily = readexPro
            )

        }

    }
}


@Composable
fun NavBarButton(
    buttonLogo: String,
    filled: String,
    onEvent: (ExpenseEvent) -> Unit
) {

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

        },
    ) {
        Icon(
            tint = colorResource(id = R.color.main_text),
            painter = icon,
            modifier = Modifier
                .height(25.dp)
                .width(25.dp),
            contentDescription = "icon"
        )
    }
}


@Composable
fun NavBar(
    modifier: Modifier = Modifier,
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavBarButton(
                buttonLogo = "home",
                filled = state.navFilled,
                onEvent = onEvent
            )
            NavBarButton(
                buttonLogo = "stats",
                filled = state.navFilled,
                onEvent = onEvent
            )
            NavBarButton(
                buttonLogo = "add",
                filled = state.navFilled,
                onEvent = onEvent
            )
        }

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


@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
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
                    date = 30072007,
                    type = "good",
                    amount = -100.0f,
                    amountToShow = "-100",
                    sortType = SortType.DATE,
                    navFilled = "add"
                ),
                onEvent = {}
            )

        }
    }
}