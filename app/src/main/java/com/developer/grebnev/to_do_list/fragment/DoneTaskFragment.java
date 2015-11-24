package com.developer.grebnev.to_do_list.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.developer.grebnev.to_do_list.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DoneTaskFragment extends Fragment {

    RecyclerView rvDoneTask;
    RecyclerView.LayoutManager layoutManager;

    public DoneTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_done_task, container, false);

        rvDoneTask = (RecyclerView) rootView.findViewById(R.id.rvDoneTasks);

        layoutManager = new LinearLayoutManager(getActivity());
        rvDoneTask.setLayoutManager(layoutManager);

        return rootView;
    }


}
