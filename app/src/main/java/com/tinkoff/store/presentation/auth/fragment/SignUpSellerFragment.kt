package com.tinkoff.store.presentation.auth.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.tinkoff.store.R
import com.tinkoff.store.databinding.FragmentSignUpSellerBinding
import com.tinkoff.store.domain.exception.ServerException
import com.tinkoff.store.presentation.auth.viewmodel.SignUpViewModel
import com.tinkoff.store.presentation.validator.RegistrationValidator
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SignUpSellerFragment : Fragment(R.layout.fragment_sign_up_seller) {
    private val binding by viewBinding(FragmentSignUpSellerBinding::bind)
    private val signUpViewModel: SignUpViewModel by viewModel()

    private val registrationValidator by lazy {
        RegistrationValidator()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        with(binding) {
            buttonLogin.setOnClickListener {
                register()
            }
            textViewSignUpCustomer.setOnClickListener {
                findNavController().navigate(R.id.action_signUpSellerFragment_to_signUpCustomerFragment)
            }
            textViewLogin.setOnClickListener {
                findNavController().navigate(R.id.action_signUpSellerFragment_to_signInFragment)
            }
        }
    }

    private fun register() {
        with(binding) {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val name = editTextName.text.toString()
            val inn = editTextINN.text.toString()
            val desc = editTextDesc.text.toString()
            val address = editTextAddress.text.toString()
            val number = editTextNumber.text.toString()

            if (registrationValidator.isValidEmail(email) && registrationValidator.isValidPassword(
                    password
                ) && registrationValidator.isValidLocation(address) && registrationValidator.isValidLocation(
                    address
                ) && registrationValidator.isValidName(name) &&
                registrationValidator.isValidINN(inn) && registrationValidator.isValidPhoneNumber(
                    number
                ) && registrationValidator.isValidDescription(desc)
            ) {
                signUpViewModel.onSignUpClick(
                    desc, email, password, inn, address, name, number
                )
            } else {
                showMessage(getString(R.string.validation_error))
                when {
                    !registrationValidator.isValidName(name) -> editTextName.error =
                        getString(R.string.validation_error_seller_name)
                    !registrationValidator.isValidDescription(desc) -> editTextDesc.error =
                        getString(R.string.validation_error_description)
                    !registrationValidator.isValidINN(inn) -> editTextINN.error =
                        getString(R.string.validation_error_INN)
                    !registrationValidator.isValidLocation(address) -> editTextAddress.error =
                        getString(R.string.validation_error_location)
                    !registrationValidator.isValidPhoneNumber(number) -> editTextNumber.error =
                        getString(R.string.validation_error_number)
                    !registrationValidator.isValidEmail(email) -> editTextEmail.error =
                        getString(R.string.validation_error_email)
                    !registrationValidator.isValidPassword(password) -> editTextPassword.error =
                        getString(R.string.validation_error_password)
                }
            }
        }
    }

    private fun initObservers() {
        signUpViewModel.sellerSignUpResult.observe(viewLifecycleOwner) { result ->
            result?.fold(onSuccess = {
                showMessage(getString(R.string.success_sign_up))
                findNavController().navigate(R.id.action_signUpSellerFragment_to_signInFragment)
            }, onFailure = {
                Timber.e(it)
                if (it is ServerException) showMessage(it.message.toString())
                else showMessage(getString(R.string.сheck_connection))
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
