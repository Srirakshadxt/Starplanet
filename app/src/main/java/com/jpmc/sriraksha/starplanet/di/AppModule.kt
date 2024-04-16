package com.jpmc.sriraksha.starplanet.di

import android.content.Context
import androidx.room.Room
import com.jpmc.sriraksha.starplanet.data.local.PlanetDatabase
import com.jpmc.sriraksha.starplanet.data.local.PlanetLocalDataSource
import com.jpmc.sriraksha.starplanet.data.remote.PlanetApi
import com.jpmc.sriraksha.starplanet.data.remote.PlanetRemoteDataSource
import com.jpmc.sriraksha.starplanet.data.repository.PlanetRepository
import com.jpmc.sriraksha.starplanet.ui.viewmodel.PlanetsViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

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
        return PlanetLocalDataSource(database.planetDao())
    }

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
    fun providePlanetRemoteDataSource(planetApi: PlanetApi): PlanetRemoteDataSource {
        return PlanetRemoteDataSource(planetApi)
    }

    @Provides
    @Singleton
    fun providePlanetRepository(
        remoteDataSource: PlanetRemoteDataSource,
        localDataSource: PlanetLocalDataSource
    ): PlanetRepository {
        return PlanetRepository(remoteDataSource, localDataSource)
    }

    @Provides
    @Singleton
    fun providePlanetsViewModel(planetRepository: PlanetRepository): PlanetsViewModel {
        return PlanetsViewModel(planetRepository)
    }
}