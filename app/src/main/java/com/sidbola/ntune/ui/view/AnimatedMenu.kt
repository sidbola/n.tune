package com.sidbola.ntune.ui.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.OvershootInterpolator
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.support.v4.content.res.ResourcesCompat
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnticipateInterpolator
import com.sidbola.ntune.R
import com.sidbola.ntune.data.Instrument
import com.sidbola.ntune.data.Tuning

const val ANIMATION_DURATION = 300L
const val FIRST_LEVEL_COUNT = 3

class AnimatedMenu(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    interface OnTuningSelected {
        fun onTuningSelected(tuning: Tuning)
    }

    // Listeners
    private var mListener: OnTuningSelected? = null

    // Paints
    private val titleTextPaintLine = Paint()
    private val textPaintLine = Paint()
    private val selectedCirclePaint = Paint()
    private val currentTuningPaint = Paint()
    private val backgroundPaint = Paint()

    // Animated properties
    private var buttonRadius = 0f
    private var buttonStartPosX = 0f
    private var buttonStartPosY = 0f
    private var vectorIconBoundVariation = 0
    private var buttonCaptionSpacing = 0f
    private var buttonSpreadRadius = 0f
    private var backgroundSpreadRadius = 0f
    private var topBackgroundRadius = 0f
    private var cornerBackgroundRadius = 0f
    private var expanderItemLeft = 0f
    private var expanderItemRight = 0f
    private var expanderDeviation = 0f
    private var titleSpacingFromTop = 0f
    private var tuningItemSpacing = 0f

    // Data & state holders
    private var menuState = MenuState.DEFAULT
    private var items = ArrayList<ButtonItem>()
    private var tuningDisplays = ArrayList<String>()
    private var selectedIndex = 0
    private var selectedTuning = "Standard"
    private var showTunings = false
    private var shouldShowAllIcons = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // Set dimensions for animated properties
        buttonRadius = width / 17f
        buttonStartPosX = buttonRadius * 1.5f
        buttonStartPosY = buttonRadius * 1.5f
        buttonSpreadRadius = width / 4f
        buttonCaptionSpacing = height / 21f
        backgroundSpreadRadius = width * 0.8f
        vectorIconBoundVariation = width / 30
        titleTextPaintLine.textSize = height / 45f
        textPaintLine.textSize = height / 100f
        currentTuningPaint.textSize = height / 25f
        expanderDeviation = width / 4f
        titleSpacingFromTop = height / 7f
        tuningItemSpacing = height / 12f

        // Set default locations for buttons
        for (item in items) {
            item.x = buttonStartPosX
            item.y = buttonStartPosY
        }
    }

    init {
        val font = ResourcesCompat.getFont(context, R.font.raleway_light)

        textPaintLine.typeface = font
        textPaintLine.color = Color.parseColor("#ffffff")
        textPaintLine.isAntiAlias = true
        textPaintLine.style = Paint.Style.STROKE
        textPaintLine.textAlign = Paint.Align.CENTER
        textPaintLine.alpha = 0

        titleTextPaintLine.typeface = font
        titleTextPaintLine.color = Color.parseColor("#ffffff")
        titleTextPaintLine.isAntiAlias = true
        titleTextPaintLine.style = Paint.Style.STROKE
        titleTextPaintLine.textAlign = Paint.Align.CENTER
        titleTextPaintLine.alpha = 0

        currentTuningPaint.typeface = font
        currentTuningPaint.color = Color.parseColor("#000000")
        currentTuningPaint.isAntiAlias = true
        currentTuningPaint.style = Paint.Style.STROKE

        backgroundPaint.color = Color.parseColor("#000000")
        backgroundPaint.isAntiAlias = true
        backgroundPaint.alpha = 240

        selectedCirclePaint.color = Color.parseColor("#00695C")
        selectedCirclePaint.isAntiAlias = true

        for (instrument in Instrument.values()) {
            items.add(ButtonItem(buttonStartPosX, buttonStartPosY, instrument.toString(), instrument.name, instrument.getImage))
        }
    }

    fun setTuningSelectedListener(listener: OnTuningSelected) {
        this.mListener = listener
    }

    private fun animateOpenMenu() {
        bringToFront()

        val currentTuningTextAnimator = ValueAnimator.ofInt(255, 0)
        currentTuningTextAnimator.duration = 200
        currentTuningTextAnimator.interpolator = AccelerateDecelerateInterpolator()
        currentTuningTextAnimator.addUpdateListener {
            currentTuningPaint.alpha = it.animatedValue as Int
            invalidate()
        }
        currentTuningTextAnimator.start()

        // Animate background
        val bgAnimator = ValueAnimator.ofFloat(0f, backgroundSpreadRadius)
        bgAnimator.duration = 200
        bgAnimator.interpolator = AccelerateDecelerateInterpolator()
        bgAnimator.addUpdateListener {
            cornerBackgroundRadius = it.animatedValue as Float
            invalidate()
        }

        // Animate buttons once background finishes animating
        bgAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)

                shouldShowAllIcons = true
                textPaintLine.alpha = 255
                var currentDeg = 0.0
                var levelCount = FIRST_LEVEL_COUNT
                var sep = 90.0 / (levelCount - 1)
                var radiusMultiplier = 1
                var currentCount = 0

                for (itemIndex in 0 until items.size) {
                    if (currentCount == levelCount) {
                        levelCount += 2
                        radiusMultiplier++
                        currentCount = 0
                        sep = 90.0 / (levelCount - 1)
                        currentDeg = 0.0
                    }
                    val targetX = buttonStartPosX + ((buttonSpreadRadius * radiusMultiplier) * Math.cos(Math.toRadians(currentDeg))).toFloat()
                    val targetY = buttonStartPosY + ((buttonSpreadRadius * radiusMultiplier) * Math.sin(Math.toRadians(currentDeg))).toFloat()

                    val xAnimator = ValueAnimator.ofFloat(buttonStartPosX, targetX)
                    xAnimator.duration = ANIMATION_DURATION
                    xAnimator.interpolator = OvershootInterpolator()
                    xAnimator.addUpdateListener {
                        items[itemIndex].x = it.animatedValue as Float
                        invalidate()
                    }
                    xAnimator.start()

                    val yAnimator = ValueAnimator.ofFloat(buttonStartPosY, targetY)
                    yAnimator.duration = ANIMATION_DURATION
                    yAnimator.interpolator = OvershootInterpolator()
                    yAnimator.addUpdateListener {
                        items[itemIndex].y = it.animatedValue as Float
                        invalidate()
                    }
                    yAnimator.start()

                    currentDeg += sep
                    currentCount++
                }
            }
        })
        bgAnimator.start()
    }

    private fun animateCloseMenu() {
        bringToFront()

        for (itemIndex in 0 until items.size) {
            val xAnimator = ValueAnimator.ofFloat(items[itemIndex].x, buttonStartPosX)
            xAnimator.duration = ANIMATION_DURATION
            xAnimator.interpolator = AnticipateInterpolator()
            xAnimator.addUpdateListener {
                items[itemIndex].x = it.animatedValue as Float
                invalidate()
            }
            xAnimator.start()

            val yAnimator = ValueAnimator.ofFloat(items[itemIndex].y, buttonStartPosY)
            yAnimator.duration = ANIMATION_DURATION
            yAnimator.interpolator = AnticipateInterpolator()
            yAnimator.addUpdateListener {
                items[itemIndex].y = it.animatedValue as Float
                invalidate()
            }
            yAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    shouldShowAllIcons = false
                    textPaintLine.alpha = 0

                    val bgAnimator = ValueAnimator.ofFloat(backgroundSpreadRadius, 0f)
                    bgAnimator.duration = 200
                    bgAnimator.interpolator = AccelerateDecelerateInterpolator()
                    bgAnimator.addUpdateListener {
                        cornerBackgroundRadius = it.animatedValue as Float
                        invalidate()
                    }

                    bgAnimator.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            super.onAnimationEnd(animation)
                            val currentTuningTextAnimator = ValueAnimator.ofInt(0, 255)
                            currentTuningTextAnimator.duration = 200
                            currentTuningTextAnimator.interpolator = AccelerateDecelerateInterpolator()
                            currentTuningTextAnimator.addUpdateListener {
                                currentTuningPaint.alpha = it.animatedValue as Int
                                invalidate()
                            }
                            currentTuningTextAnimator.start()
                        }
                    })

                    bgAnimator.start()
                }
            })
            yAnimator.start()
        }
    }

    private fun animateItemSelected() {
        bringToFront()

        val bgAnimator = ValueAnimator.ofFloat(backgroundSpreadRadius, 0f)
        bgAnimator.duration = 200
        bgAnimator.interpolator = AccelerateDecelerateInterpolator()
        bgAnimator.addUpdateListener {
            cornerBackgroundRadius = it.animatedValue as Float
            invalidate()
        }
        bgAnimator.start()

        val xAnimator = ValueAnimator.ofFloat(items[selectedIndex].x, width / 2f)
        xAnimator.duration = ANIMATION_DURATION
        xAnimator.interpolator = AccelerateDecelerateInterpolator()
        xAnimator.addUpdateListener {
            items[selectedIndex].x = it.animatedValue as Float
            invalidate()
        }
        xAnimator.start()

        val yAnimator = ValueAnimator.ofFloat(items[selectedIndex].y, buttonStartPosY)
        yAnimator.duration = ANIMATION_DURATION
        yAnimator.interpolator = AccelerateDecelerateInterpolator()
        yAnimator.addUpdateListener {
            items[selectedIndex].y = it.animatedValue as Float
            invalidate()
        }
        yAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                shouldShowAllIcons = false
                textPaintLine.alpha = 0

                showTunings = true

                val titleTextAnimator = ValueAnimator.ofInt(0, 255)
                titleTextAnimator.duration = 500
                titleTextAnimator.interpolator = AccelerateDecelerateInterpolator()
                titleTextAnimator.addUpdateListener {
                    titleTextPaintLine.alpha = it.animatedValue as Int
                    invalidate()
                }
                titleTextAnimator.start()

                val topBgAnimator = ValueAnimator.ofFloat(0f, titleSpacingFromTop + tuningDisplays.size * tuningItemSpacing)
                topBgAnimator.duration = 200
                topBgAnimator.interpolator = AccelerateDecelerateInterpolator()
                topBgAnimator.addUpdateListener {
                    topBackgroundRadius = it.animatedValue as Float
                    invalidate()
                }
                topBgAnimator.start()

                val expanderLeftAnimator = ValueAnimator.ofFloat(width / 2f, width / 2f - expanderDeviation)
                expanderLeftAnimator.duration = 200
                expanderLeftAnimator.interpolator = AccelerateDecelerateInterpolator()
                expanderLeftAnimator.addUpdateListener {
                    expanderItemLeft = it.animatedValue as Float
                    invalidate()
                }
                expanderLeftAnimator.start()

                val expanderRightAnimator = ValueAnimator.ofFloat(width / 2f, width / 2f + expanderDeviation)
                expanderRightAnimator.duration = 200
                expanderRightAnimator.interpolator = AccelerateDecelerateInterpolator()
                expanderRightAnimator.addUpdateListener {
                    expanderItemRight = it.animatedValue as Float
                    invalidate()
                }
                expanderRightAnimator.start()
            }
        })
        yAnimator.start()
    }

    private fun animateCloseItemSelected() {
        bringToFront()

        for (itemIndex in 0 until items.size) {

            val titleTextAnimator = ValueAnimator.ofInt(255, 0)
            titleTextAnimator.duration = 200
            titleTextAnimator.interpolator = AccelerateDecelerateInterpolator()
            titleTextAnimator.addUpdateListener {
                titleTextPaintLine.alpha = it.animatedValue as Int
                invalidate()
            }
            titleTextAnimator.start()

            val titleAnimatorLeft = ValueAnimator.ofFloat(expanderItemLeft, width / 2f)
            titleAnimatorLeft.duration = ANIMATION_DURATION
            titleAnimatorLeft.interpolator = AnticipateInterpolator()
            titleAnimatorLeft.addUpdateListener {
                expanderItemLeft = it.animatedValue as Float
                invalidate()
            }
            titleAnimatorLeft.start()

            val titleAnimatorRight = ValueAnimator.ofFloat(expanderItemRight, width / 2f)
            titleAnimatorRight.duration = ANIMATION_DURATION
            titleAnimatorRight.interpolator = AnticipateInterpolator()
            titleAnimatorRight.addUpdateListener {
                expanderItemRight = it.animatedValue as Float
                invalidate()
            }

            titleAnimatorRight.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)

                    showTunings = false

                    val xAnimator = ValueAnimator.ofFloat(items[itemIndex].x, buttonStartPosX)
                    xAnimator.duration = ANIMATION_DURATION
                    xAnimator.interpolator = AnticipateInterpolator()
                    xAnimator.addUpdateListener {
                        items[itemIndex].x = it.animatedValue as Float
                        invalidate()
                    }
                    xAnimator.start()

                    val yAnimator = ValueAnimator.ofFloat(items[itemIndex].y, buttonStartPosY)
                    yAnimator.duration = ANIMATION_DURATION
                    yAnimator.interpolator = AnticipateInterpolator()
                    yAnimator.addUpdateListener {
                        items[itemIndex].y = it.animatedValue as Float
                        invalidate()
                    }
                    yAnimator.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            super.onAnimationEnd(animation)
                            shouldShowAllIcons = false
                            textPaintLine.alpha = 0

                            val topBgAnimator = ValueAnimator.ofFloat(titleSpacingFromTop + tuningDisplays.size * tuningItemSpacing, 0f)
                            topBgAnimator.duration = 200
                            topBgAnimator.interpolator = AccelerateDecelerateInterpolator()
                            topBgAnimator.addUpdateListener {
                                topBackgroundRadius = it.animatedValue as Float
                                invalidate()
                            }

                            topBgAnimator.addListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator?) {
                                    super.onAnimationEnd(animation)
                                    val currentTuningTextAnimator = ValueAnimator.ofInt(0, 255)
                                    currentTuningTextAnimator.duration = 200
                                    currentTuningTextAnimator.interpolator = AccelerateDecelerateInterpolator()
                                    currentTuningTextAnimator.addUpdateListener {
                                        currentTuningPaint.alpha = it.animatedValue as Int
                                        invalidate()
                                    }
                                    currentTuningTextAnimator.start()
                                }
                            })

                            topBgAnimator.start()
                        }
                    })
                    yAnimator.start()
                }
            })
            titleAnimatorRight.start()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawCircle(0f, 0f, cornerBackgroundRadius, backgroundPaint)
        canvas?.drawCircle(width / 2f, 0f, topBackgroundRadius, backgroundPaint)

        canvas?.drawText(selectedTuning, buttonStartPosX + (buttonRadius * 1.5f), buttonStartPosY + buttonRadius / 2, currentTuningPaint)

        for (itemIndex in 0 until items.size) {
            if (itemIndex == selectedIndex) {
                canvas?.drawCircle(items[itemIndex].x, items[itemIndex].y, buttonRadius, selectedCirclePaint)

                items[itemIndex].image.setBounds(items[itemIndex].x.toInt() - vectorIconBoundVariation,
                    items[itemIndex].y.toInt() - vectorIconBoundVariation,
                    items[itemIndex].x.toInt() + vectorIconBoundVariation,
                    items[itemIndex].y.toInt() + vectorIconBoundVariation)

                items[itemIndex].image.draw(canvas)

                canvas?.drawText(items[itemIndex].simpleName, items[itemIndex].x, items[itemIndex].y + buttonCaptionSpacing, textPaintLine)
            } else if (shouldShowAllIcons) {

                items[itemIndex].image.setBounds(items[itemIndex].x.toInt() - vectorIconBoundVariation,
                    items[itemIndex].y.toInt() - vectorIconBoundVariation,
                    items[itemIndex].x.toInt() + vectorIconBoundVariation,
                    items[itemIndex].y.toInt() + vectorIconBoundVariation)

                items[itemIndex].image.draw(canvas)

                canvas?.drawText(items[itemIndex].simpleName, items[itemIndex].x, items[itemIndex].y + buttonCaptionSpacing, textPaintLine)
            }
        }

        if (showTunings) {
            canvas?.drawCircle(expanderItemLeft, buttonStartPosY, buttonRadius, selectedCirclePaint)
            canvas?.drawCircle(expanderItemRight, buttonStartPosY, buttonRadius, selectedCirclePaint)
            canvas?.drawRect(expanderItemLeft, buttonStartPosY - buttonRadius, expanderItemRight, buttonStartPosY + buttonRadius, selectedCirclePaint)
            canvas?.drawText(items[selectedIndex].simpleName, width / 2f, buttonStartPosY + (titleTextPaintLine.textSize / 3), titleTextPaintLine)

            for (i in 0 until tuningDisplays.size) {
                canvas?.drawText(tuningDisplays[i], width / 2f, titleSpacingFromTop + (tuningItemSpacing * i), titleTextPaintLine)
            }

            items[selectedIndex].image.setBounds(expanderItemLeft.toInt() - vectorIconBoundVariation,
                buttonStartPosY.toInt() - vectorIconBoundVariation,
                expanderItemLeft.toInt() + vectorIconBoundVariation,
                buttonStartPosY.toInt() + vectorIconBoundVariation)

            items[selectedIndex].image.draw(canvas)
        }

        super.onDraw(canvas)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            return true
        }
        if (event?.action == MotionEvent.ACTION_UP) {
            when (menuState) {
                MenuState.DEFAULT -> if (indexOfButtonPressed(event) != -1) {
                    animateOpenMenu()
                    menuState = MenuState.OPEN
                }
                MenuState.OPEN -> {
                    val click = indexOfButtonPressed(event)
                    if (click != -1) {
                        selectedIndex = click
                        shouldShowAllIcons = false
                        animateItemSelected()
                        menuState = MenuState.INSTRUMENT_SELECTED

                        tuningDisplays.clear()
                        for (tuning in Instrument.valueOf(items[selectedIndex].name).availableTunings) {
                            tuningDisplays.add(tuning.name)
                        }

                        // mListener?.onTuningSelected(Instrument.valueOf(items[selectedIndex].name).availableTunings[0])
                    } else {
                        animateCloseMenu()
                        menuState = MenuState.DEFAULT
                    }
                }
                MenuState.INSTRUMENT_SELECTED -> {
                    val click = indexOfTuningSelected(event)
                    if (click != -1) {
                        animateCloseItemSelected()
                        mListener?.onTuningSelected(Instrument.valueOf(items[selectedIndex].name).availableTunings[click])
                        selectedTuning = tuningDisplays[click]
                        menuState = MenuState.DEFAULT
                    } else {
                        animateCloseItemSelected()
                        mListener?.onTuningSelected(Instrument.valueOf(items[selectedIndex].name).availableTunings[0])
                        selectedTuning = tuningDisplays[0]
                        menuState = MenuState.DEFAULT
                    }
                }
            }
            invalidate()
            return true
        }
        return super.onTouchEvent(event)
    }

    private fun indexOfTuningSelected(event: MotionEvent): Int {

        for (i in 0 until tuningDisplays.size) {
            if (event.x > width / 2f - expanderDeviation && event.x < width / 2f + expanderDeviation &&
                event.y > titleSpacingFromTop + (tuningItemSpacing * i) - 30f && event.y < titleSpacingFromTop + (tuningItemSpacing * i) + 30f) {
                return i
            }
        }

        return -1
    }

    private fun indexOfButtonPressed(event: MotionEvent): Int {
        for (i in 0 until items.size) {
            if (event.x > items[i].x - buttonRadius && event.x < items[i].x + buttonRadius &&
                event.y > items[i].y - buttonRadius && event.y < items[i].y + buttonRadius) {
                return i
            }
        }

        return -1
    }
}

data class ButtonItem(
    var x: Float,
    var y: Float,
    var simpleName: String,
    var name: String,
    var image: Drawable
)

enum class MenuState {
    DEFAULT, OPEN, INSTRUMENT_SELECTED
}