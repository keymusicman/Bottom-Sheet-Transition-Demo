package com.maleev.bottomsheetanimation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.maleev.bottomsheetanimation.R
import com.maleev.bottomsheetanimation.withDemoBottomSheet

class HighFragment : Fragment(R.layout.fragment_high) {
    companion object {
        fun newInstance() = HighFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.back_button)
            .setOnClickListener { withDemoBottomSheet { goBack() } }
    }
}