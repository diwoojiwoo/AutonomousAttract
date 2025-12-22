package com.onethefull.autonomous.attract.robot


import com.onethefull.autonomous.attract.utils.logger.DWLog
import com.onethefull.wonderfulrobotmodule.robot.BaseRobotController
import kotlinx.coroutines.*
import org.json.JSONObject

object DrivingManager {

    private var job = SupervisorJob()
    private var scope = CoroutineScope(Dispatchers.IO + job)

    @Volatile
    private var isDriving = false

    private const val POSITION = 11
    private const val DEGREE = 3000
    private const val SPEED = 0.08f
    private const val MOVE_DELAY = 500L
    private const val STOP_DELAY = 400L

    /*∞ 주행의 개념 (중요)
    ∞ = 좌회전 원 + 우회전 원
    한쪽 바퀴 빠르게 / 반대쪽 느리게*/

    private const val CIRCLE_DURATION = 2800L
    private const val CURVE_SPEED = 0.08f
    private const val CURVE_RATIO = 0.6f   // 회전 반경

    fun startForwardBackward(repeatCount: Int = 2) {
        if (isDriving) return

        val service = BaseRobotController.robotService
        val motor = service?.robotMotor

        if (service == null || motor == null) {
            DWLog.e("DrivingManager: robotService null")
            return
        }

        resetScope()
        isDriving = true

        motor.setWheelLockState(false)

        scope.launch {
            try {
                repeat(repeatCount) {
                    DWLog.d("▶ Forward")
                    sendMotor(SPEED)
                    delay(MOVE_DELAY)

                    stopWheel()
                    delay(STOP_DELAY)

                    DWLog.d("◀ Backward")
                    sendMotor(-SPEED)
                    delay(MOVE_DELAY)

                    stopWheel()
                    delay(STOP_DELAY)
                }
            } catch (e: CancellationException) {
                DWLog.e("Driving cancelled")
            } catch (e: Exception) {
                DWLog.e("Driving error :: ${e.message}")
            } finally {
                motor.setWheelLockState(true)
                stopWheel()
                isDriving = false
            }
        }
    }

    fun stop() {
        DWLog.d("Driving stop")
        resetScope()
        stopWheel()
        isDriving = false
    }

    fun emergencyStop() {
        DWLog.e("Emergency Stop")
        resetScope()
        stopWheel()
        isDriving = false
    }

    private fun resetScope() {
        job.cancel()
        job = SupervisorJob()
        scope = CoroutineScope(Dispatchers.IO + job)
    }

    private fun sendMotor(speed: Float) {
        BaseRobotController.robotService?.setMotor(
            JSONObject().apply {
                put("position", POSITION)
                put("degree", DEGREE)
                put("speed", speed)
            }.toString()
        )
    }

    fun startInfinityLoop() {
        if (isDriving) return
        startInfinityInternal(loop = true)
    }

    fun startInfinityOnce(onComplete: (() -> Unit)? = null) {
        if (isDriving) return
        startInfinityInternal(loop = false, onComplete)
    }

    private fun startInfinityInternal(
        loop: Boolean,
        onComplete: (() -> Unit)? = null
    ) {
        val service = BaseRobotController.robotService
        val motor = service?.robotMotor

        if (service == null || motor == null) {
            DWLog.e("DrivingManager: robotService null")
            return
        }

        resetScope()
        isDriving = true
        motor.setWheelLockState(false)

        scope.launch {
            try {
                do {
                    runCircle(left = true)
                    runCircle(left = false)
                } while (loop && isActive)
            } catch (e: CancellationException) {
                DWLog.e("Infinity driving cancelled")
            } finally {
                motor.setWheelLockState(true)
                stopWheel()
                isDriving = false
                onComplete?.invoke()
            }
        }
    }

    private suspend fun runCircle(left: Boolean) {
        DWLog.d(if (left) "∞ Left circle" else "∞ Right circle")

        val speed = if (left) CURVE_SPEED else -CURVE_SPEED
        val ratio = if (left) CURVE_RATIO else -CURVE_RATIO

        sendCurveMotor(speed, ratio)
        delay(CIRCLE_DURATION)

        stopWheel()
        delay(STOP_DELAY)
    }

    private fun sendCurveMotor(speed: Float, curve: Float) {
        BaseRobotController.robotService?.setMotor(
            JSONObject().apply {
                put("position", POSITION)
                put("degree", (DEGREE * curve).toInt())
                put("speed", speed)
            }.toString()
        )
    }



    private fun stopWheel() {
        BaseRobotController.robotService?.robotMotor?.stopWheel()
    }
}


