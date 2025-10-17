package com.example.st181_halloween_maker.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.st181_halloween_maker.R
import com.example.st181_halloween_maker.core.base.BaseActivity
import com.example.st181_halloween_maker.core.extensions.handleBack
import com.example.st181_halloween_maker.core.extensions.onSingleClick
import com.example.st181_halloween_maker.core.extensions.startIntentAnim
import com.example.st181_halloween_maker.core.utils.DataLocal.getAvatarAsset
import com.example.st181_halloween_maker.databinding.ActivityCategoryBinding
import com.example.st181_halloween_maker.ui.customize.CustomizeActivity

class CategoryActivity : BaseActivity<ActivityCategoryBinding>() {
    private val avatarList = ArrayList<String>()
    private val avatarAdapter by lazy {
        CategoryAdapter(this)
    }
    override fun setViewBinding(): ActivityCategoryBinding {
        return ActivityCategoryBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {
        initData()
        initRcv()
    }

    override fun viewListener() {
        binding.apply {
            btnBack.onSingleClick {
                handleBack()
            }
        }
        handleRcv()
    }

    override fun initText() {

    }

    private fun initData() {
        binding.apply {
            avatarList.clear()
            avatarList.addAll(getAvatarAsset(this@CategoryActivity))
        }
    }
    private fun initRcv() {
        binding.apply {
            rcv.adapter = avatarAdapter
            rcv.itemAnimator = null
            avatarAdapter.submitList(avatarList)
        }
    }
    private fun handleRcv(){
        binding.apply {
            avatarAdapter.onItemClick = { path ->
                startIntentAnim(CustomizeActivity::class.java, path)
            }
        }
    }

}