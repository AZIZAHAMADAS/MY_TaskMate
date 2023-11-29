package net.penguincoders.doit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.penguincoders.doit.Adapters.ToDoAdapter;
import net.penguincoders.doit.Model.ToDoModel;
import net.penguincoders.doit.Utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

class MainActivity : AppCompatActivity(), ToDoDialogCloseListener {

private lateinit var viewModel: ToDoViewModel
private lateinit var tasksAdapter: ToDoAdapter
private val taskList: MutableList<ToDoModel> = mutableListOf()

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        val tasksRecyclerView: RecyclerView = findViewById(R.id.tasksRecyclerView)
        tasksRecyclerView.layoutManager = LinearLayoutManager(this)

        tasksAdapter = ToDoAdapter(this, taskList)
        tasksRecyclerView.adapter = tasksAdapter

        val itemTouchHelper = ItemTouchHelper(RecyclerItemTouchHelper(tasksAdapter))
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView)

        val fab: FloatingActionButton = findViewById(R.id.fab)

        viewModel = ViewModelProvider(this).get(ToDoViewModel::class.java)
        viewModel.getAllTasks().observe(this, { tasks ->
        taskList.clear()
        taskList.addAll(tasks.reversed())
        tasksAdapter.notifyDataSetChanged()
        })

        fab.setOnClickListener {
        showAddNewTaskDialog()
        }
        }

private fun showAddNewTaskDialog() {
        val addTaskDialog = AddNewTaskDialog.newInstance()
        addTaskDialog.show(supportFragmentManager, AddNewTaskDialog.TAG)
        }

        override fun onDialogClose() {
        viewModel.refreshTasks() // This triggers a new data fetch from the database
        }
        }
