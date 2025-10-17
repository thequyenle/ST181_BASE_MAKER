package com.example.st181_halloween_maker.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil.setContentView
import com.example.st181_halloween_maker.R
import com.example.st181_halloween_maker.core.base.BaseActivity
import com.example.st181_halloween_maker.core.utils.SystemUtils
import com.example.st181_halloween_maker.databinding.ActivitySplashBinding
import com.example.st181_halloween_maker.ui.intro.IntroActivity
import com.example.st181_halloween_maker.ui.language.LanguageActivity

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    private var check = false
    override fun setViewBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {
        if(!isTaskRoot && intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intent.action != null && intent.action.equals(Intent.ACTION_MAIN)){
            finish(); return;
        }
        val handle = Handler()
        handle.postDelayed({
                               if (SystemUtils.isFirstLang(this@SplashActivity)) {
                                   startActivity(Intent(this@SplashActivity, LanguageActivity::class.java))
                                   check = true
                                   finishAffinity()
                               } else {
                                   startActivity(Intent(this@SplashActivity, IntroActivity::class.java))
                                   check = true
                                   finishAffinity()
                               }

                           },3000)
    }

    override fun viewListener() {

    }

    override fun initText() {

    }

    override fun onBackPressed() {
        if(check){
            super.onBackPressed()
        }else{
            check = false
        }
    }

}