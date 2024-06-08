package com.dataxing.indiapolls.ui.profiles.survey.questions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy


class DropDownTypeQuestionFragment : QuestionFragmentBase() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var text = ""
        item.options.indexOfFirst { it.isSelected }.let { index ->
            if (index >= 0) {
                text = item.options[index].value
            }
        }

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                DropDownTypeQuestionScreen(item, text) {
                    onItemClick(it)
                }
            }
        }
    }
    private fun onItemClick(position: Int) {
        item.options[position].isSelected = true
    }
}