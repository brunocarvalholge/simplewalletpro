package br.com.tolive.simplewalletpro.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;

import br.com.tolive.simplewalletpro.R;
import br.com.tolive.simplewalletpro.adapter.EntriesListAdapter;
import br.com.tolive.simplewalletpro.constants.Constants;
import br.com.tolive.simplewalletpro.db.EntryDAO;
import br.com.tolive.simplewalletpro.model.Entry;
import br.com.tolive.simplewalletpro.utils.DialogAddEntryMaker;
import br.com.tolive.simplewalletpro.views.CustomTextView;

public class EntriesListFragmentFragment extends Fragment implements MenuActivity.OnFiltroApplyListener {
    private static final int FIRST_ELEMENT = 0;
    private static final int DATE_YEAR = 2;
    private static final int NO_ROWS_AFFECTED = 0;
    public static final String EXTRA_KEY_ENTRY_DETAILS = "entry_details";
    public static final double MONTH_WITH_NO_ENTRIES = 0.00;

    ArrayList<Entry> entries;
    Entry entry;
    EntryDAO dao;
    LinearLayout containerBalance;
    ListView entriesList;
    CustomTextView textBalanceNumber;
    CustomTextView textGainNumber;
    CustomTextView textExpenseNumber;
    int month;

    int prevMonth;
    int prevYear;

    public EntriesListFragmentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dao = EntryDAO.getInstance(getActivity());

        Calendar calendar = Calendar.getInstance();
        month = calendar.get(Calendar.MONTH);

        entries = dao.getEntry(month);

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        textBalanceNumber = (CustomTextView) view.findViewById(R.id.fragment_list_text_balance_number);
        textGainNumber = (CustomTextView) view.findViewById(R.id.fragment_list_text_gain_number);
        textExpenseNumber = (CustomTextView) view.findViewById(R.id.fragment_list_text_expense_number);
        entriesList = (ListView) view.findViewById(R.id.fragment_list_list_entries);
        containerBalance = (LinearLayout) view.findViewById(R.id.fragment_list_container_balance);

        registerForContextMenu(entriesList);

        refreshList(entries);

        entriesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                entry = entries.get(position);
                return false;
            }
        });

        entriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent detailsIntent = new Intent(getActivity(), DetailsActivity.class);
                detailsIntent.putExtra(EXTRA_KEY_ENTRY_DETAILS, entries.get(position));

                startActivity(detailsIntent);
            }
        });

        return view;
    }

    @Override
    public void onFiltroApply(ArrayList<Entry> entries) {
        this.entries = entries;
        if(entries.size() > 0){
            month = entries.get(FIRST_ELEMENT).getMonth();
        }
        refreshList(entries);
    }

    private void refreshList(ArrayList<Entry> entries) {
        if(entries.size() == 0){
            textBalanceNumber.setText(String.format("%.2f", MONTH_WITH_NO_ENTRIES));
            textGainNumber.setText(String.format("%.2f", (MONTH_WITH_NO_ENTRIES)));
            textExpenseNumber.setText(String.format("%.2f", (MONTH_WITH_NO_ENTRIES)));
//            ((ActionBarActivity)getActivity()).getSupportActionBar().setIcon(R.drawable.ic_title_red);
            containerBalance.setBackgroundColor(getActivity().getResources().getColor(R.color.red));
            return ;
        }
        EntriesListAdapter adapter = new EntriesListAdapter(entries, getActivity());
        entriesList.setAdapter(adapter);
        Float gain = dao.getGain(month);
        Float expense = dao.getExpense(month);

        textBalanceNumber.setText(String.format("%.2f", (gain - expense)));
        textGainNumber.setText(String.format("%.2f", (gain)));
        textExpenseNumber.setText(String.format("%.2f", (expense)));
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        float yellow = sharedPreferences.getFloat(Constants.SP_KEY_YELLOW, Constants.SP_YELLOW_DEFAULT);
        float red = sharedPreferences.getFloat(Constants.SP_KEY_RED, Constants.SP_RED_DEFAULT);

        ActionBar actionBar = ((ActionBarActivity) getActivity()).getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_red));

        int color;
        if((gain - expense) < red){
//            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_red));
            color = getActivity().getResources().getColor(R.color.red);
        } else if((gain - expense) < yellow){
//            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_yellow));
            color = getActivity().getResources().getColor(R.color.yellow);
        } else{
//            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_green));
            color = getActivity().getResources().getColor(R.color.green);
        }
        containerBalance.setBackgroundColor(color);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        final MenuItem itemEdit = menu.add(getActivity().getResources().getString(R.string.fragment_list_contextmenu_item_edit));
        itemEdit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                prevMonth = entry.getMonth();
                prevYear = Integer.valueOf(entry.getDate().split("/")[DATE_YEAR]);
                DialogAddEntryMaker dialogAddEntryMaker = new DialogAddEntryMaker(getActivity());
                dialogAddEntryMaker.setOnClickOkListener(new DialogAddEntryMaker.OnClickOkListener() {
                    @Override
                    public void onClickOk(Entry entry, int recurrency) {
                        if (dao.update(entry) > NO_ROWS_AFFECTED) {
                            Toast.makeText(getActivity(), R.string.dialog_edit_sucess, Toast.LENGTH_SHORT).show();
                            if (prevMonth != entry.getMonth() || prevYear != Integer.valueOf(entry.getDate().split("/")[DATE_YEAR])) {
                                entries.remove(entry);
                            }
                            refreshList(entries);
                        } else {
                            Toast.makeText(getActivity(), R.string.dialog_edit_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                AlertDialog dialog = dialogAddEntryMaker.makeAddDialog(entry);
                dialog.show();
                return false;
            }
        });

        final MenuItem itemDelete = menu.add(getActivity().getResources().getString(R.string.fragment_list_contextmenu_item_delete));
        itemDelete.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                dao.delete(entry.getId());
                entries.remove(entry);
                refreshList(entries);
                return false;
            }
        });
    }
}
