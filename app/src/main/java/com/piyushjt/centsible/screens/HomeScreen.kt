package com.piyushjt.centsible.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.piyushjt.centsible.Expense
import com.piyushjt.centsible.ExpenseCard
import com.piyushjt.centsible.ExpenseEvent
import com.piyushjt.centsible.ExpenseState
import com.piyushjt.centsible.Heading
import com.piyushjt.centsible.Types
import com.piyushjt.centsible.UI
import com.piyushjt.centsible.UI.readexPro
import com.piyushjt.centsible.Util
import com.piyushjt.centsible.ui.theme.CentsibleTheme


// All Expenses to be shown in a Nav Item
@Composable
fun ALlExpenses(
    onEvent: (ExpenseEvent) -> Unit,
    state: ExpenseState,
    navController: NavController
) {

    val isSearching = state.searchQuery.isNotBlank()

    // Start scrolled past the search bar (index 0) so it's hidden
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = 1
    )

    LazyColumn(
        state = listState,
        modifier = Modifier
            .padding(horizontal = 24.dp)
    ) {

        // Item 0: Search Bar (hidden above viewport initially)
        item {
            SearchBar(
                state = state,
                onEvent = onEvent
            )
        }

        // Item 1: Total Balance card (hidden when searching)
        item {
            AnimatedVisibility(
                visible = !isSearching,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                TotalBalance(
                    state = state
                )
            }
        }

        ListOfExpenses(
            state = state,
            navController = navController,
            isSearching = isSearching
        )
    }
}


// Search Bar
@Composable
fun SearchBar(
    state: ExpenseState,
    onEvent: (ExpenseEvent) -> Unit
) {

    TextField(
        value = state.searchQuery,
        onValueChange = { onEvent(ExpenseEvent.SetSearchQuery(it)) },
        placeholder = {
            Text(
                text = UI.strings("search_transactions"),
                fontFamily = readexPro,
                fontSize = 14.sp
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = UI.colors("light_text"),
                modifier = Modifier.size(20.dp)
            )
        },
        trailingIcon = {
            if (state.searchQuery.isNotEmpty()) {
                IconButton(
                    onClick = { onEvent(ExpenseEvent.SetSearchQuery("")) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = UI.colors("light_text"),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = UI.colors("card_background"),
            unfocusedContainerColor = UI.colors("card_background"),
            cursorColor = UI.colors("text"),
            focusedTextColor = UI.colors("text"),
            unfocusedTextColor = UI.colors("text"),
            focusedPlaceholderColor = UI.colors("light_text"),
            unfocusedPlaceholderColor = UI.colors("light_text"),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        textStyle = androidx.compose.ui.text.TextStyle(
            fontFamily = readexPro,
            fontSize = 14.sp
        ),
        shape = RoundedCornerShape(20.dp),
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 12.dp)
    )

}


// List of All Expenses
fun LazyListScope.ListOfExpenses(
    state: ExpenseState,
    navController: NavController,
    isSearching: Boolean
) {

    // Heading — changes text when searching
    item {
        Heading(
            text = if (isSearching)
                UI.strings("search_results")
            else
                UI.strings("recent_transactions")
        )
    }


    // All Expenses
    items(state.expenses) { expense ->

        ExpenseCard(
            expense = expense,
            navController = navController
        )

    }

}


// Total Balance in the Header — wrapped in a card
@Composable
fun TotalBalance(
    state: ExpenseState,
) {

    Column(
        modifier = Modifier
            .padding(top = 24.dp, bottom = 12.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(UI.colors("card_background"))
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {

        Text(
            text = UI.strings("total_balance"),
            color = UI.colors("light_text"),
            fontSize = 18.sp,
            fontFamily = readexPro
        )

        // Balance
        Text(
            text = Util.formatInIndianSystem(state.totalAmount),
            color =
                if (state.totalAmount >= 0)
                    UI.colors("lime")
                else
                    UI.colors("red"),
            fontSize = 34.sp,
            fontFamily = readexPro
        )

    }

}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun HomeScreenPreview() {

    CentsibleTheme {
        Surface(
            modifier = Modifier
                .background(UI.colors("background"))
                .fillMaxWidth()
                .padding(top = 42.dp)
        ) {

            ALlExpenses(
                state = ExpenseState(
                    expenses = listOf(

                        Expense(
                            title = "Title",
                            description = "Desc",
                            type = Types.ENT.type,
                            amount = -100.0f,
                            date = 20241231L,
                            id = 1
                        )
                    ),
                    totalAmount = 521985f
                ),
                onEvent = {},
                navController = androidx.navigation.compose.rememberNavController()
            )
        }

    }
}