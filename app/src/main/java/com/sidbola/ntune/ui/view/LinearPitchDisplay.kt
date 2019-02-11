package com.sidbola.ntune.ui.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.sidbola.ntune.R
import kotlin.math.roundToInt

class LinearPitchDisplay(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var paintLines = Paint()
    private var cursorPaint = Paint()
    private var cursorInnerPaint = Paint()
    private var targetCirclePaint = Paint()
    private var promptPaint = Paint()

    private var frequency: Float
    private var targetFrequency: Float
    private var currentLineX: Float
    private var currentLineFutureX: Float

    private var promptPositionY = 0f
    private var prompt = ""
    private var promptStatus = -1

    private val TOO_HIGH = 2
    private val IN_TUNE = 1
    private val TOO_LOW = 0

    var cents = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        promptPositionY = height * .15f
        promptPaint.textSize = height / 15f
    }

    init {
        val font = ResourcesCompat.getFont(context, R.font.raleway_light)

        frequency = -1f
        targetFrequency = -1f
        currentLineX = -50f
        currentLineFutureX = 0f

        promptPaint.typeface = font
        promptPaint.color = Color.BLACK
        promptPaint.isAntiAlias = true
        promptPaint.style = Paint.Style.STROKE
        promptPaint.textAlign = Paint.Align.CENTER
        promptPaint.alpha = 0

        paintLines.color = Color.parseColor("#000000")
        paintLines.isAntiAlias = true
        paintLines.style = Paint.Style.STROKE
        paintLines.strokeJoin = Paint.Join.ROUND
        paintLines.strokeCap = Paint.Cap.ROUND
        paintLines.strokeWidth = context.resources.displayMetrics.density * 1.5f

        cursorPaint.color = Color.parseColor("#000000")
        cursorPaint.isAntiAlias = true
        cursorPaint.style = Paint.Style.STROKE
        cursorPaint.strokeJoin = Paint.Join.ROUND
        cursorPaint.strokeCap = Paint.Cap.ROUND
        cursorPaint.strokeWidth = context.resources.displayMetrics.density * 3f

        cursorInnerPaint.color = Color.parseColor("#00695C")
        cursorInnerPaint.isAntiAlias = true
        cursorInnerPaint.style = Paint.Style.FILL
        cursorInnerPaint.strokeJoin = Paint.Join.ROUND
        cursorInnerPaint.strokeCap = Paint.Cap.ROUND

        targetCirclePaint.color = Color.parseColor("#000000")
        targetCirclePaint.isAntiAlias = true
        targetCirclePaint.style = Paint.Style.STROKE
        targetCirclePaint.strokeJoin = Paint.Join.ROUND
        targetCirclePaint.strokeCap = Paint.Cap.ROUND
        targetCirclePaint.strokeWidth = context.resources.displayMetrics.density * 6f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawLine(0f, height / 2f, width / 2f - 40, height / 2f, paintLines)
        canvas?.drawLine(width / 2f + 40, height / 2f, width.toFloat(), height / 2f, paintLines)
        canvas?.drawCircle(width / 2f, height / 2f, 40f, targetCirclePaint)


        if (frequency != -1f) {
            canvas?.drawCircle(currentLineX, height / 2f, 34f, cursorInnerPaint)
            canvas?.drawCircle(currentLineX, height / 2f, 35f, cursorPaint)

            canvas?.drawText(prompt, width / 2f, promptPositionY, promptPaint)
        }
    }

    private fun animatePromptChange(newPrompt: String, newStatus: Int) {
        if (promptStatus != newStatus) {
            promptStatus = newStatus
            val animateDown = ValueAnimator.ofInt(255, 0)
            animateDown.duration = 300
            animateDown.interpolator = DecelerateInterpolator()
            animateDown.addUpdateListener {
                promptPaint.alpha = it.animatedValue as Int
                invalidate()
            }

            val animatePosUp = ValueAnimator.ofFloat(height * .15f, height * .15f - 20)
            animatePosUp.duration = 300
            animatePosUp.interpolator = DecelerateInterpolator()
            animatePosUp.addUpdateListener {
                promptPositionY = it.animatedValue as Float
                invalidate()
            }
            animatePosUp.start()

            animateDown.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    prompt = newPrompt

                    val animateUp = ValueAnimator.ofInt(0, 255)
                    animateUp.duration = 300
                    animateUp.interpolator = DecelerateInterpolator()
                    animateUp.addUpdateListener {
                        promptPaint.alpha = it.animatedValue as Int
                        invalidate()
                    }
                    animateUp.start()

                    val animatePosDown = ValueAnimator.ofFloat(height * .15f - 20f, height * .15f)
                    animatePosDown.duration = 300
                    animatePosDown.interpolator = DecelerateInterpolator()
                    animatePosDown.addUpdateListener {
                        promptPositionY = it.animatedValue as Float
                        invalidate()
                    }
                    animatePosDown.start()
                }
            })

            animateDown.start()
        }
    }

    private fun updatePrompt() {
        when {
            currentLineX > width / 2f + 20f -> {
                animatePromptChange("Too high! Tune down", TOO_HIGH)
            }
            currentLineX < width / 2f - 20f && currentLineX > 0 -> {
                animatePromptChange("Too low! Tune up", TOO_LOW)
            }
            currentLineX >= width / 2f - 20 && currentLineX <= width / 2f + 20 -> {
                animatePromptChange("You're in tune!", IN_TUNE)
            }
            else -> {
                animatePromptChange("", -1)
            }
        }
    }

    private fun animateCursor() {
        val animator = ValueAnimator.ofFloat(currentLineX, currentLineFutureX)
        animator.duration = 400
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener {
            this.currentLineX = it.animatedValue as Float
            invalidate()
        }
        animator.start()
    }

    fun updateDisplay(currentFrequency: Float, targetFrequency: Float) {

        if (currentFrequency != -1f) {
            cents =
                (1200 * 3.322038403 * Math.log10((currentFrequency / targetFrequency).toDouble())).roundToInt()
            this.frequency = currentFrequency
            this.targetFrequency = targetFrequency

            val unitLength = width / 40
            val bottomEnd = targetFrequency - 20

            val translatedCurFreq = currentFrequency - bottomEnd
            currentLineFutureX = translatedCurFreq * unitLength
        } else {
            currentLineFutureX = -50f
        }

        animateCursor()
        updatePrompt()


        invalidate()
        requestLayout()
    }
}