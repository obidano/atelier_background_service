package com.odc.odctrackingcommercial.lib.db

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.odc.odctrackingcommercial.models.ArticlesModel
import com.odc.odctrackingcommercial.models.LocationModel
import com.odc.odctrackingcommercial.models.UserModel

class RoomConverters {
    @TypeConverter
    fun convertArticleToString(data: ArticlesModel): String {
        return Gson().toJson(data, ArticlesModel::class.java)
    }

    @TypeConverter
    fun convertStringToArticle(data: String): ArticlesModel {
        return Gson().fromJson(data, ArticlesModel::class.java)
    }
    @TypeConverter
    fun convertUserToString(data: UserModel): String {
        return Gson().toJson(data, UserModel::class.java)
    }

    @TypeConverter
    fun convertStringToUser(data: String): UserModel {
        return Gson().fromJson(data, UserModel::class.java)
    }

    @TypeConverter
    fun convertLocationToString(data: LocationModel): String {
        return Gson().toJson(data, LocationModel::class.java)
    }

    @TypeConverter
    fun convertStringToLocation(data: String): LocationModel {
        return Gson().fromJson(data, LocationModel::class.java)
    }
}