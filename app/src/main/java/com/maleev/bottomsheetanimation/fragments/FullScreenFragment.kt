package com.maleev.bottomsheetanimation.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.maleev.bottomsheetanimation.R
import com.maleev.bottomsheetanimation.withDemoBottomSheet

class FullScreenFragment : Fragment(R.layout.fragment_full_screen) {
    companion object {
        fun newInstance() = FullScreenFragment()

        private const val LOREM_IPSUM =
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Toolbar>(R.id.toolbar)
            .setNavigationOnClickListener { withDemoBottomSheet { goBack() } }

        view.findViewById<RecyclerView>(R.id.recycler)
            .apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter =
                    LoremIpsumAdapter(
                        LOREM_IPSUM.split(
                            " "
                        ),
                        requireContext()
                    )
                addItemDecoration(
                    DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
                )
            }
    }
}

class LoremIpsumAdapter(private val items: List<String>, context: Context) :
    RecyclerView.Adapter<LoremIpsumViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoremIpsumViewHolder =
        layoutInflater
            .inflate(R.layout.layout_lorem_ipsum_item, parent, false)
            .let {
                LoremIpsumViewHolder(
                    it
                )
            }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: LoremIpsumViewHolder, position: Int) {
        holder.setText(items[position])
    }
}

class LoremIpsumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val textView: TextView = itemView.findViewById(R.id.text)

    fun setText(text: String) {
        textView.text = text
    }

}