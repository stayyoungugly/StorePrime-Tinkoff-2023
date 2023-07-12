package com.tinkoff.store.presentation.rv

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.tinkoff.store.MyApplication
import com.tinkoff.store.R
import com.tinkoff.store.databinding.ItemCartProductBinding
import com.tinkoff.store.domain.entity.CartProduct

class CartProductItem(val product: CartProduct) : AbstractBindingItem<ItemCartProductBinding>() {

    val id = product.id
    var title = product.title

    override var identifier: Long
        get() = product.hashCode().toLong()
        set(value) {}

    override val type: Int
        get() = R.id.cart_item_id

    private val glideOptions by lazy {
        RequestOptions()
            .priority(Priority.HIGH)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
    }

    override fun bindView(binding: ItemCartProductBinding, payloads: List<Any>) {
        val viewHolder =
            ViewHolder(binding)

        binding.textViewPrice.text = product.price.toString() + "₽"
        binding.textViewTitle.text = product.title
        binding.textViewAmount.text = product.quantity.toString()

        if (!product.imageUrls.isNullOrEmpty()) {
            Glide.with(MyApplication.appContext)
                .load(product.imageUrls[0])
                .apply(glideOptions)
                .into(binding.imageViewProductPicture)
        }
        binding.root.setTag(R.id.product_item_id, viewHolder)

    }

    override fun createBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): ItemCartProductBinding {
        return ItemCartProductBinding.inflate(inflater, parent, false)
    }

    override fun unbindView(binding: ItemCartProductBinding) {
        binding.textViewPrice.text = null
        binding.textViewTitle.text = null
        Glide.with(MyApplication.appContext).clear(binding.imageViewProductPicture)
    }

    class ViewHolder(val binding: ItemCartProductBinding) : RecyclerView.ViewHolder(binding.root) {
        val button = binding.imageButtonCancel
    }
}
