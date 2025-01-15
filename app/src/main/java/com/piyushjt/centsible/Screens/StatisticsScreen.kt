package com.piyushjt.centsible.Screens

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.piyushjt.centsible.Expense
import com.piyushjt.centsible.ExpenseEvent
import com.piyushjt.centsible.Heading
import com.piyushjt.centsible.R
import com.piyushjt.centsible.readexPro
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


// Statistics Screen
@SuppressLint("CoroutineCreationDuringComposition")
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
                map = map,
                statsDuration = statsDuration
            )
        }
        else {
            MonthlyStatsCard(
                onEvent = onEvent,
                map = map,
                statsDuration = statsDuration
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
    map: MutableMap<Long, Float>,
    statsDuration: MutableState<String>,
) {

    val map = (0 until 7).mapIndexed { index, _ ->
        (20240101 + index) to (0 .. 1000).random().toFloat()
    }.toMap()

    val highestVal = map.values.toList().maxOrNull() ?: 0f

    var heightDp by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current


    Box(
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(colorResource(id = R.color.card_background))
            .aspectRatio(1.2f)
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
    map: MutableMap<Long, Float>,
    statsDuration: MutableState<String>,
) {

    val map = (0 until 30).mapIndexed { index, _ ->
        (20240101 + index) to (0 .. 1000).random().toFloat()
    }.toMap()

    val highestVal = map.values.toList().maxOrNull() ?: 0f

    var heightDp by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current


    Box(
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(colorResource(id = R.color.card_background))
            .aspectRatio(1.2f)
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
                                .width(6.dp)
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