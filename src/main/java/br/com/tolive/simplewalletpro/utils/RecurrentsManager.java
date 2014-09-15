package br.com.tolive.simplewalletpro.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.ArrayList;

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
    }

    public ArrayList<Entry> getRecurrentDaily(){
        ArrayList<Entry> recurrentDaily = new ArrayList<Entry>();
        Entry teste = new Entry();
        teste.setDescription("TesteD1");
        teste.setValue(new Float(10));
        teste.setCategory(0);
        teste.setDate("0/0/0");
        teste.setType(0);
        recurrentDaily.add(teste);
        Entry teste2 = new Entry();
        teste2.setDescription("TesteD2");
        teste2.setValue(new Float(10));
        teste2.setCategory(0);
        teste2.setDate("0/0/0");
        teste2.setType(1);
        recurrentDaily.add(teste2);
        return recurrentDaily;
    }

    public ArrayList<Entry> getRecurrentMonthly(){
        ArrayList<Entry> recurrentMothly = new ArrayList<Entry>();
        Entry teste = new Entry();
        teste.setDescription("TesteM1");
        teste.setValue(new Float(100));
        teste.setCategory(0);
        teste.setDate("0/0/0");
        teste.setType(0);
        recurrentMothly.add(teste);
        Entry teste2 = new Entry();
        teste2.setDescription("TesteM2");
        teste2.setValue(new Float(100));
        teste2.setCategory(0);
        teste2.setDate("0/0/0");
        teste2.setType(1);
        recurrentMothly.add(teste2);
        return recurrentMothly;
    }

    public void saveRecurrentDaily(ArrayList<Entry> recurrentsDaily){

    }

    public void saveRecurrentMonthly(ArrayList<Entry> recurrentsMonthly){

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

    private static ArrayList<Entry> fromJson(String entries){
        return null;
    }
}
