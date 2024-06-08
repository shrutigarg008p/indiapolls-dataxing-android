package com.dataxing.indiapolls.ui.profiles.survey.questions.checkbox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.dataxing.indiapolls.ui.profiles.survey.questions.QuestionFragmentBase

class CheckBoxTypeQuestionFragment : QuestionFragmentBase() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                CheckBoxTypeQuestionScreen(item) {
                }
            }
        }
    }
}