package com.sidbola.ntune.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import com.sidbola.ntune.model.Instrument
import com.sidbola.ntune.model.NotePitchInfo
import android.widget.LinearLayout
import android.widget.TextView
import com.sidbola.ntune.R

class NotesDisplay(context: Context, attrs: AttributeSet): LinearLayout(context, attrs) {
    private var instrumentNoteInfo: Array<NotePitchInfo>?
    private var buttons: Array<CircleButton?>?
    private var buttonLayoutParams: LayoutParams
    private val buttonLinearLayout: LinearLayout
    private val hertzTextView: TextView

    init {
        inflate(getContext(), R.layout.view_notes_display, this)
        instrumentNoteInfo = null
        buttonLinearLayout = findViewById(R.id.ll_button_holder)
        hertzTextView = findViewById(R.id.tv_hertz)
        hertzTextView.text = "0 Hz"
        buttons = null
        buttonLayoutParams = LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT,
            1f
        )
    }

    private fun setupView(){
        buttons = arrayOfNulls(instrumentNoteInfo!!.size)

        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER
        weightSum = buttons!!.size.toFloat()

        for (i in 0 until instrumentNoteInfo!!.size){
            val button = CircleButton(context)
            button.text = instrumentNoteInfo!![i].note
            button.layoutParams = buttonLayoutParams
            buttonLinearLayout.addView(button)
            buttons!![i] = button
        }
    }

    fun setInstrumentNoteInfo(instrument: Instrument){
        instrumentNoteInfo = instrument.getNotePitchFrequencies()
        setupView()
    }

    fun deselectButtons(exception: CircleButton?){
        if (exception != null){
            for (button in buttons!!){
                if (button != exception) button?.setHighlighted(false)
            }
        } else {
            for (button in buttons!!){
                button?.setHighlighted(false)
            }
        }

        hertzTextView.text = "0 Hz"
    }

    private fun selectNote(noteIndex: Int){
        deselectButtons(buttons!![noteIndex]!!)
        buttons!![noteIndex]?.setHighlighted(true)
        hertzTextView.text = instrumentNoteInfo!![noteIndex].freq.toString() + " Hz"
    }


    fun setActiveNote(noteIndex: Int){
        selectNote(noteIndex)
    }



}