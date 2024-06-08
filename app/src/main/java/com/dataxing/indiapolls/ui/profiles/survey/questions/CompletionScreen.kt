package com.dataxing.indiapolls.ui.profiles.survey.questions

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dataxing.indiapolls.R
import com.dataxing.indiapolls.ui.theme.DefaultButton
import com.dataxing.indiapolls.ui.theme.defaultMediumTextView
import com.dataxing.indiapolls.ui.theme.white

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CompletionScreen(onCompleted: () -> Unit) {
    Scaffold(
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = white,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.complete),
                        contentDescription =  null,
                        modifier = Modifier.size(120.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(64.dp))
                    Text(
                        text = stringResource(id = R.string.thank_you_for_completion),
                        style = defaultMediumTextView.copy(fontSize = 18.sp),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 40.dp)
                    )
                    Spacer(modifier = Modifier.height(64.dp))
                    DefaultButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 64.dp, bottom = 24.dp, start = 24.dp, end = 24.dp)
                            .height(52.dp),
                        stringResource(id = R.string.complete)
                    ) {
                        onCompleted()
                    }
                }
            }
        }
    )
}