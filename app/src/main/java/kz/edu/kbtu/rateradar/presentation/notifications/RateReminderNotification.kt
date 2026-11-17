package kz.edu.kbtu.rateradar.presentation.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import kz.edu.kbtu.rateradar.MainActivity
import kz.edu.kbtu.rateradar.R
import kz.edu.kbtu.rateradar.domain.model.ExchangeRate

private const val CHANNEL_ID = "rate_reminders"
private const val NOTIFICATION_ID = 1

internal fun canShowRateReminder(context: Context): Boolean {
    if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) return false

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val manager = context.getSystemService(NotificationManager::class.java)
        if (manager.getNotificationChannel(CHANNEL_ID)?.importance == NotificationManager.IMPORTANCE_NONE) {
            return false
        }
    }
    return true
}

internal fun showRateReminderNotification(context: Context, rate: ExchangeRate) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
    ) return
    if (!canShowRateReminder(context)) return

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val manager = context.getSystemService(NotificationManager::class.java)
        val channel = NotificationChannel(
            CHANNEL_ID,
            context.getString(R.string.rate_reminder_channel),
            NotificationManager.IMPORTANCE_DEFAULT,
        )
        manager.createNotificationChannel(channel)
    }

    val intent = Intent(context, MainActivity::class.java)
        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
    val openApp = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_rate_reminder)
        .setContentTitle(context.getString(R.string.rate_reminder_title))
        .setContentText(context.getString(R.string.reference_rate_value, rate.base, rate.rate, rate.quote))
        .setContentIntent(openApp)
        .setAutoCancel(true)
        .build()

    NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
}
