package com.pd.todo.ui.list

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pd.todo.data.entities.ToDo
import com.pd.todo.data.repository.ToDoRepository
import com.pd.todo.utils.Resource
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ListToDoViewModel @ViewModelInject constructor(
    private val repository: ToDoRepository
) : ViewModel() {

    val allTodo: LiveData<Resource<List<ToDo>>> = repository.getAllToDos()

    fun delete(toDo: ToDo) = GlobalScope.launch {
        repository.delete(toDo)
    }
}