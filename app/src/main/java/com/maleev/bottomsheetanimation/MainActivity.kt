package com.maleev.bottomsheetanimation

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val isSharedElementTransitionSupported =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.button).setOnClickListener {
            DemoBottomSheetDialogFragment().show(supportFragmentManager, "tag")
        }
        findViewById<View>(R.id.sdk_version_hint).visibility =
            if (isSharedElementTransitionSupported) View.GONE else View.VISIBLE
    }
}