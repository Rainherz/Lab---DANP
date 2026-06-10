package com.example.lab06.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.domain.repository.AlertRepository
import com.example.lab06.MainActivity
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.first

class AlertSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface AlertWorkerEntryPoint {
        fun alertRepository(): AlertRepository
    }

    override suspend fun doWork(): Result {
        val entryPoint = EntryPointAccessors.fromApplication(
            applicationContext,
            AlertWorkerEntryPoint::class.java
        )
        val repository = entryPoint.alertRepository()

        return try {
            // Obtener alertas cacheadas antes del sync
            val oldAlertIds = repository.getAlerts().first().map { it.id }.toSet()

            // Ejecutar sincronización
            repository.syncAlerts()

            // Obtener nuevas alertas
            val newAlerts = repository.getAlerts().first()
            val newlyAdded = newAlerts.filter { !oldAlertIds.contains(it.id) }

            // Si hay nuevas alertas, disparar notificación push
            if (newlyAdded.isNotEmpty()) {
                val criticalCount = newlyAdded.count { it.severity.uppercase() == "CRÍTICO" }
                val highCount = newlyAdded.count { it.severity.uppercase() == "ALTO" }
                
                val title = "⚠️ Nuevas vulnerabilidades detectadas"
                val text = if (criticalCount > 0 || highCount > 0) {
                    "Se detectaron ${newlyAdded.size} amenazas en tu stack ($criticalCount críticas, $highCount altas). ¡Revisa SecAlerts!"
                } else {
                    "Se encontraron ${newlyAdded.size} alertas de severidad media/baja en tu infraestructura."
                }
                
                showNotification(title, text)
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "secalerts_sync_channel"
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Alertas de Seguridad",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones de nuevas vulnerabilidades en tu infraestructura"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.stat_sys_warning)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1001, notification)
    }
}
