package com.example.animalocmaker.ui.customize

import android.content.Context
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.animalocmaker.core.base.BaseAdapter
import com.example.animalocmaker.core.extensions.gone
import com.example.animalocmaker.core.extensions.onSingleClick
import com.example.animalocmaker.core.extensions.visible
import com.example.animalocmaker.core.utils.DataLocal
import com.example.animalocmaker.core.utils.key.AssetsKey
import com.example.animalocmaker.data.custom.ItemNavCustomModel
import com.example.animalocmaker.databinding.ItemCusBinding
import com.facebook.shimmer.ShimmerDrawable


class CustomizeLayerAdapter(val context: Context) :
    BaseAdapter<ItemNavCustomModel, ItemCusBinding>(ItemCusBinding::inflate) {

    var onItemClick: ((ItemNavCustomModel, Int) -> Unit) = { _, _ -> }
    var onNoneClick: ((Int) -> Unit) = {}
    var onRandomClick: (() -> Unit) = {}

    override fun onBind(
        binding: ItemCusBinding,
        item: ItemNavCustomModel,
        position: Int
    ) {
        val shimmerDrawable = ShimmerDrawable().apply {
            setShimmer(DataLocal.shimmer)
        }
        binding.apply {

            vFocus.isVisible = item.isSelected

            when (item.path) {
                AssetsKey.NONE_LAYER -> {
                    btnNone.visible()
                    btnRandom.gone()
                    imvImage.gone()
                }
                AssetsKey.RANDOM_LAYER -> {
                    btnNone.gone()
                    btnRandom.visible()
                    imvImage.gone()
                }
                else -> {
                    btnNone.gone()
                    imvImage.visible()
                    btnRandom.gone()
                    Glide.with(root).load(item.path).placeholder(shimmerDrawable).into(imvImage)
                }
            }

            binding.imvImage.onSingleClick { onItemClick.invoke(item, position) }

            binding.btnRandom.onSingleClick { onRandomClick.invoke() }

            binding.btnNone.onSingleClick { onNoneClick.invoke(position) }
        }

    }
}