package fine.koacaiia.mnfwms;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class FcmProcessService extends FirebaseMessagingService{
    public static final String NOTIFICATION_CHANNEL_ID = "10001";

    public FcmProcessService(){

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        Map<String,String> data=remoteMessage.getData();
        String contents=data.get("content");
        String depotName=data.get("depotName");
        String Bl=data.get("Bl");

       notificationMessage(depotName,contents,Bl);

    }

    private void notificationMessage(String nickName, String contents, String bl) {
        Intent intent=new Intent(this,WebList.class);
        intent.putExtra("Bl",bl);
        PendingIntent contentIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        Intent mIntent=new Intent(this,MainActivity.class);
        mIntent.putExtra("Bl",bl);
        PendingIntent mContentIntent=PendingIntent.getActivity(this,0,mIntent,PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder builder=getNotificationBuilder(nickName,bl)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(nickName)
                .setContentText(contents)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(contents))
                .setContentIntent(mContentIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .addAction(R.mipmap.ic_launcher,"Confirm",contentIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        String notificationChannelId=bl.substring(bl.length()-4);
        Log.i("duatjsrb",notificationChannelId);

        notificationManager.notify(Integer.parseInt(notificationChannelId),builder.build());

    }



    private NotificationCompat.Builder getNotificationBuilder(String ask, String alert) {
        NotificationCompat.Builder builder=null;
        NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel=new NotificationChannel(ask,alert,NotificationManager.IMPORTANCE_HIGH);
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        manager.createNotificationChannel(channel);
        builder=new NotificationCompat.Builder(this,ask);
        return builder;
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.i("koacaiia","OnNewToken Called:"+s);
    }
}
