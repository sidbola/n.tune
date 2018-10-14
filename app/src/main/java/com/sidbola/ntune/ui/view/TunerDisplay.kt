package com.sidbola.ntune.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import com.sidbola.ntune.R
import com.sidbola.ntune.model.Instrument
import com.sidbola.ntune.model.NotePitchInfo
import kotlin.math.abs
import kotlin.math.roundToInt

class TunerDisplay(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {
    private var instrument: Instrument
    private val notesDisplay: NotesDisplay
    private val pitchDisplay: PitchDisplay
    private val pitchDeviation: PitchDeviation
    private var frequency: Float

    init {
        inflate(getContext(), R.layout.view_tuner, this)

        instrument = Instrument.GUITAR
        frequency = 0f
        notesDisplay = findViewById(R.id.nd_current_notes)
        pitchDisplay = findViewById(R.id.pd_current_pitch)
        pitchDeviation = findViewById(R.id.pdv_pitch_deviation)

        notesDisplay.setInstrumentNoteInfo(instrument)

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


    fun updateFrequency(freq: Float){
        val closestNote = getClosestNote()

        frequency = freq
        pitchDeviation.updateInfo(freq, closestNote.freq)
        pitchDisplay.updateDisplay(freq, closestNote.freq)
        if (freq != -1f) notesDisplay.setActiveNote(closestNote.index - 1)
        else notesDisplay.deselectButtons(null)

        invalidate()
        requestLayout()
    }


    private fun getClosestNote(): NotePitchInfo{
        var closest = Float.MAX_VALUE
        var targetIndex = 0
        val notes = instrument.getNotePitchFrequencies()
        for (i in 0 until notes.size){
            if (abs(notes[i].freq - frequency) < closest){
                closest = abs(notes[i].freq - frequency)
                targetIndex = i
            }
        }

        return notes[targetIndex]
    }

}





