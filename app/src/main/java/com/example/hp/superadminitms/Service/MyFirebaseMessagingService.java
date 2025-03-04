//package com.example.hp.superadminitms.Service;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.TaskStackBuilder;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.util.Log;
//
//import androidx.core.app.NotificationCompat;
//
//import com.example.hp.superadminitms.R;
//import com.example.hp.superadminitms.Activity.Menus.RTORelatedInformation;
////import com.google.firebase.messaging.FirebaseMessagingService;
////import com.google.firebase.messaging.RemoteMessage;
//
//import java.util.Random;
//
//public class MyFirebaseMessagingService extends FirebaseMessagingService {
//    private final String DEFAULT_NOTIFICATION_CHANNEL = "default_notification_channel";
//
//    public static void start(Context context) {
//        Intent startServiceIntent = new Intent(context, MyFirebaseInstanceIDService.class);
//        context.startService(startServiceIntent);
//
//        Intent notificationServiceIntent = new Intent(context, MyFirebaseMessagingService.class);
//        context.startService(notificationServiceIntent);
//    }
//
//    @Override
//    public void onMessageReceived(RemoteMessage remoteMessage) {
//
//        super.onMessageReceived(remoteMessage);
//
//        Log.d("MYLOG", "NotificationTitle:" + remoteMessage.getData().get("title"));
//
//        showNotification(remoteMessage);
//    }
//
//    public void showNotification(RemoteMessage message) {
//        Intent i = new Intent(this, RTORelatedInformation.class);
////        i.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
////        i.setAction(Intent.ACTION_BOOT_COMPLETED);
//        TaskStackBuilder taskStackBuilder = null;
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            taskStackBuilder = TaskStackBuilder.create(this);
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            taskStackBuilder.addParentStack(RTORelatedInformation.class);
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            taskStackBuilder.addNextIntent(i);
//        }
//        PendingIntent pi = null;
//        NotificationCompat.Builder notification = null;
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        int Notification_Id = new Random().nextInt(60000);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            pi = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel(DEFAULT_NOTIFICATION_CHANNEL, "My Default Notifications", NotificationManager.IMPORTANCE_HIGH);
//// Configure the notification channel.
//            notificationChannel.setDescription("Default Channel");
//            notificationChannel.enableVibration(true);
//            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
//            notificationManager.createNotificationChannel(notificationChannel);
//            notification = new NotificationCompat.Builder(this)
//                    .setContentTitle(message.getData().get("title"))
//                    .setContentText(message.getData().get("body"))
//                    .setContentIntent(pi)
//                    .setTicker(message.getData().get("body"))
//                    .setCategory(Notification.CATEGORY_EVENT)
//                    .setSmallIcon(R.drawable.ic_launcher_foreground)
//                    .setPriority(Notification.GROUP_ALERT_ALL)
//                    .setDefaults(Notification.DEFAULT_SOUND)
//                    .setGroupSummary(true)
//                    .setDefaults(Notification.FLAG_NO_CLEAR)
//                    .setDefaults(Notification.FLAG_ONGOING_EVENT)
//                    .setChannelId(DEFAULT_NOTIFICATION_CHANNEL);
//        } else {
//            notification = new NotificationCompat.Builder(this)
//                    .setContentTitle(message.getData().get("title"))
//                    .setContentText(message.getData().get("body"))
//                    .setContentIntent(pi)
//                    .setTicker(message.getData().get("body"))
//                    .setCategory(Notification.CATEGORY_EVENT)
//                    .setSmallIcon(R.drawable.ic_launcher_foreground)
//                    .setPriority(Notification.GROUP_ALERT_ALL)
//                    .setDefaults(Notification.DEFAULT_SOUND)
//                    .setGroupSummary(true)
//                    .setDefaults(Notification.FLAG_NO_CLEAR)
//                    .setDefaults(Notification.FLAG_ONGOING_EVENT)
//                    .setChannelId(DEFAULT_NOTIFICATION_CHANNEL);
//        }
//        notificationManager.notify(Notification_Id, notification.build());
//    }
//}