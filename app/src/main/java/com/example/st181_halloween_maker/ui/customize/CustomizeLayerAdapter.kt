package com.example.st181_halloween_maker.ui.customize

import android.content.Context
import androidx.core.view.isVisible
import com.example.st181_halloween_maker.core.base.BaseAdapter
import com.example.st181_halloween_maker.core.extensions.gone
import com.example.st181_halloween_maker.core.extensions.onSingleClick
import com.example.st181_halloween_maker.core.extensions.visible
import com.example.st181_halloween_maker.core.utils.key.AssetsKey
import com.example.st181_halloween_maker.data.custom.ItemNavCustomModel
import com.example.st181_halloween_maker.databinding.ItemCusBinding


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
        binding.apply {
//            val isBody = item.path.contains("/${AssetsKey.BODY_1}")
//            vFocus.isVisible = item.isSelected

//            when (position) {
//                0 -> {
//                    if (isBody) {
//                        btnNone.gone()
//                        imvImage.gone()
//                        btnRandom.visible()
//                    } else {
//                        btnNone.visible()
//                        imvImage.gone()
//                        btnRandom.gone()
//                    }
//                }
//
//                1 -> {
//                    if (!isBody) {
//                        btnNone.gone()
//                        imvImage.gone()
//                        btnRandom.visible()
//                    } else {
//                        btnNone.gone()
//                        imvImage.visible()
//                        btnRandom.gone()
//                        loadImageGlide(root, item.path, imvImage)
//                    }
//                }
//
//                else -> {
//                    btnNone.gone()
//                    imvImage.visible()
//                    btnRandom.gone()
//                    loadImageGlide(root, item.path, imvImage)
//                }
//            }

            binding.imvImage.onSingleClick { onItemClick.invoke(item, position) }

            binding.btnRandom.onSingleClick { onRandomClick.invoke() }

            binding.btnNone.onSingleClick { onNoneClick.invoke(position) }
        }

    }
}