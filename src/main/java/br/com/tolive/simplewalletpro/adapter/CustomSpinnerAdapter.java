package br.com.tolive.simplewalletpro.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.com.tolive.simplewalletpro.R;
import br.com.tolive.simplewalletpro.constants.Constantes;

/**
 * Created by bruno.carvalho on 02/07/2014.
 */
public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    private Context context;
    private String[] data;
    LayoutInflater inflater;

    /*************  CustomAdapter Constructor *****************/
    public CustomSpinnerAdapter( Context context, int textViewResourceId, String[] objects) {
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

        View view = inflater.inflate(R.layout.simple_spinner_item, parent, false);

        TextView label        = (TextView) view.findViewById(android.R.id.text1);
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/" + context.getResources().getString(R.string.app_font));
        label.setTypeface(tf);

        label.setText(data[position]);

        return view;
    }
}
