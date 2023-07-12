package com.tinkoff.store.presentation.catalog.fragment

import android.os.Bundle
import android.view.View
import android.view.View.NOT_FOCUSABLE
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.tinkoff.store.R
import com.tinkoff.store.databinding.FragmentCatalogBinding
import com.tinkoff.store.domain.exception.ServerException
import com.tinkoff.store.presentation.catalog.viewmodel.CatalogViewModel
import com.tinkoff.store.presentation.main.fragment.MainPageFragmentDirections
import com.tinkoff.store.presentation.rv.CategoryItem
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CatalogFragment : Fragment(R.layout.fragment_catalog) {
    private val binding by viewBinding(FragmentCatalogBinding::bind)
    private val catalogViewModel: CatalogViewModel by viewModel()

    private val categoryItemAdapter by lazy {
        ItemAdapter<CategoryItem>()
    }

    private val categoryFastAdapter by lazy {
        FastAdapter.with(categoryItemAdapter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()

        catalogViewModel.onGetCategories()

        binding.layoutSearch.editTextSearch.focusable = NOT_FOCUSABLE
        binding.layoutSearch.editTextSearch.setOnClickListener {
            findNavController().navigate(R.id.action_catalogFragment_to_searchFragment)
        }
        binding.buttonTryAgain.setOnClickListener {
            beginLoading()
            catalogViewModel.onGetCategories()
        }
        setItemListeners()
        binding.recyclerViewLike.adapter = categoryFastAdapter
    }

    private fun setItemListeners() {
        categoryFastAdapter.onClickListener = { _, _, item, _ ->
            val action = CatalogFragmentDirections.actionCatalogFragmentToSearchFragment(
                item.name,
                item.typeCategory
            )
            findNavController().navigate(action)
            true
        }
    }


    private fun initObservers() {
        catalogViewModel.categoryList.observe(viewLifecycleOwner) { result ->
            result?.fold(onSuccess = {
                FastAdapterDiffUtil[categoryItemAdapter] =
                    it.map(::CategoryItem)
                stopLoading()
            }, onFailure = {
                Timber.e(it)
                onError()
                if (it is ServerException) showMessage(it.message.toString())
                else showMessage(getString(R.string.сheck_connection))
            })
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

