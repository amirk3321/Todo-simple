package com.todoapp.fragmants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.todoapp.R
import com.todoapp.databinding.FragmentAddTodoBinding


class AddTodoFragment : DialogFragment() {


    private lateinit var  binding: FragmentAddTodoBinding
    private lateinit var addTodoListener: AddTodoListener


    fun setAddTodoListener(addTodoListener: AddTodoListener) {
        this.addTodoListener = addTodoListener
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        addTodo()


        binding.closeButtonPopUp.setOnClickListener {
            dismiss()
        }
    }

    private fun addTodo() {
        binding.addButtonPopUp.setOnClickListener {
            val title = binding.namepopUp.text.toString()


            if (title.isNotEmpty()) {
                addTodoListener.onAddTodo(title)
                dismiss()
            }
        }
    }


    interface AddTodoListener {
        fun onAddTodo(name : String)
    }

}