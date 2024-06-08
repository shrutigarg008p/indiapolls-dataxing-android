package com.dataxing.indiapolls.ui.reward

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dataxing.indiapolls.R
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.ui.theme.DefaultButton
import com.dataxing.indiapolls.ui.theme.black
import com.dataxing.indiapolls.ui.theme.defaultHeaderTextView
import com.dataxing.indiapolls.ui.theme.defaultMediumTextView
import com.dataxing.indiapolls.ui.theme.primary
import com.dataxing.indiapolls.ui.theme.white
import com.dataxing.indiapolls.ui.util.showMessage

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RewardScreen(
    context: Context,
    navController: NavController,
    viewModel: RewardViewModel
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val result by viewModel.result.collectAsState()
    var rewards by remember { mutableStateOf<List<RewardItemView>>(emptyList()) }

    Scaffold(
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = white
            ) {
                when(result) {
                    is Result.Success -> {
                        val data = (result as Result.Success<List<RewardItemView>>).data
                        rewards = data
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
                    Text(
                        text = stringResource(id = R.string.my_rewards),
                        style = defaultHeaderTextView,
                        modifier = Modifier.padding(top = 40.dp)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .padding(top = 40.dp)
                            .weight(1f)
                    ) {
                        itemsIndexed(rewards) { _, item ->
                            RewardItem(item)
                        }
                    }

                    DefaultButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp, bottom = 20.dp)
                            .height(52.dp),
                        stringResource(id = R.string.redeem_points)
                    ) {
                        navController.navigate(R.id.action_nav_rewards_to_redeemPointsFragment)
                    }
                }
            }
        }
    )
}

@Composable
fun RewardItem(item: RewardItemView) {
    Row(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .background(color = black, shape = RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.name,
            style = defaultMediumTextView.copy(color = white),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = item.points,
            style = defaultMediumTextView.copy(color = white),
            modifier = Modifier
                .background(color = primary, shape = RoundedCornerShape(32.dp))
                .padding(horizontal = 20.dp)
        )
    }
}

