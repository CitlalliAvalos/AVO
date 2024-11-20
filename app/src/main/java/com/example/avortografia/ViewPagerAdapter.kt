package com.example.avortografia

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fa:FragmentActivity): FragmentStateAdapter(fa) {
    //Lógica para que cada fragmento se llene distinto
    companion object {
        private const val ARG_OBJECT = "object"
    }
    override fun getItemCount(): Int= 3 // número de fragments que va a tener el swipe

    override fun createFragment(position: Int): Fragment {
        //Retorna una nueva instancia de fragmento, los fragmentos que se van a abrir con el swip
       // val fragment= Fragmento1()
        // fragment.arguments = Bundle().apply {
            // The object is just an integer.
        //    putInt(ARG_OBJECT, position + 1)
        //}
       // return fragment

        return when (position+1){
            1-> {Fragmento1()}
            2-> {Fragment2()}
            3->{Fragmento3()}
            else->{Fragmento1()}

        }


    }
}