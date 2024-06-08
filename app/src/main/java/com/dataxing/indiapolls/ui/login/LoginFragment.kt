package com.dataxing.indiapolls.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dataxing.indiapolls.ui.ViewModelFactory
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {
    private val loginViewModel: LoginViewModel by viewModels { ViewModelFactory() }

    private val callbackManager = CallbackManager.Factory.create()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            initializeFacebookLogin()

            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                LoginScreen(context, activity, findNavController(), loginViewModel, ::loginWithFacebook)
            }
        }
    }

    private fun initializeFacebookLogin() {
        auth = Firebase.auth

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    handleFacebookAccessToken(result.accessToken)
                }

                override fun onCancel() {
                    print("")
                }

                override fun onError(error: FacebookException) {
                    print("")
                }
            })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    loginViewModel.loginWithFacebook(user?.email.orEmpty(), user?.uid.toString(), user?.phoneNumber)
                    print("user: $user")
                } else {
                    print("")
                }
            }
    }

    private fun loginWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, callbackManager, listOf("email", "public_profile"))
    }
}