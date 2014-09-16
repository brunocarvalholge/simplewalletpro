package br.com.tolive.simplewalletpro.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;

import br.com.tolive.simplewalletpro.R;
import br.com.tolive.simplewalletpro.constants.Constants;
import br.com.tolive.simplewalletpro.model.Entry;

/**
 * Created by bruno.carvalho on 08/09/2014.
 */
public class RecurrentsManager {

    public static final String KEY_LIST = "list";

    private Context context;
    private SharedPreferences sharedPreferences;

    public RecurrentsManager(Context context){
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFERENCES, Context.MODE_PRIVATE);
    }

    public ArrayList<Entry> getRecurrentDaily(){
        String jRecurrentDaily = sharedPreferences.getString(Constants.SP_KEY_RECURRENT_DAILY, Constants.SP_RECURRENT_DAILY_DEFAULT);
        if(jRecurrentDaily.equals(Constants.SP_RECURRENT_DAILY_DEFAULT)){
            return new ArrayList<Entry>();
        } else {
            return fromJson(jRecurrentDaily);
        }
    }

    public ArrayList<Entry> getRecurrentMonthly(){
        String jRecurrentMonthly = sharedPreferences.getString(Constants.SP_KEY_RECURRENT_MONTHLY, Constants.SP_RECURRENT_MONTHLY_DEFAULT);
        if(jRecurrentMonthly.equals(Constants.SP_RECURRENT_MONTHLY_DEFAULT)){
            return new ArrayList<Entry>();
        } else {
            return fromJson(jRecurrentMonthly);
        }
    }

    public void saveRecurrentDaily(ArrayList<Entry> recurrentsDaily){
        String json = toJson(recurrentsDaily);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SP_KEY_RECURRENT_DAILY, json);
        editor.apply();
    }

    public void saveRecurrentMonthly(ArrayList<Entry> recurrentsMonthly){
        String json = toJson(recurrentsMonthly);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SP_KEY_RECURRENT_MONTHLY, json);
        editor.apply();
    }

    private static String toJson(ArrayList<Entry> entries) {
        try {
            JSONStringer json = new JSONStringer();
            json.object().key(KEY_LIST).array().object().key(Entry.ENTITY_NAME).array();

            for (Entry entry : entries) {
                json.object().key(Entry.ID).value(entry.getId())
                        .key(Entry.DESCRIPTION).value(entry.getDescription())
                        .key(Entry.VALUE).value(String.format("%.2f",entry.getValue()))
                        .key(Entry.TYPE).value(entry.getType())
                        .key(Entry.CATEGORY).value(entry.getCategory())
                        .key(Entry.DATE).value(entry.getDate())
                        .key(Entry.MONTH).value(entry.getMonth())
                        .endObject();
            }

            String sJson = json.endArray().endObject().endArray().endObject().toString();

            return sJson;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private static ArrayList<Entry> fromJson(String jEntries){
        ArrayList<Entry> recurrentList = new ArrayList<Entry>();
        try {
            JSONObject json = new JSONObject(jEntries);
            JSONArray list = json.getJSONArray(EntryConverter.LIST);
            JSONArray entries = list.getJSONObject(0).getJSONArray(Entry.ENTITY_NAME);
            for(int i = 0; !entries.isNull(i); i++){
                Entry entry = new Entry();
                JSONObject jEntry = entries.getJSONObject(i);

                entry.setId(jEntry.getLong(Entry.ID));
                entry.setDescription(jEntry.getString(Entry.DESCRIPTION));
                entry.setValue(Float.parseFloat(jEntry.getString(Entry.VALUE).replace(',','.')));
                entry.setType(jEntry.getInt(Entry.TYPE));
                entry.setCategory(jEntry.getInt(Entry.CATEGORY));
                entry.setDate(jEntry.getString(Entry.DATE));
                entry.setMonth(jEntry.getInt(Entry.MONTH));

                recurrentList.add(entry);
            }
            //Toast.makeText(this.context, this.context.getResources().getString(R.string.fragment_recovery_text_sucess), Toast.LENGTH_SHORT).show();
        } catch (JSONException e){
            throw new RuntimeException(e);
        }
        return recurrentList;
    }
}
