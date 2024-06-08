package com.dataxing.indiapolls.ui.reward

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dataxing.indiapolls.ui.ViewModelFactory

class RewardFragment : Fragment() {
    private val rewardViewModel: RewardViewModel by viewModels { ViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                RewardScreen(context, findNavController(),  rewardViewModel)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        rewardViewModel.fetchReward()
    }
}