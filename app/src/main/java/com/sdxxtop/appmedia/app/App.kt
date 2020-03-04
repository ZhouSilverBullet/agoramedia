package com.sdxxtop.appmedia.app

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import android.util.Log
import com.sdxxtop.appmedia.BuildConfig
import com.sdxxtop.guardianapp.model.NetWorkSession
import com.sdxxtop.sdkagora.AgoraSession

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        //        initWebViewServer();
        if (isAppProcess(getCurProcessName())) {
            AgoraSession.init(this)
        }
        NetWorkSession.init(this, BuildConfig.DEBUG)
    }

    fun isAppProcess(process: String?): Boolean {
        return packageName.equals(process)
    }

    fun getCurProcessName(): String? {
        try {
            val pid = Process.myPid()
            val mActivityManager =
                getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            for (appProcess in mActivityManager
                .runningAppProcesses) {
                if (appProcess.pid == pid) {
                    return appProcess.processName
                }
            }
        } catch (e: Exception) {
            Log.e("getCurProcessName:", e.message)
        }
        return packageName
    }
}