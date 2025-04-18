package com.piyushjt.centsible.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.piyushjt.centsible.Expense
import com.piyushjt.centsible.ExpenseCard
import com.piyushjt.centsible.ExpenseEvent
import com.piyushjt.centsible.ExpenseState
import com.piyushjt.centsible.MainScreen
import com.piyushjt.centsible.R
import com.piyushjt.centsible.Types
import com.piyushjt.centsible.UI
import com.piyushjt.centsible.UI.readexPro
import com.piyushjt.centsible.Util
import com.piyushjt.centsible.ui.theme.CentsibleTheme
import kotlin.math.abs


// Statistics Screen
@Composable
fun Stats(
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit,
    navFilled: MutableState<String>
) {

    BackHandler {
        navFilled.value = "home"
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = CenterHorizontally
    ) {

        // Header
        Text(
            text = UI.strings("statistics"),
            color = UI.colors("main_text"),
            fontSize = 18.sp,
            fontFamily = readexPro
        )

        TotalExpense(
            state = state
        )

        StatsCard(
            state = state,
            onEvent = onEvent
        )

        ExpensesInPeriod(
            state = state
        )


    }


}


@Composable
fun TotalExpense(
    state: ExpenseState
) {

    val amount = state.amountsInPeriod.sum()
    val lastAmount = state.amountsInLastPeriod.sum()

    val percentageChange =
        if (lastAmount == 0f)
            100
        else
            ((amount - lastAmount) / lastAmount * 100).toInt()


    Column (
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
            .background(Color.Transparent),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Bottom
    ) {

        Column {
            Text(
                text = UI.strings("total_expense"),
                color = UI.colors("light_text"),
                fontSize = 14.sp,
                fontFamily = readexPro
            )
        }

        Row (
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ) {

            Text(
                text = Util.formatInIndianSystem(amount),
                color = UI.colors("main_text"),
                fontSize = 30.sp,
                fontFamily = readexPro
            )


            Icon(
                modifier = Modifier
                    .padding(start = 10.dp, bottom = 6.dp)
                    .height(22.dp)
                    .width(22.dp),

                tint =
                if (percentageChange > 0)
                    UI.colors("red")
                else
                    UI.colors("lime"),
                painter = painterResource(
                    id =
                    if (percentageChange > 0)
                        R.drawable.increased
                    else
                        R.drawable.decreased
                ),

                contentDescription =
                    if (percentageChange > 0)
                        UI.strings("increased")
                    else
                        UI.strings("decreased")
            )

            Text(
                modifier = Modifier
                    .padding(start = 5.dp, bottom = 3.dp),
                text = "${abs(percentageChange)}%",
                color = UI.colors("main_text"),
                fontSize = 18.sp,
                fontFamily = readexPro
            )

        }
    }

}




@Composable
fun PeriodChangeButton(
    state: ExpenseState,
    next: Boolean,
    onEvent: (ExpenseEvent) -> Unit
) {


    IconButton(
        modifier = Modifier
            .size(40.dp),

        onClick = {

            val dateForPeriod = state.dateForPeriod

            val newDateForPeriod =
                if (next)
                    Util.getNextWeekDate(dateForPeriod)
                else
                    Util.getPreviousWeekDate(dateForPeriod)

            onEvent(ExpenseEvent.ChangeDateForPeriod(newDateForPeriod))

        },

        colors = IconButtonColors(
            contentColor = UI.colors("main_text"),
            disabledContentColor = UI.colors("background"),
            containerColor = UI.colors("background"),
            disabledContainerColor = UI.colors("background")
        )

    ) {

        // Icon
        Icon(
            modifier = Modifier
                .height(30.dp)
                .width(30.dp),

            painter =  painterResource(
                id = if (next) R.drawable.forward else R.drawable.back
            ),

            contentDescription =
                if (next)
                    UI.strings("next_week")
                else
                    UI.strings("previous_week")

        )

    }


}


