package com.onethefull.autonomous.attract.robot

import com.onethefull.autonomous.attract.App
import com.onethefull.autonomous.attract.utils.logger.DWLog
import com.onethefull.wonderfulrobotmodule.robot.BaseRobotController
import com.onethefull.wonderfulrobotmodule.robot.IMotionCallback
import com.onethefull.wonderfulrobotmodule.robot.KebbiMotion
import kotlinx.coroutines.*
import org.json.JSONObject

object InfinityDrivingManager {

    interface DrivingListener {
        fun onDrivingFinished()
    }

    var drivingListener: DrivingListener? = null
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private const val MOVE = 11
    private const val TURN = 12

    private const val MAX_WHEEL_DELAY = 1250L
    private const val WHEEL_MOVE_SPEED = 0.05f
    private const val WHEEL_TURN_SPEED = 25f
    private const val ACTION_MOVE = 11
    private const val ACITON_TURN = 12

    const val DIRECTION_FORWARD = 1
    const val DIRECTION_BACKWARD = -1

    const val DIRECTION_LEFT = -1
    const val DIRECTION_RIGHT = 1

    fun startInfinityLoop() {
        stop()
        BaseRobotController.robotService?.robotMotor?.setWheelLockState(false)

        scope.launch {
            try {
                repeat(1) { drawFigureEight() }
                delay(4000L)
                repeat(20) { forwardMoving() }

                // 주행 종료 시점 콜백
                withContext(Dispatchers.Main) {
                    drivingListener?.onDrivingFinished()
                }
            } finally {
                stopWheel()
            }
        }
    }


    fun emergencyStop() = stop()

    private suspend fun forwardMoving() { //delay : distance*speed
        DWLog.d("forwardMoving")
        try {
            BaseRobotController.robotService?.setMotor(JSONObject().apply {
                this.put("position", 11)
                this.put("degree", 5000)
                this.put("speed", 0.06f)
            }.toString())
            delay(100)
            BaseRobotController.robotService?.robotMotor?.stopWheel()
        } catch (e: Exception) {
            e.printStackTrace()
            BaseRobotController.robotService?.robotMotor?.stopWheel()
        }
    }

    private suspend fun backwardMoving() {
        try {
            BaseRobotController.robotService?.setMotor(JSONObject().apply {
                this.put("position", 11)
                this.put("degree", 5000)
                this.put("speed", -0.04f)
            }.toString())
            delay(100)
            BaseRobotController.robotService?.robotMotor?.stopWheel()
        } catch (e: Exception) {
            e.printStackTrace()
            BaseRobotController.robotService?.robotMotor?.stopWheel()
        }
    }

    private suspend fun turnWheelLeft(degree: Int = 30) {
        DWLog.d("turnWheelLeft")
        try {
            BaseRobotController.robotService?.robotMotor?.turnWheelLeft(degree)
            delay(500)
            BaseRobotController.robotService?.robotMotor?.stopWheel()
        } catch (e: Exception) {
            e.printStackTrace()
            BaseRobotController.robotService?.robotMotor?.stopWheel()
        }
    }

    private suspend fun turnWheelRight(degree: Int = 30) {
        try {
            BaseRobotController.robotService?.robotMotor?.turnWheelRight(degree)
            delay(500)
            BaseRobotController.robotService?.robotMotor?.stopWheel()
        } catch (e: Exception) {
            e.printStackTrace()
            BaseRobotController.robotService?.robotMotor?.stopWheel()
        }
    }

    suspend fun drawFigureEight() {
        DWLog.d("666_BA_TurnL360")
        delay(1800)
        val actionName = "666_BA_TurnL360"
        BaseRobotController.robotService
            ?.robotMotor
            ?.motionStart(actionName, object : IMotionCallback.Stub() {
                override fun finishMotion() {
                    DWLog.d("${App.TAG} finishMotion [$actionName]")
                }
            })
    }


    /**
     * position : 11  , degree * speed = 1초 , speed -0.1~0.1 : 전후진
     * position : 12 ,  degree * speed = 1초 , degree/sec -30/30 : 회전
     * @param position 11 전후진 , 12 회전
     * @param degree degree * speed = 1초
     * @param speed speed -0.1~0.1 : 전후진,  degree/sec -30/30 : 회전
     * @param delay during
     */
    private suspend fun startWheelAction(
        position: Int,
        degree: Int,
        speed: Float,
        delay: Long,
    ) {
        try {
            BaseRobotController.robotService?.setMotor(wheelCommand(position, degree, speed))
            delay(delay)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val wheelCommand: (Int, Int, Float) -> (String) = { position, degree, speed ->
        try {
            JSONObject().apply {
                this.put("position", position)
                this.put("degree", degree)
                this.put("speed", speed)
            }.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            String()
        }

    }

    private suspend fun infinityOnce() {
        // 왼쪽 고리
        leftHalfCircle()

        // 중앙 교차
        crossCenter()

        // 오른쪽 고리
        rightHalfCircle()

        // 중앙 복귀
        crossCenter()
    }

    private suspend fun forwardOnce() {
        BaseRobotController.robotService?.setMotor(
            JSONObject().apply {
                put("position", 11)
                put("degree", 2000)   // 중요: 작게
                put("speed", 0.08f)  // 고정
            }.toString()
        )
        delay(180)   // ★ 여기 핵심
    }

    private suspend fun microTurnLeft() {
        BaseRobotController.robotService?.setMotor(
            JSONObject().apply {
                put("position", 12)
                put("degree", 600)     // 아주 작게
                put("speed", -4f)
            }.toString()
        )
        delay(120)
    }

    private suspend fun microTurnRight() {
        BaseRobotController.robotService?.setMotor(
            JSONObject().apply {
                put("position", 12)
                put("degree", 600)
                put("speed", 4f)
            }.toString()
        )
        delay(120)
    }

    private suspend fun leftHalfCircle() {
        repeat(10) {
            forwardOnce()
            forwardOnce()
            microTurnLeft()
        }
    }

    private suspend fun rightHalfCircle() {
        repeat(10) {
            forwardOnce()
            forwardOnce()
            microTurnRight()
        }
    }

    private suspend fun crossCenter() {
        repeat(6) {
            forwardOnce()
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
