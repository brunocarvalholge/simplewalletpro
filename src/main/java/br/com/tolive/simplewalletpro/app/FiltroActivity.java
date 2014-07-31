package br.com.tolive.simplewalletpro.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Spinner;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.tolive.simplewalletpro.R;
import br.com.tolive.simplewalletpro.adapter.CustomSpinnerAdapter;
import br.com.tolive.simplewalletpro.db.EntryDAO;
import br.com.tolive.simplewalletpro.model.Entry;


public class FiltroActivity extends Activity {
    public static final String EXTRA_KEY_FILTRO_ENTRIES = "entries_filtro";
    Spinner spinnerMonth;
    Spinner spinnerYear;

    private String[] months;
    private String[] years;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);

        AdRequest request = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("E6E54B90007CAC7A62F9EC7857F3A989")
                .build();
        AdView adView = (AdView) findViewById(R.id.ad_filtro);
        adView.loadAd(request);

        months = getResources().getStringArray(R.array.spinner_months);
        years = getResources().getStringArray(R.array.spinner_years);

        spinnerMonth = (Spinner) findViewById(R.id.fragment_filtro_spinner_month);
        spinnerYear = (Spinner) findViewById(R.id.fragment_filtro_spinner_year);
        Calendar calendar = Calendar.getInstance();

        CustomSpinnerAdapter adapterMonth = new CustomSpinnerAdapter(this, R.layout.simple_spinner_item, months);
        adapterMonth.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(adapterMonth);
        spinnerMonth.setSelection(calendar.get(Calendar.MONTH));

        CustomSpinnerAdapter adapterYear = new CustomSpinnerAdapter(this, R.layout.simple_spinner_item, years);
        adapterYear.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(adapterYear);
        spinnerYear.setSelection(getYear(calendar.get(Calendar.YEAR), years));

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.ic_back);
    }

    private int getYear(int currentYear, String[] years) {
        for (int i = 0; i < years.length; i++){
            if(years[i].equals(String.valueOf(currentYear))){
                return i;
            }
        }
        return 0;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filtro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_filtrar) {
            EntryDAO dao = EntryDAO.getInstance(this);
            ArrayList<Entry> entries = dao.getEntry(String.valueOf(spinnerMonth.getSelectedItemPosition()+1), (String) spinnerYear.getSelectedItem());

            Intent returnIntent = new Intent();
            returnIntent.putExtra(EXTRA_KEY_FILTRO_ENTRIES, entries);
            setResult(RESULT_OK, returnIntent);
            finish();
            return true;
        } else if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
