package com.onethefull.autonomous.attract

import android.app.Activity
import android.app.ActivityOptions
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PowerManager
import com.onethefull.autonomous.attract.ui.main.MainActivity
import com.onethefull.autonomous.attract.utils.logger.DWLog
import com.onethefull.wonderfulrobotmodule.provider.DasomProvider
import com.onethefull.wonderfulrobotmodule.robot.BaseRobotController
import com.onethefull.wonderfulrobotmodule.scene.SceneEventListener
import com.onethefull.wonderfulrobotmodule.scene.SceneHelper
import java.io.Serializable

class App : Application() {
    lateinit var provider: DasomProvider

    /**
     * 로봇 서비스 연결 상태를 수신하는 리스너입니다.
     * BaseRobotController.initialize의 파라미터에 맞는 StartListener를 사용합니다.
     */
    private val robotStartListener = object : BaseRobotController.StartListener {
        override fun onStartService() {
            DWLog.d("Robot service connected! (onStartService)")
            isRobotConnected = true
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        provider = DasomProvider(this)

        // 로봇 컨트롤러를 초기화하고 서비스에 연결합니다.
        DWLog.d("Initializing Robot Controller...")
        BaseRobotController.initialize(this, robotStartListener)

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

    fun onCommand(action: String?, params: Bundle?, suggestion: Serializable?) {
        val send = Intent(instance, MainActivity::class.java)
        send.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        send.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        DWLog.w("onCommand action: $action")

        startActivity(send, ActivityOptions.makeCustomAnimation(this@App, 0, 0).toBundle())
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

        const val TAG = "AutonomousAttract"

        var isRobotConnected = false
            private set
    }
}
