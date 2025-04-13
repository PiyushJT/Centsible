package com.piyushjt.centsible.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.piyushjt.centsible.Expense
import com.piyushjt.centsible.ExpenseEvent
import com.piyushjt.centsible.ExpenseState
import com.piyushjt.centsible.MainScreen
import com.piyushjt.centsible.R
import com.piyushjt.centsible.Types
import com.piyushjt.centsible.UI
import com.piyushjt.centsible.UI.DividerLine
import com.piyushjt.centsible.UI.readexPro
import com.piyushjt.centsible.Util.DialogBox
import com.piyushjt.centsible.Util.parseExpensesFromJson
import com.piyushjt.centsible.ui.theme.CentsibleTheme


@Composable
fun Settings(
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
                onEvent = onEvent
            )

            PolicyAndLicense()


        }
    }
}



@Composable
fun DataManager(
    onEvent: (ExpenseEvent) -> Unit
) {

    // ALl Data Managing items
    Column(
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(UI.colors("card_background"))

    ) {

        // Save Copy of Data in a file
        ExportData(
            onEvent = onEvent
        )
        DividerLine()

        // Import Data from a file
        ImportData(
            onEvent = onEvent
        )
        DividerLine()

        // Delete All Data
        DeleteAllData(
            onEvent = onEvent
        )

    }

}


@Composable
fun ExportData(
    onEvent: (ExpenseEvent) -> Unit
) {

    val context = LocalContext.current


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable {
                // Exporting the data
                onEvent(ExpenseEvent.ExportData(context))
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        Text(
            modifier = Modifier.padding(start = 24.dp),
            text = UI.strings("export_data"),
            color = UI.colors("main_text"),
            fontSize = 16.sp,
            fontFamily = readexPro
        )

    }

}


@Composable
fun ImportData(
    onEvent: (ExpenseEvent) -> Unit
) {

    // Some String Values
    val dataImportSuccessful = UI.strings("data_import_successful")
    val fileNotSupported = UI.strings("file_not_supported")

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),

        onResult = { uri: Uri? ->

            uri?.let {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                // Read file as text
                val jsonText = context.contentResolver.openInputStream(it)?.bufferedReader()?.use { reader ->
                    reader.readText()
                }

                jsonText?.let {

                    try {
                        // Parses into Expense Object's list
                        val expenses = parseExpensesFromJson(it)

                        // Saving the expenses
                        for (expense in expenses) {
                            onEvent(ExpenseEvent.SaveExpense(expense))
                        }

                        Toast.makeText(context, dataImportSuccessful, Toast.LENGTH_SHORT).show()

                    }
                    catch (e: Exception) {
                        // Handling parsing errors
                        Log.e("Parse", "File not Supported: ${e.message}", e)
                        Toast.makeText(context, fileNotSupported, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable {
                launcher.launch(arrayOf("*/*"))
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {

        Text(
            modifier = Modifier.padding(start = 24.dp),
            text = UI.strings("import_data"),
            color = UI.colors("main_text"),
            fontSize = 16.sp,
            fontFamily = readexPro
        )

        Text(
            modifier = Modifier.padding(start = 6.dp),
            text = UI.strings("upload_file"),
            color = UI.colors("hint_main_text"),
            fontSize = 14.sp,
            fontFamily = readexPro
        )

    }

}


@Composable
fun DeleteAllData(
    onEvent: (ExpenseEvent) -> Unit
) {

    val context = LocalContext.current
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
            text = UI.strings("delete_all_data"),
            color = UI.colors("red"),
            fontSize = 16.sp,
            fontFamily = readexPro
        )

    }


    // Confirmation Dialog
    DialogBox(
        isDialogVisible = isDialogVisible,
        title = UI.strings("delete_all_data"),
        message = UI.strings("delete_all_data_message"),
        posBtnText = UI.strings("delete"),
        negBtnText = UI.strings("cancel"),
        onPosBtnClick = {
            onEvent(ExpenseEvent.ExportData(context))
            onEvent(ExpenseEvent.DeleteAllExpenses)
        }
    )

}



@Composable
fun PolicyAndLicense() {

    // Privacy Policy and License
    Column(
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(UI.colors("card_background"))

    ) {

        PrivacyPolicy()
        DividerLine()

        License()
        DividerLine()

        GithubSourceCode()

    }

}


@Composable
fun PrivacyPolicy() {

    val uri = UI.strings("privacy_policy_uri")
    val context = LocalContext.current

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable {

                // Open link in external browser
                val intent = Intent(Intent.ACTION_VIEW, uri.toUri())
                context.startActivity(intent)

            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ){

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
fun License() {

    val uri = UI.strings("license_uri")
    val context = LocalContext.current

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable {

                // Open link in external browser
                val intent = Intent(Intent.ACTION_VIEW, uri.toUri())
                context.startActivity(intent)

            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ){

        Text(
            modifier = Modifier.padding(start = 24.dp),
            text = UI.strings("open_source_license"),
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
fun GithubSourceCode() {

    val uri = UI.strings("github_uri")
    val context = LocalContext.current

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable {

                // Open link in external browser
                val intent = Intent(Intent.ACTION_VIEW, uri.toUri())
                context.startActivity(intent)

            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ){

        Text(
            modifier = Modifier.padding(start = 24.dp),
            text = UI.strings("github_repo"),
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