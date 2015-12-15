package th.ac.tn.gert;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;

public class BackgroundService extends Service {
    private static final String TAG = "BackgroundService";
    private int counter = 1;

    private NotificationManager notificationMgr;
    private ThreadGroup myThreads = new ThreadGroup("ServiceWorker");

    public BackgroundService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.v(TAG, "in onCreate()");
        notificationMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        displayNotificationMessage("Background Service is running");

    }
/*
    public void doClick(View view) {
        switch(view.getId()) {
            case R.id.startBtn:
                Log.v(TAG, "Starting service... counter = " + counter);
                Intent intent = new Intent(this,
                        BackgroundService.class);
                intent.putExtra("counter", counter++);
                startService(intent);
                break;
            case R.id.stopBtn:
                stopService();
        }
    }
*/
    private void stopService(){
        Log.v(TAG, "Stopping service...");
        if(stopService(new Intent(this,BackgroundService.class)))
            Log.v(TAG, "Stop service was successful");
        else
            Log.v(TAG, "stopService was unsuccessful");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void displayNotificationMessage(String message){
        PendingIntent contentIntent = PendingIntent.getActivity(this,0, new Intent(this,Tab1Activity.class),0);
        Notification notification = new NotificationCompat.Builder(this).setContentTitle(message).setContentText("Touch to run off service").setSmallIcon(R.drawable.emo_im_winking).setTicker("Starting up!!!").setContentIntent(contentIntent).setOngoing(true).build();
        notificationMgr.notify(0, notification);
    }
}
