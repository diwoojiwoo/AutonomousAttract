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
//    private val LOADINGS = arrayOf(UiAction.EMOTION_KID_CHORONG, UiAction.EMOTION_KID_THINKING_2)
    private val LOADINGS = arrayOf(UiAction.EMOTION_KID_THINKING_2, UiAction.EMOTION_KID_LOADING)

    private val animationList by lazy {

        HashMap<String, Int>().apply {

            put(UiAction.DEFAULT, R.raw.kid_listening)
//            put(UiAction.SPEAKING, R.raw.kid_talking1)
            put(UiAction.LISTENING, R.raw.kid_listening)
//            put(UiAction.COMMON_LISTENING_0, R.raw.kebbi_common_listening_2)
//            put(UiAction.COMMON_LISTENING_1, R.raw.kebbi_common_listening_6)
//            put(UiAction.COMMON_LISTENING_2, R.raw.kebbi_common_listening_5)
//            put(UiAction.COMMON_LISTENING_3, R.raw.kebbi_common_listening_3)
//            put(UiAction.COMMON_LISTENING_4, R.raw.kebbi_common_listening_4)
//            put(UiAction.COMMON_LISTENING_5, R.raw.kebbi_common_listening_7)

            put(UiAction.LOADING, R.raw.kid_chorong)
            put(UiAction.EMOTION_KID_LOVE, R.raw.kid_love)
            put(UiAction.EMOTION_KID_NORMAL, R.raw.kid_normal)
            put(UiAction.EMOTION_KID_SHY, R.raw.kid_shy)
            put(UiAction.EMOTION_KID_SLEEPY, R.raw.kid_sleepy)
            put(UiAction.EMOTION_KID_CHORONG, R.raw.kid_chorong)

        }
    }

    override fun start() {
        repeat(100) { index ->
            val key = "${UiAction.NEW_COMMON_LISTENING_}$index"
            val value = R.raw.kebbi_common_new_listening_00001 + index
            animationList[key] = value
        }
        repeat(4) { index ->
            val key = "${UiAction.COMMON_SPEAKING_}$index"
            val value = R.raw.kid_talking1 + index
            animationList[key] = value
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
        val resVar = "${UiAction.NEW_COMMON_LISTENING_}${Random.nextInt(100)}"
        val resId = animationList[resVar]
        DWLog.d("[${UiAction.TAG}] getListeningCommonAnimation $resId $resVar")
        return resId ?: R.raw.kebbi_common_listening_2

    }

    private fun getSpeakingCommonAnimation(): Int {
        val resVar = "${UiAction.COMMON_SPEAKING_}${Random.nextInt(4)}"
        val resId = animationList[resVar]
        DWLog.d("[${UiAction.TAG}] getSpeakingCommonAnimation $resId $resVar")
        return resId ?: R.raw.kid_talking
    }

    private fun getListeningAnimationOneTime(): Int {
//        var index = (DasomSharedPreference.getInstance(App.instance)?.latestListeningIndex ?: 0)
        var index = 0
        if (index < 0 || index > 6) {
            index = 0
//            DasomSharedPreference.getInstance(App.instance)?.latestListeningIndex = 0
        }

        return animationList[
            when (index) {
                0 -> UiAction.COMMON_LISTENING_0
                1 -> UiAction.COMMON_LISTENING_1
                2 -> UiAction.COMMON_LISTENING_2
                3 -> UiAction.COMMON_LISTENING_3
                4 -> UiAction.COMMON_LISTENING_4
                5 -> UiAction.COMMON_LISTENING_5
                else -> UiAction.COMMON_LISTENING_0
            }
        ] ?: R.raw.kebbi_common_listening_2
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