package com.todoapp.fragmants

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.todoapp.R
import com.todoapp.databinding.FragmentSignInBinding
import kotlin.math.log


class SignInFragment : Fragment() {


    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSignInBinding

   override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(inflater,container,false)

       return  binding.root

        //return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        firebaseAuth = FirebaseAuth.getInstance()

        submitSignIn()

        binding.singUpButton.setOnClickListener {
            navController.navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }

    private fun submitSignIn() {

        binding.signInButton.setOnClickListener {

            val email = binding.emailController.text.toString()
            val password = binding.passwordController.text.toString()

            Log.d("SignInFragment", "Email: $email")
            Log.d("SignInFragment", "Password: $password")
            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->

                        print(task)
                        if (task.isSuccessful) {
                            navController.navigate(R.id.action_signInFragment_to_homeFragment)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignInFragment", "signInWithEmail:failure", task.exception)
                            Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }
    }



}