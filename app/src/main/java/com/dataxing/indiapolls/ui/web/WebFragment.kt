package com.dataxing.indiapolls.ui.web

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.dataxing.indiapolls.databinding.FragmentWebBinding

class WebFragment : Fragment() {
    private var _binding: FragmentWebBinding? = null

    private val binding get() = _binding!!

    private lateinit var webViewModel: WebViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWebBinding.inflate(inflater, container, false)
        val root: View = binding.root

        webViewModel = ViewModelProvider(this).get(WebViewModel::class.java)

        binding.webView.settings.apply {
            javaScriptEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
        }
        binding.webView.loadUrl("https://www.snapchat.com/spotlight/W7_EDlXWTBiXAEEniNoMPwAAYamJnZ2FtZ2JjAYYtb6HZAYYtb6F7AAAAAA?share_id=uLob5nJMS2Ok9hjUZltGqw&locale=en_IN&sid=0439862faee3461c98f2aab0edf0c4be&surveyid=853946&userid=e036ddf2-8aeb-47be-839e-934e704616b9")

        return root
    }
}