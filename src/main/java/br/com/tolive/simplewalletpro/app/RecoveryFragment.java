package br.com.tolive.simplewalletpro.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import br.com.tolive.simplewalletpro.R;
import br.com.tolive.simplewalletpro.constants.Constants;
import br.com.tolive.simplewalletpro.db.EntryDAO;
import br.com.tolive.simplewalletpro.model.Entry;
import br.com.tolive.simplewalletpro.utils.DialogEmailMaker;
import br.com.tolive.simplewalletpro.utils.EntryConverter;

/**
 * Created by bruno.carvalho on 10/07/2014.
 */
public class RecoveryFragment extends Fragment{

    public RecoveryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recovery, container, false);

        ImageView imageSdCardStore = (ImageView) view.findViewById(R.id.fragment_recory_image_sdcard);

        imageSdCardStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isExternalStorageReadable()) {
                    EntryDAO dao = EntryDAO.getInstance(getActivity());
                    File dir = Environment.getExternalStorageDirectory();
                    //TODO : Option to store more them 1 file
                    String filename = Constants.STORE_FILE_NAME +
                            Constants.STORE_FILE_EXT;
                    File file = new File(dir, Constants.STORE_FOLDER_NAME + "/" + filename);
                    try {
                        JSONObject json = getJson(file);
                        JSONArray list = json.getJSONArray(EntryConverter.LIST);
                        JSONArray entries = list.getJSONObject(0).getJSONArray(Entry.ENTITY_NAME);
                        dao.deleteAll();
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

                            dao.insert(entry);
                        }
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.fragment_recovery_text_sucess), Toast.LENGTH_SHORT).show();
                    } catch (FileNotFoundException e){
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.fragment_recovery_text_fail), Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    } catch (IOException e){
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.fragment_recovery_text_fail), Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    } catch (JSONException e){
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.fragment_recovery_text_fail), Toast.LENGTH_SHORT).show();
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        return view;
    }

    private JSONObject getJson(File file) throws IOException, JSONException {
        FileInputStream fIn = new FileInputStream(file);
        BufferedReader myReader = new BufferedReader(
                new InputStreamReader(fIn));
        String aDataRow = "";
        String aBuffer = "";
        while ((aDataRow = myReader.readLine()) != null) {
            aBuffer += aDataRow + "\n";
        }
        return new JSONObject(aBuffer);
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private String formatEmailBody(ArrayList<Entry> entries) {
        return entries.toString();
    }
}
