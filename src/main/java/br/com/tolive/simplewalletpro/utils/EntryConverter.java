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

    public static String toJson(ArrayList<Entry> alunos) {
        try {
            JSONStringer json = new JSONStringer();
            json.object().key(LIST).array().object().key(Entry.ENTITY_NAME).array();

            for (Entry aluno : alunos) {
                json.object().key(Entry.ID).value(aluno.getId())
                        .key(Entry.DESCRIPTION).value(aluno.getDescription())
                        .key(Entry.VALUE).value(String.format("%.2f",aluno.getValue()))
                        .key(Entry.TYPE).value(aluno.getType())
                        .key(Entry.CATEGORY).value(aluno.getCategory())
                        .key(Entry.DATE).value(aluno.getDate())
                        .key(Entry.MONTH).value(aluno.getMonth())
                        .endObject();
            }

            String sJson = json.endArray().endObject().endArray().endObject().toString();

            return sJson;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
