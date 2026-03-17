package com.piyushjt.centsible.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.piyushjt.centsible.Expense
import com.piyushjt.centsible.ExpenseCard
import com.piyushjt.centsible.ExpenseEvent
import com.piyushjt.centsible.ExpenseState
import com.piyushjt.centsible.PeriodType
import com.piyushjt.centsible.R
import com.piyushjt.centsible.Types
import com.piyushjt.centsible.UI
import com.piyushjt.centsible.UI.readexPro
import com.piyushjt.centsible.Util
import com.piyushjt.centsible.ui.theme.CentsibleTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs
import kotlin.math.absoluteValue


// Statistics Screen
@Composable
fun Stats(
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit,
    navController: NavController,
) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalAlignment = CenterHorizontally
    ) {

        // Header
        Text(
            modifier = Modifier
                .padding(bottom = 12.dp),
            text = UI.strings("statistics"),
            color = UI.colors("main_text"),
            fontSize = 18.sp,
            fontFamily = readexPro
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = CenterHorizontally
        ) {

            TotalExpense(
                state = state
            )

            StatsCard(
                state = state,
                onEvent = onEvent
            )

            PeriodSelector(
                state = state,
                onEvent = onEvent
            )

            ExpensesInPeriod(
                state = state,
                navController = navController
            )

        }
    }
}


@Composable
fun PeriodSelector(
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth(0.9f)
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(colorResource(id = R.color.card_background))
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val modifiers = Modifier
            .weight(1f)
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))

        Box(
            modifier = modifiers
                .background(if (state.periodType == PeriodType.WEEKLY) colorResource(id = R.color.main_text) else Color.Transparent)
                .clickable { onEvent(ExpenseEvent.ChangePeriodType(PeriodType.WEEKLY)) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Weekly",
                color = if (state.periodType == PeriodType.WEEKLY) UI.colors("background") else UI.colors(
                    "light_text"
                ),
                fontSize = 14.sp,
                fontFamily = readexPro
            )
        }

        Box(
            modifier = modifiers
                .background(if (state.periodType == PeriodType.MONTHLY) colorResource(id = R.color.main_text) else Color.Transparent)
                .clickable { onEvent(ExpenseEvent.ChangePeriodType(PeriodType.MONTHLY)) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Monthly",
                color = if (state.periodType == PeriodType.MONTHLY) UI.colors("background") else UI.colors(
                    "light_text"
                ),
                fontSize = 14.sp,
                fontFamily = readexPro
            )
        }
    }
}


@Composable
fun TotalExpense(
    state: ExpenseState,
) {

    val amount = state.amountsInPeriod.sum()
    val lastAmount = state.amountsInLastPeriod.sum()

    val percentageChange =
        if (lastAmount == 0f)
            100
        else
            ((amount - lastAmount) / lastAmount * 100).toInt()


    Column(
        modifier = Modifier
            .padding(top = 12.dp)
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

        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Start
        ) {

            Text(
                text = Util.formatInIndianSystem(amount),
                color = UI.colors("main_text"),
                fontSize = 30.sp,
                fontFamily = readexPro
            )

        }
    }
}


