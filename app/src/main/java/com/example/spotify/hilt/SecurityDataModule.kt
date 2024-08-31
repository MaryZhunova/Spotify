package com.example.spotify.hilt

import com.example.spotify.data.security.SecurityRepositoryImpl
import com.example.spotify.data.security.net.ClientCredentialsApiMapper
import com.example.spotify.data.security.net.ClientCredentialsApiMapperImpl
import com.example.spotify.domain.security.SecurityRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SecurityDataModule {

    @Binds
    @Singleton
    abstract fun bindClientCredentialsApiMapper(
        clientCredentialsApiMapperImpl: ClientCredentialsApiMapperImpl
    ): ClientCredentialsApiMapper

    @Binds
    @Singleton
    abstract fun bindSecurityRepository(
        securityRepositoryImpl: SecurityRepositoryImpl
    ): SecurityRepository
}