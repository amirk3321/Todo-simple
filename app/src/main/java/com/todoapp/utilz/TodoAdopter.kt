package com.todoapp.utilz

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.todoapp.databinding.EachItemCardBinding


class TodoAdopter(private val list : MutableList<TodoData>) : RecyclerView.Adapter<TodoAdopter.TodoViewHolder>() {


    // this is the interface for the click listener
    // it has two functions deleteTodoItem and editTodoItem
    // the functions are called when the delete and edit button is clicked
    // the functions are implemented in the HomeFragment class
    // the functions are called in the onBindViewHolder function
    private var onTodoItemClickListener: OnTodoItemClickListener? = null

    // this function is responsible for setting the click listener
    fun setOnTodoItemClickListener(onTodoItemClickListener: OnTodoItemClickListener) {
        this.onTodoItemClickListener = onTodoItemClickListener
    }

    // its responsible for holding the view of each item in the recycler view
    // it help in recycling the view and avoid creating new view for each item
    inner class TodoViewHolder(val binding : EachItemCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = EachItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TodoViewHolder(binding)
    }


    // this function is responsible for returning the number of items in the list
    // it is called once to get the number of items in the list
    override fun getItemCount(): Int {
        return list.size
    }


    // this function is responsible for binding the data to the view
    // it is called for each item in the list to bind the data to the view
    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {


        // the with function is used to avoid writing holder.binding.todoItemView
        // instead we can use
        // with function change the context of the code block to the object passed in the argument
        with(holder) {
            with(list[position]) {
                // get the name of the item from the list
                binding.todoItemView.text = name


                binding.deleteTodoItemButton.setOnClickListener {
                    // delete item from list and notify adapter for changes in list
                    // notifyItemRemoved(position) will animate the deletion of item from list
                    onTodoItemClickListener!!.deleteTodoItem(this)
                }


                binding.editTodoItemButton.setOnClickListener {
                    Toast.makeText(binding.root.context, "Edit button clicked ${it}", Toast.LENGTH_SHORT).show()
                    // edit item from list and notify adapter for changes in list
                    onTodoItemClickListener!!.editTodoItem(this)
                }

            }
        }
    }

    interface OnTodoItemClickListener {
        fun deleteTodoItem(todoData: TodoData)
        fun editTodoItem(todoData: TodoData)
    }

}