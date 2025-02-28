package com.piyushjt.centsible.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.piyushjt.centsible.Expense
import com.piyushjt.centsible.ExpenseEvent
import com.piyushjt.centsible.ExpenseState
import com.piyushjt.centsible.MainScreen
import com.piyushjt.centsible.R
import com.piyushjt.centsible.UI
import com.piyushjt.centsible.UI.readexPro
import com.piyushjt.centsible.ui.theme.CentsibleTheme


// Statistics Screen
@Composable
fun Stats(
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit
) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = CenterHorizontally
    ) {

        // Header
        Text(
            text = "Statistics",
            color = UI.colors("main_text"),
            fontSize = 18.sp,
            fontFamily = readexPro
        )

        ExpensesAverage()

        StatsCard()


    }


}


@Composable
fun ExpensesAverage() {

    Row (
        modifier = Modifier
            .padding(top = 34.dp)
            .fillMaxWidth()
            .background(Color.Transparent),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {

        Column {
            Text(
                text = "Expenses Average",
                color = UI.colors("light_text"),
                fontSize = 14.sp,
                fontFamily = readexPro
            )

            Text(
                text = "₹33,285.00",
                color = UI.colors("main_text"),
                fontSize = 30.sp,
                fontFamily = readexPro
            )

        }

        Icon(
            modifier = Modifier
                .padding(start = 10.dp, bottom = 6.dp)
                .height(18.dp)
                .width(18.dp),

            tint = UI.colors("main_text"),
            painter = painterResource(id = R.drawable.decreased),

            contentDescription = "Decreased"
        )

        Text(
            modifier = Modifier
                .padding(start = 5.dp, bottom = 3.dp),
            text = "20%",
            color = UI.colors("main_text"),
            fontSize = 20.sp,
            fontFamily = readexPro
        )

    }

}



@Composable
fun StatsCard(
) {

    val days = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val curDay = "Thu"
    val values = arrayOf(23, 45, 23, 55, 54, 43, 53)
    var ind = 0;

    val highestVal = values.max()

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
                .padding(horizontal = 24.dp, vertical = 12.dp)
                .fillMaxSize()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {

                for (height in values) {

                    val fraction = height/ highestVal.toFloat()

                    Column(
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = CenterHorizontally
                    ) {

                        Box(
                            modifier = Modifier
                                .defaultMinSize(minHeight = 10.dp)
                                .width(18.dp)
                                .height((heightDp - 60.dp) * fraction)
                                .clip(RoundedCornerShape(26.dp))
                                .background(colorResource(id = R.color.main_text))
                        )

                        Text(
                            modifier = Modifier
                                .padding(top = 4.dp),
                            text = days[ind],
                            color = if (curDay == days[ind])
                                    UI.colors("main_text")
                                else
                                    UI.colors("grey"),
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
                        )

                    ),
                ),
                navFilled = navFilled,
                onEvent = {}
            )
        }

    }
}