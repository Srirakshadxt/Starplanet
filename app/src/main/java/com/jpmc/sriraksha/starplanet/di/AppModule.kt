package com.jpmc.sriraksha.starplanet.di

import android.content.Context
import androidx.room.Room
import com.jpmc.sriraksha.starplanet.data.local.PlanetDatabase
import com.jpmc.sriraksha.starplanet.data.local.PlanetLocalDataSource
import com.jpmc.sriraksha.starplanet.data.local.PlanetLocalDataSourceImpl
import com.jpmc.sriraksha.starplanet.data.remote.PlanetApi
import com.jpmc.sriraksha.starplanet.data.remote.PlanetRemoteDataSource
import com.jpmc.sriraksha.starplanet.data.remote.PlanetRemoteDataSourceImpl
import com.jpmc.sriraksha.starplanet.data.repository.PlanetRepository
import com.jpmc.sriraksha.starplanet.data.repository.PlanetRepositoryImpl
import com.jpmc.sriraksha.starplanet.ui.viewmodel.PlanetsViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Provides dependency injection for the Star Planet app using Hilt.
 * Provides dependencies for API, local data source, remote data source, repository, and view model.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providePlanetApi(): PlanetApi {
        return Retrofit.Builder()
            .baseUrl("https://swapi.dev/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlanetApi::class.java)
    }

    @Provides
    @Singleton
    fun providePlanetLocalDataSource(database: PlanetDatabase): PlanetLocalDataSource {
        return PlanetLocalDataSourceImpl(database.planetDao())
    }

    /**
     * Provides the PlanetDatabase instance using Room.
     * The database is built with the given application context and named "planet-database".
     */
    @Provides
    @Singleton
    fun providePlanetDatabase(@ApplicationContext context: Context): PlanetDatabase {
        return Room.databaseBuilder(
            context,
            PlanetDatabase::class.java, "planet-database"
        ).build()
    }

    @Provides
    @Singleton
    fun providePlanetRemoteDataSource(planetRemoteDataSourceImpl: PlanetRemoteDataSourceImpl): PlanetRemoteDataSource =
        planetRemoteDataSourceImpl

    @Provides
    @Singleton
    fun providePlanetRepository(
        planetRepositoryImpl: PlanetRepositoryImpl
    ): PlanetRepository = planetRepositoryImpl

    @Provides
    @Singleton
    fun providePlanetsViewModel(planetRepository: PlanetRepository): PlanetsViewModel {
        return PlanetsViewModel(planetRepository)
    }
}