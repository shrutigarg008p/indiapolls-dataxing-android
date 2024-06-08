package com.dataxing.indiapolls.ui.profiles.survey

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dataxing.indiapolls.data.Result
import com.dataxing.indiapolls.databinding.FragmentSurveyProfilesBinding
import com.dataxing.indiapolls.ui.ViewModelFactory
import com.dataxing.indiapolls.ui.profiles.survey.itemview.SurveyQuestionItemView
import com.dataxing.indiapolls.ui.util.showMessage
import com.google.gson.Gson


class ProfilesSurveyFragment  : Fragment() {

    private val args: ProfilesSurveyFragmentArgs by navArgs()

    private var _binding: FragmentSurveyProfilesBinding? = null

    private val binding get() = _binding!!

    private val profilesViewModel: ProfilesSurveyViewModel by viewModels { ViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSurveyProfilesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val customAdapter = ProfilesSurveyPagerAdapter(this)
        binding.pager.adapter = customAdapter
        profilesViewModel.result.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Result.Success -> {
                    customAdapter.setData(result.data)
                }
                is Result.Error -> {
                }
                is Result.initialized -> {}
            }
        }

        profilesViewModel.submitAnswerResult.observe(viewLifecycleOwner) { result ->
            when(result) {
                is Result.Success -> {
                   context.showMessage(result.data)
                    findNavController().popBackStack()
                }
                is Result.Error -> context.showMessage(result.exception)
                is Result.initialized -> {}
            }
        }

       childFragmentManager.setFragmentResultListener("questionResult", this) { _, bundle ->
            val jsonString = bundle.getString("item")
            val gson = Gson()
            val item = gson.fromJson(jsonString, SurveyQuestionItemView::class.java)
            profilesViewModel.updateItem(item)
        }

        profilesViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.loadingProgressBar.visibility = View.VISIBLE
            } else {
                binding.loadingProgressBar.visibility = View.GONE
            }
        }

        return root
    }

    override fun onStart() {
        super.onStart()

        profilesViewModel.fetchProfilesSurveyQuestions(args.id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}