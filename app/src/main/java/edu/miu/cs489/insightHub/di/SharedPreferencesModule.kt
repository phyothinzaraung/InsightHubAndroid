package edu.miu.cs489.insightHub.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.miu.cs489.insightHub.data.local.preferences.PreferencesManager

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    @Provides
    fun providesSharedPreferenceManager(@ApplicationContext context: Context): PreferencesManager{
        return PreferencesManager(context = context)
    }
}