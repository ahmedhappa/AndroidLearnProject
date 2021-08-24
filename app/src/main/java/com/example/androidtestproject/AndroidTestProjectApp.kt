package com.example.androidtestproject

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp //this annotation is a must to use hilt in project it is used and initialized, to add a container that is attached to the app's lifecycle
class AndroidTestProjectApp : Application() {
    companion object {
        const val NOTIFICATION_CHANNEL_ID = "notification_channel_id"
        private const val NOTIFICATION_CHANNEL_NAME = "My Channel Name"
        const val NOTIFICATION_CHANNEL_ID2 = "notification_channel_id2"
        private const val NOTIFICATION_CHANNEL_NAME2 = "My Channel Name2"
        const val NOTIFICATION_CHANNEL_ID3 = "notification_channel_id3"
        private const val NOTIFICATION_CHANNEL_NAME3 = "My Channel Name3"
        const val NOTIFICATION_CHANNEL_ID4 = "notification_channel_id4"
        private const val NOTIFICATION_CHANNEL_NAME4 = "My Channel Name4"

        const val NOTIFICATION_CHANNEL_GROUP_ID1 = "notification_channel_group_id1"
        private const val NOTIFICATION_CHANNEL_GROUP_NAME1 = "My Channel Group Name1"
        const val NOTIFICATION_CHANNEL_GROUP_ID2 = "notification_channel_group_id2"
        private const val NOTIFICATION_CHANNEL_GROUP_NAME2 = "My Channel Group Name2"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        createNotificationChannelGroup()
    }

    //the best place to define notification channel is here in app class
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /*notification channel importance (high, low, default) affect how the notification shows
                like show it with sound and light or not */
            val notificationChannel =
                NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            /*you can customize some notification channel behaviour here like light , vibration ... , but once it is created
            it will not be changed so if you want to see the effect you have to reinstall the app */
            notificationChannel.description = "This is my Channel"
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun createNotificationChannelGroup() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // ## if you assigned a channel to group you can't change this group until you reinstall the application
            //none grouped notifications will be add to a default notification group called other
            val channelGroup1 = NotificationChannelGroup(NOTIFICATION_CHANNEL_GROUP_ID1, NOTIFICATION_CHANNEL_GROUP_NAME1)
            val channelGroup2 = NotificationChannelGroup(NOTIFICATION_CHANNEL_GROUP_ID2, NOTIFICATION_CHANNEL_GROUP_NAME2)

            val notificationChannel2 =
                NotificationChannel(NOTIFICATION_CHANNEL_ID2, NOTIFICATION_CHANNEL_NAME2, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel2.group = NOTIFICATION_CHANNEL_GROUP_ID1
            val notificationChannel3 =
                NotificationChannel(NOTIFICATION_CHANNEL_ID3, NOTIFICATION_CHANNEL_NAME3, NotificationManager.IMPORTANCE_LOW)
            notificationChannel3.group = NOTIFICATION_CHANNEL_GROUP_ID1
            val notificationChannel4 =
                NotificationChannel(NOTIFICATION_CHANNEL_ID4, NOTIFICATION_CHANNEL_NAME4, NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel4.group = NOTIFICATION_CHANNEL_GROUP_ID2

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannelGroup(channelGroup1)
            notificationManager.createNotificationChannelGroup(channelGroup2)
            notificationManager.createNotificationChannel(notificationChannel2)
            notificationManager.createNotificationChannel(notificationChannel3)
            notificationManager.createNotificationChannel(notificationChannel4)
        }
    }
}