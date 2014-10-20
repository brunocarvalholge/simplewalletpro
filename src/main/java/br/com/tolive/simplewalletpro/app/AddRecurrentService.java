package br.com.tolive.simplewalletpro.app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import br.com.tolive.simplewalletpro.R;
import br.com.tolive.simplewalletpro.db.EntryDAO;
import br.com.tolive.simplewalletpro.model.Entry;
import br.com.tolive.simplewalletpro.utils.RecurrentsManager;

public class AddRecurrentService extends Service {
    public static final String EXTRA_ENTRY = "extra_entry";
    public static final String EXTRA_RECURRENCY = "extra_recurrrency";
    public static final int NOTI_ID_RECURRENT = 0;

    private EntryDAO dao;
    private RecurrentsManager recurrentsManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        this.dao = EntryDAO.getInstance(this);
        this.recurrentsManager = new RecurrentsManager(this);

        Entry entry = (Entry) intent.getSerializableExtra(EXTRA_ENTRY);
        int recurrency = intent.getIntExtra(EXTRA_RECURRENCY, -1);

        Log.d("TESTE", "[AddService] \nentry: " + entry.toString() + "\n recurrency: " + recurrency);

        switch (recurrency){
            case RecurrentsManager.RECURRENT_NORMAL:
                dao.insert(entry);
                Log.d("TESTE", "[AddService][NORMAL] \nentry: " + dao.getEntry(8).toString() + "\n recurrency: " + recurrency);
                break;
            case RecurrentsManager.RECURRENT_DAILY:
                dao.insert(entry);
                Log.d("TESTE", "[AddService][DAILY] \nentry: " + dao.getEntry(8).toString() + "\n recurrency: " + recurrency);
                recurrentsManager.setAlarm(entry, recurrency);
                break;
            case RecurrentsManager.RECURRENT_MONTHY:
                dao.insert(entry);
                Log.d("TESTE", "[AddService]{MONTHLY] \nentry: " + dao.getEntry(8).toString() + "\n recurrency: " + recurrency);
                recurrentsManager.setAlarm(entry, recurrency);
                break;
        }

        //CreateNotification
        createNotification();

        return super.onStartCommand(intent, flags, startId);
    }

    private void createNotification() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Gastos Atualizados")
                        .setContentText("Seus gastos salvos foram debitados, clique e confira seu novo saldo")
                        .setAutoCancel(true);
        // Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MenuActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(NOTI_ID_RECURRENT, mBuilder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
}
