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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
public class StoreFragment extends Fragment{

    public StoreFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store, container, false);

        ImageView imageMailStore = (ImageView) view.findViewById(R.id.fragment_store_image_email);

        imageMailStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogEmailMaker dialogEmailMaker = new DialogEmailMaker(getActivity());
                dialogEmailMaker.setOnClickOkListener(new DialogEmailMaker.OnClickOkListener() {
                    @Override
                    public void onClickOk(String month, String year) {
                        EntryDAO dao = EntryDAO.getInstance(getActivity());
                        ArrayList<Entry> entries;
                        if (year.equals(DialogEmailMaker.SPINNER_SELECTED_DEFAULT_STRING) && month.equals(DialogEmailMaker.SPINNER_SELECTED_DEFAULT_STRING)) {
                            entries = dao.getEntry(null, null);
                        } else if (year.equals(DialogEmailMaker.SPINNER_SELECTED_DEFAULT_STRING)) {
                            entries = dao.getEntry(month, null);
                        } else if (month.equals(DialogEmailMaker.SPINNER_SELECTED_DEFAULT_STRING)) {
                            entries = dao.getEntry(null, year);
                        } else {
                            entries = dao.getEntry(month, year);
                        }
                        Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "[Gastos PRO]" + "[" + month + "]" + "[" + year + "] Relatorio de gastos");
                        intent.putExtra(Intent.EXTRA_TEXT, formatEmailBody(entries));
                        intent.setData(Uri.parse("mailto:")); // or just "mailto:" for blank
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                        startActivity(intent);
                    }
                });
                AlertDialog emailDialog = dialogEmailMaker.makeMailDialog();
                emailDialog.show();
            }
        });

        ImageView imageSdCardStore = (ImageView) view.findViewById(R.id.fragment_store_image_sdcard);

        imageSdCardStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isExternalStorageWritable()){
                    try {
                        File newFolder = new File(Environment.getExternalStorageDirectory(), Constants.STORE_FOLDER_NAME);
                        if (!newFolder.exists()) {
                            newFolder.mkdir();
                        }
                        try {
                            File file = new File(newFolder, Constants.STORE_FILE_NAME);
                            if(!file.exists()){
                                file.createNewFile();
                            }
                            EntryDAO dao = EntryDAO.getInstance(getActivity());
                            ArrayList<Entry> entries = dao.getEntry(null, null);
                            String entriesJSON = EntryConverter.toJson(entries);
                            FileOutputStream fos;
                            byte[] data = entriesJSON.getBytes();
                            try {
                                fos = new FileOutputStream(file);
                                fos.write(data);
                                fos.flush();
                                fos.close();
                            } catch (FileNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    Toast.makeText(getActivity(), "salvo no SDCard", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "erro ao abrir cartão sd", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public boolean isExternalStorageWritable() {
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
