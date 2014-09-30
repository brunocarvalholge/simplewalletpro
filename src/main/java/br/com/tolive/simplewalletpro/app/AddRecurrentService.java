package br.com.tolive.simplewalletpro.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import br.com.tolive.simplewalletpro.db.EntryDAO;
import br.com.tolive.simplewalletpro.model.Entry;
import br.com.tolive.simplewalletpro.utils.RecurrentsManager;

public class AddRecurrentService extends Service {
    public static final String EXTRA_ENTRY = "extra_entry";
    public static final String EXTRA_RECURRENCY = "extra_recurrrency";

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
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
}