@Composable
fun StatsCard(
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit
) {

    val dates = Util.getWeekDates(state.dateForPeriod)
    val previousWeekDates = Util.getWeekDates(
        Util.getPreviousWeekDate(state.dateForPeriod)
    )

    onEvent(ExpenseEvent.SetAmounts(dates))
    onEvent(ExpenseEvent.SetLastAmounts(previousWeekDates))

    onEvent(ExpenseEvent.ChangePeriod(dates.first(), dates.last()))

    val days = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val values = state.amountsInPeriod
    var ind = 0

    val highestVal = values.maxOrNull() ?: 0f

    var heightDp by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current


    Column (
        modifier = Modifier
            .padding(top = 16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(colorResource(id = R.color.card_background))
            .aspectRatio(1.274f)
            .onGloballyPositioned { coordinates ->
                heightDp = with(density) { coordinates.size.height.toDp() }
            }

    ) {

        Row (
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, top = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {

            val week =
                if (state.dateForPeriod == Util.getCurrentDate())
                    UI.strings("this_week")
                else if (state.dateForPeriod == Util.getPreviousWeekDate(Util.getCurrentDate()))
                    UI.strings("last_week")
                else
                    getTextOfWeek(state.dateForPeriod)



            Text(
                text = week,
                color = UI.colors("main_text"),
                fontSize = 18.sp,
                fontFamily = readexPro
            )

            Row (
                modifier = Modifier
                    .width(100.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                PeriodChangeButton(
                    state = state,
                    next = false,
                    onEvent = onEvent
                )

                PeriodChangeButton(
                    state = state,
                    next = true,
                    onEvent = onEvent
                )

            }
        }

        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .fillMaxSize(),
        ) {

            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {

                for (height in values) {

                    val fraction = height/ highestVal

                    Column(
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = CenterHorizontally
                    ) {

                        Box(
                            modifier = Modifier
                                .defaultMinSize(minHeight = 0.dp)
                                .width(18.dp)
                                .height((heightDp - 110.dp) * fraction)
                                .clip(RoundedCornerShape(26.dp))
                                .background(colorResource(id = R.color.main_text))
                        )

                        Text(
                            modifier = Modifier
                                .padding(top = 4.dp),
                            text = days[ind],
                            color = UI.colors("grey"),
                            fontSize = 10.sp,
                            fontFamily = readexPro
                        )
                        ind++
                    }
                }
            }

        }

    }

}


@Composable
fun ExpensesInPeriod(
    state: ExpenseState,
    navController: NavController? = null
) {

    val bottomPadding = 90.dp + Util.getBottomPadding()


    Column(
        modifier = Modifier
            .padding(top = 24.dp, bottom = bottomPadding)
            .verticalScroll(rememberScrollState()),
    ) {

        // All Expenses
        for (expense in state.expensesInPeriod) {

            ExpenseCard(
                expense = expense,
                navController = navController
            )

        }

    }

}



fun getTextOfWeek(
    periodDate: Long
): String {
    val firstDate = Util.getWeekDates(periodDate).first()
    val lastDate = Util.getWeekDates(periodDate).last()

    val fDay = (firstDate % 100).toString().padStart(2, '0') // Extract last two digits (DD)
    val fM = ((firstDate / 100) % 100).toString().padStart(2, '0') // Extract middle two digits (MM)

    val lDay = (lastDate % 100).toString().padStart(2, '0') // Extract last two digits (DD)
    val lM = ((lastDate / 100) % 100).toString().padStart(2, '0') // Extract middle two digits (MM)

    return "$fDay/$fM - $lDay/$lM"

}



@Preview
@Composable
private fun StatsScreenPreview() {

    CentsibleTheme {
        Surface(
            modifier = Modifier
                .background(UI.colors("background"))
                .fillMaxSize()
                .padding(top = 42.dp)
        ) {

            val navFilled = remember { mutableStateOf("stats") }

            MainScreen(
                state = ExpenseState(
                    expensesInPeriod = listOf(
                        Expense(
                            title = "Title",
                            description = "Desc",
                            type = Types.ENT.type,
                            amount = -100.0f,
                            date = 20241231L,
                            id = 1
                        ),

                        Expense(
                            title = "Title",
                            description = "Desc",
                            type = Types.ENT.type,
                            amount = -100.0f,
                            date = 20241231L,
                            id = 1
                        )

                    ),
                    dateForPeriod = Util.getCurrentDate(),
                    amountsInPeriod = listOf(100f, 200f, 300f, 400f, 500f, 600f, 700f),
                    amountsInLastPeriod = listOf(10f, 20f, 30f, 40f, 50f, 60f, 70f)
                ),
                navFilled = navFilled,
                onEvent = {}
            )
        }

    }
}