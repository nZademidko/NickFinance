package com.nickfinance.nikfinance.base

import android.Manifest
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment

open class PermissionFragment : Fragment() {

    private var callback: RequestPermissions? = null
    private var listPermissions = emptyList<PermissionType>()
    private var register: ActivityResultLauncher<Array<String>>? = null

    abstract class RequestPermissions {
        open fun successGetPermissions(list: List<PermissionType>, isAllGranted: Boolean) {}
        open fun failGetPermissions(list: List<PermissionType>) {}
        open fun complete() {}
    }

    override fun onDestroy() {
        super.onDestroy()
        register?.unregister()
    }

    protected fun registerPermissions() {
        register =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val isAllGranted = permissions.values.all { it }
                val listSuccess = arrayListOf<PermissionType>()
                val listFail = arrayListOf<PermissionType>()
                permissions.entries.forEach { permission ->
                    val type = PermissionType.values().find { it.value == permission.key }!!
                    if (permission.value) {
                        listSuccess.add(type)
                    } else {
                        listFail.add(type)
                    }
                }
                listPermissions.forEach { permission ->
                    val permissionTarget = permissions.entries.find { it.key == permission.value }
                    if (!listSuccess.contains(permission) && permissionTarget?.value == true) {
                        listSuccess.add(permission)
                    }
                }
                if (listSuccess.isNotEmpty()) {
                    callback?.successGetPermissions(listSuccess, isAllGranted)
                }
                if (listFail.isNotEmpty()) {
                    callback?.failGetPermissions(listFail)
                }
                callback?.complete()
            }
    }

    protected fun requestPermissions(callback: RequestPermissions, list: List<PermissionType>) {
        this.callback = callback
        this.listPermissions = list
        if (register == null) {
            throw RuntimeException("Необходимо вызвать registerPermissions() в методе onCreate() частного фрагмента")
        } else {
            register?.launch(list.map { it.value }.toTypedArray())
        }
    }

    enum class PermissionType(val value: String, val textDenied: String) {
        ACCESS_COARSE_LOCATION(
            value = Manifest.permission.ACCESS_COARSE_LOCATION,
            textDenied = "Перейдите в разрешения и активируйте доступ к местоположению"
        ),
        ACCESS_FINE_LOCATION(
            value = Manifest.permission.ACCESS_FINE_LOCATION,
            textDenied = "Перейдите в разрешения и активируйте доступ к местоположению"
        ),
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        POST_NOTIFICATION(
            value = Manifest.permission.POST_NOTIFICATIONS,
            textDenied = "Перейдите в разрешения и активируйте доступ к оповещениям"
        ),
        RECEIVE_BOOT_COMPLETED(
            value = Manifest.permission.RECEIVE_BOOT_COMPLETED,
            textDenied = "Перейдите в разрешения и активируйте доступ к оповещениям"
        ),
        CAMERA(
            value = Manifest.permission.CAMERA,
            textDenied = "Перейдите в разрешения и активируйте доступ к камере"
        )
    }
}