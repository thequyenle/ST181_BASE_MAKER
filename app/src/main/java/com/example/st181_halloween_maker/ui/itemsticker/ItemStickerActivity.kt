package com.example.st181_halloween_maker.ui.itemsticker

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil.setContentView
import com.example.st181_halloween_maker.R
import com.example.st181_halloween_maker.core.base.BaseActivity
import com.example.st181_halloween_maker.databinding.ActivityItemStickerBinding

class ItemStickerActivity : BaseActivity<ActivityItemStickerBinding>() {
    override fun setViewBinding(): ActivityItemStickerBinding {
        return ActivityItemStickerBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {

    }

    override fun viewListener() {

    }

    override fun initText() {

    }

}