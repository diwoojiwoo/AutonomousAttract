package com.onethefull.autonomous.attract

import android.app.Activity
import android.app.ActivityOptions
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PowerManager
import androidx.appcompat.app.AppCompatActivity
import com.onethefull.autonomous.attract.ui.main.MainActivity
import com.onethefull.autonomous.attract.utils.logger.DWLog
import com.onethefull.wonderfulrobotmodule.provider.DasomProvider
import com.onethefull.wonderfulrobotmodule.robot.BaseRobotController
import com.onethefull.wonderfulrobotmodule.scene.SceneEventListener
import com.onethefull.wonderfulrobotmodule.scene.SceneHelper
import java.io.Serializable

class App : Application() {
    lateinit var provider: DasomProvider
    var currentActivity: AppCompatActivity? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        provider = DasomProvider(this)
        initSceneHelper()
    }

    private fun initSceneHelper() {
        DWLog.d("initSceneHelper")
        SceneHelper.initialize(this)
        SceneHelper.setSceneEventListener(object : SceneEventListener() {
            override fun onSwitchIn(flags: Int) {
                super.onSwitchIn(flags)
                DWLog.d("onSwitchIn")
            }

            override fun onSwitchOut() {
                super.onSwitchOut()
                currentActivity?.finish()
                currentActivity = null
                mWakeLock?.release()
            }

            override fun onCommand(
                action: String?,
                params: Bundle?,
                suggestion: Serializable?,
            ) {
                super.onCommand(action, params, suggestion)
                wakeLock()
                this@App.onCommand(action, params, suggestion)
                return
            }
        })
    }

    private fun startActionActivity(send: Intent) {
        if (currentActivity != null && currentActivity is MainActivity) {
            (currentActivity as MainActivity).apply {
                intent = send
            }.run {
            }
        } else {
            send.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(send)
        }
    }

    fun onCommand(action: String?, params: Bundle?, suggestion: Serializable?) {
        val send = Intent(instance, MainActivity::class.java)
        send.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        send.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        DWLog.w("onCommand action: $action")

        startActionActivity(send)
    }

    private fun wakeLock() {
        mWakeLock =
            (applicationContext.getSystemService(Context.POWER_SERVICE) as PowerManager)
                .newWakeLock(
                    PowerManager.PARTIAL_WAKE_LOCK or
                            PowerManager.ACQUIRE_CAUSES_WAKEUP or
                            PowerManager.ON_AFTER_RELEASE,
                    "Tag:AutonomousAttractPower"
                )
        mWakeLock?.acquire()
    }

    private var mWakeLock: PowerManager.WakeLock? = null

    companion object {
        lateinit var instance: App
            private set

        const val TAG = "DasomAutonomousAttract"

        const val DEVICE_KEBBI = "KEBBI"
    }
}
