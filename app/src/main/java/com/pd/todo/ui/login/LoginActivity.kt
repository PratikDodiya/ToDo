package com.pd.todo.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.pd.todo.R
import com.pd.todo.databinding.ActivityLoginBinding
import com.pd.todo.ui.list.ListToDoActivity
import com.pd.todo.utils.Resource
import com.pd.todo.utils.TAG
import com.pd.todo.utils.hideKeyboard
import com.pd.todo.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setActionBar()
        initListeners()
        setupObservers()

    }

    private fun setActionBar() {
        setTitle(resources.getString(R.string.label_login))
    }

    private fun initListeners() {
        binding.btnLogin.setOnClickListener(this)
    }

    private fun setupObservers() {

        viewModel.response.observe(this, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    Log.d(TAG, "=======SUCCESS============${it.status}===" + it.data)
                    binding.progressBar.visibility = View.GONE

                    val intent = Intent(applicationContext, ListToDoActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                }
                Resource.Status.ERROR -> {
                    Log.d(TAG, "=======ERROR============${it.status}===" + it.data)
                    binding.progressBar.visibility = View.GONE
                    toast(it.message.toString(), Toast.LENGTH_SHORT)
                }

                Resource.Status.LOADING -> {
                    Log.d(TAG, "=======LOADING============${it.status}===" + it.data)
                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })


    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnLogin -> {
                hideKeyboard(this, v)
                binding.progressBar.visibility = View.VISIBLE
                viewModel.performValidation(this, binding)
            }
        }
    }
}