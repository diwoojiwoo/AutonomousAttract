package com.onethefull.autonomous.attract.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.onethefull.autonomous.attract.R
import com.onethefull.autonomous.attract.robot.DrivingManager
import com.onethefull.autonomous.attract.utils.logger.DWLog
import com.onethefull.wonderfulrobotmodule.robot.BaseRobotController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_main, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.button_start_driving).setOnClickListener {
            startForwardBackwardLoop()
        }

        view.findViewById<Button>(R.id.button_stop_driving).setOnClickListener {
            stopMotion()
        }
    }


    private fun startForwardBackwardLoop(
        repeatCount: Int = DEFAULT_REPEAT,
        degree: Int = DEFAULT_DEGREE,
        speed: Float = DEFAULT_SPEED
    ) {
        val service = BaseRobotController.robotService
        val motor = service?.robotMotor

        if (service == null || motor == null) {
            DWLog.e("RobotService or RobotMotor is null")
            return
        }

        DWLog.d("service=$service")
        DWLog.d("motor=$motor")

        motor.setWheelLockState(false)

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                repeat(repeatCount) {
                    // ▶ 전진
                    sendMotor(position = DEFAULT_POSITION, degree = degree, speed = speed)
                    delay(MOVE_DELAY)
                    stopWheel()
                    delay(STOP_DELAY)

                    // ◀ 후진
                    sendMotor(position = DEFAULT_POSITION, degree = degree, speed = -speed)
                    delay(MOVE_DELAY)
                    stopWheel()
                    delay(STOP_DELAY)
                }
            } catch (e: Exception) {
                DWLog.e("Driving error", e.message.toString())
            } finally {
                stopWheel()
            }
        }
    }

    private fun sendMotor(position: Int, degree: Int, speed: Float) {
        BaseRobotController.robotService?.setMotor(
            JSONObject().apply {
                put("position", position)
                put("degree", degree)
                put("speed", speed)
            }.toString()
        )
    }

    private fun stopWheel() {
        BaseRobotController.robotService?.robotMotor?.stopWheel()
    }

    private fun forwardOnceDemo() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                sendMotor(
                    position = DEFAULT_POSITION,
                    degree = 5000,
                    speed = 0.1f
                )
                delay(500)
            } finally {
                stopWheel()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        stopMotion()
    }

    fun stopMotion() {
        stopWheel()
    }

    companion object {
        private const val DEFAULT_POSITION = 11
        private const val DEFAULT_DEGREE = 3000
        private const val DEFAULT_SPEED = 0.08f
        private const val DEFAULT_REPEAT = 2
        private const val MOVE_DELAY = 500L
        private const val STOP_DELAY = 400L
    }
}
