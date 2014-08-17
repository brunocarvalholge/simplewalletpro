package br.com.tolive.simplewalletpro.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.tolive.simplewalletpro.R;
import java.util.ArrayList;

import br.com.tolive.simplewalletpro.model.Category;
import br.com.tolive.simplewalletpro.adapter.GraphTabAdapter;


/**
 * Created by bruno.carvalho on 10/07/2014.
 */
public class GraphFragment extends Fragment {
    private GraphTabAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        ViewPager gallery = (ViewPager) view.findViewById(R.id.fragment_graph_viewpager);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        ArrayList<Integer> types = new ArrayList<Integer>();
        types.add(Category.TYPE_GAIN);
        types.add(Category.TYPE_EXPENSE);
        mAdapter = new GraphTabAdapter(fm, types);

        gallery.setAdapter(mAdapter);

        return view;
    }
}
