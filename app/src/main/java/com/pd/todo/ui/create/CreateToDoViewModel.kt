package com.pd.todo.ui.create

import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pd.todo.R
import com.pd.todo.data.entities.ToDo
import com.pd.todo.data.repository.ToDoRepository
import com.pd.todo.databinding.ActivityCreateToDoBinding
import com.pd.todo.utils.toast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class CreateToDoViewModel @ViewModelInject constructor(
    private val repository: ToDoRepository
) : ViewModel() {

    var title: MutableLiveData<String> = MutableLiveData()
    var description: MutableLiveData<String> = MutableLiveData()
    var time: MutableLiveData<String> = MutableLiveData()
    var date: MutableLiveData<String> = MutableLiveData()
    var types: MutableLiveData<Int> = MutableLiveData()

    fun insert(toDo: ToDo) = GlobalScope.launch {
        repository.insert(toDo)
    }

    fun performValidation(activity: CreateToDoActivity, binding: ActivityCreateToDoBinding): Boolean {

            title.value = binding.tilTitle.editText!!.text.toString()
            description.value = binding.tilDescription.editText!!.text.toString()
            time.value = binding.tilTime.editText!!.text.toString()
            date.value = binding.tilDate.editText!!.text.toString()

            types.value = 0
            if (binding.rbDaily.isChecked) types.value = 1
            else if (binding.rbWeekly.isChecked) types.value = 2


            if (title.value.isNullOrEmpty()) {
                activity.toast(activity.getString(R.string.error_enter_title), Toast.LENGTH_LONG)
                binding.tilTitle.editText!!.requestFocus()
                return false
            }

            if (description.value.isNullOrEmpty()) {
                activity.toast(activity.getString(R.string.error_enter_description), Toast.LENGTH_LONG)
                binding.tilDescription.editText!!.requestFocus()
                return false
            }

            if (time.value.isNullOrEmpty()) {
                activity.toast(activity.getString(R.string.error_select_time), Toast.LENGTH_LONG)
                return false
            }

            if (date.value.isNullOrEmpty()) {
                activity.toast(activity.getString(R.string.error_select_date), Toast.LENGTH_LONG)
                return false
            }

            if (types.value == 0) {
                activity.toast(activity.getString(R.string.error_select_types), Toast.LENGTH_LONG)
                return false
            }

            val toDo = ToDo(
                id = 0, title = title.value!!, description = description.value!!,
                time = time.value!!, date = date.value!!, types = types.value!!,
                date_time = Date(), created = Date().toString()
            )
            insert(toDo)

            return true

    }
}