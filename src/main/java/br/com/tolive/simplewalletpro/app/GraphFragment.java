package br.com.tolive.simplewalletpro.app;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.tolive.simplewalletpro.R;
import java.util.ArrayList;

import br.com.tolive.simplewalletpro.R;
import br.com.tolive.simplewalletpro.views.GraphView;


/**
 * Created by bruno.carvalho on 10/07/2014.
 */
public class GraphFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        GraphView graph = (GraphView) view.findViewById(R.id.fragment_graph_graphview);

        ArrayList<Paint> colors = new ArrayList<Paint>();
        Paint color1 = new Paint();
        color1.setColor(0xFF26a69a);
        colors.add(color1);
        Paint color2 = (new Paint());
        color2.setColor(getResources().getColor(R.color.yellow));
        colors.add(color2);
        Paint color3 = (new Paint());
        color3.setColor(getResources().getColor(R.color.green));
        colors.add(color3);

        graph.setColors(colors);

        return view;
    }
}
