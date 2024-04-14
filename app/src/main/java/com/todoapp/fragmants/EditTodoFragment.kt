package com.todoapp.fragmants

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.todoapp.databinding.FragmentEditTodoBinding

import com.todoapp.utilz.TodoData


class EditTodoFragment : DialogFragment() {



    private lateinit var editTodoClickListener: EditTodoClickListener
    private lateinit var binding: FragmentEditTodoBinding
    var todoData: TodoData? = null

    fun setEditTodoClickListener(editTodoClickListener: EditTodoClickListener) {
        this.editTodoClickListener = editTodoClickListener
    }


    companion object {

        // TODO 3: Create a newInstance function that takes a TodoData object as a parameter

        @JvmStatic
        fun newInstance(todoData: TodoData) = EditTodoFragment().apply {
            this.todoData = todoData
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        editTodo()
    }

    private fun editTodo() {


        binding.editButtonPopUp.setOnClickListener {

            val title = binding.editTodoName.text.toString()


            if (todoData != null) {
                editTodoClickListener.onEditTodo(todoData!!, title)
                dismiss()
            }


        }
    }



    interface EditTodoClickListener {
        fun onEditTodo(item : TodoData,name : String)
    }

}