package com.onethefull.autonomous.attract.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.onethefull.autonomous.attract.R
import com.onethefull.autonomous.attract.utils.logger.DWLog
import com.onethefull.wonderfulrobotmodule.robot.BaseRobotController
import com.onethefull.wonderfulrobotmodule.robot.IRobotServiceListener
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment())
                .commitNow()
        }
    }

    override fun onResume() {
        super.onResume()
        BaseRobotController.initialize(application)
        BaseRobotController.setRobotServiceListener(robotServiceListener)
    }


    override fun onPause() {
        super.onPause()
        DWLog.e("Mainactivity onPause")
        BaseRobotController.removeRobotServiceListener(robotServiceListener)
    }

    val robotServiceListener =
        object : IRobotServiceListener.Stub() {
            override fun onResponseRobotService(type: Int, json: String?) {
            }
        }
}
