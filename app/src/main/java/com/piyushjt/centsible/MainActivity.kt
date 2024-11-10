package com.piyushjt.centsible

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
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

    Header()

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

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
    ) {

        DayDate()

        TotalBalance()

    }

}

@Preview(
    showSystemUi = true
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