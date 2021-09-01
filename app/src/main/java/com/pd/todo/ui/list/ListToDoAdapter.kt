package com.pd.todo.ui.list

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pd.todo.data.entities.ToDo
import com.pd.todo.databinding.ItemToDoBinding

class ListToDoAdapter(private val listener: ToDoItemListener) : RecyclerView.Adapter<ToDoViewHolder>() {

    interface ToDoItemListener {
        fun onClickedToDo(toDo: ToDo)
    }

    private val items = ArrayList<ToDo>()

    fun setItems(items: ArrayList<ToDo>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding: ItemToDoBinding = ItemToDoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToDoViewHolder(binding, listener)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) = holder.bind(items[position])
}

class ToDoViewHolder(private val itemBinding: ItemToDoBinding, private val listener: ListToDoAdapter.ToDoItemListener) :
    RecyclerView.ViewHolder(itemBinding.root),
    View.OnClickListener {

    private lateinit var toDo: ToDo

    init {
        itemBinding.root.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: ToDo) {
        this.toDo = item
        itemBinding.tvTitle.text = item.title
        itemBinding.tvDescription.text = item.description
        itemBinding.tvDateTime.text = "${item.date} - ${item.time}"
    }

    override fun onClick(v: View?) {
        listener.onClickedToDo(toDo)
    }
}