package com.sidbola.ntune.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.sidbola.ntune.R
import com.sidbola.ntune.ui.controller.AutoTunerController

const val RECORD_AUDIO_PERMISSION_REQUEST_CODE = 1384

class MainActivity : AppCompatActivity() {

    private lateinit var router: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        router = Conductor.attachRouter(this, main_controller_container, savedInstanceState)
        if (!router.hasRootController()) router.setRoot(RouterTransaction.with(AutoTunerController()))
    }

    override fun onBackPressed() {
        if (!router.handleBack()) super.onBackPressed()
    }
}
