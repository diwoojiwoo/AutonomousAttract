package com.onethefull.autonomous.attract.utils.animation

import android.animation.Animator
import android.app.Activity
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.ImageView
import androidx.annotation.RawRes
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.RenderMode
import com.onethefull.autonomous.attract.R
import com.onethefull.autonomous.attract.data.animation.AnimationData
import com.onethefull.autonomous.attract.utils.logger.DWLog
import com.onethefull.autonomous.attract.utils.animation.maker.AnimationMaker
import com.onethefull.dasomiconv.utils.animation.maker.AnimationMakerKebbi
import com.onethefull.autonomous.attract.utils.robot.kebbi.RobotActionKebbi
import com.onethefull.wonderfulrobotmodule.robot.BaseRobotController
import com.onethefull.wonderfulrobotmodule.robot.KebbiMotion
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Suppress("PrivatePropertyName", "SpellCheckingInspection")
class BackgroundUiActionDasom(val context: Activity, var root: View) :
    UiAction(), Handler.Callback {
    //    private val LOADINGS = arrayOf(LOADING, LOADING2)

    @Volatile
    @RawRes
    private var currentAnimResId: Int = R.raw.kid_normal


    @Volatile
    @RawRes
    private var newAnimResId = R.raw.kid_normal

    private var textJob: Job? = null

    private lateinit var animationMaker: AnimationMaker
    private var latestMotion = ""

    companion object {
        private const val TAG = "[ANIMATION]"

    }

    override fun start() {
        initAction()
        animationView = root.findViewById<LottieAnimationView?>(R.id.lottie_animation).apply {
            this.imageAssetsFolder = "lottie"
            this.setCacheComposition(true)
        }
        doNextAnimation()
    }

    private fun initAction() {
        robotAction = RobotActionKebbi()
        animationMaker = AnimationMakerKebbi()
        animationMaker.start()
    }

    /**
     * 최초 에니메이션 실행을 위한 리스너
     * 수다앱(SpeechTask) 실행시 SPEAK_ANI_STARTING 1회 실행 후
     * onAnimationEnd 이후
     * SPEAK_ANI_LISTENING 전환을 위한 리스너
     */
    private val listener = object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator) {
            DWLog.d("[${UiAction.TAG}] onAnimationRepeat -> $contentVar")
        }

        override fun onAnimationEnd(animation: Animator) {
            DWLog.d("[${UiAction.TAG}] onAnimationEnd -> $contentVar")
            if (!isAnimationCanceling) doNextAnimation()
//            animationView.removeAnimatorListener(this)
        }

        override fun onAnimationCancel(animation: Animator) {
            DWLog.d("[${UiAction.TAG}] onAnimationCancel -> $contentVar")
            isAnimationCanceling = true
        }

        override fun onAnimationStart(animation: Animator) {
            DWLog.d("[${UiAction.TAG}] onAnimationStart -> $contentVar")
            isAnimationCanceling = false
        }
    }

    private var isAnimationCanceling = false


    private fun startAnimation() {
        animationView.apply {
            enableMergePathsForKitKatAndAbove(true)
            renderMode = RenderMode.SOFTWARE
            if (!isAnimating) {
                DWLog.i("[${UiAction.TAG}] SOODA_ANIMATION : animationView is not Animating !")
                setAnimation(newAnimResId)
                scaleType = ImageView.ScaleType.FIT_CENTER
                repeatCount =
                    if (isOpening) {
                        isOpening = false
                        0
                    } else 0
                getLottieComposition(context, newAnimResId)?.let { result ->
                    setComposition(result)
                    playAnimation()
                }
            } else if (newAnimResId == currentAnimResId) {
                DWLog.i("[${UiAction.TAG}] SOODA_ANIMATION : same animation play !")
            } else {
                DWLog.i("[${UiAction.TAG}] SOODA_ANIMATION : different animation! re prepared ! $contentVar")
                getLottieComposition(context, newAnimResId)?.let { result ->
                    repeatCount = 0
                    setComposition(result)
                    playAnimation()
                }
            }
        }
        currentAnimResId = newAnimResId
    }

    private fun getContentResId(): AnimationData {
        DWLog.d("[${UiAction.TAG}] doNextAnimation -> getContentResId[contentVar] = $contentVar")
        return when {
            contentVar.startsWith(DPIMAGE_PREFIX) -> {
                if (contentVar.startsWith(DPIMAGE_PREFIX)) {
                    try {
                        imageContentView(contentVar.split(DPIMAGE_SPILITER)[1])
                        setContentVar("smile")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                animationMaker.getDefaultAnimation()
            }

            else -> animationMaker.getAnimationData(contentVar)
        }
    }

    /**
     * 로딩 중 시나리오
     */
    private fun startWaitingAction() {
        // robotAction.startAction(RobotAction.ACTION_NAME)
        DWLog.d("[${UiAction.TAG}] startWaitingAction $textJob")
        startMotion(
            if (loadingActionStep == 0) listOf("666_TA_TalkY", "666_TA_YoR").random()
            else "666_LE_ListenSong"
        )
    }

    private fun finishAction() {
        loadingActionStep = 0
        robotAction.releaseAction()
        textJob?.cancel()
        textJob = null
        DWLog.d("[${UiAction.TAG}] finishAction $textJob [loadingActionSteop]$loadingActionStep")
    }

    private val handlerException = CoroutineExceptionHandler { context, throwable ->
        DWLog.w("[${UiAction.TAG}]  CoroutineExceptionHandler ${throwable.message}")
    }

    private var loadingActionStep = 0

    override fun doNextAnimation() {

        DWLog.d("[${UiAction.TAG}]  doNextAnimation [isWait:$isWait]\t[isWakeUp:$isWakeUp]\t[customHintText:$customHintText]")
        if (isOpening) {
            animationMaker.getAnimationData(LISTENING)
        } else {
            if (isWakeUp) {
                finishAction()
                animationMaker.getAnimationData(LISTENING)
            } else {
                if (isWait) {
                    val loadingAni = animationMaker.getLoadingAnimations()[loadingActionStep]
                    startWaitingAction()
                    animationMaker.getAnimationData(loadingAni)
                } else {
                    if (isSpeaking) {
                        finishAction()
                        getContentResId()
                    } else {
                        animationMaker.getAnimationData(contentVar)
                    }
                }
            }
        }.run {
            MainScope().launch {
                DWLog.d("[${UiAction.TAG}]  doNextAnimation -> run ::>> ${this@run}")
                newAnimResId = this@run.resId
                startAnimation()
            }
        }
    }


    /**
     * 로봇 모션 실행
     *
     * @param motion 모션
     */
    @Suppress("KotlinConstantConditions")
    override fun startMotion(motion: String) {
        DWLog.d("motions_test", "startMotion $motion")
        CoroutineScope(Dispatchers.IO).launch {
            delay(2000)
            BaseRobotController.robotService?.robotMotor?.motionStart(
                "right_hand_raise",
                null
            )
        }
    }


    /**
     * 대기 액션 추가
     *
     * @param latestMotion 모션 네임
     */
    private fun addMotion(latestMotion: String) {
        val motion = KebbiMotion.getKebbiMotionEM().random()
        DWLog.d("[${UiAction.TAG}] finishMotion  -> addMotion $isWait $isWakeUp $motion")
        BaseRobotController.robotService?.robotMotor?.motionStop()
        CoroutineScope(Dispatchers.IO).launch {
            delay(500)
//            if (!isWait && !isWakeUp && !App.instance.isMute()) {
//                BaseRobotController.robotService?.robotMotor?.motionStart(
//                    motion,
//                    if (isWait || isWakeUp) null else object : IMotionCallback.Stub() {
//                        override fun finishMotion() {
//                            DWLog.d("[${UiAction.TAG}] finishMotion $isWait $isWakeUp $latestMotion")
//                            if (!isWait && !isWakeUp && App.instance.currentActivity != null) addMotion(
//                                latestMotion
//                            )
//                        }
//                    })
            }
        }

    override fun release() {
        animationView.removeAnimatorListener(listener)
        finishAction()
    }

    /**
     * 이미지 url 출력
     *
     * @param url 이미지 url
     */
    override fun imageContentView(url: String) {
        DWLog.d("[${UiAction.TAG}] imageContentView $url")
        animationView.visibility = View.GONE
    }

    override fun handleMessage(msg: Message): Boolean {
        return true
    }
}



