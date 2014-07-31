package br.com.tolive.simplewalletpro.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.ActionBar;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import br.com.tolive.simplewalletpro.R;
import br.com.tolive.simplewalletpro.model.Entry;
import br.com.tolive.simplewalletpro.views.CustomTextView;


public class DetailsActivity extends Activity {
    private Entry entry;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        AdRequest request = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("E6E54B90007CAC7A62F9EC7857F3A989")
                .build();
        AdView adView = (AdView) findViewById(R.id.ad_details);
        adView.loadAd(request);

        Intent intent = getIntent();
        this.entry = (Entry) intent.getSerializableExtra(EntriesListFragmentFragment.EXTRA_KEY_ENTRY_DETAILS);

        RelativeLayout container = (RelativeLayout) findViewById(R.id.activity_details_container);

        if(entry.getType() == Entry.TYPE_GAIN){
            container.setBackgroundColor(getResources().getColor(R.color.green));
        } else{
            container.setBackgroundColor(getResources().getColor(R.color.red));
        }

        CustomTextView txtDescription = (CustomTextView) findViewById(R.id.activity_details_text_description);
        txtDescription.setText(entry.getDescription());

        CustomTextView txtValue = (CustomTextView) findViewById(R.id.activity_details_text_value);
        txtValue.setText(String.format("%.2f", entry.getValue()));

        CustomTextView txtDate = (CustomTextView) findViewById(R.id.activity_details_text_date);
        txtDate.setText(entry.getDate());

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.ic_back);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
