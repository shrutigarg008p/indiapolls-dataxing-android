package com.dataxing.indiapolls.ui.profiles.survey.questions

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dataxing.indiapolls.R
import com.dataxing.indiapolls.ui.profiles.survey.itemview.SurveyQuestionItemView
import com.dataxing.indiapolls.ui.theme.DefaultDropdownMenu
import com.dataxing.indiapolls.ui.theme.defaultMediumTextView
import com.dataxing.indiapolls.ui.theme.white

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DropDownTypeQuestionScreen(item: SurveyQuestionItemView, text: String, onClick: (Int) -> Unit) {
    var value by remember { mutableStateOf( text ) }

    Scaffold(
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = white
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = item.text,
                        style = defaultMediumTextView.copy(fontSize = 18.sp),
                        modifier = Modifier.padding(top = 40.dp)
                    )

                    DefaultDropdownMenu(
                        40.dp,
                        text = value,
                        onValueChange = { index, item ->
                            value = item
                            onClick(index)
                        },
                        label = stringResource(id = R.string.select_option),
                        isError = false,
                        singleLine = true,
                        errorText = "",
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        options = item.options.map { it.value }
                    )
                }
            }
        }
    )
}