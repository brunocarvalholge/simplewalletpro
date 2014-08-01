package br.com.tolive.simplewalletpro.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.com.tolive.simplewalletpro.R;

/**
 * Created by Bruno on 31/07/2014.
 */
public class CustomSpinnerAdapterCategory extends ArrayAdapter<String> {
    private Context context;
    private String[] data;
    LayoutInflater inflater;

    /*************  CustomAdapter Constructor *****************/
    public CustomSpinnerAdapterCategory( Context context, int textViewResourceId, String[] objects) {
        super(context, textViewResourceId, objects);
        /********** Take passed values **********/
        this.context = context;
        this.data     = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.simple_spinner_item, parent, false);
        }

        TypedArray colors = context.getResources()
                .obtainTypedArray(R.array.categoryColors);

        TextView label        = (TextView) convertView.findViewById(android.R.id.text1);
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/" + context.getResources().getString(R.string.app_font));
        label.setTypeface(tf);

        Resources resources = context.getResources();
        label.setBackgroundColor(resources.getColor(colors.getResourceId(position, resources.getColor(R.color.gray))));

        colors.recycle();

        label.setText(data[position]);

        return convertView;
    }
}

