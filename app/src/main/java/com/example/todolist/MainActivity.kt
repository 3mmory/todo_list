package com.example.todolist

import AddTaskFragment
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var taskList: MutableList<Pair<String, String>>  // Pair of Task Name and Description
    private lateinit var adapter: CustomAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private val TASKS_KEY = "tasks"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("com.example.todolist", MODE_PRIVATE)

        taskList = loadTasks()
        adapter = CustomAdapter(this, taskList, ::onEditTask, ::onDeleteTask)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            showAddTaskDialog()
        }
    }

    private fun showAddTaskDialog() {
        val addTaskFragment = AddTaskFragment()
        addTaskFragment.setOnTaskAddedListener { task, description ->
            taskList.add(task to description)
            saveTasks()
            adapter.notifyDataSetChanged()
        }
        addTaskFragment.show(supportFragmentManager, "AddTaskFragment")
    }

    private fun onEditTask(position: Int) {
        val currentTask = taskList[position]
        val editTaskFragment = AddTaskFragment.newInstance(currentTask.first, currentTask.second)
        editTaskFragment.setOnTaskAddedListener { updatedTask, updatedDescription ->
            taskList[position] = updatedTask to updatedDescription
            saveTasks()
            adapter.notifyDataSetChanged()
        }
        editTaskFragment.show(supportFragmentManager, "EditTaskFragment")
    }

    private fun onDeleteTask(position: Int) {
        taskList.removeAt(position)
        saveTasks()
        adapter.notifyDataSetChanged()
    }

    private fun saveTasks() {
        val gson = Gson()
        val json = gson.toJson(taskList)
        val editor = sharedPreferences.edit()
        editor.putString(TASKS_KEY, json)
        editor.apply()
    }

    private fun loadTasks(): MutableList<Pair<String, String>> {
        val gson = Gson()
        val json = sharedPreferences.getString(TASKS_KEY, null)
        val type = object : TypeToken<MutableList<Pair<String, String>>>() {}.type
        return if (json != null) {
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }
}
