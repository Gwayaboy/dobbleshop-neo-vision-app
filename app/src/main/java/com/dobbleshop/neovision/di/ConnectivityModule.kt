package com.dobbleshop.neovision.di

import com.dobbleshop.neovision.data.connectivity.ConnectionManager
import com.dobbleshop.neovision.data.connectivity.FakeConnectionManager
import com.dobbleshop.neovision.data.device.BluetoothDeviceManager
import com.dobbleshop.neovision.data.device.FakeBluetoothDeviceManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for connectivity-related dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ConnectivityModule {
    
    @Binds
    @Singleton
    abstract fun bindConnectionManager(
        fakeConnectionManager: FakeConnectionManager
    ): ConnectionManager
    
    @Binds
    @Singleton
    abstract fun bindBluetoothDeviceManager(
        fakeBluetoothDeviceManager: FakeBluetoothDeviceManager
    ): BluetoothDeviceManager
}
