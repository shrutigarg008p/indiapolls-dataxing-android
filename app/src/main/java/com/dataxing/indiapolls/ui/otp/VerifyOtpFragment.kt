package com.dataxing.indiapolls.ui.otp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dataxing.indiapolls.ui.ViewModelFactory

class VerifyOtpFragment : Fragment() {
    private val verifyOtpViewModel: VerifyOtpViewModel by viewModels { ViewModelFactory() }

    private val args: VerifyOtpFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                VerifyOtpScreen(
                    context,
                    activity,
                    findNavController(),
                    args.otpArgs.userId,
                    args.otpArgs.mobileNumber,
                    args.otpArgs.completionNavigation,
                    verifyOtpViewModel
                )
            }
        }
    }
}