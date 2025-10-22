package com.example.animalocmaker.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.animalocmaker.R
import com.example.animalocmaker.SettingsActivity
import com.example.animalocmaker.core.base.BaseActivity
import com.example.animalocmaker.core.extensions.onSingleClick
import com.example.animalocmaker.core.extensions.startIntentAnim
import com.example.animalocmaker.databinding.ActivityHomeBinding
import com.example.animalocmaker.ui.category.CategoryActivity
import com.example.animalocmaker.ui.category.CategoryAdapter
import com.example.animalocmaker.ui.lissticker.ListStickerActivity
import com.example.animalocmaker.ui.mycreation.MycreationActivity

class HomeActivity : BaseActivity<ActivityHomeBinding>() {

    override fun setViewBinding(): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {

    }

    override fun viewListener() {
        binding.apply {
            btnCreate.onSingleClick {
                startIntentAnim(CategoryActivity::class.java)
            }
            btnSetting.onSingleClick {
                startIntentAnim(SettingsActivity::class.java)
            }
            btnSticker.onSingleClick {
                startIntentAnim(ListStickerActivity::class.java)
            }
            btnMyCreation.onSingleClick {
                startIntentAnim(MycreationActivity::class.java)
            }
        }
    }

    override fun initText() {

    }

}