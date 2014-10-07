package br.com.tolive.simplewalletpro.app;

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

import br.com.tolive.simplewalletpro.R;
import br.com.tolive.simplewalletpro.adapter.GraphSubListAdapter;
import br.com.tolive.simplewalletpro.db.EntryDAO;
import br.com.tolive.simplewalletpro.model.Category;
import br.com.tolive.simplewalletpro.adapter.GraphTabAdapter;
import br.com.tolive.simplewalletpro.model.Entry;
import br.com.tolive.simplewalletpro.views.CustomTextView;
import br.com.tolive.simplewalletpro.views.GraphView;


/**
 * Created by bruno.carvalho on 10/07/2014.
 */
public class GraphFragmentTab extends Fragment {
    private GraphView graph;
    private ListView sub;
    private CustomTextView emptyGraph;

    private int type;
    private int month;
    private ArrayList<Category> categories;
    private ArrayList<Float> percents;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph_tab, container, false);

        type = getArguments().getInt(GraphTabAdapter.BUNDLE_KEY_TAB_TYPE, Category.TYPE_GAIN);
        Calendar calendar = Calendar.getInstance();
        month = getArguments().getInt(GraphTabAdapter.BUNDLE_KEY_TAB_MONTH, calendar.get(Calendar.MONTH));
        Log.d("TAG", "month from budle: " + month);

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

        graph = (GraphView) view.findViewById(R.id.fragment_graph_graphview);
        sub = (ListView) view.findViewById(R.id.fragment_graph_list);
        emptyGraph = (CustomTextView) view.findViewById(R.id.fragment_graph_text_empty);

        setGraph();

        return view;
    }

    private void setGraph() {
        //TODO : CREATE category fake to hold entries with no category
        final EntryDAO dao = EntryDAO.getInstance(getActivity());
        categories = dao.getCategories(type);
        percents = dao.getPercents(categories, month);
        float total = percents.get(percents.size() - 1);

        if(total > 0) {
            emptyGraph.setVisibility(View.GONE);
            graph.setVisibility(View.VISIBLE);
            sub.setVisibility(View.VISIBLE);
            categories = removeZero(categories, percents);
            orderSubtitle();
            graph.setCategories(categories);
            graph.setPercents(percents);
            GraphSubListAdapter adapter = new GraphSubListAdapter(categories, percents, getActivity());
            sub.setAdapter(adapter);
        } else {
            graph.setVisibility(View.GONE);
            sub.setVisibility(View.INVISIBLE);
            emptyGraph.setVisibility(View.VISIBLE);
        }
    }

    private void orderSubtitle() {
        //TODO do it with quicksort if necessary
        int size = percents.size() - 1;
        //It is size - 1 bucause the last item is the total amount
        for(int i = 0; i < size - 1; i++){
            int subs = i;
            for(int j = i + 1; j < size; j++){
                if(percents.get(j) > percents.get(subs)){
                    subs = j;
                }
            }
            Float aux = percents.get(i);
            Category aux2 = categories.get(i);
            percents.set(i, percents.get(subs));
            categories.set(i, categories.get(subs));
            percents.set(subs, aux);
            categories.set(subs, aux2);
        }
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
