package com.fuad.post8_230018015

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.fuad.post8_230018015.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var taskRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvTask.layoutManager = LinearLayoutManager(this)

        // Use the explicit URL from google-services.json to ensure connection to the correct region
        taskRef = FirebaseDatabase.getInstance("https://post8-2300018015-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("task")

        fetchData()

        binding.fabAddTask.setOnClickListener {
            AddTask(this, taskRef).show()
        }
    }

    private fun fetchData() {
        taskRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<Task>()

                snapshot.children.forEach {
                    it.getValue(Task::class.java)?.let(list::add)
                }

                if (list.isEmpty()) {
                    binding.tvTask.visibility = View.GONE
                    binding.layoutEmpty.root.visibility = View.VISIBLE
                } else {
                    binding.tvTask.visibility = View.VISIBLE
                    binding.layoutEmpty.root.visibility = View.GONE
                    binding.tvTask.adapter = TaskAdapter(list, taskRef)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}