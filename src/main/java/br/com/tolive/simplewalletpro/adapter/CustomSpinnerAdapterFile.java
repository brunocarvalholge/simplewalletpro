package br.com.tolive.simplewalletpro.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import br.com.tolive.simplewalletpro.R;

/**
 * Created by bruno.carvalho on 02/07/2014.
 */
public class CustomSpinnerAdapterFile extends ArrayAdapter<File> {
    private Activity context;
    private ArrayList<File> data;
    LayoutInflater inflater;

    /*************  CustomAdapter Constructor *****************/
    public CustomSpinnerAdapterFile(Activity context, int textViewResourceId, ArrayList<File> objects) {
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

        TextView label        = (TextView) convertView.findViewById(android.R.id.text1);
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/" + context.getResources().getString(R.string.app_font));
        label.setTypeface(tf);
        label.setTextColor(context.getResources().getColor(R.color.gray));
        label.setBackgroundColor(context.getResources().getColor(R.color.snow));
        final DisplayMetrics metrics;
        metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        label.setTextSize(getDPI(18, metrics));

        String name;
        File file = data.get(position);
        if(file == null){
            name = context.getResources().getString(R.string.dialog_recovery_text);
            label.setText(name);
        } else {
            name = file.getName();
            label.setText(name.substring(0,name.lastIndexOf(".")));
        }
        return convertView;
    }

    public static int getDPI(int size, DisplayMetrics metrics){
        return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }
}
