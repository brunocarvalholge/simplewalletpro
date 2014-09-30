package br.com.tolive.simplewalletpro.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AddRecurrentService extends Service {
    public AddRecurrentService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
