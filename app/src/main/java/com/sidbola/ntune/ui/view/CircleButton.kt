package com.sidbola.ntune.ui.view

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.Button

class CircleButton(context: Context, attrs: AttributeSet?) : Button(context, attrs) {

    private val circlePaint = Paint()
    private var circleAlpha = 0
    private var isHighlighted = false
    private var curTextColor = 0

    init {

        setBackgroundColor(Color.TRANSPARENT)

        curTextColor = Color.BLACK
        setTextColor(curTextColor)

        circlePaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas?) {
        circlePaint.color = Color.parseColor("#00695C")
        circlePaint.alpha = circleAlpha
        canvas?.drawCircle(width / 2f, height / 2f, height / 2f, circlePaint)

        super.onDraw(canvas)
    }

    constructor(context: Context) : this(context, null)

    fun setHighlighted(isHighlighted: Boolean) {
        this.isHighlighted = isHighlighted

        if (isHighlighted) {
            animateUp()
        } else {
            animateDown()
        }
    }

    private fun animateUp() {
        val animator = ValueAnimator.ofInt(circleAlpha, 255)
        animator.duration = 300

        animator.addUpdateListener {
            this.circleAlpha = it.animatedValue as Int
            invalidate()
        }
        animator.start()

        val toColor = Color.WHITE
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), curTextColor, toColor)
        colorAnimation.duration = 300
        colorAnimation.addUpdateListener {
            curTextColor = it.animatedValue as Int
            this.setTextColor(it.animatedValue as Int)
            invalidate()
        }
        colorAnimation.start()
    }

    private fun animateDown() {
        val animator = ValueAnimator.ofInt(circleAlpha, 0)
        animator.duration = 300

        animator.addUpdateListener {
            this.circleAlpha = it.animatedValue as Int
            invalidate()
        }
        animator.start()

        val toColor = Color.BLACK
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), curTextColor, toColor)
        colorAnimation.duration = 300
        colorAnimation.addUpdateListener {
            curTextColor = it.animatedValue as Int
            this.setTextColor(it.animatedValue as Int)
            invalidate()
        }
        colorAnimation.start()
    }
}