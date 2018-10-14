package com.sidbola.ntune.ui.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.util.AttributeSet
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.Button

class CircleButton(context: Context, attrs: AttributeSet?): Button(context, attrs) {

    private val circlePaint = Paint()
    private var circleAlpha = 0
    private var isHighlighted = false

    init {

        setBackgroundColor(Color.TRANSPARENT)

        setTextColor(Color.parseColor("#ffffff"))

        circlePaint.isAntiAlias = true
    }

    override fun onDraw(canvas: Canvas?) {
        circlePaint.color = Color.parseColor("#25b86b")
        circlePaint.alpha = circleAlpha
        canvas?.drawCircle(width/2f, height/2f, height/2f, circlePaint)

        super.onDraw(canvas)
    }

    constructor(context: Context): this(context, null)

    fun setHighlighted(isHighlighted: Boolean){
        this.isHighlighted = isHighlighted

        if (isHighlighted){
            animateUp()
        } else {
            animateDown()
        }
    }



    private fun animateUp() {
        val animator = ValueAnimator.ofInt(circleAlpha,255)
        animator.duration = 300

        animator.addUpdateListener {
            this.circleAlpha = it.animatedValue as Int
            invalidate()
        }
        animator.start()
    }

    private fun animateDown() {
        val animator = ValueAnimator.ofInt(circleAlpha,0)
        animator.duration = 300

        animator.addUpdateListener {
            this.circleAlpha = it.animatedValue as Int
            invalidate()
        }
        animator.start()
    }


}