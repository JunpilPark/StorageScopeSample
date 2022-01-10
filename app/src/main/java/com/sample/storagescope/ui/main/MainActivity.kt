package com.sample.storagescope.ui.main

import android.content.Intent
import androidx.databinding.DataBindingUtil
import com.sample.storagescope.R
import com.sample.storagescope.databinding.ActivityMainBinding
import com.sample.storagescope.ui.BaseActivity
import com.sample.storagescope.ui.storage_info.StorageInfoActivity

internal class MainActivity: BaseActivity <ActivityMainBinding> () {

    override fun initBinding() {
        binding.btnSystemInfo.setOnClickListener { moveSystemInfoScreen() }
    }

    private fun moveSystemInfoScreen() {
        val intent = Intent(this, StorageInfoActivity::class.java)
        startActivity(intent)
    }

    override fun getViewDataBinding(): ActivityMainBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_main)
    }
}