package br.com.tolive.simplewalletpro.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.tolive.simplewalletpro.R;
import br.com.tolive.simplewalletpro.db.EntryDAO;
import br.com.tolive.simplewalletpro.model.Category;
import br.com.tolive.simplewalletpro.model.Entry;
import br.com.tolive.simplewalletpro.utils.DialogAddCategoryMaker;

/**
 * Created by Bruno on 09/08/2014.
 */
public class EditCategoriesExpListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<Category>> listDataChild;
    private DisplayMetrics metrics;
    private EntryDAO dao;
    private AlertDialog dialog;
    private OnUpdateListListener mListener;

    public EditCategoriesExpListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<Category>> listChildData, DisplayMetrics metrics) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
        this.dao = EntryDAO.getInstance(context);
        this.metrics = metrics;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final int type = groupPosition;
        final Category category = (Category) getChild(groupPosition, childPosition);

        //if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = infalInflater.inflate(R.layout.view_list_edit_categories, null);
        //}

        Resources resources = context.getResources();
        if(!isLastChild) {
            TypedArray colors = resources.obtainTypedArray(R.array.categoryColors);
            view.setBackgroundColor(resources.getColor(colors.getResourceId(category.getColor(), resources.getColor(R.color.gray))));
            colors.recycle();

            TextView txtListChild = (TextView) view
                    .findViewById(R.id.textView_list_category_name);
            txtListChild.setTextColor(resources.getColor(R.color.snow));

            txtListChild.setText(category.getName());

            //ImageView imageAdd = (ImageView) convertView
            //        .findViewById(R.id.imageView_list_category_edit);
            //TODO : Add button ic_edit_category image
            //imageAdd.setImageDrawable(resources.getDrawable(R.drawable.button_add_old));

            //imageAdd.setOnClickListener(new View.OnClickListener() {
            //    @Override
            //    public void onClick(View view) {
            /*        DialogAddCategoryMaker dialogAddCategory = new DialogAddCategoryMaker(context, metrics, type);
                    dialogAddCategory.setOnClickOkListener(new DialogAddCategoryMaker.OnClickOkListener() {
                        @Override
                        public void onClickOk(Category category, boolean isNew) {
                            if(dao.updateCategory(category) != 0){
                                Toast.makeText(context, R.string.dialog_edit_categoty_sucess, Toast.LENGTH_SHORT).show();
                                //updateList(category, type, isNew);
                                notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, R.string.dialog_edit_categoty_fail, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialog = dialogAddCategory.makeAddCategryDialog(category);
                    dialog.show();*/
            //   }
            //});
        } else {
            view.setBackgroundColor(resources.getColor(R.color.snow));
            TextView txtListChild = (TextView) view
                    .findViewById(R.id.textView_list_category_name);
            txtListChild.setText(category.getName());
            txtListChild.setTextColor(resources.getColor(R.color.gray));

            //ImageView imageAdd = (ImageView) convertView
            //        .findViewById(R.id.imageView_list_category_edit);
            //TODO : Add button ic_add_category image
            //imageAdd.setImageDrawable(resources.getDrawable(R.drawable.ic_add));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogAddCategoryMaker dialogAddCategory = new DialogAddCategoryMaker(context, metrics, type);
                    dialogAddCategory.setOnClickOkListener(new DialogAddCategoryMaker.OnClickOkListener() {
                        @Override
                        public void onClickOk(Category category, boolean isNew) {
                            long id = dao.insertCategory(category);
                            if (id != -1) {
                                Toast.makeText(context, R.string.dialog_add_categoty_sucess, Toast.LENGTH_SHORT).show();
                                category.setId(id);
                                updateList(category, type, isNew);
                            } else {
                                Toast.makeText(context, R.string.dialog_add_categoty_fail, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    dialog = dialogAddCategory.makeAddCategryDialog(null);
                    dialog.show();
                }
            });
        }
        return view;
    }

    private void updateList(Category category, int groupPosition, boolean isNew) {
        int oldSize = this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
        if(isNew) {
            this.listDataChild.get(this.listDataHeader.get(groupPosition)).add(oldSize - 1, category);
        }
        int newSize = this.listDataChild.get(this.listDataHeader.get(groupPosition)).size();
        try{
            mListener.onUpdate(oldSize, newSize);
        } catch (NullPointerException e){
            throw new RuntimeException(e + "\nMost implement setOnUpdateListListener");
        }
        this.notifyDataSetChanged();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public void setOnUpdateListListener (OnUpdateListListener listener){
        mListener = listener;
    }

    public interface OnUpdateListListener {
        public void onUpdate(int oldListSize, int newListSize);
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
