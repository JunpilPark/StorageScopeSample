package com.sample.storagescope.ui.storage_info

import android.app.Application
import android.os.Build
import android.os.storage.StorageManager
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.io.File
import java.util.*

class StorageViewModel(application: Application): AndroidViewModel(application) {
    private val _internalStoragePaths = MutableLiveData<String>()
    val internalStoragePath: LiveData<String> get() = _internalStoragePaths


    init {
        val internalPathsString = StringBuilder()
        getFilesDirInfoString(internalPathsString)
        internalPathsString.appendLine()
        getCacheDirInfoString(internalPathsString)
        _internalStoragePaths.value = internalPathsString.toString()
    }

    private fun getCacheDirInfoString(internalPathsString: StringBuilder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            internalPathsString.appendLine("cacheDir 의 사용 가능한 용량:")
            internalPathsString.appendLine(convertShortStorageUnit(getAvailableBytes(getApplication<Application>().cacheDir)))
        }
        internalPathsString.appendLine()
        internalPathsString.appendLine("cacheDir.absolutePath:")
        internalPathsString.appendLine(getApplication<Application>().cacheDir.absolutePath)
        internalPathsString.appendLine()
        internalPathsString.appendLine("cacheDir.path:")
        internalPathsString.appendLine(getApplication<Application>().cacheDir.path)
    }

    private fun getFilesDirInfoString(internalPathsString: StringBuilder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            internalPathsString.appendLine("filesDir 의 사용 가능한 용량:")
            internalPathsString.appendLine(convertShortStorageUnit(getAvailableBytes(getApplication<Application>().filesDir)))
        }
        internalPathsString.appendLine()
        internalPathsString.appendLine("filesDir.absolutePath:")
        internalPathsString.appendLine(getApplication<Application>().filesDir.absolutePath)
        internalPathsString.appendLine()
        internalPathsString.appendLine("filesDir.path:")
        internalPathsString.appendLine(getApplication<Application>().filesDir.path)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAvailableBytes(filesDir: File): Long {
        val storageManager = getApplication<Application>().getSystemService<StorageManager>()!!
        val appSpecificInternalDirUuid: UUID = storageManager.getUuidForPath(filesDir)
        return storageManager.getAllocatableBytes(appSpecificInternalDirUuid)
    }

    private fun convertShortStorageUnit(byte: Long): String {
        val unit = listOf("Byte", "KB", "MB", "GB", "TB")
        var divide: Float = byte.toFloat()
        for (i: Int in 0..unit.size) {
            val temp:Float = divide / 1024.0f
            if(temp <= 1.0f) {
                return "$divide ${unit[i]}"
            }
            divide = temp
        }
        return "$divide ${unit.last()}"
    }
}