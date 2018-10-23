package com.sidbola.ntune.ui.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.sidbola.ntune.R

class PitchDisplay(context: Context, attrs: AttributeSet): View(context, attrs) {

    private var paintLines = Paint()
    private var currentPaintLine = Paint()
    private var textPaintLine = Paint()
    private var frequency: Float
    private var targetFrequency: Float
    private var currentLineX: Float
    private var currentLineFutureX: Float

    init {
        frequency = -1f
        targetFrequency = -1f
        currentLineX = 0f
        currentLineFutureX = 0f


        paintLines.color = Color.parseColor("#212121")
        paintLines.isAntiAlias = true
        paintLines.style = Paint.Style.STROKE
        paintLines.strokeJoin = Paint.Join.ROUND
        paintLines.strokeCap = Paint.Cap.ROUND
        paintLines.strokeWidth = context.resources.displayMetrics.density * 1.5f

        val font = ResourcesCompat.getFont(context, R.font.raleway_light)
        textPaintLine.typeface = font
        textPaintLine.color = Color.parseColor("#414141")
        textPaintLine.isAntiAlias = true
        textPaintLine.style = Paint.Style.STROKE
        textPaintLine.textAlign = Paint.Align.CENTER
        textPaintLine.textSize = 10f

        currentPaintLine.color = Color.WHITE
        currentPaintLine.isAntiAlias = true
        currentPaintLine.style = Paint.Style.STROKE
        currentPaintLine.strokeJoin = Paint.Join.ROUND
        currentPaintLine.strokeCap = Paint.Cap.ROUND
        currentPaintLine.strokeWidth = context.resources.displayMetrics.density * 1.5f
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val barSeparationAmount = (width/42f)

        // Calculate line heights
        val highLineY = (height/10f) * 2
        val lowLineY = (height/7.5f) * 2
        val allLinesY = (measuredHeight).toFloat()

        // Calculate positions for high lines
        val firstLineX = (barSeparationAmount * 1)
        val middleLineX = (barSeparationAmount * 41)
        val lastLineX = (barSeparationAmount * 21)

        // Paint high lines
        canvas?.drawLine(firstLineX, highLineY, firstLineX, allLinesY, paintLines)
        canvas?.drawLine(middleLineX, highLineY, middleLineX, allLinesY, paintLines)
        canvas?.drawLine(lastLineX, highLineY, lastLineX, allLinesY, paintLines)

        // Paint low lines
        for (i in 2..40) {
            val lineZeroX = (barSeparationAmount * i)
            canvas?.drawLine(lineZeroX, lowLineY, lineZeroX, allLinesY, paintLines)
        }

        // Paint numbering
        textPaintLine.textSize = height/8f
        canvas?.drawText("-20",height/8f, height/8f, textPaintLine)
        canvas?.drawText("0",width/2f, height/8f, textPaintLine)
        canvas?.drawText("+20",width - (height/8f), height/8f, textPaintLine)

        if (frequency != -1f){
            canvas?.drawLine(currentLineX,highLineY,currentLineX,allLinesY,currentPaintLine)
        }
    }

    private fun animateCursor() {
        val animator = ValueAnimator.ofFloat(currentLineX,currentLineFutureX)
        animator.duration = 500
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener {
            this.currentLineX = it.animatedValue as Float
            invalidate()
        }
        animator.start()
    }

    fun updateDisplay (currentFrequency: Float, targetFrequency: Float){
        this.frequency = currentFrequency
        this.targetFrequency = targetFrequency

        val unitLength = width/40
        val bottomEnd = targetFrequency - 20

        val translatedCurFreq = currentFrequency - bottomEnd
        currentLineFutureX = translatedCurFreq * unitLength

        /*
        val ratio = (width/2)/targetFrequency
        currentLineFutureX = currentFrequency * ratio
        */

        animateCursor()

        invalidate()
        requestLayout()
    }

}