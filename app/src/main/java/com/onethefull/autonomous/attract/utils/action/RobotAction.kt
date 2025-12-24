package com.onethefull.autonomous.attract.utils.action

/**
 * Created by Douner on 2023/11/28.
 */
interface RobotAction {

    companion object {
        const val ACTION_NAME= "startWaiting"
    }

    fun startAction(action: String)

    fun releaseAction()
}