package com.weilai.jigsawpuzzle.util

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

object PermissionUtils {
    fun askPermission(context: Context, vararg permission: String, method: () -> Unit) {
            XXPermissions.with(context)
                .permission(permission)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                        if (all) {
                            method.invoke()
                        }
                    }

                    override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                        if (never) {
                            XXPermissions.startPermissionActivity(context, permissions)
                        }
                    }

                })

    }

    fun askStorageAndCameraPermission(context: Context, method: () -> Unit) {
        XXPermissions.with(context)
            .permission(Permission.MANAGE_EXTERNAL_STORAGE, Permission.CAMERA)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                    if (all) {
                        method.invoke()
                    }
                }

                override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                    if (never) {
                        XXPermissions.startPermissionActivity(context, permissions)
                    }
                }

            })
    }

    fun gotoPermission(context: Context) {
        try {
            val brand = Build.BRAND //手机厂商
            if (TextUtils.equals(
                    brand.toLowerCase(),
                    "redmi"
                ) || TextUtils.equals(brand.toLowerCase(), "xiaomi")
            ) {
                gotoMiUiPermission(context) //小米
            } else if (TextUtils.equals(brand.toLowerCase(), "meizu")) {
                gotoMeiZuPermission(context)
            }
//            else if (TextUtils.equals(
//                    brand.toLowerCase(),
//                    "huawei"
//                ) || TextUtils.equals(brand.toLowerCase(), "honor")
//            ) {
//                gotoHuaWeiPermission(context)
//            }
            else {
                context.startActivity(
                    getAppDetailSettingIntent(
                        context
                    )
                )
            }
        } catch (e: Exception) {
            ToastUtil.showCenterToast("打开权限页面失败，请重新打开")
        }
    }


    /**
     * 跳转到miui的权限管理页面
     */
    private fun gotoMiUiPermission(context: Context) {
        try { // MIUI 8
            val localIntent = Intent("miui.intent.action.APP_PERM_EDITOR")
            localIntent.setClassName(
                "com.miui.securitycenter",
                "com.miui.permcenter.permissions.PermissionsEditorActivity"
            )
            localIntent.putExtra("extra_pkgname", context.packageName)
            context.startActivity(localIntent)
        } catch (e: Exception) {
            try { // MIUI 5/6/7
                val localIntent = Intent("miui.intent.action.APP_PERM_EDITOR")
                localIntent.setClassName(
                    "com.miui.securitycenter",
                    "com.miui.permcenter.permissions.AppPermissionsEditorActivity"
                )
                localIntent.putExtra("extra_pkgname", context.packageName)
                context.startActivity(localIntent)
            } catch (e1: Exception) { // 否则跳转到应用详情
                context.startActivity(getAppDetailSettingIntent(context))
            }
        }
    }

    /**
     * 跳转到魅族的权限管理系统
     */
    private fun gotoMeiZuPermission(context: Context) {
        try {
            val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.putExtra("packageName", context.applicationContext.packageName)
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            context.startActivity(getAppDetailSettingIntent(context))
        }
    }

    /**
     * 华为的权限管理页面
     */
    private fun gotoHuaWeiPermission(context: Context) {
        try {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val comp = ComponentName(
                "com.huawei.systemmanager",
                "com.huawei.permissionmanager.ui.MainActivity"
            ) //华为权限管理
            intent.component = comp
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            context.startActivity(getAppDetailSettingIntent(context))
        }
    }

    /**
     * 获取应用详情页面intent（如果找不到要跳转的界面，也可以先把用户引导到系统设置页面）
     */
    private fun getAppDetailSettingIntent(context: Context): Intent? {
        val localIntent = Intent()
        try {
            localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            if (Build.VERSION.SDK_INT >= 9) {
                localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
                localIntent.data = Uri.fromParts("package", context.packageName, null)
            } else if (Build.VERSION.SDK_INT <= 8) {
                localIntent.action = Intent.ACTION_VIEW
                localIntent.setClassName(
                    "com.android.settings",
                    "com.android.settings.InstalledAppDetails"
                )
                localIntent.putExtra("com.android.settings.ApplicationPkgName", context.packageName)
            }
            context.startActivity(localIntent)
        } catch (e: Exception) {
            ToastUtil.showCenterToast("打开权限页面失败，请重新打开")
        }
        return localIntent
    }
}