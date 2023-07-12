package com.tinkoff.store.presentation.product.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.tinkoff.store.R
import com.tinkoff.store.databinding.FragmentProductBinding
import com.tinkoff.store.domain.entity.Product
import com.tinkoff.store.domain.exception.ServerException
import com.tinkoff.store.presentation.product.viewmodel.ProductFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class ProductFragment : Fragment(R.layout.fragment_product) {
    private val binding by viewBinding(FragmentProductBinding::bind)
    private val productViewModel: ProductFragmentViewModel by viewModel()

    private val args: ProductFragmentArgs by navArgs()

    private val navView by lazy {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    }

    private var id: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        id = args.productId
        productViewModel.onGetProductById(id)

        with(binding) {
            buttonTryAgain.setOnClickListener {
                beginLoading()
            }
            buttonAdd.setOnClickListener {
                productViewModel.onAddToCartClick(id)
                buttonAdd.visibility = View.INVISIBLE
                buttonDelete.visibility = View.VISIBLE
            }
            buttonDelete.setOnClickListener {
                productViewModel.onDeleteFromCartClick(id)
                buttonAdd.visibility = View.VISIBLE
                buttonDelete.visibility = View.GONE
            }
            imageButtonBack.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

        }
    }

    private fun initObservers() {
        productViewModel.mainProduct.observe(viewLifecycleOwner) { result ->
            result?.fold(onSuccess = {
                setMainProduct(it)
                stopLoading()
            }, onFailure = {
                Timber.e(it)
                onError()
                if (it is ServerException) showMessage(it.message.toString())
                else showMessage(getString(R.string.сheck_connection))
            })
        }

        productViewModel.cartProduct.observe(viewLifecycleOwner) { result ->
            result?.fold(onSuccess = {
                showMessage(getString(R.string.successful_cart_add))
            }, onFailure = {
                Timber.e(it)
                if (it is ServerException) {
                    if (it.message == "UNAUTHORIZED") {
                        showMessage(getString(R.string.error_access_denied))
                        navView.selectedItemId = R.id.userFragment
                    } else showMessage(it.message.toString())
                } else showMessage(getString(R.string.сheck_connection))
            })
        }

        productViewModel.error.observe(viewLifecycleOwner) { result ->
            Timber.e(result)
            if (result is ServerException) showMessage(result.message.toString())
            else showMessage(getString(R.string.сheck_connection))
        }
    }

    private val glideOptions by lazy {
        RequestOptions()
            .priority(Priority.HIGH)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
    }

    private fun setMainProduct(product: Product) {
        with(binding) {
            textViewParamCategory.text = product.category
            textViewParamAddress.text = product.sellerCountry + ", " + product.sellerCity
            textViewDesc.text = product.description
            textViewPrice.text = product.price.toString() + "₽"
            textViewTitle.text = product.title
            textViewSellerTitle.text = product.sellerName
            if (!product.imageUrls.isNullOrEmpty()) {
                Glide.with(requireContext())
                    .load(product.imageUrls[0])
                    .apply(glideOptions)
                    .into(binding.imageViewPicture)
            }
        }

    }

    private fun beginLoading() {
        with(binding) {
            progressBarLoading.visibility = View.VISIBLE
            buttonTryAgain.visibility = View.GONE
            scrollView.visibility = View.GONE
            buttonAdd.visibility = View.GONE
        }
    }


    private fun stopLoading() {
        with(binding) {
            progressBarLoading.visibility = View.GONE
            scrollView.visibility = View.VISIBLE
            buttonTryAgain.visibility = View.GONE
            buttonAdd.visibility = View.VISIBLE
        }
    }

    private fun onError() {
        with(binding) {
            progressBarLoading.visibility = View.GONE
            buttonTryAgain.visibility = View.VISIBLE
            scrollView.visibility = View.GONE
            buttonAdd.visibility = View.GONE
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

