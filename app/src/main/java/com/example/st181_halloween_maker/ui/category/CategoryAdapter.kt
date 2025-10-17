package com.example.st181_halloween_maker.ui.category

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.girlmaker.create.avatar.creator.databinding.ItemCategoryBinding
import com.example.st181_halloween_maker.core.extensions.onSingleClick
import com.example.st181_halloween_maker.core.utils.SystemUtils.shimmerDrawable
import kotlin.collections.ArrayList


class CategoryAdapter(val context: Context): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    private val avatarList: ArrayList<String> = arrayListOf()
    var onItemClick: ((String,Int) -> Unit)? = null

    inner class CategoryViewHolder(private val binding : ItemCategoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(path:String, position: Int){
                Glide.with(binding.root).load(path).placeholder(shimmerDrawable).error(shimmerDrawable).into(binding.imvImage)

            binding.root.onSingleClick {
                onItemClick?.invoke(path,position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
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