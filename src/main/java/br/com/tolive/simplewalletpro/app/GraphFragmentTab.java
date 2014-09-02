package br.com.tolive.simplewalletpro.app;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

import br.com.tolive.simplewalletpro.R;
import br.com.tolive.simplewalletpro.adapter.GraphSubListAdapter;
import br.com.tolive.simplewalletpro.db.EntryDAO;
import br.com.tolive.simplewalletpro.model.Category;
import br.com.tolive.simplewalletpro.adapter.GraphTabAdapter;
import br.com.tolive.simplewalletpro.views.CustomTextView;
import br.com.tolive.simplewalletpro.views.GraphView;


/**
 * Created by bruno.carvalho on 10/07/2014.
 */
public class GraphFragmentTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph_tab, container, false);

        int type = getArguments().getInt(GraphTabAdapter.BUNDLE_KEY_TAB_TYPE, Category.TYPE_GAIN);

        ImageView switcher;
        switch (type){
            case Category.TYPE_EXPENSE:
                switcher = (ImageView) view.findViewById(R.id.fragment_graph_image_switch_rigth);
                switcher.setVisibility(View.VISIBLE);
                break;
            default:
                switcher = (ImageView) view.findViewById(R.id.fragment_graph_image_switch_left);
                switcher.setVisibility(View.VISIBLE);
                break;
        }

        GraphView graph = (GraphView) view.findViewById(R.id.fragment_graph_graphview);
        ListView sub = (ListView) view.findViewById(R.id.fragment_graph_list);
        CustomTextView emptyGraph = (CustomTextView) view.findViewById(R.id.fragment_graph_text_empty);

        //TODO : CREATE category fake to hold entries with no category
        final EntryDAO dao = EntryDAO.getInstance(getActivity());
        ArrayList<Category> categories = dao.getCategories(type);
        Calendar calendar = Calendar.getInstance();
        ArrayList<Float> percents = dao.getPercents(categories, calendar.get(Calendar.MONTH));
        float total = percents.get(percents.size() - 1);

        Log.d("tag", "total: " + total);
        if(total > 0) {
            emptyGraph.setVisibility(View.GONE);
            graph.setVisibility(View.VISIBLE);
            sub.setVisibility(View.VISIBLE);
            categories = removeZero(categories, percents);
            graph.setCategories(categories);
            graph.setPercents(percents);
            GraphSubListAdapter adapter = new GraphSubListAdapter(categories, percents, getActivity());
            sub.setAdapter(adapter);
        } else {
            graph.setVisibility(View.GONE);
            sub.setVisibility(View.INVISIBLE);
            emptyGraph.setVisibility(View.VISIBLE);
        }

        return view;
    }

    private ArrayList<Category> removeZero(ArrayList<Category> categories, ArrayList<Float> percents) {
        int size = categories.size() - 1;
        for (int i = size; i >= 0 ;i--){
            if(percents.get(i) == 0){
                percents.remove(i);
                categories.remove(i);
            }
        }
        return categories;
    }
}
