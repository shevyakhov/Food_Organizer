package com.chelz.foodorganizer.utils.notifications

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.chelz.foodorganizer.R
import com.chelz.foodorganizer.screens.settings.presentation.SettingsViewModel
import java.util.Calendar

class LocalNotificationManager(private val context: Context) {

	private val notificationManager: NotificationManager by lazy {
		context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
	}

	init {
		createNotificationChannel()
	}

	fun scheduleNotification(notificationId: Int, name: String, triggerTime: Long, daysBefore: Int) {
		if (areNotificationsAllowed()) {
			val cal = Calendar.getInstance()

			val preferredTime = (getPreferredTime() ?: "14:00").split(":").map { it.toInt() }
			val hour = preferredTime.first() * oneHourInMillis
			val minute = preferredTime.last() * oneMinuteInMillis
			val hourAndMinute = hour + minute
			val finalTime = triggerTime + hourAndMinute - daysBefore * oneDayInMillis
			val dx = finalTime - cal.timeInMillis
			if (dx < 0) return
			val notificationBuilder = Notification.Builder(context, FOOD_EXPIRATION_CHANNEL_ID)
				.setSmallIcon(R.drawable.recipes_ic)
				.setCategory(NotificationCompat.CATEGORY_ALARM)
				.setAutoCancel(true)
				.setGroup(ExpireBroadcastReceiver.EXPIRE_NOTIFICATION_GROUP)
				.setWhen(finalTime)
				.setOnlyAlertOnce(true)
				.setContentTitle(name)
				.setContentText("$name испортится через $daysBefore ${if (daysBefore == 1) "день" else "дня"}")

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
				notificationBuilder.setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
					.setGroupAlertBehavior(Notification.GROUP_ALERT_CHILDREN)
			}

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
				notificationBuilder.setAllowSystemGeneratedContextualActions(true)
			}
			val notification = notificationBuilder.build()

			val intent = Intent(context, ExpireBroadcastReceiver::class.java)
			intent.putExtra(ExpireBroadcastReceiver.NOTIFICATION_ID, notificationId)
			intent.putExtra(ExpireBroadcastReceiver.NOTIFICATION, notification)
			val pendingIntent = PendingIntent.getBroadcast(
				context,
				notificationId,
				intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
			)

			val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
			alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, finalTime, pendingIntent)
		}
	}

	fun cancelScheduledNotification(notificationId: Int) {
		val intent = Intent(context, ExpireBroadcastReceiver::class.java)
		intent.putExtra(ExpireBroadcastReceiver.NOTIFICATION_ID, notificationId)
		val pendingIntent = PendingIntent.getBroadcast(
			context,
			notificationId,
			intent,
			PendingIntent.FLAG_IMMUTABLE
		)

		val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		alarmManager.cancel(pendingIntent)
	}

	private fun createNotificationChannel() {
		val notificationChannel = NotificationChannel(FOOD_EXPIRATION_CHANNEL_ID, FOOD_EXPIRATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT).apply {
			description = FOOD_EXPIRATION_CHANNEL_DESCRIPTION
		}
		val notificationManager = context.getSystemService(NotificationManager::class.java)
		notificationManager.createNotificationChannel(notificationChannel)
	}

	fun areNotificationsAllowed() = notificationManager.areNotificationsEnabled()

	private fun getPreferredTime() =
		context.getSharedPreferences(SettingsViewModel.TIME_PREFERENCE, Context.MODE_PRIVATE).getString(SettingsViewModel.TIME_PREFERENCE_KEY, "14:00")

	companion object {

		const val oneDayInMillis = 24 * 60 * 60 * 1000
		private const val oneMinuteInMillis = 60 * 1000
		private const val oneHourInMillis = 60 * 60 * 1000
		private const val FOOD_EXPIRATION_CHANNEL_ID = "FOOD_EXPIRATION_CHANNEL_ID"
		private const val FOOD_EXPIRATION_CHANNEL_NAME = "Просроченные продукты"
		private const val FOOD_EXPIRATION_CHANNEL_DESCRIPTION = "Успользуются для уведомления об истечении срока годности"
	}

}
