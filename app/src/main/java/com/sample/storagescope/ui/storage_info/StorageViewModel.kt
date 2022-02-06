package com.sample.storagescope.ui.storage_info

import android.app.Application
import android.os.Build
import android.os.Environment
import android.os.storage.StorageManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sample.storagescope.ui.storage_info.data.Paragraph
import com.sample.storagescope.ui.storage_info.data.Section
import java.io.File
import java.util.*

class StorageViewModel(application: Application): AndroidViewModel(application) {
    private val _internalStoragePaths = MutableLiveData<String>()
    val internalStoragePath: LiveData<String> get() = _internalStoragePaths

    private val _externalStoragePaths = MutableLiveData<String>()
    val externalStoragePath: LiveData<String> get() = _externalStoragePaths

    init {
        _internalStoragePaths.value = getInternalStorageSections(application).joinToString("\n") { it.toString() }
        _externalStoragePaths.value = getExternalStorageSections(application).joinToString("\n") { it.toString() }
    }

    private fun getExternalStorageSections(application: Application): List<Section> {
        return listOf(
            getExternalStorageAvailableSection(),
            getExternalStoragePathSection(application),
            getExternalCacheStoragePathSection(application)
        )
    }

    // 읽기, 쓰기 모두 가능한 지 확인
    private fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    // 최소한 읽기는 가능한지 가능
    private fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }

    private fun getExternalStorageAvailableSection(): Section {
        val paragraph = Paragraph(
            "외부저장소 마운트 여부",
            when {
                isExternalStorageWritable() -> "정상 마운트 됨, 읽기/쓰기 가능"
                isExternalStorageReadable() -> "정상 마운트 됨, 읽기 가능"
                else -> "마우트 안됨, 외부저장소 사용 불가"
            })
        return Section().apply {
            append(paragraph)
        }
    }

    private fun getExternalStoragePathSection(application: Application): Section {
        val externalStorageVolumes: Array<File> = ContextCompat.getExternalFilesDirs(application, null) // 외부저장소
        val paragraph = Paragraph(
            "외부 저장소 위치 (index 0 default)",
            externalStorageVolumes.mapIndexed { index, file -> "[$index] ${file.path}" }.joinToString("\n")
        )
        return Section().apply {
            append(paragraph)
        }
    }

    private fun getExternalCacheStoragePathSection(application: Application): Section {
        val externalStorageVolumes: Array<File> = ContextCompat.getExternalCacheDirs(application) // 외부저장소
        val paragraph = Paragraph(
            "외부 저장소의 캐시 디렉토리 위치 (index 0 default)",
            externalStorageVolumes.mapIndexed { index, file -> "[$index] ${file.path}" }.joinToString("\n")
        )
        return Section().apply {
            append(paragraph)
        }
    }


    private fun getInternalStorageSections(application: Application) = listOf(
        getFilesDirDirInInternalStorageSection(application),
        getCacheDirInInternalStorageSection(application)
    )

    private fun getCacheDirInInternalStorageSection(application: Application): Section {
        val section = Section()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            section.append(Paragraph("cacheDir 의 사용 가능한 용량", getAvailableBytes(application.cacheDir).toShortStorageUnit()))
        }
        section.append(Paragraph("cacheDir.absolutePath", application.cacheDir.absolutePath))
            .append(Paragraph("cacheDir.path", application.cacheDir.path))
        return section
    }

    private fun getFilesDirDirInInternalStorageSection(application: Application): Section {
        val section = Section()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            section.append(Paragraph("filesDir 의 사용 가능한 용량", getAvailableBytes(application.filesDir).toShortStorageUnit()))
        }
        return  section.append(Paragraph("filesDir.absolutePath", application.filesDir.absolutePath)) // 내부저장소
            .append(Paragraph("filesDir.path", application.filesDir.path))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getAvailableBytes(filesDir: File): Long {
        val storageManager = getApplication<Application>().getSystemService<StorageManager>()!!
        val appSpecificInternalDirUuid: UUID = storageManager.getUuidForPath(filesDir)
        return storageManager.getAllocatableBytes(appSpecificInternalDirUuid)
    }

}

private fun Long.toShortStorageUnit() : String {
    val unit = listOf("Byte", "KB", "MB", "GB", "TB")
    var divide: Float = this.toFloat()
    for (i: Int in 0..unit.size) {
        val temp:Float = divide / 1024.0f
        if(temp <= 1.0f) {
            return "$divide ${unit[i]}"
        }
        divide = temp
    }
    return "$divide ${unit.last()}"
}