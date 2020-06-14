package com.maleev.bottomsheetanimation.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.maleev.bottomsheetanimation.R
import com.maleev.bottomsheetanimation.withDemoBottomSheet
import java.util.concurrent.TimeUnit

class RootFragment : Fragment(R.layout.fragment_root) {
    companion object {
        fun newInstance() = RootFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonShowHighFragment = view.findViewById<View>(R.id.button_show_higher_fragment)
        val buttonShowSmallFragment = view.findViewById<View>(R.id.button_show_smaller_fragment)
        val buttonShowFullScreenFragment =
            view.findViewById<View>(R.id.button_show_full_screen_fragment)
        val buttonShowLongLoadingFragment =
            view.findViewById<View>(R.id.button_show_long_load_fragment)

        buttonShowHighFragment
            .setOnClickListener { withDemoBottomSheet { goToHigherFragment() } }
        buttonShowSmallFragment
            .setOnClickListener { withDemoBottomSheet { goToSmallerFragment() } }
        buttonShowFullScreenFragment
            .setOnClickListener { withDemoBottomSheet { goToFullScreenFragment() } }
        buttonShowLongLoadingFragment
            .setOnClickListener {
                buttonShowHighFragment.isEnabled = false
                buttonShowSmallFragment.isEnabled = false
                buttonShowFullScreenFragment.isEnabled = false
                buttonShowLongLoadingFragment.isEnabled = false

                withDemoBottomSheet { goToLongLoadingFragment() }
                requireView().postDelayed({
                    buttonShowHighFragment.isEnabled = true
                    buttonShowSmallFragment.isEnabled = true
                    buttonShowFullScreenFragment.isEnabled = true
                    buttonShowLongLoadingFragment.isEnabled = true
                }, TimeUnit.SECONDS.toMillis(3))
            }
    }
}