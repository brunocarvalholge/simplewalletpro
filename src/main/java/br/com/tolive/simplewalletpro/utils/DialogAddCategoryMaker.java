package br.com.tolive.simplewalletpro.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.tolive.simplewalletpro.R;
import br.com.tolive.simplewalletpro.model.Category;
import br.com.tolive.simplewalletpro.views.CustomTextView;

/**
 * Created by Bruno on 11/08/2014.
 */
public class DialogAddCategoryMaker {
    private static final int IMAGE_VIEW_COLOR_SIZE = 35;
    private static final int IMAGE_VIEW_MARGIN = 1;
    private static final int CONTAINER_MARGIN = 2;
    private int NUM_COLORS_COLUMN = 8;

    private Context context;
    private DisplayMetrics metrics;
    private int type;

    private AlertDialog dialog;
    private OnClickOkListener mListener;

    private int selectedColor = 0;
    private boolean isNew;

    public DialogAddCategoryMaker(Context context, DisplayMetrics metrics, int type){
        this.context = context;
        this.metrics = metrics;
        this.type = type;
    }

    public AlertDialog makeAddCategryDialog(Category category){
        this.dialog = DialogACustomddCategoryMaker(category);
        setDialog(dialog);
        return dialog;
    }

    private AlertDialog DialogACustomddCategoryMaker(Category category) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater)   context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.dialog_add_category, null);
        final EditText editTextCategoryName = (EditText) view.findViewById(R.id.dialog_add_category_edittext_name);
        final LinearLayout containerColors = (LinearLayout) view.findViewById(R.id.dialog_add_category_container_colors);
        CustomTextView okButton = (CustomTextView) view.findViewById(R.id.dialog_add_category_text_ok);
        CustomTextView cancelButton = (CustomTextView) view.findViewById(R.id.dialog_add_category_text_cancel);

        final Resources resources = context.getResources();
        TypedArray colors = resources.obtainTypedArray(R.array.categoryColors);

        final Category newCategory;
        if(category != null){
            isNew = false;
            newCategory = category;
            selectedColor = newCategory.getColor();
            editTextCategoryName.setText(category.getName());
        } else{
            newCategory = new Category();
            newCategory.setColor(selectedColor);
            isNew = true;
        }

        for  (int indexColor = 0, count = 0; count < colors.length()/NUM_COLORS_COLUMN; count++){
            LinearLayout column = new LinearLayout(context);
            LinearLayout.LayoutParams containerParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            int containerMargin = getDPI(CONTAINER_MARGIN, metrics);
            containerParams.setMargins(0, containerMargin, 0, containerMargin);
            column.setLayoutParams(containerParams);
            column.setOrientation(LinearLayout.HORIZONTAL);
            column.setGravity(Gravity.CENTER_HORIZONTAL);
            for (int j = 0; j < NUM_COLORS_COLUMN; j++, indexColor++){
                final ImageView imageView = new ImageView(context);
                int size = getDPI(IMAGE_VIEW_COLOR_SIZE, metrics);
                LinearLayout.LayoutParams imageLayoutParams= new LinearLayout.LayoutParams(size, size);
                int margin = getDPI(IMAGE_VIEW_MARGIN, metrics);
                imageLayoutParams.setMargins(margin, margin, margin, margin);
                imageView.setLayoutParams(imageLayoutParams);
                imageView.setBackgroundColor(resources.getColor(colors.getResourceId(indexColor, resources.getColor(R.color.gray))));
                imageView.setId(indexColor);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //int id = view.getId();
                        setSelectedColor((ImageView) view);
                    }
                });
                if(newCategory.getColor() == indexColor){
                    //TODO : create image ic_selected_color
                    imageView.setImageDrawable(resources.getDrawable(R.drawable.ic_confirm));
                }
                column.addView(imageView);
            }
            containerColors.addView(column);
        }
        colors.recycle();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String categoryName = editTextCategoryName.getText().toString();
                if(categoryName.equals("")){
                    Toast.makeText(context, resources.getString(R.string.dialog_add_category_invalid_name), Toast.LENGTH_SHORT).show();
                } else {
                    if (mListener != null) {
                        newCategory.setName(categoryName);
                        newCategory.setColor(getSelectedColor());
                        newCategory.setType(type);
                        mListener.onClickOk(newCategory, isNew);
                    }

                    DialogAddCategoryMaker.this.dialog.dismiss();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAddCategoryMaker.this.dialog.cancel();
            }
        });

        dialog.setView(view);
        return dialog.create();
    }

    private int getSelectedColor() {
        return selectedColor;
    }

    private void setSelectedColor(ImageView selectedView) {
        LinearLayout column = (LinearLayout) selectedView.getParent();
        LinearLayout root = (LinearLayout) column.getParent();
        for (int i = 0; i < root.getChildCount(); i++){
            for (int j = 0; j < column.getChildCount(); j++) {
                ImageView child = (ImageView) ((LinearLayout) root.getChildAt(i)).getChildAt(j);
                child.setImageDrawable(null);
            }
        }
        selectedView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_confirm));
        selectedColor = selectedView.getId();
    }

    public static int getDPI(int size, DisplayMetrics metrics){
        return (size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }

    private void setDialog(AlertDialog dialog){
        this.dialog = dialog;
    }

    public void setOnClickOkListener (OnClickOkListener listener){
        mListener = listener;
    }

    public interface OnClickOkListener {
        public void onClickOk(Category category, boolean isNew);
    }
}
