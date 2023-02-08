package com.odc.odctrackingcommercial.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.odc.odctrackingcommercial.lib.utils.Constantes

@Entity(tableName = Constantes.TB_ARTICLES)
data class ArticlesModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nom: String,
    val prixVente: Double,
)