@Composable
fun PeriodChangeButton(
    state: ExpenseState,
    next: Boolean,
    onEvent: (ExpenseEvent) -> Unit,
) {


    IconButton(
        modifier = Modifier
            .size(40.dp),

        onClick = {

            val dateForPeriod = state.dateForPeriod

            val newDateForPeriod =
                if (state.periodType == PeriodType.WEEKLY) {
                    if (next)
                        Util.getNextWeekDate(dateForPeriod)
                    else
                        Util.getPreviousWeekDate(dateForPeriod)
                } else {
                    if (next)
                        Util.getNextMonthDate(dateForPeriod)
                    else
                        Util.getPreviousMonthDate(dateForPeriod)
                }

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

            painter = painterResource(
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
    onEvent: (ExpenseEvent) -> Unit,
) {

    val dates = if (state.periodType == PeriodType.WEEKLY)
        Util.getWeekDates(state.dateForPeriod)
    else
        Util.getMonthDates(state.dateForPeriod)

    val previousPeriodDates = if (state.periodType == PeriodType.WEEKLY)
        Util.getWeekDates(Util.getPreviousWeekDate(state.dateForPeriod))
    else
        Util.getMonthDates(Util.getPreviousMonthDate(state.dateForPeriod))

    onEvent(ExpenseEvent.SetAmounts(dates))
    onEvent(ExpenseEvent.SetLastAmounts(previousPeriodDates))

    onEvent(ExpenseEvent.ChangePeriod(dates.first(), dates.last()))

    val values = state.amountsInPeriod
    var ind = 0

    val highestVal = values.maxOrNull() ?: 0f

    var heightDp by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current


    Column(
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

        Row(
            modifier = Modifier
                .padding(start = 24.dp, end = 24.dp, top = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {

            val label = if (state.periodType == PeriodType.WEEKLY) {
                if (state.dateForPeriod == Util.getCurrentDate())
                    UI.strings("this_week")
                else if (state.dateForPeriod == Util.getPreviousWeekDate(Util.getCurrentDate()))
                    UI.strings("last_week")
                else
                    getTextOfWeek(state.dateForPeriod)
            } else {
                val inputFormatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
                val date = inputFormatter.parse(state.dateForPeriod.toString())
                val outputFormatter = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
                outputFormatter.format(date ?: Date())
            }



            Text(
                text = label,
                color = UI.colors("main_text"),
                fontSize = 18.sp,
                fontFamily = readexPro
            )

            Row(
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


        val chartPoints = remember(state.amountsInPeriod, dates) {
            if (state.amountsInPeriod.isEmpty()) emptyList()
            else {
                val maxAmount = state.amountsInPeriod.maxOrNull() ?: 1f
                val safeMaxAmount = if (maxAmount == 0f) 1f else maxAmount

                state.amountsInPeriod.mapIndexed { index, amount ->
                    val x = if (state.amountsInPeriod.size > 1) {
                        index.toFloat() / (state.amountsInPeriod.size - 1)
                    } else 0.5f

                    val y = amount / safeMaxAmount

                    val dateLong = dates.getOrNull(index) ?: 0L
                    val dateStr = if (dateLong != 0L) {
                        val day = (dateLong % 100).toString().padStart(2, '0')
                        val month = ((dateLong / 100) % 100).toString().padStart(2, '0')
                        "$day/$month"
                    } else ""

                    val dayOfWeek = if (dateLong != 0L) {
                        val inputFormatter = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
                        val date = inputFormatter.parse(dateLong.toString())
                        val outputFormatter = SimpleDateFormat("EEEE", Locale.getDefault())
                        outputFormatter.format(date ?: Date())
                    } else ""

                    ChartPoint(
                        x = x,
                        y = y,
                        amount = amount,
                        date = dateStr,
                        day = dayOfWeek
                    )
                }
            }
        }

        val totalExpense = state.amountsInPeriod.sum()
        val averageExpense = if (state.periodType == PeriodType.WEEKLY) state.weeklyAverage else state.monthlyAverage
        
        val isOverAverage = totalExpense > averageExpense && averageExpense > 0f
        val chartColor = if (isOverAverage) UI.colors("red") else UI.colors("lime")

        InteractiveAreaChart(
            points = chartPoints,
            lineColor = chartColor,
            fillColor = chartColor.copy(alpha = 0.1f)
        )

    }

}

data class ChartPoint(
    val x: Float,
    val y: Float,
    val amount: Float,
    val date: String,
    val day: String,
)


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
public fun InteractiveAreaChart(
    points: List<ChartPoint>,
    lineColor: Color = Color.Green,
    fillColor: Color = Color.Green.copy(alpha = 0.5f),
) {
    var selectedPoint by remember { mutableStateOf<ChartPoint?>(null) }
    var touchX by remember { mutableStateOf(-1f) }

    LaunchedEffect(points) {
        selectedPoint = null
        touchX = -1f
    }

    if (points.isEmpty()) return

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val maxWidth = constraints.maxWidth.toFloat()
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(points) {
                    detectDragGestures(
                        onDragEnd = { /* Do nothing to retain last value */ },
                        onDrag = { change, _ ->
                            val x = change.position.x
                            touchX = x
                            // Logic to find the closest point based on x-coordinate
                            selectedPoint = points.minByOrNull { abs((it.x * size.width) - x) }
                        }
                    )
                }
        ) {
            val width = size.width
            val height = size.height

            val strokePath = Path().apply {
                if (points.isNotEmpty()) {
                    moveTo(points[0].x * width, (1 - points[0].y) * height)
                    for (i in 0 until points.size - 1) {
                        val pStart = points[i]
                        val pEnd = points[i + 1]

                        val xStart = pStart.x * width
                        val yStart = (1 - pStart.y) * height
                        val xEnd = pEnd.x * width
                        val yEnd = (1 - pEnd.y) * height

                        // Control points for smooth horizontal entry/exit
                        val cx1 = xStart + (xEnd - xStart) / 2
                        val cy1 = yStart
                        val cx2 = xStart + (xEnd - xStart) / 2
                        val cy2 = yEnd

                        cubicTo(cx1, cy1, cx2, cy2, xEnd, yEnd)
                    }
                }
            }

            // 1. Draw the Gradient Fill
            val fillPath = Path().apply {
                addPath(strokePath)
                lineTo(points.last().x * width, height)
                lineTo(points.first().x * width, height)
                close()
            }
            drawPath(fillPath, brush = Brush.verticalGradient(listOf(fillColor, Color.Transparent)))

            // 2. Draw the Line
            drawPath(strokePath, color = lineColor, style = Stroke(width = 3.dp.toPx()))

            // 3. Draw Hover Indicator
            if (selectedPoint != null) {
                val circleX = selectedPoint!!.x * width
                val circleY = (1 - selectedPoint!!.y) * height

                // Vertical dashed line
                drawLine(
                    color = lineColor,
                    start = Offset(circleX, 0f),
                    end = Offset(circleX, height),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                )

                // Selection Circle
                drawCircle(
                    color = lineColor,
                    radius = 8.dp.toPx(),
                    center = Offset(circleX, circleY)
                )
            }
        }

        // --- 5. Tooltip Popup ---
        selectedPoint?.let { pt ->
            ChartTooltip(pt, touchX, maxWidth) // Pass maxWidth
        }
    }
}

@Composable
fun ChartTooltip(point: ChartPoint, touchX: Float, maxWidth: Float) {
    var tooltipWidth by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .onGloballyPositioned {
                tooltipWidth = it.size.width
            }
            .offset {
                val halfWidth = tooltipWidth / 2
                val x = (touchX - halfWidth).coerceIn(0f, maxWidth - tooltipWidth)
                IntOffset(x.toInt(), -5)
            }
            .shadow(4.dp, RoundedCornerShape(8.dp))
            .background(UI.colors("card_background").copy(alpha = 0.8f), RoundedCornerShape(8.dp))
            .border(0.5.dp, Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = CenterHorizontally) {
            Text(
                text = Util.formatInIndianSystem(point.amount),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp, // Smaller font
                color = UI.colors("main_text"),
                fontFamily = readexPro
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = point.date,
                    fontSize = 10.sp, // Smaller font
                    color = UI.colors("light_text"),
                    fontFamily = readexPro
                )
                Text(
                    text = " • ",
                    fontSize = 10.sp,
                    color = UI.colors("light_text")
                )
                Text(
                    text = point.day,
                    fontSize = 10.sp, // Smaller font
                    color = UI.colors("light_text"),
                    fontFamily = readexPro
                )
            }
        }
    }
}


@Composable
fun ExpensesInPeriod(
    state: ExpenseState,
    navController: NavController,
) {

    var selectedCategory by remember { mutableStateOf<Types?>(null) }

    val categorizedExpenses = remember(state.expensesInPeriod) {
        state.expensesInPeriod
            .groupBy { it.type }
            .map { (type, expenses) ->
                CategorySummary(
                    type = Util.getTypeByString(type),
                    totalAmount = expenses.sumOf { it.amount.toDouble() }.toFloat(),
                    transactionCount = expenses.size
                )
            }
            .sortedByDescending { it.totalAmount.absoluteValue }
    }


    Column(
        modifier = Modifier
            .padding(top = 24.dp)
    ) {

        if (categorizedExpenses.isNotEmpty()) {
            Text(
                text = "Categories",
                color = UI.colors("heading_text"),
                fontSize = 18.sp,
                fontFamily = readexPro,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            for (category in categorizedExpenses) {
                if (selectedCategory == null || selectedCategory == category.type) {
                    CategoryCard(
                        category = category,
                        onClick = {
                            selectedCategory =
                                if (selectedCategory == category.type) null else category.type
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = if (selectedCategory == null) "All Expenses" else "${selectedCategory?.type} Expenses",
                color = UI.colors("heading_text"),
                fontSize = 18.sp,
                fontFamily = readexPro,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // All Expenses
            for (expense in state.expensesInPeriod) {
                if (selectedCategory == null || expense.type == selectedCategory?.type) {
                    ExpenseCard(
                        expense = expense,
                        navController = navController
                    )
                }
            }
        } else {
            Text(
                text = "No Expenses",
                color = UI.colors("heading_text"),
                fontSize = 18.sp,
                fontFamily = readexPro,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

    }

}


@Composable
fun CategoryCard(
    category: CategorySummary,
    onClick: () -> Unit,
) {
    val bgColor = Util.getRandomColor()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(UI.colors("card_background"))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = Util.image(category.type),
                    contentDescription = category.type.type,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = category.type.type.replaceFirstChar { it.uppercase() },
                    color = UI.colors("text"),
                    fontSize = 16.sp,
                    fontFamily = readexPro
                )
                Text(
                    text = "${category.transactionCount} transactions",
                    color = UI.colors("light_text"),
                    fontSize = 12.sp,
                    fontFamily = readexPro
                )
            }
        }

        Text(
            text = "-₹%.2f".format(abs(category.totalAmount)),
            color = UI.colors("text"),
            fontSize = 16.sp,
            fontFamily = readexPro
        )
    }
}


data class CategorySummary(
    val type: Types,
    val totalAmount: Float,
    val transactionCount: Int,
)


fun getTextOfWeek(
    periodDate: Long,
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
        ) {

            Stats(
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
                onEvent = {},
                navController = androidx.navigation.compose.rememberNavController()
            )
        }

    }
}