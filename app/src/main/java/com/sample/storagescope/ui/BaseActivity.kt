package com.sample.storagescope.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding

internal abstract class BaseActivity<VB: ViewDataBinding>: AppCompatActivity() {
    lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewDataBinding()
        initBinding()
    }

    abstract fun getViewDataBinding(): VB
    abstract fun initBinding()
}