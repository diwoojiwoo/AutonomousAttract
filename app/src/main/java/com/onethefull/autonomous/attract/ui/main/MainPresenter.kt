package com.onethefull.autonomous.attract.ui.main

import android.app.Activity
import android.os.Bundle
import com.onethefull.autonomous.attract.R
import com.onethefull.autonomous.attract.robot.InfinityDrivingManager
import com.onethefull.autonomous.attract.utils.animation.UiAction
import com.onethefull.autonomous.attract.utils.logger.DWLog
import com.onethefull.autonomous.attract.utils.speech.GCTextToSpeech
import com.onethefull.wonderfulrobotmodule.robot.BaseRobotController
import com.onethefull.wonderfulrobotmodule.robot.KebbiMotion
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by sjw on 2025. 12. 24.
 */
class MainPresenter(
    private val context: Activity,
    private val mainView: MainContract.View,
    private val backgroundUi: UiAction,
) : MainContract.Presenter, GCTextToSpeech.Callback {

    val jokes = listOf(
        context.getString(R.string.attract_human_joke_1),
        context.getString(R.string.attract_human_joke_2),
        context.getString(R.string.attract_human_joke_3),
        context.getString(R.string.attract_human_joke_4),
        context.getString(R.string.attract_human_joke_5)
    ).shuffled().toMutableList() // 랜덤 순서, 중복 없이

    var currentJokes = jokes.shuffled().toMutableList()

    init {
        InfinityDrivingManager.drivingListener = object : InfinityDrivingManager.DrivingListener {
            override fun onDrivingFinished() {
                speakMent()
            }
        }
    }

    override fun init() {
        backgroundUi.start()
    }

    override fun start() {
        connect()
    }

    override fun initState() {
        if (backgroundUi.isWait) {
            backgroundUi.isWait = false
            backgroundUi.doNextAnimation()
        }
    }

    override fun initSpeakingState() {
        backgroundUi.isWait = false
        backgroundUi.setContentVar(UiAction.SPEAKING)
        backgroundUi.doNextAnimation()
    }

    override fun onSpeechStart() {
        speechStarted()
    }

    override fun onSpeechFinish() {
        speechFinished()
    }

    override fun onGenieSTTResult(result: String) {}

    private fun speechStarted() {
        backgroundUi.isWait = false
        backgroundUi.isSpeaking = true
        backgroundUi.setContentVar(UiAction.SPEAKING)
        backgroundUi.doNextAnimation()
    }

    private fun speechFinished() {
        DWLog.d("speechFinished")
        changeStatusSpeechFinished()
        InfinityDrivingManager.startInfinityLoop()
    }

    private fun changeStatusSpeechFinished() {
        backgroundUi.apply {
            isWait = false
            isSpeaking = false
        }
        backgroundUi.setContentVar(UiAction.LISTENING)
        backgroundUi.doNextAnimation()
    }

    override fun connect() {
        DWLog.d("SpeechTaskPresenter Connect")
        GCTextToSpeech.getInstance()?.setCallback(this)
        GCTextToSpeech.getInstance()?.start(context)
    }

    override fun disconnect() {
        backgroundUi.release()
        GCTextToSpeech.getInstance()?.requestReleaseSpeech()
    }

    override fun speakMent() {
        DWLog.d("speakMent")
        if (currentJokes.isEmpty()) {
            currentJokes = jokes.shuffled().toMutableList() // 리스트 초기화
        }

        val text = currentJokes.removeAt(0)
        GCTextToSpeech.getInstance()?.speech(text)
    }
}