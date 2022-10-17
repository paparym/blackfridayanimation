package com.practice.blackfridayanimation

import android.os.Bundle
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import com.bluelinelabs.conductor.Conductor.attachRouter
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.Router.PopRootControllerMode
import com.bluelinelabs.conductor.RouterTransaction
import com.practice.blackfridayanimation.pager.AchievementTicketsPagerController

class MainActivity : ComponentActivity() {
    private var router: Router? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val container = findViewById<FrameLayout>(R.id.conductorContainer)
        router = attachRouter(this, container, savedInstanceState)
            .setPopRootControllerMode(PopRootControllerMode.NEVER)
        if (!router!!.hasRootController()) {
            router!!.setRoot(RouterTransaction.with(AchievementTicketsPagerController()))
        }
    }

    override fun onBackPressed() {
        if (!router!!.handleBack()) {
            super.onBackPressed()
        }
    }
}
