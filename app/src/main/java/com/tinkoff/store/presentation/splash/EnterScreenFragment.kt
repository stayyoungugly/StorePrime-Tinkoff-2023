package com.tinkoff.store.presentation.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.tinkoff.store.R
import com.tinkoff.store.databinding.FragmentEnterBinding
import com.tinkoff.store.presentation.auth.viewmodel.SignInViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class EnterScreenFragment : Fragment(R.layout.fragment_enter) {
    private val binding by viewBinding(FragmentEnterBinding::bind)
    private val signInViewModel: SignInViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        findNavController().navigate(R.id.action_enterScreenFragment_to_mainPageFragment)
    }

    private fun initObservers() {
        signInViewModel.signInResult.observe(viewLifecycleOwner) { result ->
            result?.fold(onSuccess = {
                showMessage(getString(R.string.login_success))
                findNavController().navigate(R.id.action_signUpCustomerFragment_to_signInFragment)
            }, onFailure = {
                Timber.e(it)
                showMessage(it.message.toString())
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
