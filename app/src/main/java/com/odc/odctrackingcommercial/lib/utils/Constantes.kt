package com.odc.odctrackingcommercial.lib.utils

import android.annotation.SuppressLint
import com.odc.odctrackingcommercial.models.ActivitesModel
import com.odc.odctrackingcommercial.models.ArticlesModel
import com.odc.odctrackingcommercial.models.UserModel
import java.text.SimpleDateFormat
import java.util.*

object Constantes {
    val BASE_URL: String="http://congocoop.com:3200"
    const val DATABASE_NAME = "DB"
    const val TB_TODO_NAME = "tb_dodo"
    const val TB_ACTIVITES = "tb_activites"
    const val TB_ARTICLES = "tb_articles"
    const val TB_LOCATIONS = "tb_locations"

    const val LIST_SCREEN = "list/{action}"
    const val TASK_SCREEN = "task/{taskId}"

    const val PREF_NAME = "odc_prefs"
    const val PREF_KEY_USER = "user_key"
    const val PREF_KEY_IDENTIFIANT = "identifiant_key"

    val FakeArticles = listOf<ArticlesModel>(
        ArticlesModel(id = 1, nom = "Lait", prixVente = 10.0)
    )

    @SuppressLint("SimpleDateFormat")
    val dateFormatter = SimpleDateFormat("dd-MM-yyyy HH:mm")
    val dateFormatter2 = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
    val FakeIDentifiant=UserModel(UUID.randomUUID().toString(), "ODC")
    val FakeActivites = listOf<ActivitesModel>(
        ActivitesModel(
            description = "Lorem Ipsum Achat",
            category = Categorie.Revenus,
            montant = 100.00,
            date = dateFormatter.format(Calendar.getInstance().time), // "13/01/2022 12:00",
            article = FakeArticles[0],
            quantite = 10,
            identifiant = FakeIDentifiant
        )
    )

}