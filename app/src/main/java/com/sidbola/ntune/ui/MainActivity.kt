package com.sidbola.ntune.ui

import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.WindowManager
import android.widget.Toast
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import kotlinx.android.synthetic.main.activity_main.*
import be.tarsos.dsp.pitch.PitchProcessor.PitchEstimationAlgorithm
import com.sidbola.ntune.R
import com.sidbola.ntune.data.Tuning
import com.sidbola.ntune.ui.view.AnimatedMenu
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch

const val RECORD_AUDIO_PERMISSION_REQUEST_CODE = 1384

class MainActivity : AppCompatActivity() {

    private lateinit var dispatcher: AudioDispatcher
    private lateinit var pitchDetectionHandler: PitchDetectionHandler
    private lateinit var pitchProcessor: PitchProcessor
    private var pitch = 0.0f
    private var delay = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        if (hasPermissionToRecord()) {
            initializeTuner()
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_PERMISSION_REQUEST_CODE
            )
        }

        instrument_menu.setTuningSelectedListener(object : AnimatedMenu.OnTuningSelected {
            override fun onTuningSelected(tuning: Tuning) {
                td_main_tuner.updateTuning(tuning)
            }
        })
    }

    private fun hasPermissionToRecord(): Boolean {
        return ContextCompat.checkSelfPermission(this,
            android.Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    }

    private fun initializeTuner() {
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0)

        pitchDetectionHandler = PitchDetectionHandler { pitchDetectionResult, _ ->
            this.pitch = pitchDetectionResult.pitch
            delay++
            if (delay > 10) {
                delay = 0
                runOnUiThread {
                    td_main_tuner.updateFrequency(pitch)
                }
            }
        }

        pitchProcessor = PitchProcessor(PitchEstimationAlgorithm.FFT_YIN, 22050f, 1024, pitchDetectionHandler)
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
                    Toast.makeText(applicationContext, "TunerDisplay needs microphone to function", Toast.LENGTH_LONG).show()
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
