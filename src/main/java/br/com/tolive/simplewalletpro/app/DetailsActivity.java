package br.com.tolive.simplewalletpro.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import br.com.tolive.simplewalletpro.R;
import br.com.tolive.simplewalletpro.db.EntryDAO;
import br.com.tolive.simplewalletpro.model.Category;
import br.com.tolive.simplewalletpro.model.Entry;
import br.com.tolive.simplewalletpro.views.CustomTextView;


public class DetailsActivity extends ActionBarActivity {
    private Entry entry;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

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

        CustomTextView txtCategory = (CustomTextView) findViewById(R.id.activity_details_text_category);
        EntryDAO dao = EntryDAO.getInstance(this);
        Category category = dao.getCategory(entry.getCategory());
        if(category != null) {
            txtCategory.setText(category.getName());
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_red));
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
