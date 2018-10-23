package com.sidbola.ntune.ui.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.widget.LinearLayout
import com.sidbola.ntune.R
import com.sidbola.ntune.data.Instrument
import com.sidbola.ntune.data.Tuning
import kotlin.math.abs

class TunerDisplay(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {
    private var tuning: Tuning
    private val notesDisplay: NotesDisplay
    private val pitchDisplay: PitchDisplay
    private val pitchDeviation: PitchDeviation
    private var frequency: Float

    init {
        inflate(getContext(), R.layout.view_tuner, this)

        tuning = Instrument.GUITAR.availableTunings[0]
        frequency = 0f
        notesDisplay = findViewById(R.id.nd_current_notes)
        pitchDisplay = findViewById(R.id.pd_current_pitch)
        pitchDeviation = findViewById(R.id.pdv_pitch_deviation)

        notesDisplay.setTuningNoteInfo(tuning)

        val params0 = LinearLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )

        layoutParams = params0

        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER

        val params1 = LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT,
            0,
            1.5f
        )

        val params2 = LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT,
            0,
            1f
        )

        val params3 = LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT,
            0,
            2f
        )


        notesDisplay.layoutParams = params1
        pitchDisplay.layoutParams = params2
        pitchDeviation.layoutParams = params3

    }

    fun initialize(){

    }


    fun updateFrequency(freq: Float){
        frequency = freq

        val closestNoteIndex = getClosestNote()

        pitchDeviation.updateInfo(freq, tuning.notes[closestNoteIndex].frequency)
        pitchDisplay.updateDisplay(freq, tuning.notes[closestNoteIndex].frequency)
        if (freq != -1f) notesDisplay.setActiveNote(closestNoteIndex - 1)
        else notesDisplay.deselectButtons(null)

        invalidate()
        requestLayout()
    }

    fun updateTuning(newTuning: Tuning){
        tuning = newTuning

        notesDisplay.setTuningNoteInfo(tuning)


        invalidate()
        requestLayout()
    }


    private fun getClosestNote(): Int {
        var closest = 8000f
        var targetIndex = 0
        val notes = tuning.notes
        for (i in 0 until notes.size){
            if (abs(notes[i].frequency - frequency) < closest){
                closest = abs(notes[i].frequency - frequency)
                targetIndex = i
            }
        }

        return targetIndex
    }

}





