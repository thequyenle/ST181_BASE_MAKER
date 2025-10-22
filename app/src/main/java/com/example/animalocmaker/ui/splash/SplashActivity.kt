package com.example.animalocmaker.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.lifecycleScope
import com.example.animalocmaker.R
import com.example.animalocmaker.core.base.BaseActivity
import com.example.animalocmaker.core.helper.AssetHelper
import com.example.animalocmaker.core.helper.MediaHelper
import com.example.animalocmaker.core.utils.SystemUtils
import com.example.animalocmaker.core.utils.key.AssetsKey
import com.example.animalocmaker.data.custom.LayerListModel
import com.example.animalocmaker.databinding.ActivitySplashBinding
import com.example.animalocmaker.ui.home.DataViewModel
import com.example.animalocmaker.ui.intro.IntroActivity
import com.example.animalocmaker.ui.language.LanguageActivity
import kotlinx.coroutines.launch
import java.util.ArrayList

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    private var check = false
    private val viewModel: DataViewModel by viewModels()
    override fun setViewBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {
        if (!isTaskRoot && intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intent.action != null && intent.action.equals(
                Intent.ACTION_MAIN
            )
        ) {
            finish(); return;
        }

        viewModel.ensureData(this)

    }

    override fun dataObservable() {
        lifecycleScope.launch {
            viewModel.allData.collect { data ->
                if (data.isNotEmpty()){
                    if (SystemUtils.isFirstLang(this@SplashActivity)) {
                        startActivity(Intent(this@SplashActivity, LanguageActivity::class.java))
                        check = true
                        finishAffinity()
                    } else {
                        startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
                        check = true
                        finishAffinity()
                    }
                }
            }
        }
    }
    override fun viewListener() {

    }

    override fun initText() {

    }

    override fun onBackPressed() {
        if (check) {
            super.onBackPressed()
        } else {
            check = false
        }
    }

}