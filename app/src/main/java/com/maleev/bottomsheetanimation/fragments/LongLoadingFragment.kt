package com.maleev.bottomsheetanimation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.maleev.bottomsheetanimation.R
import com.maleev.bottomsheetanimation.withDemoBottomSheet
import java.util.concurrent.TimeUnit

class LongLoadingFragment : Fragment(R.layout.fragment_long_loading) {
    companion object {
        fun newInstance() = LongLoadingFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.back_button)
            .setOnClickListener { withDemoBottomSheet { goBack() } }

        postponeEnterTransition()

        view.postDelayed({
            view.findViewById<View>(R.id.text).visibility = View.VISIBLE
            startPostponedEnterTransition()
        }, TimeUnit.SECONDS.toMillis(3))
    }
}