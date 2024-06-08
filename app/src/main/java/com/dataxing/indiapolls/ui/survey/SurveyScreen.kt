package com.dataxing.indiapolls.ui.survey

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.dataxing.indiapolls.R
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.ui.home.HomeActivity
import com.dataxing.indiapolls.ui.theme.SurveyOutlinedButton
import com.dataxing.indiapolls.ui.theme.black
import com.dataxing.indiapolls.ui.theme.defaultMediumTextView
import com.dataxing.indiapolls.ui.theme.primary
import com.dataxing.indiapolls.ui.theme.smallSizeMediumFontTextView
import com.dataxing.indiapolls.ui.theme.white
import com.dataxing.indiapolls.ui.util.launchUrl
import com.dataxing.indiapolls.ui.util.showMessage

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SurveyScreen(
    context: Context,
    activity: Activity?,
    viewModel: SurveyViewModel
) {
    var dashboardMessage by remember { mutableStateOf("") }
    var dashboardMessageColorCode by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()
    val uiState by viewModel.dashboardMessageUiState.collectAsState()
    val result by viewModel.result.collectAsState()
    var surveys by remember { mutableStateOf<List<SurveyItemView>>(emptyList()) }

    Scaffold(
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = white
            ) {
                when(result) {
                    is Result.Success -> {
                        val data = (result as Result.Success<List<SurveyItemView>>).data
                        surveys = data

                        time = data.firstOrNull()?.let {
                            "${it.time} ${ContextCompat.getString(context, R.string.min)}"
                        } ?: ""
                    }
                    is Result.Error -> context.showMessage((result as Result.Error).exception)
                    is Result.initialized -> {}
                }

                when(uiState) {
                    is Result.Success -> {
                        val data = (uiState as Result.Success<DashboardMessageUiState>).data
                        dashboardMessage = data.messages
                        dashboardMessageColorCode = data.colorCode
                    }
                    is Result.Error -> context.showMessage((result as Result.Error).exception)
                    is Result.initialized -> {}
                }

                if (isLoading) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(50.dp),
                            color = primary
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .absolutePadding(left = 24.dp, right = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Top Layout
                    Column(
                        modifier = Modifier
                            .padding(top = 40.dp)
                            .fillMaxWidth()
                    ) {
                        if (dashboardMessage.isNotEmpty()) {
                            Text(
                                text = dashboardMessage.split(".")[0],
                                style = defaultMediumTextView.copy(color = Color(android.graphics.Color.parseColor(dashboardMessageColorCode))),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() },
                                        onClick = {
                                            val isProfilePending =
                                                viewModel.dashboardMessageUiState.value.dataOrNull()?.isProfilePending
                                                    ?: false

                                            val homeActivity = activity as? HomeActivity
                                            homeActivity?.let {
                                                if (isProfilePending) {
                                                    homeActivity.setNavViewCheckedItem(R.id.nav_profiles)
                                                } else {
                                                    homeActivity.setNavViewCheckedItem(R.id.nav_rewards)
                                                }
                                            }
                                        }
                                    )
                            )
                            Text(
                                text = dashboardMessage.split(".")[1],
                                style = defaultMediumTextView.copy(color = Color(android.graphics.Color.parseColor("#8fce00"))),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 16.dp)
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() },
                                        onClick = {
                                            val isProfilePending =
                                                viewModel.dashboardMessageUiState.value.dataOrNull()?.isProfilePending
                                                    ?: false

                                            val homeActivity = activity as? HomeActivity
                                            homeActivity?.let {
                                                if (isProfilePending) {
                                                    homeActivity.setNavViewCheckedItem(R.id.nav_profiles)
                                                } else {
                                                    homeActivity.setNavViewCheckedItem(R.id.nav_rewards)
                                                }
                                            }
                                        }
                                    )
                            )
                        }

                        Row(
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(id = R.string.live_survey),
                                style = defaultMediumTextView
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(primary)
                            )
                        }
                    }

                    // First Survey View Group
                    Row(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .background(color = black, shape = RoundedCornerShape(8.dp))
                            .fillMaxWidth()
                            .clickable(
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() },
                                onClick = {
                                    val firstSurveyItem = viewModel.result.value
                                        .dataOrNull()
                                        ?.firstOrNull()
                                    if (firstSurveyItem != null) {
                                        context.launchUrl(firstSurveyItem.url)
                                    } else {
                                        context.showMessage(R.string.live_survey_not_available)
                                    }
                                }
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_survey),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(24.dp)
                                    .padding(end = 4.dp)
                            )
                            Text(
                                text = stringResource(id = R.string.take_a_survey),
                                style = smallSizeMediumFontTextView.copy(color = white),
                                modifier = Modifier.weight(1f),
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = time,
                                style = smallSizeMediumFontTextView.copy(color = white),
                                modifier = Modifier.padding(end = 4.dp)
                            )
                            Image(
                                painter = painterResource(id = R.drawable.ic_arrow_forward),
                                contentDescription = null
                            )
                        }
                    }

                    // Survey Text
                    Text(
                        text = stringResource(id = R.string.survey),
                        style = defaultMediumTextView,
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .align(Alignment.Start)
                    )

                    LazyRow(
                        modifier = Modifier
                            .padding(top = 14.dp)
                            .weight(1f)
                    ) {
                        itemsIndexed(surveys) { _, item ->
                            SurveyItem(context, item) {
                                context.launchUrl(it.url)
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun SurveyItem(context: Context, item: SurveyItemView, onItemClick: (SurveyItemView) -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .size(width = 270.dp, height = 330.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = {
                    onItemClick(item)
                }
            ),
        colors = CardDefaults.cardColors(containerColor = Color(android.graphics.Color.parseColor(item.color))),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SurveyOutlinedButton(
                    modifier = Modifier
                        .wrapContentWidth(),
                    stringResource(id = R.string.new_text)
                ) {
                }

                Text(
                    text = item.time?.let {
                        "$it ${ContextCompat.getString(context, R.string.min)}"
                    } ?: "",
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(end = 14.dp),
                    color = Color.White
                )
            }
            Text(
                text = item.title,
                style = defaultMediumTextView.copy(color = white),
                color = Color.White,
                modifier = Modifier
                    .padding(top = 14.dp, start = 14.dp)
            )

            LazyColumn(
                modifier = Modifier.weight(1f).padding(top = 14.dp, start = 14.dp, end = 14.dp)
            ) {
                itemsIndexed(item.values) { _, item ->
                    SurveySubItem(item)
                }
            }

            SurveyOutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp, start = 14.dp, end = 14.dp)
                    .height(48.dp),
                stringResource(id = R.string.take_survey)
            ) {
                onItemClick(item)
            }
        }
    }
}

@Composable
fun SurveySubItem(item: String) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_check_circle),
            contentDescription = null,
            modifier = Modifier.padding(top = 4.dp).size(14.dp),
            tint = white
        )
        Text(
            text = item,
            style = defaultMediumTextView.copy(color = white),
            modifier = Modifier.padding(start = 14.dp).align(Alignment.Top)
        )
    }
}