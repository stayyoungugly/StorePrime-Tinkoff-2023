package com.tinkoff.store.presentation.seller.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.tinkoff.store.R
import com.tinkoff.store.databinding.FragmentSellerProductsBinding
import com.tinkoff.store.domain.entity.CartProduct
import com.tinkoff.store.presentation.rv.CartProductItem

class SellerProductsFragment : Fragment(R.layout.fragment_seller_products) {
    private val binding by viewBinding(FragmentSellerProductsBinding::bind)
    // private val cartViewModel: SellerProductsViewModel by viewModel()

    private val productItemAdapter by lazy {
        ItemAdapter<CartProductItem>()
    }

    private val productFastAdapter by lazy {
        FastAdapter.with(productItemAdapter)
    }

    private var list = ArrayList<CartProduct>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()

        with(binding) {
            buttonTryAgain.setOnClickListener {
                beginLoading()
            }

            buttonAdd.setOnClickListener {
                findNavController().navigate(R.id.action_sellerProductsFragment_to_addProductFragment)
            }

            recyclerViewProducts.adapter = productFastAdapter
        }
    }

    private fun initObservers() {
//        cartViewModel.cartProductsList.observe(viewLifecycleOwner) { result ->
//            result?.fold(onSuccess = {
//                if (it.isEmpty()) {
//                    binding.progressBarLoading.visibility = View.GONE
//                    binding.textViewEmpty.visibility = View.VISIBLE
//                } else {
//                    list = it as ArrayList<CartProduct>
//                    FastAdapterDiffUtil[productItemAdapter] =
//                        list.map(::CartProductItem)
//                    stopLoading()
//                }
//            }, onFailure = {
//                Timber.e(it)
//                if (it is ServerException) {
//                    if (it.message == "UNAUTHORIZED") {
//                        showMessage(getString(R.string.error_access_denied))
//                        binding.progressBarLoading.visibility = View.GONE
//                    } else {
//                        onError()
//                        showMessage(it.message.toString())
//                    }
//                } else {
//                    onError()
//                    showMessage(getString(R.string.сheck_connection))
//                }
//            })
//        }
    }

    private fun beginLoading() {
        with(binding) {
            progressBarLoading.visibility = View.VISIBLE
            scrollView.visibility = View.GONE
            buttonTryAgain.visibility = View.GONE
        }
    }

    private fun stopLoading() {
        with(binding) {
            progressBarLoading.visibility = View.GONE
            scrollView.visibility = View.VISIBLE
            buttonTryAgain.visibility = View.GONE
        }
    }

    private fun onError() {
        with(binding) {
            progressBarLoading.visibility = View.GONE
            buttonTryAgain.visibility = View.VISIBLE
            scrollView.visibility = View.GONE
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

