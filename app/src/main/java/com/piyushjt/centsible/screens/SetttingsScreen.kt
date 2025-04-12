package com.piyushjt.centsible.screens

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.piyushjt.centsible.UI
import androidx.core.net.toUri
import com.piyushjt.centsible.Expense
import com.piyushjt.centsible.ExpenseEvent
import com.piyushjt.centsible.ExpenseState
import com.piyushjt.centsible.MainScreen
import com.piyushjt.centsible.R
import com.piyushjt.centsible.Types
import com.piyushjt.centsible.UI.DividerLine
import com.piyushjt.centsible.UI.readexPro
import com.piyushjt.centsible.Util.DialogBox
import com.piyushjt.centsible.Util.getAllData
import com.piyushjt.centsible.ui.theme.CentsibleTheme


@Composable
fun Settings(
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit
) {

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(UI.colors("background"))
            .padding(horizontal = 24.dp)

    ){

        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = CenterHorizontally
        ) {

            // Header
            Text(
                text = UI.strings("settings"),
                color = UI.colors("main_text"),
                fontSize = 18.sp,
                fontFamily = readexPro
            )

            DataManager(
                state = state,
                onEvent = onEvent
            )


            PrivacyPolicy()


        }
    }
}


@Composable
fun PrivacyPolicy() {

    val context = LocalContext.current

    val uri = UI.strings("privacy_policy_uri")

    Row(
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(UI.colors("card_background"))
            .height(60.dp)
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, uri.toUri())
                context.startActivity(intent)
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start

    ) {
        Text(
            modifier = Modifier.padding(start = 24.dp),
            text = UI.strings("privacy_policy"),
            color = UI.colors("main_text"),
            fontSize = 16.sp,
            fontFamily = readexPro
        )

        Icon(
            modifier = Modifier
                .padding(start = 12.dp)
                .height(20.dp)
                .width(20.dp),
            painter = painterResource(id = R.drawable.ext_link),
            tint = UI.colors("main_text"),
            contentDescription = UI.strings("ext_link_icon")

        )
    }

}





@Composable
fun DataManager(
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit
) {


    Column(
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(UI.colors("card_background"))

    ) {

        ExportData(
            state = state
        )
        DividerLine()

        ImportData()
        DividerLine()

        DeleteAllData(
            onEvent = onEvent
        )


    }




}


@Composable
fun DeleteAllData(
    onEvent: (ExpenseEvent) -> Unit
) {

    val isDialogVisible = remember { mutableStateOf(false) }

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable {
                isDialogVisible.value = true
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ){

        Text(
            modifier = Modifier.padding(start = 24.dp),
//                text = UI.strings("delete_all_data"),
            text = "Delete All Data",
            color = UI.colors("red"),
            fontSize = 16.sp,
            fontFamily = readexPro
        )

    }


    DialogBox(
        isDialogVisible = isDialogVisible,
//        title = UI.strings("delete_all_data"),
        title = "Delete All Data",
//        message = UI.strings("delete_all_data_message"),
        message = "This will PERMANENTLY delete all the expenses stored in the app.",
        posBtnText = UI.strings("delete"),
        negBtnText = UI.strings("cancel"),
        onPosBtnClick = {
            onEvent(ExpenseEvent.DeleteAllExpenses)
        }
    )

}


@Composable
fun ExportData(
    state: ExpenseState
) {

    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    var toast: Toast? by remember { mutableStateOf(null) }

    val dataSaved = "Data saved to Clipboard."
//        val dataSaved = ("data_saved")

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable {

                val data = getAllData(state = state)

                clipboardManager.setText(AnnotatedString(data))

                toast?.cancel()
                toast = Toast.makeText(context, dataSaved, Toast.LENGTH_LONG)
                toast?.show()


            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        Text(
            modifier = Modifier.padding(start = 24.dp),
//                text = UI.strings("export_data"),
            text = "Export Data",
            color = UI.colors("main_text"),
            fontSize = 16.sp,
            fontFamily = readexPro
        )

    }

}


@Composable
fun ImportData() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable { },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        Text(
            modifier = Modifier.padding(start = 24.dp),
//                text = UI.strings("import_data"),
            text = "Import Data",
            color = UI.colors("main_text"),
            fontSize = 16.sp,
            fontFamily = readexPro
        )

    }

}



@Preview
@Composable
private fun SettingsScreenPreview() {

    CentsibleTheme {
        Surface(
            modifier = Modifier
                .background(UI.colors("background"))
                .fillMaxSize()
                .padding(top = 42.dp)
        ) {

            val navFilled = remember { mutableStateOf("set") }

            MainScreen(
                state = ExpenseState(
                    expenses = listOf(

                        Expense(
                            title = "Title",
                            description = "Desc",
                            type = Types.MISC.type,
                            amount = -100.0f,
                            date = 20241231L,
                            id = 1
                        )
                    ),
                ),
                navFilled = navFilled,
                onEvent = {},
            )
        }

    }
}