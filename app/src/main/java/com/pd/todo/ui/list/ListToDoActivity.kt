package com.pd.todo.ui.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.pd.todo.R
import com.pd.todo.data.entities.ToDo
import com.pd.todo.databinding.ActivityListToDoBinding
import com.pd.todo.ui.create.CreateToDoActivity
import com.pd.todo.utils.Resource
import com.pd.todo.utils.TAG
import com.pd.todo.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListToDoActivity : AppCompatActivity(), ListToDoAdapter.ToDoItemListener, View.OnClickListener {

    lateinit var binding: ActivityListToDoBinding
    private val viewModel: ListToDoViewModel by viewModels()
    private lateinit var adapter: ListToDoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListToDoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setActionBar()
        initListeners()
        setupRecyclerView()
        setupObservers()
    }

    private fun setActionBar() {
        setTitle(resources.getString(R.string.label_list_todo))
    }

    private fun initListeners() {
        binding.fabAdd.setOnClickListener(this)
    }

    private fun setupRecyclerView() {
        adapter = ListToDoAdapter(this)
        binding.rvToDo.layoutManager = LinearLayoutManager(this)
        binding.rvToDo.adapter = adapter
    }

    private fun setupObservers() {
        viewModel.allTodo.observe(this, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    Log.d(TAG, "=======SUCCESS============${it.status}===" + it.data)
                    binding.progressBar.visibility = View.GONE
                    if (!it.data.isNullOrEmpty()) {
                        adapter.setItems(ArrayList(it.data))
                        binding.tvNoData.visibility = View.GONE
                    } else {
                        adapter.setItems(arrayListOf())
                        binding.tvNoData.visibility = View.VISIBLE
                    }
                }
                Resource.Status.ERROR -> {
                    Log.d(TAG, "=======SUCCESS============${it.status}===" + it.data)
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                    binding.tvNoData.visibility = View.GONE
                }

                Resource.Status.LOADING ->
                    binding.progressBar.visibility = View.VISIBLE
            }
        })
    }

    override fun onClickedToDo(toDo: ToDo) {
        showAlertDialog(toDo)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fabAdd -> {
                val intent = Intent(applicationContext, CreateToDoActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun showAlertDialog(toDo: ToDo) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialog.setTitle(getString(R.string.app_name))
        alertDialog.setMessage(getString(R.string.msg_do_you_want_delete_item))
        alertDialog.setPositiveButton(
            getString(R.string.action_yes)
        ) { _, _ ->
            viewModel.delete(toDo)
            toast(getString(R.string.msg_item_delete_success), Toast.LENGTH_LONG)
        }
        alertDialog.setNegativeButton(
            getString(R.string.action_no)
        ) { _, _ -> }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }
}