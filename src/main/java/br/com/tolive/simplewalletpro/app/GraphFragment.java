package br.com.tolive.simplewalletpro.app;

import android.app.ActionBar;
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
import br.com.tolive.simplewalletpro.model.Entry;


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

        gallery.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                ActionBar actionBar = getActivity().getActionBar();
                switch (i) {
                    case Entry.TYPE_GAIN :
                        actionBar.setTitle(getActivity().getResources().getString(R.string.fragment_graph_text_gain));
                        break;
                    case Entry.TYPE_EXPENSE :
                        actionBar.setTitle(getActivity().getResources().getString(R.string.fragment_graph_text_expense));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        gallery.setAdapter(mAdapter);

        return view;
    }
}
