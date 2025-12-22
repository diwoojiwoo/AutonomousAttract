package com.onethefull.autonomous.attract.utils.action

import androidx.lifecycle.lifecycleScope
import com.onethefull.autonomous.attract.App
import com.onethefull.autonomous.attract.ui.main.MainActivity
import com.onethefull.wonderfulrobotmodule.robot.BaseRobotController
import com.onethefull.wonderfulrobotmodule.utils.DWLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.coroutines.coroutineContext
import kotlin.random.Random

/**
 * Created by Douner on 1/12/24.
 */
@Suppress("KotlinConstantConditions")
object ActionList {

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


    /**
     *
     */
    private val motorCommand: (Int, Int, Int) -> (String) = { position, degree, speed ->
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

    /**
     * 캐비 왼팔 제어
     * @param z left_shoulder_z(7), range(5, -85)
     * @param y left_shoulder_y(8), range(70, -200)
     * @param x left_shoulder_x(9), range(100,-3)
     * @param bow left_bow_y(10) range(0,80)
     */
    private fun motorLeftArmCommand(
        z: Pair<Int, Int>,
        y: Pair<Int, Int>,
        x: Pair<Int, Int>,
        bow: Pair<Int, Int>,
    ) {
        BaseRobotController.robotService?.let { motor ->
            motor.setMotor(motorCommand(7, z.first, z.second))
            motor.setMotor(motorCommand(8, y.first, y.second))
            motor.setMotor(motorCommand(9, x.first, x.second))
            motor.setMotor(motorCommand(10, bow.first, bow.second))
        }
    }

    /**
     * 캐비 오른팔 제어
     * @param z right_shoulder_z(3), range(5, -85)
     * @param y right_shoulder_y(4), range(70, -200)
     * @param x right_shoulder_x(5), range(100,-3)
     * @param bow right_bow_y(6) range(0,80)
     */
    private fun motorRightArmCommand(
        z: Pair<Int, Int>,
        y: Pair<Int, Int>,
        x: Pair<Int, Int>,
        bow: Pair<Int, Int>,
    ) {
        BaseRobotController.robotService?.let { motor ->
            motor.setMotor(motorCommand(3, z.first, z.second))
            motor.setMotor(motorCommand(4, y.first, y.second))
            motor.setMotor(motorCommand(5, x.first, x.second))
            motor.setMotor(motorCommand(6, bow.first, bow.second))
        }
    }

    private const val MAX_WHEEL_DELAY = 1250L
    private const val WHEEL_MOVE_SPEED = 0.05f
    private const val WHEEL_TURN_SPEED = 25f
    private const val ACTION_MOVE = 11
    private const val ACITON_TURN = 12

    const val DIRECTION_FORWARD = 1
    const val DIRECTION_BACKWARD = -1

    const val DIRECTION_LEFT = -1
    const val DIRECTION_RIGHT = 1

    private suspend fun startRobotWheel() {
        try {
            while (coroutineContext.isActive) {
                DWLog.w("ActionList startRobotWheel")
                var moveSpeed = 0.00f
                var turnSpeed = 5f
                for (i in 1..5) {
                    moveSpeed += 0.01f
                    turnSpeed += 4f
                    val moveDistance = (MAX_WHEEL_DELAY / moveSpeed).toInt()
                    val wheelDistance = (MAX_WHEEL_DELAY / turnSpeed).toInt()
                    DWLog.w("ActionList startRobotWheel $moveSpeed $moveDistance")
                    startWheelAction(11, moveDistance, moveSpeed, 2000L)
                    startWheelAction(11, moveDistance, -moveSpeed, 2000L)
                    startWheelAction(12, wheelDistance, turnSpeed, 2000L)
                    startWheelAction(12, wheelDistance, -turnSpeed, 2000L)
                    DWLog.w("ActionList startRobotWheel cycle end $moveSpeed $moveDistance")
                }
                BaseRobotController.robotService?.robotMotor?.stopWheel()
                delay(5000)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            DWLog.w("ActionList startRobotWheel exception ${e.message}")
            BaseRobotController.robotService?.robotMotor?.stopWheel()
        }
    }

    suspend fun moveAction(direction: Int = DIRECTION_FORWARD) {
        if (direction == 1 || direction == -1) {
            val moveDistance = (MAX_WHEEL_DELAY / WHEEL_MOVE_SPEED).toInt()
            startWheelAction(
                position = ACTION_MOVE,
                degree = moveDistance,
                speed = WHEEL_MOVE_SPEED * direction,
                delay = 2000L
            )
            delay(2000L)
            BaseRobotController.robotService?.robotMotor?.stopWheel()
        }
    }

    suspend fun wheelAction(direction: Int = DIRECTION_RIGHT) {
        delay(100)
        if (direction == 1 || direction == -1) {
            val moveDistance = (MAX_WHEEL_DELAY / WHEEL_MOVE_SPEED).toInt()
            startWheelAction(
                position = ACITON_TURN,
                degree = moveDistance,
                speed = WHEEL_TURN_SPEED * direction,
                delay = 2000L
            )
            delay(2000L)
            BaseRobotController.robotService?.robotMotor?.stopWheel()
        }
    }


    // TODO REMOVE 23.10.27 영업데모
    private suspend fun startRandomWheel(index: Int) {
        DWLog.d("startRandomWheel $index")
        when (index) {
            0 -> {
                startWheelActionRemove(::testTurnWheelLeft)
                delay(750)
                startWheelActionRemove(::testTurnWheelRight)
            }

            1 -> {
                startWheelActionRemove(::testTurnWheelRight)
                delay(750)
                startWheelActionRemove(::testTurnWheelLeft)
            }

            2 -> {
                startWheelActionRemove(::testBackwardMoving)
                delay(750)
                startWheelActionRemove(::testForwardMoving)
            }

            3 -> {
                startWheelActionRemove(::testForwardMoving)
                delay(750)
                startWheelActionRemove(::testBackwardMoving)
            }

            4 -> {
                startWheelActionRemove(::testForwardMoving)
                delay(750)
                startWheelActionRemove(::testTurnWheelLeft)
                delay(750)
                startWheelActionRemove(::testBackwardMoving)
                delay(750)
                startWheelActionRemove(::testTurnWheelRight)
            }

            5 -> {
                startWheelActionRemove(::testForwardMoving)
                delay(750)
                startWheelActionRemove(::testTurnWheelRight)
                delay(750)
                startWheelActionRemove(::testBackwardMoving)
                delay(750)
                startWheelActionRemove(::testTurnWheelLeft)
            }
        }
    }


    // TODO REMOVE 23.10.27 영업데모
    private fun testBackwardMoving() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                BaseRobotController.robotService?.setMotor(JSONObject().apply {
                    this.put("position", 11)
                    this.put("degree", 5000)
                    this.put("speed", -0.1f)
                }.toString())
                delay(500)
                BaseRobotController.robotService?.robotMotor?.stopWheel()
            } catch (e: Exception) {
                e.printStackTrace()
                BaseRobotController.robotService?.robotMotor?.stopWheel()
            }
        }
    }

    // TODO REMOVE 23.10.27 영업데모
    private fun testForwardMoving() {//delay : distance*speed
        CoroutineScope(Dispatchers.IO).launch {
            try {
                BaseRobotController.robotService?.setMotor(JSONObject().apply {
                    this.put("position", 11)
                    this.put("degree", 5000)
                    this.put("speed", 0.1f)
                }.toString())
                delay(500)
                BaseRobotController.robotService?.robotMotor?.stopWheel()
            } catch (e: Exception) {
                e.printStackTrace()
                BaseRobotController.robotService?.robotMotor?.stopWheel()
            }
        }
    }

    // TODO REMOVE 23.10.27 영업데모
    private fun testTurnWheelLeft(degree: Int = 30) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                BaseRobotController.robotService?.robotMotor?.turnWheelLeft(degree)
                delay(500)
                BaseRobotController.robotService?.robotMotor?.stopWheel()
            } catch (e: Exception) {
                e.printStackTrace()
                BaseRobotController.robotService?.robotMotor?.stopWheel()
            }
        }

    }

    // TODO REMOVE 23.10.27 영업데모
    private fun testTurnWheelRight(degree: Int = 30) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                BaseRobotController.robotService?.robotMotor?.turnWheelRight(degree)
                delay(500)
                BaseRobotController.robotService?.robotMotor?.stopWheel()
            } catch (e: Exception) {
                e.printStackTrace()
                BaseRobotController.robotService?.robotMotor?.stopWheel()
            }
        }
    }

    private fun startWheelActionRemove(action: () -> Unit) {
        App.instance.currentActivity?.let { activity ->
            (activity as MainActivity).lifecycleScope.launch {
                delay(1500)
                if (isActive) action()
            }
        }
    }


    suspend fun startListeningMotion() {
        val index = Random.nextInt(10) % 3
        DWLog.d("startListeningMotion --> $index ")
        when (index) {
            0 -> BaseRobotController.robotService?.let {
                motorRightArmCommand(Pair(0, 75), Pair(-120, 75), Pair(-15, 50), Pair(-60, 25))
                motorLeftArmCommand(Pair(0, 75), Pair(0, 75), Pair(0, 50), Pair(-40, 25))
            }

            1 -> BaseRobotController.robotService?.let {
                motorLeftArmCommand(Pair(0, 75), Pair(-120, 75), Pair(-15, 50), Pair(-60, 25))
                motorRightArmCommand(Pair(0, 75), Pair(-120, 75), Pair(-15, 50), Pair(-60, 25))
            }

            else -> BaseRobotController.robotService?.let {
                motorLeftArmCommand(Pair(0, 75), Pair(-120, 75), Pair(-15, 50), Pair(-60, 25))
                motorRightArmCommand(Pair(0, 75), Pair(0, 75), Pair(0, 50), Pair(-40, 25))
            }

        }
    }
}

