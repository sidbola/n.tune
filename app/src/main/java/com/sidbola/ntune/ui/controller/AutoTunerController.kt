package com.sidbola.ntune.ui.controller

import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import com.bluelinelabs.conductor.Controller
import com.sidbola.ntune.R
import com.sidbola.ntune.data.Tuning
import com.sidbola.ntune.ui.RECORD_AUDIO_PERMISSION_REQUEST_CODE
import com.sidbola.ntune.ui.view.AnimatedMenu
import kotlinx.android.synthetic.main.controller_auto_tuner.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AutoTunerController : Controller() {

    private lateinit var dispatcher: AudioDispatcher
    private lateinit var pitchDetectionHandler: PitchDetectionHandler
    private lateinit var pitchProcessor: PitchProcessor
    private var pitch = 0.0f
    private var delay = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.controller_auto_tuner, container, false)

        if (hasPermissionToRecord()) {
            initializeTuner()
        } else {
            ActivityCompat.requestPermissions(
                activity!!,
                arrayOf(android.Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_PERMISSION_REQUEST_CODE
            )
        }

        view.instrument_menu.setTuningSelectedListener(object : AnimatedMenu.OnTuningSelected {
            override fun onTuningSelected(tuning: Tuning) {
                view.td_main_tuner.updateTuning(tuning)
            }
        })

        return view
    }

    private fun hasPermissionToRecord(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity!!,
            android.Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun initializeTuner() {
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0)

        pitchDetectionHandler = PitchDetectionHandler { pitchDetectionResult, _ ->
            this.pitch = pitchDetectionResult.pitch
            delay++
            if (delay > 10) {
                delay = 0
                activity?.runOnUiThread {
                    view?.td_main_tuner?.updateFrequency(pitch)
                }
            }
        }

        pitchProcessor = PitchProcessor(
            PitchProcessor.PitchEstimationAlgorithm.FFT_YIN,
            22050f,
            1024,
            pitchDetectionHandler
        )
        dispatcher.addAudioProcessor(pitchProcessor)

        GlobalScope.launch {
            dispatcher.run()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            RECORD_AUDIO_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    initializeTuner()
                } else {
                    Toast.makeText(
                        applicationContext,
                        "TunerDisplay needs microphone to function",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dispatcher.stop()
    }
}