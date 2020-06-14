package com.maleev.bottomsheetanimation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.transition.Transition
import android.transition.TransitionValues
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import androidx.core.animation.doOnStart

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class BottomSheetSharedTransition : Transition {

    @Suppress("unused")
    constructor() : super()

    @Suppress("unused")
    constructor(
        context: Context?,
        attrs: AttributeSet?
    ) : super(context, attrs)

    companion object {
        private const val PROP_HEIGHT = "heightTransition:height"

        // the property PROP_VIEW_TYPE is workaround that allows to run transition always
        // even if height was not changed. It's required as we should set container height
        // to WRAP_CONTENT after animation complete
        private const val PROP_VIEW_TYPE = "heightTransition:viewType"
        private const val ANIMATION_DURATION = 400L

        private val TransitionProperties = arrayOf(PROP_HEIGHT, PROP_VIEW_TYPE)
    }

    override fun getTransitionProperties(): Array<String> = TransitionProperties

    override fun captureStartValues(transitionValues: TransitionValues) {
        // we capture current fragment height...
        transitionValues.values[PROP_HEIGHT] = transitionValues.view.height
        transitionValues.values[PROP_VIEW_TYPE] = "start"

        // ... and then pin parent height to specific value.
        // It's needed for fragment container not to jump during transition
        (transitionValues.view.parent as View).apply {
            this.layoutParams.height = this.height
            this.layoutParams = this.layoutParams
        }
    }

    override fun captureEndValues(transitionValues: TransitionValues) {
        // for end values we should re-measure parent as earlier we pinned its height
        // and new fragment cannot take height it needs
        transitionValues.values[PROP_HEIGHT] = getViewHeight(transitionValues.view.parent as View)
        transitionValues.values[PROP_VIEW_TYPE] = "end"
    }

    override fun createAnimator(
        sceneRoot: ViewGroup?,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator? {
        if (startValues == null || endValues == null) {
            return null
        }

        val animators = listOf<Animator>(
            prepareHeightAnimator(
                startValues.values[PROP_HEIGHT] as Int,
                endValues.values[PROP_HEIGHT] as Int,
                endValues.view
            ),
            prepareAlphaAnimator(endValues.view)
        )

        return AnimatorSet()
            .apply {
                playTogether(animators)
                doOnStart { startValues.view.alpha = 0f }
            }
    }

    private fun prepareAlphaAnimator(view: View): Animator =
        ObjectAnimator.ofFloat(
            view,
            "alpha",
            0f,
            1f
        )
            .apply {
                duration =
                    ANIMATION_DURATION
                interpolator = AccelerateInterpolator()
            }

    private fun prepareHeightAnimator(
        startHeight: Int,
        endHeight: Int,
        view: View
    ) = ValueAnimator.ofInt(startHeight, endHeight)
        .apply {
            duration =
                ANIMATION_DURATION

            addUpdateListener { animation ->
                (view.parent as? View)?.apply {
                    layoutParams.height = animation.animatedValue as Int
                    layoutParams = layoutParams
                }
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    (view.parent as? View)?.apply {
                        layoutParams.height =
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        layoutParams = layoutParams
                    }
                }

                override fun onAnimationCancel(animation: Animator?) {
                }

                override fun onAnimationStart(animation: Animator?) {
                }
            })
        }

    private fun getViewHeight(view: View): Int {
        // Получаем ширину экрана
        val deviceWidth = getScreenWidth(view)

        // Попросим View измерить себя при указанной ширине экрана
        val widthMeasureSpec =
            View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.EXACTLY)
        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)

        return view
            // измеряем:
            .apply { measure(widthMeasureSpec, heightMeasureSpec) }
            // получаем измеренную высоту:
            .measuredHeight
            // если View хочет занять высоту больше доступной высоты экрана, мы должны вернуть высоту экрана:
            .coerceAtMost(getScreenHeight(view))
    }

    private fun getScreenHeight(view: View) =
        getDisplaySize(view).y - getStatusBarHeight(view.context)

    private fun getScreenWidth(view: View) =
        getDisplaySize(view).x

    private fun getDisplaySize(view: View) =
        Point().also {
            (view.context.getSystemService(
                Context.WINDOW_SERVICE
            ) as WindowManager).defaultDisplay.getSize(it)
        }

    private fun getStatusBarHeight(context: Context): Int =
        context.resources
            .getIdentifier("status_bar_height", "dimen", "android")
            .takeIf { resourceId -> resourceId > 0 }
            ?.let { resourceId -> context.resources.getDimensionPixelSize(resourceId) }
            ?: 0
}