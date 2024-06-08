package com.dataxing.indiapolls.ui.survey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dataxing.indiapolls.ui.ViewModelFactory

class SurveyFragment : Fragment() {
    private val surveyViewModel: SurveyViewModel by viewModels { ViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                SurveyScreen(context, activity, surveyViewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()

        surveyViewModel.fetchSurvey()
        surveyViewModel.fetchProfile()
    }
}