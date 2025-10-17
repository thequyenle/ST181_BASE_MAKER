package com.example.st181_halloween_maker.ui.lissticker

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil.setContentView
import com.example.st181_halloween_maker.R
import com.example.st181_halloween_maker.core.base.BaseActivity
import com.example.st181_halloween_maker.databinding.ActivityListStickerBinding

class ListStickerActivity : BaseActivity<ActivityListStickerBinding>() {
    override fun setViewBinding(): ActivityListStickerBinding {
        return ActivityListStickerBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {

    }

    override fun viewListener() {

    }

    override fun initText() {

    }

}