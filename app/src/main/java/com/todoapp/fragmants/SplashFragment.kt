package com.todoapp.fragmants

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.todoapp.R

class SplashFragment : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()

        Handler(Looper.myLooper()!!).postDelayed({
            checkUser()
        }, 2000)

    }

    private fun checkUser() {
        if (firebaseAuth.currentUser != null) {
            Navigation.findNavController(requireView()).navigate(R.id.action_splashFragment_to_homeFragment)
        } else {
            Navigation.findNavController(requireView()).navigate(R.id.action_splashFragment_to_signInFragment)
        }
    }

}