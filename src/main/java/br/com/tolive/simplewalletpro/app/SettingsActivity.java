package br.com.tolive.simplewalletpro.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import br.com.tolive.simplewalletpro.constants.Constantes;
import br.com.tolive.simplewalletpro.R;
import br.com.tolive.simplewalletpro.views.CustomTextView;


public class SettingsActivity extends Activity {
    //private static final String MSG_ERROR_INTERVAL = "Os valores devem estar entre 0 e 99";
    private static final String MSG_ERROR = "O valor amarelo deve ser maior que o vermelho";
    private static final String EMPTY = "";
    private static final String ERROR_INVALID_INPUT = "Valor inválido";
    EditText editYellow;
    EditText editRed;
    CustomTextView textPercentGreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        AdRequest request = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("E6E54B90007CAC7A62F9EC7857F3A989")
                .build();
        AdView adView = (AdView) findViewById(R.id.ad_settings);
        adView.loadAd(request);

        editYellow = (EditText) findViewById(R.id.fragment_settings_edit_yellow);
        editRed = (EditText) findViewById(R.id.fragment_settings_edit_red);
        textPercentGreen = (CustomTextView) findViewById(R.id.fragment_settings_text_color_set_percent_green_number);

        SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, MODE_PRIVATE);
        float yellow = sharedPreferences.getFloat(Constantes.SP_KEY_YELLOW, Constantes.SP_YELLOW_DEFAULT);
        float red = sharedPreferences.getFloat(Constantes.SP_KEY_RED, Constantes.SP_RED_DEFAULT);

        textPercentGreen.setText(String.format("+%.0f",yellow));
        editYellow.setText(String.format("%.0f",yellow));
        editRed.setText(String.format("%.0f",red));

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.ic_back);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_confirm) {
            if(editYellow.getText().toString().equals(EMPTY) || editRed.getText().toString().equals(EMPTY)){
                Toast.makeText(this, ERROR_INVALID_INPUT, Toast.LENGTH_SHORT).show();
            } else {
                float yellow = Float.valueOf(editYellow.getText().toString());
                float red = Float.valueOf(editRed.getText().toString());
               // if ((yellow > 0 && yellow < 100) && (yellow > 0 && yellow < 100)) {
                if (yellow > red) {
                    SharedPreferences sharedPreferences = getSharedPreferences(Constantes.SHARED_PREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putFloat(Constantes.SP_KEY_YELLOW, yellow);
                    editor.putFloat(Constantes.SP_KEY_RED, red);

                    editor.commit();

                    setResult(RESULT_OK);
                    finish();
                    return true;
                } else {
                    Toast.makeText(this, MSG_ERROR, Toast.LENGTH_SHORT).show();
                }
               // } else {
                    //Toast.makeText(this, MSG_ERROR_INTERVAL, Toast.LENGTH_SHORT).show();
               // }
            }
        } else if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
