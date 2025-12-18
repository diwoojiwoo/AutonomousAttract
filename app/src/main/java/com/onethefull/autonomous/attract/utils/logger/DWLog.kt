package com.onethefull.autonomous.attract.utils.logger

import android.util.Log
import com.onethefull.autonomous.attract.App

object DWLog {

    // 로그를 항상 활성화하도록 수정합니다.
    private const val debug = true

    private fun buildLogMsg(message: String): String {
        return "[${Thread.currentThread().name}] - $message"
    }

    fun i(message: String) {
        if (debug) Log.i(App.TAG, buildLogMsg(message))
    }

    fun i(TAG: String, message: String) {
        if (debug) Log.i(TAG, message)
    }

    fun w(message: String) {
        if (debug) Log.w(App.TAG, buildLogMsg(message))
    }

    fun w(TAG: String, message: String) {
        if (debug) Log.w(TAG, message)
    }

    fun d(message: String) {
        if (debug) Log.d(App.TAG, buildLogMsg(message))
    }

    fun d(TAG: String, message: String) {
        if (debug) Log.d(TAG, message)
    }

    fun v(message: String) {
        if (debug) Log.v(App.TAG, buildLogMsg(message))
    }

    fun v(TAG: String, message: String) {
        if (debug) Log.v(TAG, message)
    }

    fun e(message: String) {
        if (debug) Log.e(App.TAG, buildLogMsg(message))
    }

    fun e(TAG: String, message: String) {
        if (debug) Log.e(TAG, message)
    }
}
