package com.tinkoff.store.presentation.seller.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.tinkoff.store.R
import com.tinkoff.store.databinding.FragmentAddProductBinding
import com.tinkoff.store.presentation.seller.viewmodel.AddProductsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream

class AddProductFragment : Fragment(R.layout.fragment_add_product) {
    private val binding by viewBinding(FragmentAddProductBinding::bind)
  //  private val addViewModel: AddProductsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        binding.buttonAdd.setOnClickListener {
            showMessage(getString(R.string.successfull_product_add))
        }
        val getContent =
            registerForActivityResult(ActivityResultContracts.GetContent()) { localUri ->
                binding.ivPhoto.setImageURI(localUri)
            }
        binding.ivPhoto.setOnClickListener {
            getContent.launch("image/*")
        }
    }

    private lateinit var oldAvatar: Bitmap

    private fun setAvatar(avatar: ByteArray?) {
        oldAvatar = if (avatar == null) {
            val bitmap =
                BitmapFactory.decodeResource(requireContext().resources, R.drawable.no_photo)
            binding.ivPhoto.setImageBitmap(bitmap)
            bitmap
        } else {
            val bitmap = BitmapFactory.decodeByteArray(avatar, 0, avatar.size)
            binding.ivPhoto.setImageBitmap(bitmap)
            bitmap
        }
    }

    private fun getAvatar(): ByteArray {
        val bitmap = if (binding.ivPhoto.drawable == null) {
            oldAvatar
        } else {
            (binding.ivPhoto.drawable as BitmapDrawable).bitmap
        }
        return ByteArrayOutputStream().run {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, this)
            toByteArray()
        }
    }

    private fun initObservers() {
    }

    private fun showMessage(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }

}
