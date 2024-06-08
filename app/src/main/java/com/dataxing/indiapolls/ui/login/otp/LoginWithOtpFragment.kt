package com.dataxing.indiapolls.ui.login.otp

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


class LoginWithOtpFragment : Fragment() {
    private val loginWithOtpViewModel: LoginWithOtpViewModel by viewModels{ ViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                LoginWithOtpScreen(context, findNavController(), loginWithOtpViewModel)
            }
        }
    }
}