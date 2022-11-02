
package com.feisukj.base.util

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Handler
import androidx.fragment.app.Fragment
import android.widget.Toast
import com.feisukj.base.BaseApplication


/**
 * Author : Gupingping
 * Date : 2018/7/15
 * Mail : gu12pp@163.com
 */

fun Activity.toast(msg: String) {
    Handler(mainLooper).post {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
fun Activity.toast(msgRes: Int) {
    Handler(mainLooper).post {
        Toast.makeText(this, msgRes, Toast.LENGTH_SHORT).show()
    }
}

fun Fragment.toast(msg: String) {
    Handler(BaseApplication.application.mainLooper).post {
        Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show()
    }
}
fun Fragment.toast(msgRes: Int) {
    Handler(BaseApplication.application.mainLooper).post {
        Toast.makeText(this.context, msgRes, Toast.LENGTH_SHORT).show()
    }
}

fun Context.lg(msg: String) {
    LogUtils.e(msg)

}

fun Activity.lg(msg: String) {
    LogUtils.e(this.localClassName + "-->" + msg)
}

fun Fragment.lg(msg: String) {
    LogUtils.e(msg)
}

fun Application.toast(msg: String) {
    Handler(mainLooper).post {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

