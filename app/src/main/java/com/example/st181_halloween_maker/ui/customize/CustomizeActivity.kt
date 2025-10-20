package com.example.st181_halloween_maker.ui.customize

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.lifecycleScope
import com.example.st181_halloween_maker.R
import com.example.st181_halloween_maker.core.base.BaseActivity
import com.example.st181_halloween_maker.core.dialog.ConfirmDialog
import com.example.st181_halloween_maker.core.extensions.dLog
import com.example.st181_halloween_maker.core.extensions.eLog
import com.example.st181_halloween_maker.core.extensions.hideNavigation
import com.example.st181_halloween_maker.core.extensions.startIntent
import com.example.st181_halloween_maker.core.utils.key.IntentKey
import com.example.st181_halloween_maker.core.utils.key.ValueKey
import com.example.st181_halloween_maker.databinding.ActivityCustomizeBinding
import com.example.st181_halloween_maker.ui.home.DataViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import kotlin.jvm.java
import kotlin.text.get

class CustomizeActivity : BaseActivity<ActivityCustomizeBinding>() {
    private val viewModel: CustomizeViewModel by viewModels()
    private val dataViewModel: DataViewModel by viewModels()
    val colorLayerAdapter by lazy { ColorLayerAdapter(this) }
    val customizeLayerAdapter by lazy { CustomizeLayerAdapter(this) }

    override fun setViewBinding(): ActivityCustomizeBinding {
        return ActivityCustomizeBinding.inflate(LayoutInflater.from(this))
    }

    override fun initView() {
        initRcv()
        dataViewModel.ensureData(this)
    }

    override fun dataObservable() {
//        allData
        lifecycleScope.launch {
            dataViewModel.allData.collect { list ->
                if (list.isNotEmpty()) {
                    viewModel.positionSelected = intent.getIntExtra(IntentKey.INTENT_KEY, 0)
                    viewModel.setDataCustomize(list[viewModel.positionSelected])
                    viewModel.setIsDataAPI(viewModel.positionSelected >= ValueKey.POSITION_API)
                    initData()
                }
            }
        }
    }

    override fun viewListener() {

    }

    override fun initText() {

    }
    private fun initRcv() {
        binding.apply {
            rcvLayer.apply {
                adapter = customizeLayerAdapter
                itemAnimator = null
            }

            rcvColor.apply {
                adapter = colorLayerAdapter
                itemAnimator = null
            }

//            rcvNavigation.apply {
//                adapter = bottomNavigationAdapter
//                itemAnimator = null
//            }
        }
    }

    private fun initData(){
        val handleExceptionCoroutine = CoroutineExceptionHandler { _, throwable ->
            eLog("initData: ${throwable.message}")
            CoroutineScope(Dispatchers.Main).launch {
                dismissLoading(true)
                val dialogExit = ConfirmDialog(this@CustomizeActivity, R.string.error, R.string.an_error_occurred)
                dialogExit.show()
                dialogExit.onNoClick = {
                    dialogExit.dismiss()
                    finish()
                }
                dialogExit.onYesClick = {
                    dialogExit.dismiss()
                    hideNavigation(true)
                    startIntent(CustomizeActivity::class.java, viewModel.positionSelected)
                    finish()
                }
            }
        }

        CoroutineScope(SupervisorJob() + Dispatchers.IO + handleExceptionCoroutine).launch {
            var pathImageDefault = ""
            // Get data from list
            val deferred1 = async {
//                viewModel.resetDataList()
//                viewModel.setCategoryList(attributes)
                viewModel.addValueToItemNavList()
                viewModel.setFocusItemNavDefault()
                viewModel.setBottomNavigationListDefault()

                viewModel.itemNavList.value.forEach {
                    Log.d("nbhieu", "itemNavList: $it")
                }
                Log.d("nbhieu", "--------------------------------------------------------------------------------------------------------")
                viewModel.bottomNavigationList.value.forEach {
                    Log.d("nbhieu", "bottomNavigationList: $it")
                }
                dLog("deferred1")
                return@async true
            }
        }
    }
}