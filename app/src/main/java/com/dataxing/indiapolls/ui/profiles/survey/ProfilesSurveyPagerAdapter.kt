package com.dataxing.indiapolls.ui.profiles.survey

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dataxing.indiapolls.ui.profiles.survey.itemview.SurveyQuestionItemView
import com.dataxing.indiapolls.ui.profiles.survey.questions.checkbox.CheckBoxTypeQuestionFragment
import com.dataxing.indiapolls.ui.profiles.survey.questions.CompletionFragment
import com.dataxing.indiapolls.ui.profiles.survey.questions.DropDownTypeQuestionFragment
import com.dataxing.indiapolls.ui.profiles.survey.questions.radiobutton.RadioButtonTypeQuestionFragment
import com.google.gson.Gson


class ProfilesSurveyPagerAdapter(fa: Fragment) : FragmentStateAdapter(fa) {
    private var items = mutableListOf<SurveyQuestionItemView>()
    override fun getItemCount(): Int = items.size

    override fun createFragment(position: Int): Fragment {
        val item = items[position]
        when(item.displayType) {
             1 -> {
                val fragment = DropDownTypeQuestionFragment()
                 fragment.arguments = createBundle(item)
                 return fragment
            }

            2 -> {
                val fragment = RadioButtonTypeQuestionFragment()
                fragment.arguments = createBundle(item)
                return fragment
            }

            4 -> {
                val fragment = CheckBoxTypeQuestionFragment()
                fragment.arguments = createBundle(item)
                return fragment
            }

            -1 -> {
                val fragment = CompletionFragment()
                fragment.arguments = createBundle(item)
                return fragment
            }

            else -> { throw IllegalStateException("UnKnown display type") }
        }
    }

    private fun createBundle(item: SurveyQuestionItemView): Bundle {
        val bundle = Bundle()
        bundle.putString("item", Gson().toJson(item))
        return bundle
    }

    fun setData(items: List<SurveyQuestionItemView>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }
}