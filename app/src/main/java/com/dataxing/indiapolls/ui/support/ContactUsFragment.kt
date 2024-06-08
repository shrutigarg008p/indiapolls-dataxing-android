package com.dataxing.indiapolls.ui.support

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dataxing.indiapolls.R
import com.dataxing.indiapolls.ui.ViewModelFactory

class ContactUsFragment : Fragment() {
    private val contactUsViewModel: ContactUsViewModel by viewModels { ViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                ContactUsScreen(context, findNavController(), resources.getStringArray(R.array.select_query_array).toList(), contactUsViewModel)
            }
        }
    }
}