package com.sidbola.ntune.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.sidbola.ntune.R
import kotlin.math.roundToInt

class PitchDeviation(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private val centDeviationTextView: TextView
    private val sensedHertzTextView: TextView

    init {
        inflate(getContext(), R.layout.view_pitch_deviation, this)
        centDeviationTextView = findViewById(R.id.tv_cent_deviation)
        sensedHertzTextView = findViewById(R.id.tv_sensed_hertz)

        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER
    }

    fun updateInfo(sensedHertz: Float, targetHertz: Float) {
        if (sensedHertz != -1f) {
            val cents =
                (1200 * 3.322038403 * Math.log10((sensedHertz / targetHertz).toDouble())).roundToInt()
                    .toString()
            sensedHertzTextView.text = sensedHertz.roundToInt().toString() + "Hz"
            centDeviationTextView.text = "$cents ct"
        } else {
            centDeviationTextView.text = ("0 ct")
            sensedHertzTextView.text = context.getString(R.string.defaultHertz)
        }
    }
}