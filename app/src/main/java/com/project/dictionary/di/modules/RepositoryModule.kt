package com.project.dictionary.di.modules

import com.project.dictionary.di.NAME_LOCAL
import com.project.dictionary.di.NAME_REMOTE
import com.project.dictionary.model.data.DataModel
import com.project.dictionary.model.datasource.DataSource
import com.project.dictionary.model.datasource.RetrofitImpl
import com.project.dictionary.model.datasource.RoomDataBaseImpl
import com.project.dictionary.model.repository.Repository
import com.project.dictionary.model.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton


@Module
class RepositoryModule {

    @Provides
    @Singleton
    @Named(NAME_REMOTE)
    internal fun provideRepositoryRemote(@Named(NAME_REMOTE) dataSourceRemote: DataSource<List<DataModel>>): Repository<List<DataModel>> =
        RepositoryImpl(dataSourceRemote)

    @Provides
    @Singleton
    @Named(NAME_LOCAL)
    internal fun provideRepositoryLocal(@Named(NAME_LOCAL) dataSourceLocal: DataSource<List<DataModel>>): Repository<List<DataModel>> =
        RepositoryImpl(dataSourceLocal)

    @Provides
    @Singleton
    @Named(NAME_REMOTE)
    internal fun provideDataSourceRemote(): DataSource<List<DataModel>> =
        RetrofitImpl()

    @Provides
    @Singleton
    @Named(NAME_LOCAL)
    internal fun provideDataSourceLocal(): DataSource<List<DataModel>> = RoomDataBaseImpl()
}