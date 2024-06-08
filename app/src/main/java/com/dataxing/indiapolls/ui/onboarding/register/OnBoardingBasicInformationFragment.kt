package com.dataxing.indiapolls.ui.onboarding.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dataxing.indiapolls.R
import com.dataxing.indiapolls.ui.onboarding.OnBoardingViewModel

class OnBoardingBasicInformationFragment : Fragment() {
    private val viewModel: OnBoardingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                OnBoardingBasicInformationScreen(
                    context,
                    activity,
                    parentFragmentManager,
                    findNavController(),
                    resources.getStringArray(R.array.select_gender).toList(),
                    viewModel
                )
            }
        }
    }
}