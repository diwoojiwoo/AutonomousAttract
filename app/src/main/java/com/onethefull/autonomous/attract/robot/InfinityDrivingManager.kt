package com.onethefull.autonomous.attract.robot

import com.onethefull.autonomous.attract.utils.logger.DWLog
import com.onethefull.wonderfulrobotmodule.robot.BaseRobotController
import kotlinx.coroutines.*
import org.json.JSONObject
object InfinityDrivingManager {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private const val MOVE = 11
    private const val TURN = 12

    private const val STEP_SPEED = 0.03f      // 전진 속도
    private const val CURVE_SPEED = 5f        // 살짝 회전
    private const val WHEEL_TURN_SPEED = 15f  // 한쪽 바퀴 중심 회전 속도
    private const val STEP_DURATION = 750L
    private const val TURN_DURATION = 500L    // 바퀴 중심 회전 시간

    fun startInfinityLoop() {
        stop()
        BaseRobotController.robotService?.robotMotor?.setWheelLockState(false)

        scope.launch {
            try {
                while (isActive) {
                    singleForwardCurveStep()
                }
            } finally {
                stopWheel()
            }
        }
    }

    fun emergencyStop() = stop()
    suspend fun singleForwardCurveStep() {
        try {
            DWLog.d("InfinityDrivingManager", "Step START: singleForwardCurveStep")

            // 왼쪽으로 살짝 꺾으면서 전진
            BaseRobotController.robotService?.setMotor(
                JSONObject().apply {
                    put("position", 11)   // 전진
                    put("degree", 5000)
                    put("speed", 0.02f)   // 느린 전진
                }.toString()
            )
            BaseRobotController.robotService?.setMotor(
                JSONObject().apply {
                    put("position", 12)   // 회전
                    put("degree", 5000)
                    put("speed", -5f)     // 왼쪽 살짝
                }.toString()
            )
            DWLog.d("InfinityDrivingManager", "Moving forward with slight left curve")
            delay(2000)  // 2초 이동
            BaseRobotController.robotService?.robotMotor?.stopWheel()
            DWLog.d("InfinityDrivingManager", "Stopped after curve step")
            delay(200) // 관성 방지

            // 앞으로 직진 1초
            BaseRobotController.robotService?.setMotor(
                JSONObject().apply {
                    put("position", 11)
                    put("degree", 5000)
                    put("speed", 0.02f)
                }.toString()
            )
            DWLog.d("InfinityDrivingManager", "Moving forward straight for 1 sec")
            delay(1000)  // 1초 이동
            BaseRobotController.robotService?.robotMotor?.stopWheel()
            DWLog.d("InfinityDrivingManager", "Stopped after straight step")

            DWLog.d("InfinityDrivingManager", "Step END: singleForwardCurveStep")
        } catch (e: Exception) {
            DWLog.e("InfinityDrivingManager", "Exception in singleForwardCurveStep: ${e.message}")
            e.printStackTrace()
        }
    }


    private fun stopWheel() {
        try {
            BaseRobotController.robotService?.robotMotor?.stopWheel()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stop() {
        try {
            scope.coroutineContext.cancelChildren()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            stopWheel()
        }
    }
}
