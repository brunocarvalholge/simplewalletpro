package br.com.tolive.simplewalletpro.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.ArrayList;

import br.com.tolive.simplewalletpro.model.Entry;

/**
 * Created by Bruno on 16/08/2014.
 */
public class EntryConverter {

    public static final String LIST = "list";

    public static String toJson(ArrayList<Entry> entries) {
        try {
            JSONStringer json = new JSONStringer();
            json.object().key(LIST).array().object().key(Entry.ENTITY_NAME).array();

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

    public static ArrayList<Entry> fromJson(String entries){
        return null;
    }
}
