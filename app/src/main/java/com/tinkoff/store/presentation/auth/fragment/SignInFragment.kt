package com.tinkoff.store.presentation.auth.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.color.utilities.MaterialDynamicColors.onError
import com.google.android.material.snackbar.Snackbar
import com.tinkoff.store.R
import com.tinkoff.store.databinding.FragmentSignInBinding
import com.tinkoff.store.domain.exception.ServerException
import com.tinkoff.store.presentation.auth.viewmodel.SignInViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.net.URLDecoder
import java.net.URLEncoder

class SignInFragment : Fragment(R.layout.fragment_sign_in) {
    private val binding by viewBinding(FragmentSignInBinding::bind)
    private val signInViewModel: SignInViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        with(binding) {
            buttonLogin.setOnClickListener {
                login()
            }
            textViewSignUpCustomer.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_signUpCustomerFragment)
            }
            textViewSignUpSeller.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_signUpSellerFragment)
            }
        }
    }

    private fun login() {
        with(binding) {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                signInViewModel.onLoginClick(
                    email, password
                )
            } else {
                if (email.isEmpty()) {
                    editTextEmail.error = getString(R.string.validation_error_empty_email)
                }
                if (password.isEmpty()) {
                    editTextPassword.error = getString(R.string.validation_error_empty_password)
                }
            }

        }
    }

    private fun initObservers() {
        signInViewModel.signInResult.observe(viewLifecycleOwner) { result ->
            result?.fold(onSuccess = {
                signInViewModel.onGetAccount()
            }, onFailure = {
                Timber.e(it)
                if (it is ServerException) showMessage(it.message.toString())
                else showMessage(getString(R.string.сheck_connection))
            })
        }

        signInViewModel.account.observe(viewLifecycleOwner) { result ->
            result?.fold(onSuccess = {
                if (it.surname.isNullOrEmpty()) {
                    findNavController().navigate(R.id.action_signInFragment_to_adminFragment)
                } else findNavController().navigate(R.id.action_signInFragment_to_mainPageFragment)
            }, onFailure = {
                Timber.e(it)
                if (it is ServerException) {
                    if (it.message == "UNAUTHORIZED") {
                        showMessage(getString(R.string.error_access_denied))
                    } else showMessage(it.message.toString())
                } else showMessage(getString(R.string.сheck_connection))
            })
        }
    }

    private fun showMessage(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

}
