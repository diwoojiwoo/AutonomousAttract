package com.onethefull.dasomiconv.utils.animation.maker

import androidx.annotation.RawRes
import com.onethefull.autonomous.attract.R
import com.onethefull.autonomous.attract.data.animation.AnimationData
import com.onethefull.autonomous.attract.utils.animation.UiAction
import com.onethefull.autonomous.attract.utils.animation.maker.AnimationMaker
import com.onethefull.autonomous.attract.utils.logger.DWLog
import kotlin.random.Random


/**
 * Created by Douner on 2023/11/28.
 */

@Suppress("SpellCheckingInspection")
class AnimationMakerKebbi : AnimationMaker {

    @RawRes
    val defaultRes = R.raw.kid_normal
    private val LOADINGS = arrayOf(UiAction.EMOTION_KID_THINKING_2, UiAction.EMOTION_KID_LOADING)

    private val animationList by lazy {
        HashMap<String, Int>().apply {
            put(UiAction.DEFAULT, R.raw.kid_normal)
            put(UiAction.LISTENING, R.raw.kid_listening)
            put(UiAction.LOADING, R.raw.kid_chorong)
            put(UiAction.EMOTION_KID_LOVE, R.raw.kid_love)
            put(UiAction.EMOTION_KID_NORMAL, R.raw.kid_normal)
            put(UiAction.EMOTION_KID_SHY, R.raw.kid_shy)
            put(UiAction.EMOTION_KID_SLEEPY, R.raw.kid_sleepy)
            put(UiAction.EMOTION_KID_CHORONG, R.raw.kid_chorong)
        }
    }

    override fun start() {
        repeat(4) { index ->
            val key = "${UiAction.NEW_COMMON_LISTENING_}$index"
            animationList[key] = R.raw.kid_normal
        }
        repeat(4) { index ->
            val key = "${UiAction.COMMON_SPEAKING_}$index"
            animationList[key] = R.raw.kid_talking
        }
    }

    override fun getAnimationData(contentVar: String): AnimationData {
        DWLog.i("[${UiAction.TAG}] getAnimationData $contentVar")
        val resId = when (contentVar) {
            UiAction.LISTENING -> getListeningCommonAnimation()
            UiAction.SPEAKING -> getSpeakingCommonAnimation()
            else -> animationList[contentVar]
        }
        return if (resId != null) {
            AnimationData(resId, contentVar, 0)
        } else {
            getDefaultAnimation()
        }
    }

    private fun getListeningCommonAnimation(): Int {
        val resVar = "${UiAction.LISTENING}${Random.nextInt(100)}"
        val resId = animationList[resVar]
        DWLog.d("[${UiAction.TAG}] getListeningCommonAnimation $resId $resVar")
        return resId ?: R.raw.kid_normal
    }

    private fun getSpeakingCommonAnimation(): Int {
        val resVar = "${UiAction.COMMON_SPEAKING_}${Random.nextInt(4)}"
        val resId = animationList[resVar]
        DWLog.d("[${UiAction.TAG}] getSpeakingCommonAnimation $resId $resVar")
        return resId ?: R.raw.kid_talking
    }

    override fun getDefaultAnimation(): AnimationData {
        return AnimationData(R.raw.kid_talking, UiAction.SPEAKING)
    }

    override fun getLoadingAnimations(): Array<String> {
        return LOADINGS
    }

    override fun getListeningMessage(): String {
        return ""
    }

    override fun getSpeakingMessage(): String {
        return ""
    }

    override fun getLoadingMessage(): String {
        return ""
    }

}