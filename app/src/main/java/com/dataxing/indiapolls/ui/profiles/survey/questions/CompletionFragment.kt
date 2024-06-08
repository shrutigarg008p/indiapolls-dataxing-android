package com.dataxing.indiapolls.ui.profiles.survey.questions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy

class CompletionFragment : QuestionFragmentBase() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                CompletionScreen {
                    onCompleted()
                }
            }
        }
    }

    private fun onCompleted() {
        item.options.firstOrNull()?.let {
            it.isSelected = true
            setResult()
        }
    }
}