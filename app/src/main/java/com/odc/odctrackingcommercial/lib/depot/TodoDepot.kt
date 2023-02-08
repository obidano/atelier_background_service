package com.odc.odctrackingcommercial.lib.depot

import com.odc.odctrackingcommercial.lib.dao.TodoDao
import com.odc.odctrackingcommercial.models.TodoModel
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class TodoDepot @Inject constructor(private val query: TodoDao) {

    val getAllTodo = query.getAllData()
    val sortByLowPriority = query.sortbyLowPrioriry()
    val sortByHighPriority = query.sortbyHighPrioriry()

    fun getSelectedTodo(todoID: Int) = query.getSelectedData(todoID)

    suspend fun addTodo(todo: TodoModel) {
        query.addTodo(todo)
    }

    suspend fun updateTodo(todo: TodoModel) {
        query.updateTodo(todo)
    }

    suspend fun deleteTodo(todo: TodoModel) {
        query.deleteTodo(todo)
    }

    suspend fun deleteAllTodo() {
        query.deleteAll()
    }

    fun searchTodo(search: String) = query.search(search)
}