package com.example.animalocmaker.ui.lissticker

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.animalocmaker.core.extensions.onSingleClick
import com.example.animalocmaker.core.utils.SystemUtils.shimmerDrawable
import com.example.animalocmaker.databinding.ItemCategoryBinding
import com.example.animalocmaker.databinding.ItemListStickerBinding
import kotlin.collections.ArrayList


class ListStickerAdapter(val context: Context): RecyclerView.Adapter<ListStickerAdapter.ListStickerViewHolder>() {
    private val avatarList: ArrayList<String> = arrayListOf()
    var onItemClick: ((String,Int) -> Unit)? = null

    inner class ListStickerViewHolder(private val binding : ItemListStickerBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(path:String, position: Int){
                Glide.with(binding.root).load(path).placeholder(shimmerDrawable).error(shimmerDrawable).into(binding.imvImage)

            binding.root.onSingleClick {
                onItemClick?.invoke(path,position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListStickerViewHolder {
        val binding = ItemListStickerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListStickerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListStickerViewHolder, position: Int) {
        val item = avatarList[position]
        holder.bind(item,position)
    }

    override fun getItemCount(): Int {
        return avatarList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: ArrayList<String>) {
        avatarList.clear()
        avatarList.addAll(list)
        notifyDataSetChanged()
    }


}