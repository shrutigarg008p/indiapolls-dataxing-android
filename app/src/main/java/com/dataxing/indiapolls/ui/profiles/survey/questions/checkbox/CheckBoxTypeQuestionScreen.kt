package com.dataxing.indiapolls.ui.profiles.survey.questions.checkbox

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.collection.emptyLongSet
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dataxing.indiapolls.ui.profiles.survey.itemview.OptionItemView
import com.dataxing.indiapolls.ui.profiles.survey.itemview.SurveyQuestionItemView
import com.dataxing.indiapolls.ui.theme.black
import com.dataxing.indiapolls.ui.theme.defaultMediumTextView
import com.dataxing.indiapolls.ui.theme.primary
import com.dataxing.indiapolls.ui.theme.white

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CheckBoxTypeQuestionScreen(item: SurveyQuestionItemView, onCheckedChange: (Boolean) -> Unit) {
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
                        modifier = Modifier.padding(top = 40.dp, bottom = 40.dp)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        itemsIndexed(item.options) { _, item ->
                            CheckBoxTypeQuestionItem(item) {

                                    onCheckedChange(it)


                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun CheckBoxTypeQuestionItem(item: OptionItemView, onCheckedChange: (Boolean) -> Unit) {
    var checked by remember { mutableStateOf(item.isSelected) }
    // Initialize states for the child checkboxes
    val optionCheckedStates = remember { mutableStateListOf(false, false, false) }
    Row(
        modifier = Modifier
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Checkbox(
            checked = checked,
            onCheckedChange = {
                checked = it
                item.isSelected = it
            },
            colors = CheckboxDefaults.colors(checkedColor = primary),
            modifier = Modifier.padding(end = 8.dp)
        )
        Text(
            text = item.value,
            style = defaultMediumTextView
        )
    }



    Text(
        if(checked)
            if (item.hint=="MA")
                "Clear Other checkboxes"
            else
                ""
        else
            ""

    )
}