package com.tinkoff.store.presentation.auth.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.tinkoff.store.R
import com.tinkoff.store.databinding.FragmentSignUpCustomerBinding
import com.tinkoff.store.domain.exception.ServerException
import com.tinkoff.store.presentation.auth.viewmodel.SignUpViewModel
import com.tinkoff.store.presentation.validator.RegistrationValidator
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SignUpCustomerFragment : Fragment(R.layout.fragment_sign_up_customer) {
    private val binding by viewBinding(FragmentSignUpCustomerBinding::bind)
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
            textViewSignUpSeller.setOnClickListener {
                findNavController().navigate(R.id.action_signUpCustomerFragment_to_signUpSellerFragment)
            }
            textViewLogin.setOnClickListener {
                findNavController().navigate(R.id.action_signUpCustomerFragment_to_signInFragment)
            }
        }
    }

    private fun register() {
        with(binding) {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val name = editTextName.text.toString()
            val surname = editTextSurname.text.toString()
            val birth = editTextBirth.text.toString()
            val number = editTextNumber.text.toString()
            val address = editTextAddress.text.toString()
            val gender = radioGroupGender.checkedRadioButtonId
            var genderType = "male"
            when (gender) {
                R.id.radioButton_male -> genderType = "MALE"
                R.id.radioButton_female -> genderType = "FEMALE"
            }

            if (registrationValidator.isValidEmail(email) && registrationValidator.isValidPassword(
                    password
                ) && registrationValidator.isValidAddress(address) && registrationValidator.isValidDate(
                    birth
                ) && registrationValidator.isValidName(name) &&
                registrationValidator.isValidDate(birth) && registrationValidator.isValidPhoneNumber(
                    number
                ) && registrationValidator.isValidName(surname) && (gender != -1)
            ) {
                signUpViewModel.onSignUpClick(
                    birth, email, genderType, name, password, number, surname, address
                )
            } else {
                showMessage(getString(R.string.validation_error))
                when {
                    !registrationValidator.isValidName(name) -> editTextName.error =
                        getString(R.string.validation_error_name)
                    !registrationValidator.isValidName(surname) -> editTextSurname.error =
                        getString(R.string.validation_error_surname)
                    !registrationValidator.isValidDate(birth) -> editTextBirth.error =
                        getString(R.string.validation_error_date)
                    (gender == -1) -> radioButtonFemale.error =
                        getString(R.string.validation_error_gender)
                    !registrationValidator.isValidAddress(address) -> editTextAddress.error =
                        getString(R.string.validation_error_address)
                    !registrationValidator.isValidPhoneNumber(number) -> editTextNumber.error =
                        getString(R.string.validation_error_number)
                    !registrationValidator.isValidEmail(email) -> editTextEmail.error =
                        getString(R.string.validation_error_email)
                    !registrationValidator.isValidPassword(password) ->
                        editTextPassword.error =
                            getString(R.string.validation_error_password)
                }
            }
        }
    }


    private fun initObservers() {
        signUpViewModel.customerSignUpResult.observe(viewLifecycleOwner) { result ->
            result?.fold(onSuccess = {
                showMessage(getString(R.string.success_sign_up))
                findNavController().navigate(R.id.action_signUpCustomerFragment_to_signInFragment)
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
