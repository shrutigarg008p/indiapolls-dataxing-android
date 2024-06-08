package com.dataxing.indiapolls.ui.reward.redeem

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

class RedeemPointsFragment : Fragment() {
    private val redeemPointsViewModel: RedeemPointsViewModel by viewModels { ViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                RedeemPointsScreen(context, findNavController(),  redeemPointsViewModel)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        redeemPointsViewModel.fetchRedemptionModes()
    }
}