package com.sample.storagescope.ui.storage_info

import androidx.databinding.DataBindingUtil
import com.sample.storagescope.R
import com.sample.storagescope.databinding.ActivityStorageInfoBinding
import com.sample.storagescope.ui.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class StorageInfoActivity: BaseActivity<ActivityStorageInfoBinding>() {
    private val viewModel by viewModel<StorageViewModel>()

    override fun getViewDataBinding(): ActivityStorageInfoBinding {
        return DataBindingUtil.setContentView(this, R.layout.activity_storage_info)
    }

    override fun initBinding() {
        binding.storageViewModel = viewModel
    }

}