package com.piyushjt.centsible

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
import com.piyushjt.centsible.Screens.ALlExpenses
import com.piyushjt.centsible.Screens.AddExpense
import com.piyushjt.centsible.Screens.EditExpenseScreen
import com.piyushjt.centsible.Screens.Stats
import com.piyushjt.centsible.ui.theme.CentsibleTheme
import kotlinx.serialization.Serializable

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
                                expense = Expense(
                                    title = args.title,
                                    description = args.description,
                                    type = args.type,
                                    amount = args.amount,
                                    id = args.id,
                                    date = args.date,
                                ),
                                navController = navController
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
    val title: String,
    val description: String?,
    val type: String,
    val amount: Float,
    val date: Long,
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

                    navController?.navigate(
                        EditExpenseScreen(
                                title = title,
                                description = description,
                                type = type,
                                amount = amount,
                                date = 20240101L,
                                id = id
                        )
                    )

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