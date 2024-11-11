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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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

                    MainScreen()

                }
            }
        }
    }
}


@Composable
fun MainScreen(

) {

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
    ) {

        Header()

        ListOfExpenses()

    }

}

// Custom font
val readexPro = FontFamily(
    Font(R.font.readex_pro, FontWeight.Medium)
)

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
            .padding(top = 24.dp)
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
            .padding(top = 24.dp, bottom = 18.dp),
        text = "Recent Transactions",
        color = colorResource(id = R.color.heading_text),
        fontSize = 18.sp,
        fontFamily = readexPro
    )

}

@Composable
fun ListOfExpenses(
) {
    Column {

        Heading()


        Expense("misc")

        Expense("food")

        Expense("shopping")

        Expense("travel")

        Expense("ent")

        Expense("grocery")

        Expense("everyday")

        Expense("skill")


    }

}


@Composable
fun Expense(
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

    val bgColor =  bgColors[(1..5).random()]!!


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
                        contentDescription = "Shopping cart image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .padding(10.dp)

                    )


                }

                Column(
                    modifier = Modifier
                        .height(90.dp)
                        .padding(start = 10.dp),
                    verticalArrangement = Arrangement.Center

                ) {

                    Text(
                        text = "ABCDEFGHIJKLMNO",
                        color = colorResource(id = R.color.text),
                        fontSize = 14.sp,
                        fontFamily = readexPro
                    )

                    Text(
                        // display type if description is null
                        text = "Ent",
                        color = colorResource(id = R.color.light_text),
                        fontSize = 12.sp,
                        fontFamily = readexPro
                    )
                }
            }


            Text(
                modifier = Modifier
                    .padding(14.dp),
                text = "-\$145.00",
                color = colorResource(id = R.color.main_text),
                fontSize = 16.sp,
                fontFamily = readexPro
            )

        }

        Spacer(
            modifier = Modifier
                .height(2.dp)
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

            MainScreen()

        }
    }
}