package br.com.tolive.simplewalletpro.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import br.com.tolive.simplewalletpro.R;
import br.com.tolive.simplewalletpro.adapter.CustomSpinnerAdapter;
import br.com.tolive.simplewalletpro.adapter.CustomSpinnerAdapterCategory;
import br.com.tolive.simplewalletpro.constants.Constantes;
import br.com.tolive.simplewalletpro.db.EntryDAO;
import br.com.tolive.simplewalletpro.model.Category;
import br.com.tolive.simplewalletpro.model.Entry;
import br.com.tolive.simplewalletpro.views.CustomTextView;

/**
 * Created by bruno.carvalho on 04/07/2014.
 */
public class DialogAddEntryMaker {
    private static final String EMPTY = "";
    private static final int DATE_YEAR = 2;
    private static final int DATE_MONTH = 1;
    private static final int DATE_DAY = 0;

    private OnClickOkListener mListener;
    private Context context;
    AlertDialog dialog;

    public DialogAddEntryMaker(Context context){
        this.context = context;
    }

    public AlertDialog makeAddDialog(){
        this.dialog = makeCustomAddDialog(null);
        setDialog(dialog);
        return dialog;
    }

    public AlertDialog makeAddDialog(Entry entry){
        this.dialog = makeCustomAddDialog(entry);
        setDialog(dialog);
        return dialog;
    }

    private void setDialog(AlertDialog dialog){
        this.dialog = dialog;
    }

    /**
     * After call this method, call setDialog to update dialog reference
     * so the button don't get NullPointeException.
     *
     * @return custom AlertDialog
     */
    private AlertDialog makeCustomAddDialog(final Entry entry) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater)   context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_add, null);

        final EditText editTextDescription = (EditText) view.findViewById(R.id.dialog_add_edittext_description);
        final EditText editTextValue = (EditText) view.findViewById(R.id.dialog_add_edittext_value);
        final RadioGroup radioGroupType = (RadioGroup) view.findViewById(R.id.dialog_add_radiogroup_type);
        final RadioButton radioGain = (RadioButton) view.findViewById(R.id.dialog_add_radiobutton_gain);
        final RadioButton radioExpense = (RadioButton) view.findViewById(R.id.dialog_add_radiobutton_expense);
        final Spinner categorySpinner = (Spinner) view.findViewById(R.id.dialog_add_spinner_category);

        EntryDAO dao = EntryDAO.getInstance(context);
        ArrayList<Category> categories = dao.getCategories();
        String[] categoriesNames = getCategoriesNames(categories);
        CustomSpinnerAdapterCategory adapterCategory = new CustomSpinnerAdapterCategory(context, R.layout.simple_spinner_item, categoriesNames);
        adapterCategory.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapterCategory);

        final LinearLayout containerChooseDate = (LinearLayout) view.findViewById(R.id.dialog_add_container_choose_date);

        final DatePicker datePicker = (DatePicker) view.findViewById(R.id.dialog_add_datepicker);

        containerChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.setVisibility(View.VISIBLE);
                containerChooseDate.setVisibility(View.GONE);
            }
        });

        CustomTextView okButton = (CustomTextView) view.findViewById(R.id.dialog_add_text_ok);
        CustomTextView cancelButton = (CustomTextView) view.findViewById(R.id.dialog_add_text_cancel);

        if(entry != null){
            editTextDescription.setText(entry.getDescription());
            editTextValue.setText(String.format("%.2f", entry.getValue()));
            if (entry.getType() == Entry.TYPE_GAIN) {
                radioGain.setChecked(true);
                radioExpense.setChecked(false);
            } else {
                radioGain.setChecked(false);
                radioExpense.setChecked(true);
            }
            String[] split = entry.getDate().split("/");
            datePicker.updateDate(Integer.valueOf(split[DATE_YEAR]), Integer.valueOf(split[DATE_MONTH]) - 1, Integer.valueOf(split[DATE_DAY]));
        }

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonOk) {
                String description = editTextDescription.getText().toString();

                int typeRadioButtonId = radioGroupType.getCheckedRadioButtonId();
                int type = typeRadioButtonId == R.id.dialog_add_radiobutton_expense ? Entry.TYPE_EXPENSE : Entry.TYPE_GAIN;

                int category = categorySpinner.getSelectedItemPosition();

                int month;
                String date;

                if (datePicker.getVisibility() == View.VISIBLE) {
                    month = datePicker.getMonth();
                    date = datePicker.getDayOfMonth() + "/" + (month + 1) + "/" + datePicker.getYear();
                } else {
                    Calendar calendar = Calendar.getInstance();
                    month = calendar.get(Calendar.MONTH);
                    date = calendar.get(Calendar.DAY_OF_MONTH) + "/" + (month + 1) + "/" + calendar.get(Calendar.YEAR);
                }

                if (editTextValue.getText().toString().equals(EMPTY)) {
                    Toast.makeText(context, R.string.dialog_add_invalid_value, Toast.LENGTH_SHORT).show();
                } else {
                    Float value = Float.parseFloat(formatToDot(editTextValue.getText().toString()));
                    if (editTextDescription.getText().toString().equals(EMPTY)){
                        editTextDescription.setText(R.string.dialog_add_no_descripition);
                    }

                    if(entry == null) {
                        Entry newEntry = new Entry();
                        newEntry.setDescription(description);
                        newEntry.setValue(value);
                        newEntry.setType(type);
                        newEntry.setCategory(category);
                        newEntry.setDate(date);
                        newEntry.setMonth(month);

                        if (mListener != null) {
                            mListener.onClickOk(newEntry);
                            DialogAddEntryMaker.this.dialog.dismiss();
                        }
                    } else{
                        entry.setDescription(description);
                        entry.setValue(value);
                        entry.setType(type);
                        entry.setCategory(category);
                        entry.setDate(date);
                        entry.setMonth(month);

                        if (mListener != null) {
                            mListener.onClickOk(entry);
                            DialogAddEntryMaker.this.dialog.dismiss();
                        }
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View buttonCancel) {
                DialogAddEntryMaker.this.dialog.cancel();
            }
        });

        dialog.setView(view);
        return dialog.create();
    }

    private String[] getCategoriesNames(ArrayList<Category> categories) {
        ArrayList<String> names = new ArrayList<String>();
        for(Category category : categories){
            names.add(category.getName());
        }
        String[] namesList = new String[names.size()];
        return names.toArray(namesList);
    }

    private String formatToDot(String number) {
        if(number.contains(",")){
            return number.split(",")[0] + "." + number.split(",")[1];
        } else {
            return  number;
        }
    }


    public void setOnClickOkListener (OnClickOkListener listener){
        mListener = listener;
    }

    public interface OnClickOkListener {
        public void onClickOk(Entry entry);
    }
}
