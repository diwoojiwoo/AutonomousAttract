package com.onethefull.autonomous.attract.robot

import android.os.Handler
import android.os.Looper
import com.onethefull.autonomous.attract.App
import com.onethefull.autonomous.attract.utils.logger.DWLog
import com.onethefull.wonderfulrobotmodule.robot.BaseRobotController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject

object DrivingManager {

    /**
     * 8자 주행 패턴을 시작하는 함수입니다. (현재는 테스트 코드를 실행합니다)
     */
    fun startFigureEightPattern() {
        // 코루틴을 사용하여 전진 테스트 함수를 실행합니다.
        CoroutineScope(Dispatchers.Default).launch {
            testDriveForward()
        }
    }

    /**
     * 모든 움직임을 즉시 중지합니다.
     */
    fun stopMotion() {
        if (App.isRobotConnected) {
            DWLog.i("DrivingManager: Sending stop command.")
            BaseRobotController.robotService?.robotMotor?.reset()
            BaseRobotController.robotService?.robotMotor?.stopWheel()
        } else {
            DWLog.w("DrivingManager: Cannot stop motion, service not connected.")
        }
    }

    /**
     * 2초 동안 전진했다가 멈추는 테스트 함수입니다.
     */
    private suspend fun testDriveForward() {
        DWLog.d("Executing testDriveForward...")

        // 1. 2초 동안 전진
        driveForwardFor(2000L)

        DWLog.d("testDriveForward complete.")
    }

    /**
     * 지정된 시간(duration) 동안 앞으로 전진하는 suspend 함수입니다.
     */
    private suspend fun driveForwardFor(duration: Long) {
        if (!App.isRobotConnected) {
            DWLog.e("Cannot drive forward: Robot service not connected.")
            return
        }

        DWLog.d("Driving forward for ${duration}ms...")

        // 전진 명령 전송
        val command = JSONObject().apply {
            put("position", 11)
            put("degree", 5000) // 충분히 큰 값으로 설정하여 시간 동안 계속 움직이게 함
            put("speed", 0.1f)
        }.toString()
        BaseRobotController.robotService?.setMotor(command)

        // 지정된 시간(duration)만큼 대기
        delay(duration)

        // 정지 명령 전송
        stopMotion()
    }
}
