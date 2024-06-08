package com.dataxing.indiapolls.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dataxing.indiapolls.ui.ViewModelFactory

class DashboardFragment : Fragment() {

    private val dashboardViewModel: DashboardViewModel by viewModels { ViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                DashboardScreen(context,  dashboardViewModel)
            }
        }
    }

    override fun onStart() {
        super.onStart()

        dashboardViewModel.fetchDashboard()
    }
}