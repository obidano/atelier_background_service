package com.odc.odctrackingcommercial.lib.di

import android.content.Context
import androidx.room.Room
import com.odc.odctrackingcommercial.lib.db.MyDatabase
import com.odc.odctrackingcommercial.lib.depot.DataStoreDepot
import com.odc.odctrackingcommercial.lib.utils.Constantes
import com.odc.odctrackingcommercial.lib.utils.LocationHelper
import com.odc.odctrackingcommercial.lib.utils.SocketUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, MyDatabase::class.java, Constantes.DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideTodoDao(database: MyDatabase) = database.todoQueries()

    @Singleton
    @Provides
    fun provideArticleDao(database: MyDatabase) = database.articlesQueries()

    @Singleton
    @Provides
    fun provideActiviteDao(database: MyDatabase) = database.activitesQueries()

    @Singleton
    @Provides
    fun provideLocationDao(database: MyDatabase) = database.locationQueries()

    @Singleton
    @Provides
    fun provideLocationHelper(@ApplicationContext context: Context) = LocationHelper(context)

    @Singleton
    @Provides
    fun provideSocket(@ApplicationContext context: Context): SocketUtils {
        return SocketUtils(context)
    }

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStoreDepot {
        return DataStoreDepot(context)
    }
}