package com.tinkoff.store.presentation.person.user.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.tinkoff.store.R
import com.tinkoff.store.databinding.FragmentUserBinding
import com.tinkoff.store.domain.entity.Account
import com.tinkoff.store.domain.exception.ServerException
import com.tinkoff.store.presentation.person.viewmodel.AccountViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class UserFragment : Fragment(R.layout.fragment_user) {
    private val binding by viewBinding(FragmentUserBinding::bind)
    private val accountViewModel: AccountViewModel by viewModel()

    private var id: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        accountViewModel.onGetAccount()
        with(binding) {
            buttonTryAgain.setOnClickListener {
                beginLoading()
                accountViewModel.onGetAccount()
            }
            textViewQuit.setOnClickListener {
                accountViewModel.onQuitClick()
                findNavController().navigate(R.id.action_userFragment_to_signInFragment)
            }
        }
    }

    private fun initObservers() {
        accountViewModel.account.observe(viewLifecycleOwner) { result ->
            result?.fold(onSuccess = {
                setAccount(it)
                stopLoading()
            }, onFailure = {
                Timber.e(it)
                if (it is ServerException) {
                    if (it.message == "UNAUTHORIZED") {
                        showMessage(getString(R.string.error_access_denied))
                        findNavController().navigate(R.id.action_userFragment_to_signInFragment)
                    } else showMessage(it.message.toString())
                } else showMessage(getString(R.string.сheck_connection))
                onError()
            })
        }
    }

    private val glideOptions by lazy {
        RequestOptions()
            .priority(Priority.HIGH)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
    }

    private fun setAccount(account: Account) {
        with(binding) {
            textViewName.text = account.name + " " + account.surname
            textViewBalanceValue.text = account.cardBalance.toString() + "₽"
        }
    }

    private fun beginLoading() {
        with(binding) {
            progressBarLoading.visibility = View.VISIBLE
            buttonTryAgain.visibility = View.GONE
            layoutInfo.visibility = View.GONE
        }
    }


    private fun stopLoading() {
        with(binding) {
            progressBarLoading.visibility = View.GONE
            layoutInfo.visibility = View.VISIBLE
            buttonTryAgain.visibility = View.GONE
        }
    }

    private fun onError() {
        with(binding) {
            progressBarLoading.visibility = View.GONE
            buttonTryAgain.visibility = View.VISIBLE
            layoutInfo.visibility = View.GONE
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

