package com.dataxing.indiapolls.ui.profiles.survey.questions

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.dataxing.indiapolls.ui.profiles.survey.itemview.SurveyQuestionItemView
import com.google.gson.Gson

open class QuestionFragmentBase : Fragment() {

    lateinit var item: SurveyQuestionItemView
    private var isResumed: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            val jsonString = it.getString("item")
            val gson = Gson()
            item = gson.fromJson(jsonString, SurveyQuestionItemView::class.java)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            val jsonString = it.getString("item")
            val gson = Gson()
            item = gson.fromJson(jsonString, SurveyQuestionItemView::class.java)
        }
    }

    override fun onResume() {
        super.onResume()
        isResumed = true
    }

    override fun onPause() {
        super.onPause()

        if (isResumed) {
            isResumed = false

            setResult()
        }
    }

    fun setResult() {
        val result = Gson().toJson(item)
        setFragmentResult("questionResult", bundleOf("item" to result))
    }
}