package com.sample.storagescope.ui.storage_info

import android.app.Application
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import androidx.annotation.RequiresApi
import androidx.core.content.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sample.storagescope.ui.storage_info.data.Paragraph
import com.sample.storagescope.ui.storage_info.data.Section
import java.io.File
import java.lang.StringBuilder
import java.util.*

class StorageViewModel(application: Application): AndroidViewModel(application) {
    private val _internalStoragePaths = MutableLiveData<String>()
    val internalStoragePath: LiveData<String> get() = _internalStoragePaths

    private val _externalStoragePaths = MutableLiveData<String>()
    val externalStoragePath: LiveData<String> get() = _externalStoragePaths

    init {
        _internalStoragePaths.value = getInternalStorageSections(application)
            .map { it.toString() }
            .reduce { string1, string2 -> "${string1}\n${string2}" }
    }

    private fun getInternalStorageSections(application: Application) = listOf(
        getFilesDirDirInInternalStorageSection(application),
        getCacheDirInInternalStorageSection(application)
    )

    private fun getCacheDirInInternalStorageSection(application: Application): Section {
        val section = Section()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            section.append(Paragraph("cacheDir 의 사용 가능한 용량", convertShortStorageUnit(getAvailableBytes(application.cacheDir))))
        }
        section.append(Paragraph("cacheDir.absolutePath", application.cacheDir.absolutePath))
            .append(Paragraph("cacheDir.path", application.cacheDir.path))
        return section
    }

    private fun getFilesDirDirInInternalStorageSection(application: Application): Section {
        val section = Section()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            section.append(Paragraph("filesDir 의 사용 가능한 용량", convertShortStorageUnit(getAvailableBytes(application.filesDir))))
        }
        return  section.append(Paragraph("filesDir.absolutePath", application.filesDir.absolutePath))
            .append(Paragraph("filesDir.path", application.filesDir.path))
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