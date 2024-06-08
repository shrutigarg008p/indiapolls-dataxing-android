package com.dataxing.indiapolls.ui.profiles.survey.questions.radiobutton

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dataxing.indiapolls.ui.profiles.survey.itemview.OptionItemView
import com.dataxing.indiapolls.ui.profiles.survey.itemview.SurveyQuestionItemView
import com.dataxing.indiapolls.ui.theme.defaultMediumTextView
import com.dataxing.indiapolls.ui.theme.primary
import com.dataxing.indiapolls.ui.theme.textPrimary
import com.dataxing.indiapolls.ui.theme.white

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RadioButtonTypeQuestionScreen(questionItem: SurveyQuestionItemView, onCheckedChange: (Boolean) -> Unit) {
    var selectedItemIndex: Int? = null
    var itemList by remember { mutableStateOf(questionItem.options) }

    Scaffold(
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = white
            ) {
                if (selectedItemIndex == null) {
                    questionItem.options.indexOfFirst { it.isSelected }.let {
                        if (it >= 0) {
                            selectedItemIndex = it
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = questionItem.text,
                        style = defaultMediumTextView.copy(fontSize = 18.sp),
                        modifier = Modifier.padding(top = 40.dp, bottom = 40.dp)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(itemList) { item ->
                            RadioButtonTypeQuestionItem(item = item) { newItem ->
                                // Update the list when an item is clicked
                                itemList = itemList.map { if(it == newItem) newItem.copy(isSelected = true) else it.copy(isSelected = false) }
                                questionItem.options.indexOfFirst { it.value == newItem.value }.let { index ->
                                    if (index >= 0) {
                                        questionItem.options.forEach {
                                            it.isSelected = false
                                        }
                                        questionItem.options[index].isSelected = true
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun RadioButtonTypeQuestionItem(item: OptionItemView, onItemClick: (OptionItemView) -> Unit) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        RadioButton(
            modifier = Modifier.padding(end = 8.dp),
            selected = item.isSelected,
            colors = RadioButtonDefaults.colors(selectedColor = primary, unselectedColor = textPrimary),
            onClick = {
                onItemClick(item)
            }
        )
        Text(
            text = item.value,
            style = defaultMediumTextView
        )
    }
}
