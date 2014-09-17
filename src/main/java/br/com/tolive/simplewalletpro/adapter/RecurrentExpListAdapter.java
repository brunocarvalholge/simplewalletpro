package br.com.tolive.simplewalletpro.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

import br.com.tolive.simplewalletpro.R;
import br.com.tolive.simplewalletpro.app.DetailsActivity;
import br.com.tolive.simplewalletpro.app.EntriesListFragmentFragment;
import br.com.tolive.simplewalletpro.db.EntryDAO;
import br.com.tolive.simplewalletpro.model.Category;
import br.com.tolive.simplewalletpro.model.Entry;

/**
 * Created by bruno.carvalho on 15/09/2014.
 */
public class RecurrentExpListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<Entry>> listDataChild;
    private AlertDialog dialog;

    public RecurrentExpListAdapter(Context context, List<String> listDataHeader,
                                        HashMap<String, List<Entry>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.view_list_recurrent_head, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.textView_list_recurrent_type);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Entry entry = (Entry) getChild(groupPosition, childPosition);

        LayoutInflater inflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_list_recurrent, null);

        Resources resources = context.getResources();
        //if(!isLastChild) {
            switch (entry.getType()){
                case Entry.TYPE_GAIN:
                    view.setBackgroundColor(resources.getColor(R.color.green));
                    break;
                case Entry.TYPE_EXPENSE:
                    view.setBackgroundColor(resources.getColor(R.color.red));
                    break;
            }

            TextView txtListChildName = (TextView) view
                    .findViewById(R.id.textView_list_recurrent_name);
            txtListChildName.setText(entry.getDescription());
            TextView txtListChildValue = (TextView) view
                    .findViewById(R.id.textView_list_recurrent_value);
            txtListChildValue.setText(String.valueOf(entry.getValue()));

            /*view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent detailsIntent = new Intent(context, DetailsActivity.class);
                    detailsIntent.putExtra(EntriesListFragmentFragment.EXTRA_KEY_ENTRY_DETAILS, entry);
                    context.startActivity(detailsIntent);
                }
            });*/

        /*} else {
            view.setBackgroundColor(resources.getColor(R.color.snow));
            TextView txtListChildName = (TextView) view
                    .findViewById(R.id.textView_list_recurrent_name);
            txtListChildName.setTextColor(resources.getColor(R.color.gray));
            TextView txtListChildValue = (TextView) view
                    .findViewById(R.id.textView_list_recurrent_value);
            txtListChildValue.setVisibility(View.GONE);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "add recorrente", Toast.LENGTH_SHORT).show();
                }
            });
        }*/

        return view;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setListDataHeader(List<String> listDataHeader) {
        this.listDataHeader = listDataHeader;
    }

    public void setListDataChild(HashMap<String, List<Entry>> listDataChild) {
        this.listDataChild = listDataChild;
    }
}
