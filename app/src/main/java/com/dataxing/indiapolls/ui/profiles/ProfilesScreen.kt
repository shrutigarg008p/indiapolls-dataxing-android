package com.dataxing.indiapolls.ui.profiles

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.dataxing.indiapolls.R
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.ui.theme.DefaultItemButton
import com.dataxing.indiapolls.ui.theme.defaultHeaderTextView
import com.dataxing.indiapolls.ui.theme.defaultMediumTextView
import com.dataxing.indiapolls.ui.theme.primary
import com.dataxing.indiapolls.ui.theme.textPrimary
import com.dataxing.indiapolls.ui.theme.white
import com.dataxing.indiapolls.ui.util.showMessage

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ProfilesScreen(
    context: Context,
    navController: NavController,
    viewModel: ProfilesViewModel
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val result by viewModel.result.collectAsState()
    var rewards by remember { mutableStateOf<List<ProfilesItemView>>(emptyList()) }

    Scaffold(
        content = {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = white
            ) {
                when(result) {
                    is Result.Success -> {
                        val data = (result as Result.Success<List<ProfilesItemView>>).data
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
                        text = stringResource(id = R.string.my_profiles),
                        style = defaultHeaderTextView,
                        modifier = Modifier.padding(top = 40.dp)
                    )

                    LazyColumn(
                        modifier = Modifier
                            .padding(top = 40.dp)
                            .weight(1f)
                    ) {
                        itemsIndexed(rewards) { _, item ->
                            ProfilesItem(item) {
                                val action = ProfilesFragmentDirections.actionNavProfilesToProfilesSurveyFragment(it)
                                navController.navigate(action)
                            }
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProfilesItem(item: ProfilesItemView, onItemClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.title,
                style = defaultMediumTextView,
                modifier = Modifier.weight(1f).padding(start = 6.dp)
            )

            if (item.isStarted) {
                DefaultItemButton(
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .height(40.dp),
                    stringResource(id = R.string.update),
                    contentColor = textPrimary,
                ) {
                    onItemClick(item.id)
                }
            } else {
                DefaultItemButton(
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .height(40.dp),
                    stringResource(id = R.string.not_started),
                    contentColor = primary,
                ) {
                    onItemClick(item.id)
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        GlideImage(
            model = item.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color.LightGray)
                .width(IntrinsicSize.Min),
            failure = placeholder(R.drawable.item_profile_image)
        )
    }
}