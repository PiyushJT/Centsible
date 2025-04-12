package com.piyushjt.centsible

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

object UI {

    @Composable
    fun colors(color: String): Color {


        val colors = mapOf(
            "logo_theme" to colorResource(id = R.color.logo_theme),
            "background" to colorResource(id = R.color.background),
            "card_background" to colorResource(id = R.color.card_background),
            "text" to colorResource(id = R.color.text),
            "main_text" to colorResource(id = R.color.main_text),
            "light_text" to colorResource(id = R.color.light_text),
            "heading_text" to colorResource(id = R.color.heading_text),

            "hint_text" to colorResource(id = R.color.hint_text),
            "hint_main_text" to colorResource(id = R.color.hint_main_text),
            "hint_light_text" to colorResource(id = R.color.hint_light_text),

            "black" to colorResource(id = R.color.black),
            "white" to colorResource(id = R.color.white),
            "red" to colorResource(id = R.color.red),
            "red50" to colorResource(id = R.color.red50),
            "lime" to colorResource(id = R.color.lime),
            "blue" to colorResource(id = R.color.blue),
            "trans" to colorResource(id = R.color.trans),
            "grey" to colorResource(id = R.color.grey),

            "green_bg" to colorResource(id = R.color.green_bg),
            "pink_bg" to colorResource(id = R.color.pink_bg),
            "gray_bg" to colorResource(id = R.color.gray_bg),
            "red_bg" to colorResource(id = R.color.red_bg),
            "cream_bg" to colorResource(id = R.color.cream_bg)
        )


        return colors[color] ?: Color.Transparent

    }

    @Composable
    fun strings(string: String): String {

        val strings = mapOf(

            "privacy_policy_uri" to stringResource(id = R.string.privacy_policy_uri),


            "privacy_policy" to stringResource(id = R.string.privacy_policy),
            "settings" to stringResource(id = R.string.settings),
            "ext_link_icon" to stringResource(id = R.string.ext_link_icon),

            "new_transaction" to stringResource(id = R.string.new_transaction),
            "title" to stringResource(id = R.string.title),
            "description" to stringResource(id = R.string.description),
            "big_values" to stringResource(id = R.string.not_support_big_values),
            "amount" to stringResource(id = R.string.amount),
            "select" to stringResource(id = R.string.select),
            "cancel" to stringResource(id = R.string.cancel),
            "empty_value" to stringResource(id = R.string.empty_value),
            "save_earning" to stringResource(id = R.string.save_earning),
            "save_expense" to stringResource(id = R.string.save_expense),
            "create_backup" to stringResource(id = R.string.create_backup),
            "restore_backup" to stringResource(id = R.string.restore_backup),


            "edit_transaction" to stringResource(id = R.string.edit_transaction),
            "back" to stringResource(id = R.string.back),
            "delete_expense" to stringResource(id = R.string.delete_expense),
            "confirm_delete" to stringResource(id = R.string.confirm_delete),
            "delete" to stringResource(id = R.string.delete),


            "recent_transactions" to stringResource(id = R.string.recent_transactions),
            "total_balance" to stringResource(id = R.string.total_balance),


            "statistics" to stringResource(id = R.string.statistics),
            "total_expense" to stringResource(id = R.string.total_expense),
            "decreased" to stringResource(id = R.string.decreased),
            "increased" to stringResource(id = R.string.increased),
            "total_expense" to stringResource(id = R.string.total_expense),
            "next_week" to stringResource(id = R.string.next_week),
            "previous_week" to stringResource(id = R.string.previous_week),
            "this_week" to stringResource(id = R.string.this_week),
            "last_week" to stringResource(id = R.string.last_week),


        )

        return strings[string] ?: ""

    }


    @Composable
    fun DividerLine(
        color: Color = colors("hint_light_text")
    ) {

        Row (
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(0.5.dp)
                    .background(color)
            )
        }

    }

    // Custom font
    val readexPro = FontFamily(
        Font(R.font.readex_pro, FontWeight.Medium)
    )

}