package br.com.tolive.simplewalletpro.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import br.com.tolive.simplewalletpro.R;
import br.com.tolive.simplewalletpro.model.Category;

/**
 * Created by Bruno on 09/08/2014.
 */
public class EditCategoriesExpListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<Category>> listDataChild;

    public EditCategoriesExpListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<Category>> listChildData) {
        this._context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final Category category = (Category) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.view_list_edit_categories, null);
        }

        Resources resources = _context.getResources();

        TypedArray colors = resources.obtainTypedArray(R.array.categoryColors);
        convertView.setBackgroundColor(resources.getColor(colors.getResourceId(category.getColor(), resources.getColor(R.color.gray))));
        colors.recycle();

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.textView_list_category_name);

        txtListChild.setText(category.getName());

        return convertView;
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
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.view_list_edit_categories_head, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.textView_list_category_type);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
