package br.com.tolive.simplewalletpro.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

        final EntryDAO dao = EntryDAO.getInstance(getActivity());
        //TODO : CREATE TabGroups -> 0 Expenses 1 Gain
        ArrayList<Category> categories = dao.getCategories(type);
        graph.setCategories(categories);

        Calendar calendar = Calendar.getInstance();
        ArrayList<Float> percents = dao.getPercents(categories, calendar.get(Calendar.MONTH));
        graph.setPercents(percents);

        ListView sub = (ListView) view.findViewById(R.id.fragment_graph_list);
        GraphSubListAdapter adapter = new GraphSubListAdapter(categories, percents, getActivity());
        sub.setAdapter(adapter);

        return view;
    }
}
