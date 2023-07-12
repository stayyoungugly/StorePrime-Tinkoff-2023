package com.tinkoff.store.presentation.main.fragment

import android.os.Bundle
import android.view.View
import android.view.View.NOT_FOCUSABLE
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.binding.BindingViewHolder
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.mikepenz.fastadapter.listeners.ClickEventHook
import com.tinkoff.store.R
import com.tinkoff.store.databinding.FragmentMainPageBinding
import com.tinkoff.store.databinding.ItemProductBinding
import com.tinkoff.store.domain.entity.Product
import com.tinkoff.store.domain.exception.ServerException
import com.tinkoff.store.presentation.main.viewmodel.MainPageViewModel
import com.tinkoff.store.presentation.rv.ProductMainItem
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainPageFragment : Fragment(R.layout.fragment_main_page) {
    private val binding by viewBinding(FragmentMainPageBinding::bind)
    private val mainPageViewModel: MainPageViewModel by viewModel()

    private val navView by lazy {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    }

    private var list = ArrayList<Product>()
    private var id: Int? = null

    private var isMainProductReceived = false
    private var isMainListReceived = false

    private val productItemAdapter by lazy {
        ItemAdapter<ProductMainItem>()
    }

    private val productFastAdapter by lazy {
        FastAdapter.with(productItemAdapter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        mainPageViewModel.onGetMainProducts()
        mainPageViewModel.onGetMainProduct()

        with(binding) {
            layoutProduct.setOnClickListener {
                val action = id?.let { idItem ->
                    MainPageFragmentDirections.actionMainPageFragmentToProductFragment(
                        idItem
                    )
                }
                if (action != null) {
                    findNavController().navigate(action)
                }
            }
            layoutSearch.editTextSearch.focusable = NOT_FOCUSABLE
            layoutSearch.editTextSearch.setOnClickListener {
                findNavController().navigate(R.id.action_mainPageFragment_to_searchFragment)
            }
            buttonTryAgain.setOnClickListener {
                beginLoading()
                if (!isMainProductReceived) {
                    mainPageViewModel.onGetMainProduct()
                }
                if (!isMainListReceived) {
                    mainPageViewModel.onGetMainProducts()
                }
            }
            setItemListeners()
            recyclerViewLike.adapter = productFastAdapter
        }
    }

    private fun setItemListeners() {
        productFastAdapter.onClickListener = { _, _, item, _ ->
            val action = MainPageFragmentDirections.actionMainPageFragmentToProductFragment(item.id)
            findNavController().navigate(action)
            true
        }

        productFastAdapter.addEventHook(object : ClickEventHook<ProductMainItem>() {
            override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                return viewHolder.asBinding<ItemProductBinding> {
                    it.imageButtonBucket
                }
            }

            override fun onClick(
                v: View,
                position: Int,
                fastAdapter: FastAdapter<ProductMainItem>,
                item: ProductMainItem
            ) {
                if (item.product.amount > 0) {
                    mainPageViewModel.onAddToCartClick(item.id)
                } else showMessage(getString(R.string.product_not_available))
            }
        })
    }

    inline fun <reified T : ViewBinding> RecyclerView.ViewHolder.asBinding(block: (T) -> View): View? {
        return if (this is BindingViewHolder<*> && this.binding is T) {
            block(this.binding as T)
        } else {
            null
        }
    }

    private fun initObservers() {
        mainPageViewModel.mainProduct.observe(viewLifecycleOwner) { result ->
            result?.fold(onSuccess = {
                isMainProductReceived = true
                id = it.id
                setMainProduct(it)
                stopLoading()
            }, onFailure = {
                Timber.e(it)
                onError()
                if (it is ServerException) showMessage(it.message.toString())
                else showMessage(getString(R.string.сheck_connection))
            })
        }
        mainPageViewModel.mainProductsList.observe(viewLifecycleOwner) { result ->
            result?.fold(onSuccess = {
                isMainListReceived = true
                list = it as ArrayList<Product>
                FastAdapterDiffUtil[productItemAdapter] =
                    list.map(::ProductMainItem)
                stopLoading()
            }, onFailure = {
                Timber.e(it)
                onError()
                if (it is ServerException) showMessage(it.message.toString())
                else showMessage(getString(R.string.сheck_connection))
            })
        }
        mainPageViewModel.cartProduct.observe(viewLifecycleOwner) { result ->
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
    }

    private val glideOptions by lazy {
        RequestOptions()
            .priority(Priority.HIGH)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
    }

    private fun setMainProduct(product: Product) {
        with(binding) {
            textViewBuyFastName.text = product.title
            textViewBuyFastPrice.text = product.price.toString() + "₽"
            if (!product.imageUrls.isNullOrEmpty()) {
                Glide.with(requireContext())
                    .load(product.imageUrls[0])
                    .apply(glideOptions)
                    .into(binding.imageViewProductPicture)
            }
        }

    }

    private fun beginLoading() {
        with(binding) {
            progressBarLoading.visibility = View.VISIBLE
            buttonTryAgain.visibility = View.GONE
            scrollView.visibility = View.GONE
        }
    }


    private fun stopLoading() {
        if (isMainListReceived && isMainProductReceived) {
            with(binding) {
                progressBarLoading.visibility = View.GONE
                scrollView.visibility = View.VISIBLE
                buttonTryAgain.visibility = View.GONE
            }
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

