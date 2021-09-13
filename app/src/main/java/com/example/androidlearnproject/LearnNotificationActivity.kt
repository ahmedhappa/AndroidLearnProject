package com.example.androidlearnproject

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import android.support.v4.media.session.MediaSessionCompat
import android.widget.RemoteViews
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.*
import com.example.androidlearnproject.databinding.ActivityLearnNotificationBinding

// ## notification channel is created in the app class (best place to create notification channel)
class LearnNotificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLearnNotificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLearnNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*use NotificationManagerCompat compat instead of normal NotificationManager to show notifications
         because it handles the notification states and sdk checks in loser api than oreo by it self*/
        val notificationManager = NotificationManagerCompat.from(this)
        binding.btnShowNotification.setOnClickListener {

            if (!notificationManager.areNotificationsEnabled()) {
                openAppNotificationSettings()
                return@setOnClickListener
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                && isNotificationChannelBlocked(AndroidLearnProjectApp.NOTIFICATION_CHANNEL_ID)
            ) {
                openChannelSetting(AndroidLearnProjectApp.NOTIFICATION_CHANNEL_ID)
                return@setOnClickListener
            }

            val myNotification = buildCustomNotification()

            /* ## if you passed the same id for every notification it will override the existing notification, so
          if you want to show more than one notification pass a different id for each notification
           */
            notificationManager.notify(1, myNotification.build())
        }

        binding.btnDeleteNotificationChannel.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                /* ## if you deleted a channel and created the same channel again it will return with same settings before it was deleted
            so there is no reason to delete and create channel for resting it 's properties */
                notificationManager.deleteNotificationChannel(AndroidLearnProjectApp.NOTIFICATION_CHANNEL_ID3)
                //we can also delete a notification channel group
                notificationManager.deleteNotificationChannelGroup(AndroidLearnProjectApp.NOTIFICATION_CHANNEL_GROUP_ID1)
            }
        }

    }


    private fun openAppNotificationSettings() {
        // you need to handle different notification settings for different versions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
                startActivity(this)
            }
        } else {
            startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:".plus(packageName))))
        }
    }

    @RequiresApi(26)
    private fun isNotificationChannelBlocked(channelId: String): Boolean {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = notificationManager.getNotificationChannel(channelId)
        return channel != null &&
                channel.importance == NotificationManager.IMPORTANCE_NONE
    }

    @RequiresApi(26)
    private fun openChannelSetting(channelId: String) {
        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
            putExtra(Settings.EXTRA_CHANNEL_ID, channelId)
            startActivity(this)
        }
    }

    private fun buildNormalNotification(): NotificationCompat.Builder {
        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.img_spider_man)

        return NotificationCompat.Builder(this, AndroidLearnProjectApp.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_star) // this icon is mandatory to the notification to be shown
            .setContentTitle("Notification Title")
            .setContentText("Notification Text")
            .setLargeIcon(largeIcon) // this icon is optional
            .setPriority(NotificationCompat.PRIORITY_HIGH) // this is the same for notification channel but for sdk lower than oreo
            .setColor(Color.BLUE)
            .setContentIntent(createNotificationPendingIntent())
            .setAutoCancel(true) // the notification is automatically canceled when the user clicks it in the panel
            .setOnlyAlertOnce(true) // sound, vibrate and ticker to be played if the notification is not already showing.
            // ## action icon will not appear on api 26 and higher
            //you can add up to 3 actions only
            .addAction(R.drawable.ic_refresh, "Action Name", createActionPendingIntent())
    }


    private fun createActionPendingIntent(): PendingIntent? {
        val receiverIntent = Intent(this, MyBroadCastReceiver::class.java).apply {
            putExtra("extra_data", "Hello from notification action")
        }
        //            this flag is to update intent with new when new notification triggers
        return PendingIntent.getBroadcast(this, 2, receiverIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun createNotificationPendingIntent(): PendingIntent? {
        val activityIntent = Intent(this, LearnNotificationActivity::class.java)
        return TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(activityIntent)
            //flags is to determine what will happen to the current intent if a new notification triggered
//            this flag is to not update intent when new notification triggers
            getPendingIntent(1, PendingIntent.FLAG_ONE_SHOT)
        }
    }

    private fun buildBigTextStyleNotification(): NotificationCompat.Builder {
        return buildNormalNotification().setStyle(
            // ## this style has a limit to 450 character
            NotificationCompat.BigTextStyle() // add a big text to notification to be shown on expanded state
                .bigText(getString(R.string.str_dummy))
                .setSummaryText("My Summary Text")
                .setBigContentTitle("My Big Content Title Big Text Style") // this title override the content title
        )
    }

    private fun buildLinesStyleNotification(): NotificationCompat.Builder {
        return buildNormalNotification().setStyle(
            // ## this style has a limit to 6 lines after 6 nothing will be shown
            NotificationCompat.InboxStyle() // add a lines to notification to be shown on expanded state
                .addLine("this is my Line 1")
                .addLine("this is my Line 2")
                .addLine("this is my Line 3")
                .addLine("this is my Line 4")
                .addLine("this is my Line 5")
                .addLine("this is my Line 6")
                .addLine("this is my Line 7") // ## this line won't be shown
                .addLine("this is my Line 8") // ## this line won't be shown
                .setSummaryText("My Summary Text")
                .setBigContentTitle("My Big Content Title Line Style") // this title override the content title
        )
    }

    private fun buildImageStyleNotification(): NotificationCompat.Builder {
        val bigPicture = BitmapFactory.decodeResource(resources, R.drawable.img_spider_man)
        return buildNormalNotification().setStyle(
            NotificationCompat.BigPictureStyle()
                .bigPicture(bigPicture)
                .bigLargeIcon(null) //setting this to null will hide large icon in expand state or you can set another pic to show it in expand state
        )
    }

    // ## we need to add a dependency for this style implementation 'androidx.media:media:1.4.0'
    private fun buildMediaStyleNotification(): NotificationCompat.Builder {
        /*to make notification background colored with image colors in android oreo and Above you have to attach media style with
        media session */
        /*this media session is used to attach media with notification but it doesn't implemented here we just created it here
        to get the feature of coloring the background of notification */
        val mediaSession = MediaSessionCompat(this, "media_session_tag")

        val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.img_spider_man)

        return NotificationCompat.Builder(this, AndroidLearnProjectApp.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_star) // this icon is mandatory to the notification to be shown
            .setContentTitle("Notification Title")
            .setContentText("Notification Text")
            .setColor(Color.BLUE)
            .setLargeIcon(largeIcon)
            .addAction(R.drawable.ic_like, "Like", null) // you can add action to this button with pending intent
            .addAction(R.drawable.ic_play, "Play", null) // you can add action to this button with pending intent
            .addAction(R.drawable.ic_pause, "Pause", null) // you can add action to this button with pending intent
            .addAction(R.drawable.ic_refresh, "Refresh", null) // you can add action to this button with pending intent
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    // here we pass var args of the index of action buttons to be shown in collapsed state starting from 0 index (play / pause)
                    .setShowActionsInCompactView(
                        1, 2
                    )
                    .setMediaSession(mediaSession.sessionToken)
            )
            .setSubText("My Sub Text")
    }

    companion object {
        val listOfMessages = mutableListOf(
            Message("Hello", "Ali"),
            Message("hi how are you", null), // pass null to indicate that is the current user
            Message("I am Fine and you", "Ali")
        )

        fun buildMessageStyleNotification(context: Context): NotificationCompat.Builder {
            //because remoteInput is enabled only in apu N+ we need to direct user to chat screen if it device is below N+
            val replyIntent: Intent
            val replyPendingIntent: PendingIntent
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                replyIntent = Intent(context, MessageStyleBroadCast::class.java)
                replyPendingIntent = PendingIntent.getBroadcast(context, 20, replyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            } else {
                replyIntent = Intent(context, LearnNotificationActivity::class.java)
                replyPendingIntent = PendingIntent.getActivity(context, 20, replyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            }

            val replyActionBuilder = NotificationCompat.Action.Builder(
                R.drawable.ic_reply,
                "Reply",
                replyPendingIntent
            )

            val replyAction = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //this is the edit text that is used to get data from user
                //this key is used to retrieve data
                //this remote input works for api N+ 24 only
                val remoteInput = RemoteInput.Builder("key_notification_remote_input")
                    // hint to be shown
                    .setLabel("Send Message...")
                    .build()
                replyActionBuilder.addRemoteInput(remoteInput).build()
            } else {
                replyActionBuilder.build()
            }


            val currentPerson = Person.Builder()
                .setName("Me")
                .build()
            val messageStyle = NotificationCompat.MessagingStyle(currentPerson)
            //it is recommended to use conversationTitle in group chat only
            messageStyle.conversationTitle = "Conversation Title"
            listOfMessages.forEach {
                val senderPerson = Person.Builder().setName(it.sender).build()
                val notificationMessage = NotificationCompat.MessagingStyle.Message(
                    it.text, it.timeStamp, senderPerson
                )
                messageStyle.addMessage(notificationMessage)
            }

            return NotificationCompat.Builder(context, AndroidLearnProjectApp.NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_star)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(Color.BLUE)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setStyle(messageStyle)
                .addAction(replyAction)
        }
    }

    private fun showProgressNotification() {
        val maxProgress = 100 //it can be any number
        val notificationManager = NotificationManagerCompat.from(this)

        val notification = NotificationCompat.Builder(this, AndroidLearnProjectApp.NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Download")
            .setContentText("Downloading In Progress")
            .setSmallIcon(R.drawable.ic_refresh)
            .setColor(Color.RED)
            //Set whether this is an ongoing notification. Ongoing notifications cannot be dismissed by the user
            .setOngoing(true)
            //setting indeterminate to true means to ignore maxProgress and progress parameters and show only a loading animation without progress
            .setProgress(maxProgress, 0, false)
            .setOnlyAlertOnce(true)

        notificationManager.notify(44, notification.build())

        Thread {
            SystemClock.sleep(2000)
            for (i in 0..maxProgress step 10) {
                notification.setProgress(maxProgress, i, false)
                notificationManager.notify(44, notification.build())
                SystemClock.sleep(1000)
            }
            notification.setOngoing(false)
            notification.setContentText("Download Finished")
            notification.setProgress(0, 0, false)
            notificationManager.notify(44, notification.build())
        }.start()

    }

    private fun groupNotification() {
        val notificationManager = NotificationManagerCompat.from(this)

        val notification1 = NotificationCompat.Builder(this, AndroidLearnProjectApp.NOTIFICATION_CHANNEL_ID)
            .setContentTitle("title1")
            .setContentText("message1")
            .setSmallIcon(R.drawable.ic_refresh)
            .setColor(Color.RED)
            .setGroup("group_notification_group")
            .build()

        val notification2 = NotificationCompat.Builder(this, AndroidLearnProjectApp.NOTIFICATION_CHANNEL_ID)
            .setContentTitle("title2")
            .setContentText("message2")
            .setSmallIcon(R.drawable.ic_add)
            .setColor(Color.BLUE)
            .setGroup("group_notification_group")
            .build()


        //this notification is used to group notifications
        val summaryNotification = NotificationCompat.Builder(this, AndroidLearnProjectApp.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_play)
            .setColor(Color.BLUE)
            .setGroup("group_notification_group")
            .setGroupSummary(true) // tells the system that this isn't a normal notification and is used to group notifications
            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN) //how alert will be used in this group
            .build()


        val notification3 = NotificationCompat.Builder(this, AndroidLearnProjectApp.NOTIFICATION_CHANNEL_ID)
            .setContentTitle("title3")
            .setContentText("message3")
            .setSmallIcon(R.drawable.ic_like)
            .setColor(Color.BLACK)
            .setGroup("group_notification_group")
            .build()


        notificationManager.notify(1, notification1)
        SystemClock.sleep(1000)
        notificationManager.notify(2, notification2)
        SystemClock.sleep(1000)
        notificationManager.notify(3, summaryNotification)
        SystemClock.sleep(1000)
        notificationManager.notify(4, notification3)
    }

    private fun buildCustomNotification(): NotificationCompat.Builder {
        /*because notification doesn't run in the app process but in the system process we need to pass views using RemoteViews
        helper class because we can't access it directly for security */
        // ## remote view doesn't support all views and view groups to see the list of supported views go to class
        val collapsedView = RemoteViews(packageName, R.layout.notification_custom_collapsed)
        val expandedView = RemoteViews(packageName, R.layout.notification_custom_expanded)

        //we can make some limited actions to views in the layout again it is because the layout is not in the app process
        collapsedView.setTextViewText(R.id.tv1_notification_collapsed, "Text Is Set Programmatically")

        val pendingIntent = PendingIntent.getActivity(
            this, 50,
            Intent(this, LearnNotificationActivity::class.java), PendingIntent.FLAG_IMMUTABLE
        )
        //add action to custom view
        expandedView.setOnClickPendingIntent(R.id.iv_notification_expanded, pendingIntent)

        return NotificationCompat.Builder(this, AndroidLearnProjectApp.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_cancel)
            .setCustomContentView(collapsedView)
            .setCustomBigContentView(expandedView)
            //.setCustomHeadsUpContentView() //this is optional to add a custom view if the notification is showed when pop up but if this view is not set the collapsed view will be shown
            .setStyle(NotificationCompat.DecoratedCustomViewStyle()) // add some styles to custom notification like header text.
    }

}

class MessageStyleBroadCast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val remoteInput = RemoteInput.getResultsFromIntent(intent)
        //this can be null so we need to check first
        context?.also {
            remoteInput?.also {
                //same key used to initialize  remote input
                val text = remoteInput.getCharSequence("key_notification_remote_input")
                val message = Message(text.toString(), null) // pass null to indicate that is the current user
                //here you can call api to send data to the backend
                LearnNotificationActivity.listOfMessages.add(message)
                val notificationManager = NotificationManagerCompat.from(context)
                val myNotification = LearnNotificationActivity.buildMessageStyleNotification(context)
                //notify with the same message id to remove the old one
                notificationManager.notify(1, myNotification.build())
            }
        }
    }
}

class MyBroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("extra_data")
        Toast.makeText(context, message ?: "", Toast.LENGTH_LONG).show()
    }
}

data class Message(
    val text: CharSequence,
    val sender: CharSequence?,
    val timeStamp: Long = System.currentTimeMillis() //time stamp is mandatory for the library
)