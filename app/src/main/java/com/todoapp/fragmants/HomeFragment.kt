package com.todoapp.fragmants


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.todoapp.R
import com.todoapp.databinding.FragmentHomeBinding
import com.todoapp.utilz.TodoAdopter
import com.todoapp.utilz.TodoData


class HomeFragment : Fragment(), AddTodoFragment.AddTodoListener , TodoAdopter.OnTodoItemClickListener , EditTodoFragment.EditTodoClickListener{


    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomeBinding
    private lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var addTodoFragment: AddTodoFragment
    private var editTodoFragment: EditTodoFragment? = null

    private  lateinit var todoAdopter: TodoAdopter
    private lateinit var todoList: MutableList<TodoData>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        getFirebaseTodoData()

    }


      private fun getFirebaseTodoData() {
            firebaseFirestore.collection("todos").document(firebaseAuth.currentUser!!.uid)
                .collection("userTodos").addSnapshotListener { value, error ->
                    if (error != null) {
                        Toast.makeText(context, "Error in fetching data", Toast.LENGTH_SHORT).show()
                        return@addSnapshotListener
                    }
                    todoList.clear()
                    value?.forEach {

                        val todo = it.data
                        val name = todo["name"] as String
                        val id = todo["id"] as String
                        val completed = todo["completed"] as Boolean
                        val todoData = TodoData(name, completed, id)
                        todoList.add(todoData)
                    }
                    Log.d("HomeFragmentList", "Todo List Size: ${todoList.size}")
                    Log.d("HomeFragmentList", "Todo List: $todoList")
                    todoAdopter.notifyDataSetChanged()
                }
          todoAdopter.notifyDataSetChanged()
        }


    private fun init(view: View) {
        firebaseAuth = FirebaseAuth.getInstance()
        navController = Navigation.findNavController(view)
        firebaseFirestore = FirebaseFirestore.getInstance()


        binding.logout.setOnClickListener {
            firebaseAuth.signOut()
            navController.navigate(R.id.action_homeFragment_to_signInFragment)
        }
        floatingActionEvent()

        // set the recycler view properties
        binding.todoListRecyclerView.setHasFixedSize(true)
        binding.todoListRecyclerView.layoutManager = LinearLayoutManager(context)
        todoList = mutableListOf()
        todoAdopter = TodoAdopter(todoList)
        todoAdopter.setOnTodoItemClickListener(this)
        binding.todoListRecyclerView.adapter = todoAdopter




    }
    private fun floatingActionEvent() {
        binding.fabButton.setOnClickListener {
            // create an instance of the AddTodoFragment
            addTodoFragment = AddTodoFragment()
            // set the listener to the fragment
            addTodoFragment.setAddTodoListener(this)
            // show the fragment
            addTodoFragment.show(childFragmentManager, "Add Todo Fragment")

        }



    }

    // this function is called when the add button is clicked
    override fun onAddTodo(name: String) {


        // create a document reference in the collection userTodos in the document with the user id
        // the document reference is used to create a document in the collection
        val docRef = firebaseFirestore.collection("todos").document(firebaseAuth.currentUser!!.uid)
            .collection("userTodos").document();

        // get the id of the document
        val docId = docRef.id

        // create a hashmap to store the data
        val todo = hashMapOf(
            "id" to docId,
            "name" to name,
            "completed" to false,
            "timestamp" to System.currentTimeMillis(),
            "uid" to firebaseAuth.currentUser!!.uid
        )

        docRef.set(todo).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("HomeFragment", "Todo Added")
            } else {
                Log.d("HomeFragment", "Todo not Added")
            }
        }

    }

    // this function is called when the delete button is clicked
    override fun deleteTodoItem(todoData: TodoData) {
        firebaseFirestore.collection("todos").document(firebaseAuth.currentUser!!.uid)
            .collection("userTodos").document(todoData.id).delete().addOnCompleteListener {
                if (it.isSuccessful) {

                    Toast.makeText(context, "Todo Deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Todo not Deleted", Toast.LENGTH_SHORT).show()

                }
            }
    }


    // this function is called when the edit button is clicked
    override fun editTodoItem(todoData: TodoData) {

        if (editTodoFragment != null) {
            // remove the fragment from the fragment manager if it is not null and is visible
            // this is to avoid adding the fragment multiple times to the fragment manager
            childFragmentManager.beginTransaction().remove(editTodoFragment!!).commit()
        }

        // create an instance of the EditTodoFragment
        editTodoFragment = EditTodoFragment.newInstance(todoData)
        // set the listener to the fragment
        editTodoFragment!!.setEditTodoClickListener(this)
        // show the fragment
        editTodoFragment!!.show(childFragmentManager, "Edit Todo Fragment")
    }

    override fun onEditTodo(item: TodoData, name: String) {

        Log.d("HomeFragmentEditTodo", "Todo: ${item}")
        val docRef = firebaseFirestore.collection("todos")
            .document(firebaseAuth.currentUser!!.uid)
            .collection("userTodos")
            .document(item.id)

        val todo = hashMapOf(
            "id" to item.id,
            "name" to name,
            "completed" to item.isCompleted,
            "timestamp" to System.currentTimeMillis(),
            "uid" to firebaseAuth.currentUser!!.uid
        )

        docRef.set(todo).addOnCompleteListener {
            if (it.isSuccessful) {
                Log.d("HomeFragment", "Todo Updated")
            } else {
                Log.d("HomeFragment", "Todo not Updated")
            }
        }
    }
}