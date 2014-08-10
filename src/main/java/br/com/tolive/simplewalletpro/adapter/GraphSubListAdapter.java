package br.com.tolive.simplewalletpro.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import br.com.tolive.simplewalletpro.R;
import br.com.tolive.simplewalletpro.model.Category;
import br.com.tolive.simplewalletpro.model.Entry;
import br.com.tolive.simplewalletpro.views.CustomTextView;

/**
 * Created by bruno.carvalho on 01/08/2014.
 */
public class GraphSubListAdapter extends BaseAdapter {
    private List<Category> categories;
    private List<Float> percents;
    private Context context;

    public GraphSubListAdapter(ArrayList<Category> categories, ArrayList<Float> percents, Context context){
        this.categories = categories;
        this.percents = percents;
        this.context = context;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return categories.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.view_list_entries, null);
        }
        Category category = categories.get(position);

        Resources resources = context.getResources();
        TypedArray colors = resources.obtainTypedArray(R.array.categoryColors);
        convertView.setBackgroundColor(resources.getColor(colors.getResourceId(category.getColor(), resources.getColor(R.color.gray))));
        colors.recycle();

        CustomTextView txtDescription = (CustomTextView) convertView.findViewById(R.id.textView_list_description);
        CustomTextView txtValue = (CustomTextView) convertView.findViewById(R.id.textView_list_value);

        txtDescription.setText(category.getName());
        txtValue.setText(String.format("%.2f", (percents.get(position)/percents.get(percents.size()-1))*100) + "%");

        return convertView;
    }
}
