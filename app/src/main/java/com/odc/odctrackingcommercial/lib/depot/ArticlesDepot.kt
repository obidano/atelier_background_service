package com.odc.odctrackingcommercial.lib.depot

import com.odc.odctrackingcommercial.lib.dao.ArticlesDao
import com.odc.odctrackingcommercial.models.ArticlesModel
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class ArticlesDepot @Inject constructor(private val query: ArticlesDao) {

    val getAllArticle = query.getAllData()

    fun getSelectedArticles(rowId: Int) = query.getSelectedData(rowId)

    suspend fun addArticle(data: ArticlesModel) {
        query.add(data)
    }

    suspend fun updateArticle(data: ArticlesModel) {
        query.update(data)
    }

    suspend fun deleteArticle(data: ArticlesModel) {
        query.delete(data)
    }

    suspend fun deleteAllArticle() {
        query.deleteAll()
    }

    fun searchArticle(search: String) = query.search(search)
}