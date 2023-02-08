package com.odc.odctrackingcommercial.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.odc.odctrackingcommercial.lib.utils.Categorie
import com.odc.odctrackingcommercial.lib.utils.Constantes

@Entity(tableName = Constantes.TB_ACTIVITES)
data class ActivitesModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String,
    val category: Categorie,
    val montant: Double,
    val date: String,
    val article: ArticlesModel,
    val prixUnitAchat: Double? = 0.0,
    val quantite: Int,
    @ColumnInfo(  name="identifiant")
    val identifiant: UserModel,
    @ColumnInfo( defaultValue = "", name="localisation")
    var localisation: LocationModel?=null,
)
