package com.chelz.foodorganizer.utils.notifications

import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build

class ExpireBroadcastReceiver : BroadcastReceiver() {

	companion object {

		const val NOTIFICATION = "EXPIRE_NOTIFICATION"
		const val NOTIFICATION_ID = "EXPIRE_NOTIFICATION_ID"
		const val EXPIRE_NOTIFICATION_GROUP = "EXPIRE_NOTIFICATION_GROUP"
	}

	override fun onReceive(context: Context, intent: Intent) {
		val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

		val notificationId = intent.getIntExtra(NOTIFICATION_ID, 0)
		val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			intent.getParcelableExtra(NOTIFICATION, Notification::class.java)
		} else {
			intent.getParcelableExtra<Notification>(NOTIFICATION)
		}
		notificationManager.notify(notificationId, notification)
	}
}