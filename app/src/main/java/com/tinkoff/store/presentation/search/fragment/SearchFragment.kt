package com.tinkoff.store.presentation.search.fragment

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
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
import com.tinkoff.store.databinding.FragmentSearchBinding
import com.tinkoff.store.databinding.ItemProductBinding
import com.tinkoff.store.domain.exception.ServerException
import com.tinkoff.store.presentation.rv.ProductMainItem
import com.tinkoff.store.presentation.search.viewmodel.SearchFragmentViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SearchFragment : Fragment(R.layout.fragment_search) {
    private val binding by viewBinding(FragmentSearchBinding::bind)
    private val searchViewModel: SearchFragmentViewModel by viewModel()

    private val args: SearchFragmentArgs by navArgs()

    private val navView by lazy {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    }

    private var categoryIsSent = false
    private var queryIsSent = false
    private var categoryAndQuerySent = false

    private var category: String? = null

    private var query = ""

    private val productItemAdapter by lazy {
        ItemAdapter<ProductMainItem>()
    }

    private val productFastAdapter by lazy {
        FastAdapter.with(productItemAdapter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        category = args.categoryType
        if (!category.isNullOrEmpty()) {
            binding.textViewTitle.text = args.categoryName
            categoryIsSent = true
            searchViewModel.onSearchByCategory(category.toString())
        } else binding.progressBarLoading.visibility = View.GONE

        binding.buttonTryAgain.setOnClickListener {
            beginLoading()
            if (categoryIsSent) searchViewModel.onSearchByCategory(category.toString())
            if (queryIsSent) searchViewModel.onSearchByQuery(query)
            if (categoryAndQuerySent) searchViewModel.onSearchByQueryAndCategory(
                query,
                category.toString()
            )
        }

        binding.imageButtonBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val searchEditText = binding.layoutSearch.editTextSearch

        searchEditText.requestFocus()
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                beginLoading()
                query = searchEditText.text.toString()
                if (!category.isNullOrEmpty()) {
                    if (query.isEmpty()) {
                        categoryIsSent = true
                        categoryAndQuerySent = false
                        searchViewModel.onSearchByCategory(category.toString())
                    } else {
                        categoryAndQuerySent = true
                        categoryIsSent = false
                        performSearchByCategory(query, category.toString())
                    }
                } else if (query.isNotEmpty()) {
                    queryIsSent = true
                    performSearch(query)
                }
                return@setOnEditorActionListener true
            }
            false
        }
        setItemListeners()
        binding.recyclerViewLike.adapter = productFastAdapter
    }

    private fun performSearch(query: String) {
        searchViewModel.onSearchByQuery(query)
    }

    private fun performSearchByCategory(query: String, category: String) {
        searchViewModel.onSearchByQueryAndCategory(query, category)
    }

    private fun setItemListeners() {
        productFastAdapter.onClickListener = { _, _, item, _ ->
            val action = SearchFragmentDirections.actionSearchFragmentToProductFragment(item.id)
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
                    searchViewModel.onAddToCartClick(item.id)
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
        searchViewModel.productsList.observe(viewLifecycleOwner) { result ->
            result?.fold(onSuccess = {
                if (it.isEmpty()) {
                    binding.progressBarLoading.visibility = View.GONE
                    binding.textViewEmpty.visibility = View.VISIBLE
                } else {
                    FastAdapterDiffUtil[productItemAdapter] =
                        it.map(::ProductMainItem)
                    stopLoading()
                }
            }, onFailure = {
                Timber.e(it)
                onError()
                if (it is ServerException) showMessage(it.message.toString())
                else showMessage(getString(R.string.сheck_connection))
            })
        }
        searchViewModel.cartProduct.observe(viewLifecycleOwner) { result ->
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

