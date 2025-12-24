package com.onethefull.autonomous.attract.utils.animation.maker

import com.onethefull.autonomous.attract.data.animation.AnimationData

/**
 * Created by Douner on 2023/11/28.
 */
interface AnimationMaker {
    fun start()
    fun getAnimationData(contentVar: String): AnimationData
    fun getDefaultAnimation(): AnimationData
    fun getLoadingAnimations(): Array<String>
    fun getListeningMessage():String
    fun getSpeakingMessage():String
    fun getLoadingMessage():String

}