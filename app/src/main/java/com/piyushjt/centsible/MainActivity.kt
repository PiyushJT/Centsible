package com.piyushjt.centsible

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.room.Room
import com.piyushjt.centsible.UI.readexPro
import com.piyushjt.centsible.screens.ALlExpenses
import com.piyushjt.centsible.screens.AddExpense
import com.piyushjt.centsible.screens.EditExpenseScreen
import com.piyushjt.centsible.screens.Settings
import com.piyushjt.centsible.screens.Stats
import com.piyushjt.centsible.ui.theme.CentsibleTheme
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class MainActivity : FragmentActivity() {

    // App lock state
    private var isUnlocked = mutableStateOf(true)

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

    private lateinit var appUpdateHelper: AppUpdateHelper

    private val updateLauncher = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode != RESULT_OK) {
            // If the update is cancelled or fails, you can request to start the update again.
            Log.e("MainActivity", "Update flow failed! Result code: ${result.resultCode}")
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT
            )
        )
        actionBar?.hide()

        appUpdateHelper = AppUpdateHelper(this)

        // Check app lock
        if (AppLockPrefs.isAppLockEnabled(this)) {
            isUnlocked.value = false
            showBiometricPrompt()
        }

        setContent {
            CentsibleTheme {
                val state by viewModel.state.collectAsState()
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()

                val onUpdateDownloaded = {
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "An update has just been downloaded.",
                            actionLabel = "RESTART"
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            appUpdateHelper.completeUpdate()
                        }
                    }
                }

                // Check for update on launch
                remember {
                    appUpdateHelper.checkForUpdate(this@MainActivity, updateLauncher, onUpdateDownloaded)
                    true
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = UI.colors("background")
                ) {
                    Scaffold(
                        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
                        bottomBar = {
                            NavBar(
                                navController = navController,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                        }
                    ) { paddingValues ->
                        CentsibleApp(
                            state = state,
                            onEvent = viewModel::onEvent,
                            navController = navController,
                            paddingValues = paddingValues
                        )
                    }
                }

                // Lock screen overlay
                if (!isUnlocked.value) {
                    LockScreen(
                        onUnlockClick = { showBiometricPrompt() }
                    )
                }
            }
        }
    }

    private fun showBiometricPrompt() {
        val executor = ContextCompat.getMainExecutor(this)

        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                isUnlocked.value = true
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                // User cancelled or error — keep locked
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                // Biometric not recognized — prompt stays open for retry
            }
        }

        val biometricPrompt = BiometricPrompt(this, executor, callback)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Unlock Centsible")
            .setSubtitle("Verify your identity to continue")
            .setAllowedAuthenticators(BIOMETRIC_WEAK or DEVICE_CREDENTIAL)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    override fun onResume() {
        super.onResume()
        if (::appUpdateHelper.isInitialized) {
            // Check if update is downloaded but not installed
            appUpdateHelper.checkUpdateStatus {
                // This will be called from any background logic if needed,
                // but since we want to trigger the UI (Snackbar),
                // we might need a way to trigger the same logic as in onCreate.
                // However, checkForUpdate already handles the listener.
                // checkUpdateStatus is specifically for when the app returns from background.
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::appUpdateHelper.isInitialized) {
            appUpdateHelper.unregisterListener()
        }
    }
}


@Composable
fun CentsibleApp(
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit,
    navController: NavHostController,
    paddingValues: PaddingValues
) {
    NavHost(
        navController = navController,
        startDestination = Add,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        enterTransition = {
            fadeIn(animationSpec = tween(350))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(350))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(350))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(350))
        }
    ) {
        composable<Home> {
            ALlExpenses(
                onEvent = onEvent,
                state = state,
                navController = navController
            )
        }

        composable<Statistics> {
            Stats(
                onEvent = onEvent,
                state = state,
                navController = navController
            )
        }

        composable<Add> {
            AddExpense(
                onEvent = onEvent,
                navController = navController
            )
        }

        composable<Settings> {
            Settings(
                onEvent = onEvent
            )
        }

        composable<EditExpense> {
            val args = it.toRoute<EditExpense>()
            EditExpenseScreen(
                title = args.title,
                description = args.description,
                type = args.type,
                amount = args.amount,
                date = args.date,
                id = args.id,
                navController = navController,
                onEvent = onEvent
            )
        }
    }
}



@Serializable
object Home

@Serializable
object Statistics

@Serializable
object Add

@Serializable
object Settings

@Serializable
data class EditExpense(
    val title: String,
    val description: String?,
    val type: String,
    val amount: Float,
    val date: Long,
    val id: Int
)




