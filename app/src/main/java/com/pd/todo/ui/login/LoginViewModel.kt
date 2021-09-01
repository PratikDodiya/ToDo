package com.pd.todo.ui.login

import android.view.View
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.pd.todo.R
import com.pd.todo.data.entities.LoginResponse
import com.pd.todo.data.repository.LoginRepository
import com.pd.todo.databinding.ActivityLoginBinding
import com.pd.todo.utils.*
import okhttp3.RequestBody
import org.json.JSONObject

class LoginViewModel @ViewModelInject constructor(
    private val repository: LoginRepository
) : ViewModel() {

    private val request = MutableLiveData<RequestBody>()

    private val _response = request.switchMap { requestBody ->
        repository.login(requestBody)
    }
    val response: LiveData<Resource<LoginResponse>> = _response


    fun start(requestBody: RequestBody) {
        request.value = requestBody
    }


    fun performValidation(activity: LoginActivity, binding: ActivityLoginBinding) : Boolean {

        if (binding.tilEmail.editText!!.text.toString().isNullOrEmpty()) {
            activity.toast(activity.getString(R.string.error_enter_email), Toast.LENGTH_LONG)
            binding.tilEmail.editText!!.requestFocus()
            binding.progressBar.visibility = View.GONE
            return false
        }

        if (!binding.tilEmail.editText!!.text.toString().isValidEmail()) {
            activity.toast(activity.getString(R.string.error_enter_valid_email), Toast.LENGTH_LONG)
            binding.tilEmail.editText!!.requestFocus()
            binding.progressBar.visibility = View.GONE
            return false
        }

        if (binding.tilPassword.editText!!.text.toString().isNullOrEmpty()) {
            activity.toast(activity.getString(R.string.error_enter_password), Toast.LENGTH_LONG)
            binding.tilPassword.editText!!.requestFocus()
            binding.progressBar.visibility = View.GONE
            return false
        }

        if (isOnline(activity).not()) {
            activity.toast(activity.getString(R.string.msg_no_internet_connection), Toast.LENGTH_LONG)
            binding.tilEmail.editText!!.clearFocus()
            binding.tilPassword.editText!!.clearFocus()
            binding.progressBar.visibility = View.GONE
            return false
        }

        val jsonObject = JSONObject()
        jsonObject.put("email", binding.tilEmail.editText!!.text.toString())
        jsonObject.put("password", binding.tilPassword.editText!!.text.toString())
//        jsonObject.put("email", "eve.holt@reqres.in")
//        jsonObject.put("password", "cityslicka")

        // Convert JSONObject to String
        val jsonObjectString = jsonObject.toString()

        // Convert in RequestBody
        val requestBody = getRequestJSONBody(jsonObjectString)

        start(requestBody)

        return true
    }
}