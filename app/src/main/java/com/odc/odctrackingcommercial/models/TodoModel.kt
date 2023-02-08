package com.odc.odctrackingcommercial.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.odc.odctrackingcommercial.lib.utils.Constantes
import com.odc.odctrackingcommercial.lib.utils.Priority

@Entity(tableName = Constantes.TB_TODO_NAME)
data class TodoModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val priority: Priority,
)