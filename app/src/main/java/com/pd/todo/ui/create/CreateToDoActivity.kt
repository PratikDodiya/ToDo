package com.pd.todo.ui.create

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.pd.todo.R
import com.pd.todo.databinding.ActivityCreateToDoBinding
import com.pd.todo.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class CreateToDoActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: ActivityCreateToDoBinding
    private val viewModel: CreateToDoViewModel by viewModels()
    lateinit var calendar: Calendar
    var year = 0
    var month = 0
    var day = 0
    var hour = 0
    var minute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateToDoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setActionBar()
        initListeners()

        calendar = Calendar.getInstance()
        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DAY_OF_MONTH)
        hour = calendar.get(Calendar.HOUR_OF_DAY)
        minute = calendar.get(Calendar.MINUTE)
    }

    private fun setActionBar() {
        setTitle(resources.getString(R.string.label_create_todo))
    }

    private fun initListeners() {
        binding.etTime.setOnClickListener(this)
        binding.etDate.setOnClickListener(this)
        binding.btnCreate.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.etTime -> {
                openTimeDialog()
            }
            R.id.etDate -> {
                openDateDialog()
            }
            R.id.btnCreate -> {
                hideKeyboard(this, v)
                if (viewModel.performValidation(this, binding)) {
                    toast(getString(R.string.msg_todo_create_success), Toast.LENGTH_LONG)
                    reminderNotification()
                    finish()
                }
            }
        }
    }

    fun openDateDialog() {
        val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            // Display selected date in textbox
            binding.etDate.setText(convertDefaultDateString("$year-$monthOfYear-$dayOfMonth"))

        }, year, month, day)

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    fun openTimeDialog() {
        val timePicker = TimePickerDialog(this, object : TimePickerDialog.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                binding.etTime.setText(convertTimeString(String.format("%d:%d", hourOfDay, minute)))
            }
        }, hour, minute, true)

        timePicker.show()
    }


    fun reminderNotification() {

        var interval = 1
        val str = binding.etDate.text.toString() + " " + binding.etTime.text.toString()
        val date = convertStringToDate(str, DEFAULT_DATE_TIME_FORMAT)

        val notificationDate = Calendar.getInstance()
        notificationDate.time = date

        val alarmStartTime = Calendar.getInstance()
        val now = Calendar.getInstance()
        alarmStartTime[Calendar.HOUR_OF_DAY] = notificationDate[Calendar.HOUR_OF_DAY]
        alarmStartTime[Calendar.MINUTE] = notificationDate[Calendar.MINUTE]
        alarmStartTime[Calendar.SECOND] = 0
        if (now.after(alarmStartTime)) {
            alarmStartTime.add(Calendar.DATE, 1)
        }

        if (binding.rbDaily.isChecked) interval = 1
        else if (binding.rbWeekly.isChecked) interval = 7

        val notificationUtils = NotificationUtils(this, binding.tilTitle.editText!!.text.toString(),
            binding.tilDescription.editText!!.text.toString(), binding.tilDate.editText!!.text.toString() + " - " + binding.tilTime.editText!!.text.toString())
        notificationUtils.setReminder(alarmStartTime.timeInMillis, interval)
    }
}