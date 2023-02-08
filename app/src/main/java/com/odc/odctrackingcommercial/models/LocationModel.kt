package com.odc.odctrackingcommercial.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.odc.odctrackingcommercial.lib.utils.Constantes

@Entity(tableName = Constantes.TB_LOCATIONS)
data class LocationModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val longitude: Double, val latitude: Double, val date: String,
    @ColumnInfo(defaultValue = "")
    val identifiant: UserModel,
)
