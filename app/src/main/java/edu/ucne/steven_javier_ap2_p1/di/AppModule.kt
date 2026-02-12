package edu.ucne.steven_javier_ap2_p1.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.steven_javier_ap2_p1.data.local.dao.CervezaDao
import edu.ucne.steven_javier_ap2_p1.data.local.database.Database
import edu.ucne.steven_javier_ap2_p1.data.repository.cervezaRepositoryImpl
import edu.ucne.steven_javier_ap2_p1.domain.repository.CervezaRepository
import edu.ucne.steven_javier_ap2_p1.domain.usecase.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): Database {
        return Room.databaseBuilder(
            appContext,
            Database::class.java,
            "Cervezas.db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCervezaDao(db: Database): CervezaDao {
        return db.cervezaDao
    }
    @Provides
    fun provideObserveCervezaUseCase(repository: CervezaRepository): ObserveCervezasUseCase {
        return ObserveCervezasUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCervezaRepository(cervezaDao: CervezaDao): CervezaRepository {
        return cervezaRepositoryImpl(cervezaDao)
    }

    @Provides
    fun provideUpsertCervezaUseCase(repository: CervezaRepository): UpsertCervezaUseCase {
        return UpsertCervezaUseCase(repository)
    }

    @Provides
    fun provideDeleteCervezaUseCase(repository: CervezaRepository): DeleteCervezaUseCase {
        return DeleteCervezaUseCase(repository)
    }

    @Provides
    fun provideGetCervezaseCase(repository: CervezaRepository): GetCervezaUseCase {
        return GetCervezaUseCase(repository)
    }

    @Provides
    fun provideGetCervezaByNombreUseCase(repository: CervezaRepository): GetCervezaByNombreUseCase {
        return GetCervezaByNombreUseCase(repository)
    }
}