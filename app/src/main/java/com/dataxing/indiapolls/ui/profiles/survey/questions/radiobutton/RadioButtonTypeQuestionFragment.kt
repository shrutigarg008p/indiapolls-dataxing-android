package com.dataxing.indiapolls.ui.profiles.survey.questions.radiobutton

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.dataxing.indiapolls.ui.profiles.survey.questions.QuestionFragmentBase

class RadioButtonTypeQuestionFragment : QuestionFragmentBase() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                RadioButtonTypeQuestionScreen(item) {
                }
            }
        }
    }
}