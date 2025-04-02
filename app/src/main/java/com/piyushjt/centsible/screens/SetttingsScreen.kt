package com.piyushjt.centsible.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.piyushjt.centsible.EditExpenseScreen
import com.piyushjt.centsible.UI
import androidx.core.net.toUri
import com.piyushjt.centsible.MainActivity
import com.piyushjt.centsible.R
import com.piyushjt.centsible.UI.readexPro


@Composable
fun Settings() {

    val context = LocalContext.current

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

            val uri = UI.strings("privacy_policy_uri")

            Row(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(UI.colors("card_background"))
                    .height(60.dp)
                    .clickable {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                        context.startActivity(intent)
                    }
                ,
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
    }
}