// Lock Screen Overlay
@Composable
fun LockScreen(
    onUnlockClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UI.colors("background")),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // App icon
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                contentDescription = "Centsible",
                modifier = Modifier
                    .size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // App name
            Text(
                text = "Centsible",
                color = UI.colors("main_text"),
                fontSize = 24.sp,
                fontFamily = readexPro,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tap to unlock",
                color = UI.colors("hint_main_text"),
                fontSize = 14.sp,
                fontFamily = readexPro
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Unlock button
            FilledTonalButton(
                onClick = onUnlockClick,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = UI.colors("card_background"),
                    contentColor = UI.colors("main_text")
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                    text = "Unlock",
                    fontSize = 16.sp,
                    fontFamily = readexPro
                )
            }

        }
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
        color = UI.colors("heading_text"),
        fontSize = 18.sp,
        fontFamily = readexPro
    )

}



// Expense Card
@SuppressLint("DefaultLocale")
@Composable
fun ExpenseCard(
    expense: Expense,
    navController: NavController?
) {

    val bgColor = Util.getRandomColor()

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ){

        /*

        Row with two children as
            1) Logo & title / Desc
            2) Amount

        */

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(UI.colors("card_background"))
                .height(80.dp)
                .clickable {

                    navController?.navigate(
                        EditExpense(
                            expense.title,
                            expense.description,
                            expense.type,
                            expense.amount,
                            expense.date,
                            expense.id
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
                        painter = Util.image(
                            Util.getTypeByString(expense.type)
                        ),
                        contentDescription = expense.type,
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
                        text = expense.title.take(20) +
                                if (expense.title.length > 20) "..." else "",
                        color = UI.colors("text"),
                        fontSize = 12.sp,
                        fontFamily = readexPro
                    )

                    // Description
                    Text(
                        text = if(expense.description.isNullOrEmpty())
                            expense.type
                        else
                            expense.description!!.take(20) +
                                    if (expense.description!!.length > 20) "..." else "",
                        color = UI.colors("light_text"),
                        fontSize = 10.sp,
                        fontFamily = readexPro
                    )

                }
            }


            val amountToShow = if (expense.amount < 0) {
                "-₹%.2f".format(-expense.amount)
            } else {
                "+₹%.2f".format(expense.amount)
            }


            // Amount
            Text(
                modifier = Modifier
                    .padding(14.dp),
                text = amountToShow,
                color = if(expense.amount < 0)
                    UI.colors("main_text")
                else
                    UI.colors("lime"),
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
    navController: NavController,
    buttonLogo: String,
    isSelected: Boolean,
    route: Any
) {


    // Setting the icon (filled/unfilled)
    val icon = when (buttonLogo) {
        "home"  -> painterResource(if (isSelected) R.drawable.home_filled else R.drawable.home)
        "stats" -> painterResource(if (isSelected) R.drawable.stats_filled else R.drawable.stats)
        "add"   -> painterResource(if (isSelected) R.drawable.add_filled else R.drawable.add)
        else    -> painterResource(if (isSelected) R.drawable.set_filled else R.drawable.set)
    }


    IconButton(
        modifier = Modifier
            .height(60.dp),

        onClick = {
            navController.navigate(route) {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }

    ) {

        // Icon
        Icon(
            modifier = Modifier
                .height(25.dp)
                .width(25.dp),

            tint = UI.colors("main_text"),
            painter = icon,

            contentDescription = buttonLogo
        )

    }
}


// Bottom Navigation Bar
@Composable
fun NavBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
            .background(UI.colors("card_background"))
            .navigationBarsPadding(),
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = CenterVertically
        ) {

            // Statistics (Graph)
            NavBarButton(
                navController = navController,
                buttonLogo = "stats",
                isSelected = currentDestination?.hierarchy?.any { it.hasRoute<Statistics>() } == true,
                route = Statistics
            )

            // Home
            NavBarButton(
                navController = navController,
                buttonLogo = "home",
                isSelected = currentDestination?.hierarchy?.any { it.hasRoute<Home>() } == true,
                route = Home
            )

            // Add Expense
            NavBarButton(
                navController = navController,
                buttonLogo = "add",
                isSelected = currentDestination?.hierarchy?.any { it.hasRoute<Add>() } == true,
                route = Add
            )

            // Settings
            NavBarButton(
                navController = navController,
                buttonLogo = "set",
                isSelected = currentDestination?.hierarchy?.any { it.hasRoute<Settings>() } == true,
                route = Settings
            )

        }

    }
}