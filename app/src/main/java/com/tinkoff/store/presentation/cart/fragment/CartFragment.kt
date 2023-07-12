package com.tinkoff.store.presentation.cart.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.binding.BindingViewHolder
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.mikepenz.fastadapter.listeners.ClickEventHook
import com.tinkoff.store.R
import com.tinkoff.store.databinding.FragmentCartBinding
import com.tinkoff.store.databinding.ItemCartProductBinding
import com.tinkoff.store.domain.entity.CartProduct
import com.tinkoff.store.domain.exception.ServerException
import com.tinkoff.store.presentation.cart.viewmodel.CartFragmentViewModel
import com.tinkoff.store.presentation.seller.viewmodel.SellerProductsViewModel
import com.tinkoff.store.presentation.rv.CartProductItem
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CartFragment : Fragment(R.layout.fragment_cart) {
    private val binding by viewBinding(FragmentCartBinding::bind)
    private val cartViewModel: CartFragmentViewModel by viewModel()

    private val productItemAdapter by lazy {
        ItemAdapter<CartProductItem>()
    }

    private val navView by lazy {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    }

    private val productFastAdapter by lazy {
        FastAdapter.with(productItemAdapter)
    }

    private var list = ArrayList<CartProduct>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        cartViewModel.onGetCartProducts()

        with(binding) {
            buttonTryAgain.setOnClickListener {
                beginLoading()
                cartViewModel.onGetCartProducts()
            }

            buttonBuy.setOnClickListener {
                val ids = ArrayList<Int>()
                for (product in list) {
                    ids.add(product.id)
                }
                cartViewModel.onBuyProductsClick(ids)
            }

            buttonEnter.setOnClickListener {
                navView.selectedItemId = R.id.userFragment
            }

            setItemListeners()
            recyclerViewProducts.adapter = productFastAdapter
        }
    }

    private fun setItemListeners() {
        productFastAdapter.onClickListener = { _, _, item, _ ->
            val action = CartFragmentDirections.actionCartFragmentToProductFragment(item.id)
            findNavController().navigate(action)
            true
        }

        productFastAdapter.addEventHook(object : ClickEventHook<CartProductItem>() {
            override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                return viewHolder.asBinding<ItemCartProductBinding> {
                    it.imageButtonCancel
                }
            }

            override fun onClick(
                v: View,
                position: Int,
                fastAdapter: FastAdapter<CartProductItem>,
                item: CartProductItem
            ) {
                cartViewModel.onDeleteFromCartClick(item.id)
                productFastAdapter.notifyAdapterItemRemoved(list.indexOf(item.product))
                list.remove(item.product)
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
        cartViewModel.cartProductsList.observe(viewLifecycleOwner) { result ->
            result?.fold(onSuccess = {
                if (it.isEmpty()) {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.textViewEmpty.visibility = View.VISIBLE
                } else {
                    list = it as ArrayList<CartProduct>
                    checkSum()
                    FastAdapterDiffUtil[productItemAdapter] =
                        list.map(::CartProductItem)
                    stopLoading()
                }
            }, onFailure = {
                Timber.e(it)
                if (it is ServerException) {
                    if (it.message == "UNAUTHORIZED") {
                        showMessage(getString(R.string.error_access_denied))
                        binding.progressBarLoading.visibility = View.GONE
                        binding.buttonEnter.visibility = View.VISIBLE
                    } else {
                        onError()
                        showMessage(it.message.toString())
                    }
                } else {
                    onError()
                    showMessage(getString(R.string.сheck_connection))
                }
            })
        }
        cartViewModel.orderList.observe(viewLifecycleOwner) { result ->
            result?.fold(onSuccess = {
                showMessage(getString(R.string.successful_order_complete))
                list.clear()
                binding.scrollView.visibility = View.GONE
                binding.buttonBuy.visibility = View.GONE
                binding.textViewEmpty.visibility = View.VISIBLE
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

    private fun checkSum() {
        var sum = 0
        for (product in list) {
            sum += product.price * product.quantity
        }
        binding.textViewTotalPrice.text = sum.toString() + "₽"
    }

    private fun beginLoading() {
        with(binding) {
            progressBarLoading.visibility = View.VISIBLE
            scrollView.visibility = View.GONE
            buttonTryAgain.visibility = View.GONE
            buttonBuy.visibility = View.GONE
        }
    }

    private fun stopLoading() {
        with(binding) {
            progressBarLoading.visibility = View.GONE
            scrollView.visibility = View.VISIBLE
            buttonTryAgain.visibility = View.GONE
            buttonBuy.visibility = View.VISIBLE
        }
    }

    private fun onError() {
        with(binding) {
            progressBarLoading.visibility = View.GONE
            buttonTryAgain.visibility = View.VISIBLE
            scrollView.visibility = View.GONE
            buttonBuy.visibility = View.GONE
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

