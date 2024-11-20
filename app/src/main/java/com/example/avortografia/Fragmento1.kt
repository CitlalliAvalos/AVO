package com.example.avortografia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragmento1.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragmento1 : Fragment() {
    companion object {
        private const val ARG_OBJECT = "object"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragmento1, container, false)
    }

   // override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
   //     super.onViewCreated(view, savedInstanceState)
   //     arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
   //         val textView: TextView = view.findViewById(R.id.textView)
   //         textView.text = "Estoy en el Fragmento "+getInt(ARG_OBJECT).toString()
   //     }
   // }

}