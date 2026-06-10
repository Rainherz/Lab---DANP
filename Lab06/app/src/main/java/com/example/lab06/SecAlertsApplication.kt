package com.example.lab06

import android.app.Application
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.lab06.worker.AlertSyncWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class SecAlertsApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupBackgroundSync()
    }

    private fun setupBackgroundSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncWorkRequest = PeriodicWorkRequestBuilder<AlertSyncWorker>(
            24, TimeUnit.HOURS // Sincronizar automáticamente una vez al día
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "SecAlertsBackgroundSync",
            ExistingPeriodicWorkPolicy.KEEP, // Mantener el existente para no reiniciar el temporizador
            syncWorkRequest
        )
    }
}
