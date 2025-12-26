package com.onethefull.autonomous.attract.utils.animation

import android.content.Context
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatTextView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.onethefull.autonomous.attract.utils.action.RobotAction
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

/**
 * Created by Douner on 4/29/21.
 */
@Suppress("unused", "SpellCheckingInspection")
abstract class UiAction {
    var isOpening = true
    var isWait = false
    var isSpeaking = false

    private lateinit var _device: DEVICE
    val mDevice get() = _device

    private var _contentVar = "empty"
    val contentVar get() = _contentVar

    lateinit var animationView: LottieAnimationView
    lateinit var robotAction: RobotAction

    enum class DEVICE {
        CLOI, BEANQ, KEBBI
    }

    companion object {

        const val TAG = "UiAction"

        const val DPIMAGE_PREFIX = "DPIMAGE"
        const val DPIMAGE_SPILITER = "``"

        const val WEATHER_SUNNY = "SUNNY"
        const val WEATHER_CLOUDY = "CLOUDY"
        const val WEATHER_SNOWY = "SNOWY"
        const val WEATHER_RAINY = "RAINY"
        const val WEATHER_WINDY = "WINDY"
        const val WEATHER_THUNDER = "THUNDER"
        const val WEATHER_THUNDER_RAIN = "THUNDER_RAIN"

        const val WEATHER_DEFAULT = "W_DEFAULT"
        private const val ANIMATION_INTERVAL_TIME = 200

        const val DEFAULT = "DEFAULT"
        const val SPEAKING = "SPEAKING"
        const val LISTENING = "LISTENING"
        const val COMMON_LISTENING_0 = "COMMON_LISTENING_2"
        const val COMMON_LISTENING_1 = "COMMON_LISTENING_3"
        const val COMMON_LISTENING_2 = "COMMON_LISTENING_4"
        const val COMMON_LISTENING_3 = "COMMON_LISTENING_5"
        const val COMMON_LISTENING_4 = "COMMON_LISTENING_6"
        const val COMMON_LISTENING_5 = "COMMON_LISTENING_7"


        const val NEW_COMMON_LISTENING_ = "NEW_COMMON_LISTENING_"
        const val COMMON_SPEAKING_ = "NEW_COMMON_SPEAKING_"

        const val COMMON_SPEAKING_0 = "NEW_COMMON_SPEAKING_0"
        const val COMMON_SPEAKING_1 = "NEW_COMMON_SPEAKING_1"
        const val COMMON_SPEAKING_2 = "NEW_COMMON_SPEAKING_2"
        const val COMMON_SPEAKING_3 = "NEW_COMMON_SPEAKING_3"

        const val LOADING = "LOADING"
        const val LOADING2 = "LOADING2"
        const val SMILE = "SMILE"
        const val STARTING = "STARTING"

        const val EMOTION_KID_HAPOOM = "kid_hapoom"
        const val EMOTION_KID_FINDING = "kid_finding"
        const val EMOTION_KID_FOUND = "kid_found"
        const val EMOTION_KID_CHORONG = "kid_chorong"

        const val EMOTION_KID_LOADING = "kid_loading"

        const val EMOTION_KID_THINKING = "kid_thinking"
        const val EMOTION_KID_THINKING_2 = "kid_thinking2"
        const val EMOTION_KID_LOVE = "kid_love"
        const val EMOTION_KID_NORMAL = "kid_namal"
        const val EMOTION_KID_PANIC = "kid_panic"
        const val EMOTION_KID_SAD = "kid_sad"
        const val EMOTION_KID_SHY = "kid_shy"
        const val EMOTION_KID_SMILE = "kid_smile"
        const val EMOTION_KID_SURPRISE = "kid_surprise"
        const val EMOTION_KID_ANGRY = "kid_angry"
        const val EMOTION_KID_SLEEPING = "kid_sleeping"
        const val EMOTION_KID_SLEEPY = "kid_sleepy"
        const val KEBBI_LISTENING = "kid_listening"

        const val EMOTION_KID_SOS = "kid_sos"

        val randomHandMotions = arrayOf(
            "fighting",
            "raising_hands",
            "hands_together",
            "hand_waving",
            "arm_open_and_close",
            "right_arm_stretching",
            "right_arm_raising",
            "bending_arms_same_time",
            "clapping_hands",
            "arms_stretching",
            "silent_pull_hands_right",
            "silent_pull_hands_left",
            "one_arm_leg_stretching",
            "one_arm_leg_stretching_side"
        )
    }

    protected fun getLottieComposition(context: Context, resId: Int): LottieComposition? {
        LottieCompositionFactory.fromRawResSync(context, resId)?.let {
            return it.value
        } ?: return null
    }


    abstract fun start()
    abstract fun doNextAnimation()

    abstract fun startMotion(motion: String)
    fun setContentVar(contentVar: String) {
        this._contentVar = contentVar
    }

    fun setContetVarDPimage(url: String) {
        setContentVar("$DPIMAGE_PREFIX$DPIMAGE_SPILITER$url")
    }

    fun initDevice(device: DEVICE) {
        _device = device
    }

    fun getValidMotion(motion: String): String {
        return when (motion) {

            "BYE" -> "666_RE_BYE"
            "LOOK_RL" -> "666_TA_LookRL"
            "KILLED" -> "666_PE_Killed"
            "SCRATCHING" -> "666_DA_Scratching"
            "ROOSTER" -> "666_IM_Rooster"
            "LOOK_LR" -> "666_TA_LookLR"
            "PICKUP" -> "666_DA_PickUp"
            "GUITAR" -> "666_PE_PlayGuitar"
            "OFF" -> "off"
            "RANDOMCHAT_WAIT" -> "randomchat_wait"
            "RANDOMCHAT_START" -> "randomchat_start"
            "RANDOMCHAT_FINISH" -> "randomchat_finish"
            "EMERGENCY_START" -> "emergency_start"
            "EMERGENCY_FINISH" -> "emergency_finish"
            "CALL_ACCEPT" -> "call_accept"
            "CALL_SEND" -> "call_send"
            "HANDS_UP" -> "hands_up"
            "MALBUT" -> "malbut_talking"
            "HUG" -> "hug"
            "MOVE_ARM_FB" -> "moveArmFrontBack"
            "LEFT_ARM_UP" -> "leftTapUp"
            "RIGHT_ARM_UP" -> "rightTapUp"
            "SHY" -> "shy"
            "CROSS_ARM_UP" -> "cross_arm_up"
            "BOTH_ARM_UP" -> "both_arm_up"
            "head_turn_left" -> "head_left"
            "head_turn_right" -> "head_right"

            // 영상인식 제어 추가
            // "turn_left" -> "head_left"
            // "turn_right" -> "head_right"

            "donot_look_head_down" -> "head_down"

            else -> motion
        }
    }

    abstract fun imageContentView(url: String)
    private fun initResource() {

    }

    fun doForceSetAnimation(context: Context, resId: Int) {
        MainScope().launch {
            getLottieComposition(context, resId)?.let { result ->
                animationView.repeatCount = 0
                animationView.setComposition(result)
                animationView.playAnimation()
            }
        }
    }

    abstract fun release()
}
