package com.sidbola.ntune.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.sidbola.ntune.R
import com.sidbola.ntune.data.Note
import com.sidbola.ntune.data.Tuning

class NotesDisplay(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private var tuningNoteInfo: Array<Note>?
    private var buttons: Array<CircleButton?>?
    private var buttonLayoutParams: LayoutParams
    private val buttonLinearLayout: LinearLayout
    private val hertzTextView: TextView

    init {
        inflate(getContext(), R.layout.view_notes_display, this)
        tuningNoteInfo = null
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

    private fun setupView() {
        buttons = arrayOfNulls(tuningNoteInfo!!.size)

        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER
        weightSum = buttons!!.size.toFloat()

        for (i in 0 until tuningNoteInfo!!.size) {
            val button = CircleButton(context)
            button.text = tuningNoteInfo!![i].noteName
            button.layoutParams = buttonLayoutParams
            buttonLinearLayout.addView(button)
            buttons!![i] = button
        }
    }

    fun setTuningNoteInfo(tuning: Tuning) {
        if (buttonLinearLayout.childCount > 0) {
            buttonLinearLayout.removeAllViews()
        }
        tuningNoteInfo = tuning.notes
        setupView()
    }

    fun deselectButtons(exception: CircleButton?) {
        if (exception != null) {
            for (button in buttons!!) {
                if (button != exception) button?.setHighlighted(false)
            }
        } else {
            for (button in buttons!!) {
                button?.setHighlighted(false)
            }
        }

        hertzTextView.text = "0 Hz"
    }

    private fun selectNote(noteIndex: Int) {
        if (noteIndex != -1) {
            deselectButtons(buttons!![noteIndex]!!)
            buttons!![noteIndex]?.setHighlighted(true)
            hertzTextView.text = tuningNoteInfo!![noteIndex].frequency.toString() + " Hz"
        }
    }

    fun setActiveNote(noteIndex: Int) {
        selectNote(noteIndex)
    }
}