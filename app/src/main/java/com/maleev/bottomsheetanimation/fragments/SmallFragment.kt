package com.maleev.bottomsheetanimation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.maleev.bottomsheetanimation.R
import com.maleev.bottomsheetanimation.withDemoBottomSheet

class SmallFragment : Fragment(R.layout.fragment_small) {
    companion object {
        fun newInstance() = SmallFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.back_button)
            .setOnClickListener { withDemoBottomSheet { goBack() } }
    }
}