package br.com.tolive.simplewalletpro.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.tolive.simplewalletpro.R;
import br.com.tolive.simplewalletpro.adapter.EditCategoriesExpListAdapter;
import br.com.tolive.simplewalletpro.adapter.RecurrentExpListAdapter;
import br.com.tolive.simplewalletpro.model.Entry;
import br.com.tolive.simplewalletpro.utils.DialogAddEntryMaker;
import br.com.tolive.simplewalletpro.utils.RecurrentsManager;

public class RecurrentActivity extends Activity {
    public static final int EXPANDAPLE_LIST_HEADER_SIZE = 50;
    public static final int EXPANDAPLE_LIST_CHILD_SIZE = 40;

    private ExpandableListView listCurrents;
    private RecurrentExpListAdapter adapter;
    private int listSize = 0;
    private List<String> listDataHeader;
    private HashMap<String, List<Entry>> listDataChild;
    private Entry selectedEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recurrent);

        listCurrents = (ExpandableListView) findViewById(R.id.activity_recurrent_list);

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<Entry>>();

        listDataHeader.add(getResources().getString(R.string.activity_recurrent_text_daily));
        listDataHeader.add(getResources().getString(R.string.activity_recurrent_text_monthly));

        RecurrentsManager recurrentsManager = new RecurrentsManager(this);

        listDataChild.put(listDataHeader.get(0), recurrentsManager.getRecurrentDaily());
        listDataChild.put(listDataHeader.get(1), recurrentsManager.getRecurrentMonthly());

        adapter = new RecurrentExpListAdapter(this, listDataHeader, listDataChild);

        registerForContextMenu(listCurrents);

        listCurrents.setOnItemLongClickListener(new ExpandableListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);

                    selectedEntry = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                    return false;
                }

                return false;
            }
        });

        final DisplayMetrics metrics;
        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        /*adapter.setOnUpdateListListener(new EditCategoriesExpListAdapter.OnUpdateListListener() {
            @Override
            public void onUpdate(int oldListSize, int newListSize) {
                listSize += (newListSize - oldListSize) * EXPANDAPLE_LIST_CHILD_SIZE;
                setDailyListHeight(metrics);
            }
        });*/

        listSize = listDataHeader.size()* EXPANDAPLE_LIST_HEADER_SIZE;
        setDailyListHeight(metrics);

        listCurrents.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int id) {
                listSize += listDataChild.get(listDataHeader.get(id)).size() * EXPANDAPLE_LIST_CHILD_SIZE;
                setDailyListHeight(metrics);
            }
        });

        listCurrents.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int id) {
                listSize -= listDataChild.get(listDataHeader.get(id)).size() * EXPANDAPLE_LIST_CHILD_SIZE;
                setDailyListHeight(metrics);
            }
        });

        listCurrents.setAdapter(adapter);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setIcon(R.drawable.ic_back);
    }

    private void setDailyListHeight(DisplayMetrics metrics) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) listCurrents.getLayoutParams();

        int height = getDPI(listSize, metrics);
        params.height = height;
        listCurrents.setLayoutParams(params);
    }

    public static int getDPI(int size, DisplayMetrics metrics){
        return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.recurrent, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_recurrent) {

            finish();
            return true;
        } else if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        final RecurrentsManager recurrentsManager = new RecurrentsManager(this);
        final Resources resources = this.getResources();

        Log.d("TAG", "ContextMenu");

        ExpandableListView.ExpandableListContextMenuInfo info =
                (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;

        int type =
                ExpandableListView.getPackedPositionType(info.packedPosition);

        final int groupPosition =
                ExpandableListView.getPackedPositionGroup(info.packedPosition);

        final int childPosition =
                ExpandableListView.getPackedPositionChild(info.packedPosition);

        // Only create a context menu for child items
        if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            final DisplayMetrics metrics;
            metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            final MenuItem itemEdit = menu.add(this.getResources().getString(R.string.fragment_list_contextmenu_item_edit));
            itemEdit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    DialogAddEntryMaker dialogAddEntryMaker = new DialogAddEntryMaker(RecurrentActivity.this);
                    dialogAddEntryMaker.setOnClickOkListener(new DialogAddEntryMaker.OnClickOkListener() {
                        @Override
                        public void onClickOk(Entry entry, int recurrency) {
                            recurrentsManager.edit(entry, recurrency);
                            if(entry.getType() == Entry.TYPE_GAIN) {
                                Toast.makeText(RecurrentActivity.this, resources.getString(R.string.activity_recurrent_text_edit_sucess_gain), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RecurrentActivity.this, resources.getString(R.string.activity_recurrent_text_edit_sucess_expense), Toast.LENGTH_SHORT).show();
                            }
                            refresh(groupPosition);
                        }
                    });
                    AlertDialog dialog = dialogAddEntryMaker.makeAddDialog(selectedEntry);
                    dialog.show();

                    return false;
                }
            });

            final MenuItem itemDelete = menu.add(this.getResources().getString(R.string.fragment_list_contextmenu_item_delete));
            itemDelete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    recurrentsManager.remove(selectedEntry);
                    refresh(groupPosition);
                    if(selectedEntry.getType() == Entry.TYPE_GAIN) {
                        Toast.makeText(RecurrentActivity.this, resources.getString(R.string.activity_recurrent_text_remove_sucess_gain), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RecurrentActivity.this, resources.getString(R.string.activity_recurrent_text_remove_sucess_expense), Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });
        }
    }

    private void refresh(int groupPosition) {
        RecurrentsManager recurrentsManager = new RecurrentsManager(this);
        if(groupPosition == 0) {
            listDataChild.remove(listDataHeader.get(0));
            listDataChild.put(listDataHeader.get(0), recurrentsManager.getRecurrentDaily());
            adapter.setListDataChild(listDataChild);
        } else if(groupPosition == 1 ){
            listDataChild.remove(listDataHeader.get(1));
            listDataChild.put(listDataHeader.get(1), recurrentsManager.getRecurrentMonthly());
            adapter.setListDataChild(listDataChild);
        }
        adapter.notifyDataSetChanged();
    }
}
