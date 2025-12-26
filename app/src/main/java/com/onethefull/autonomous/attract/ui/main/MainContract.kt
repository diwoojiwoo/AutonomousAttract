package com.onethefull.autonomous.attract.ui.main

/**
 * Created by sjw on 2025. 12. 24.
 */
interface MainContract {

    interface View {
        fun showToast(msg: String)
    }

    interface Presenter {
        fun init()
        fun start()
        fun initState()
        fun initSpeakingState()
        fun connect()
        fun disconnect()
        fun speakMent()
    }
}
