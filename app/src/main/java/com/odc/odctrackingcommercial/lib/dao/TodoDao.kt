package com.odc.odctrackingcommercial.lib.dao

import androidx.room.*
import com.odc.odctrackingcommercial.models.TodoModel
import com.odc.odctrackingcommercial.lib.utils.Constantes
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("select * from ${Constantes.TB_TODO_NAME} order by id desc")
    fun getAllData(): Flow<List<TodoModel>>

    @Query("select * from ${Constantes.TB_TODO_NAME} where id=:todoID")
    fun getSelectedData(todoID: Int): Flow<TodoModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTodo(todo: TodoModel)

    @Update
    suspend fun updateTodo(todo: TodoModel)

    @Delete
    suspend fun deleteTodo(todo: TodoModel)

    @Query("Delete from ${Constantes.TB_TODO_NAME}")
    suspend fun deleteAll()

    @Query("Select * from ${Constantes.TB_TODO_NAME} where title like :search or description like :search")
    fun search(search: String): Flow<List<TodoModel>>

    @Query(
        "Select * from ${Constantes.TB_TODO_NAME}  order by " +
                "case when priority like 'L%' then 1 " +
                "when priority like 'M%' then 2 " +
                "when priority like 'H%' then 3 END"
    )
    fun sortbyLowPrioriry(): Flow<List<TodoModel>>

    @Query(
        "Select * from ${Constantes.TB_TODO_NAME}  order by " +
                "case when priority like 'H%' then 3 " +
                "when priority like 'M%' then 2 " +
                "when priority like 'L%' then 1 END"
    )
    fun sortbyHighPrioriry(): Flow<List<TodoModel>>
}