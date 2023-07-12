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
import com.tinkoff.store.databinding.ItemCategoryBinding
import com.tinkoff.store.databinding.ItemProductBinding
import com.tinkoff.store.domain.entity.Category
import com.tinkoff.store.domain.entity.Product

class CategoryItem(val category: Category) : AbstractBindingItem<ItemCategoryBinding>() {
    val name = category.name
    val typeCategory = category.type
    val id = category.imageUrl

    override var identifier: Long
        get() = category.hashCode().toLong()
        set(value) {}

    override val type: Int
        get() = R.id.category_item_id

    private val glideOptions by lazy {
        RequestOptions()
            .priority(Priority.HIGH)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
    }

    override fun bindView(binding: ItemCategoryBinding, payloads: List<Any>) {
        val viewHolder =
            ViewHolder(binding)

        binding.textViewCategory.text = category.name
//        if (category.imageUrl.isNotEmpty()) {
//            Glide.with(MyApplication.appContext)
//                .load(category.imageUrl)
//                .apply(glideOptions)
//                .into(binding.imageViewCategoryPicture)
//        }
        binding.root.setTag(R.id.product_item_id, viewHolder)

    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemCategoryBinding {
        return ItemCategoryBinding.inflate(inflater, parent, false)
    }

    override fun unbindView(binding: ItemCategoryBinding) {
        binding.textViewCategory.text = null
        Glide.with(MyApplication.appContext).clear(binding.imageViewCategoryPicture)
    }

    class ViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root)
}
