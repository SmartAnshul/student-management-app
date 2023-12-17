package com.techmnc.studentmanagementapp.fragmentss;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techmnc.studentmanagementapp.R;
import com.techmnc.studentmanagementapp.activity.studentManagementsActivity;

public class Detail extends Fragment {

    public Detail() {
        // Required empty public constructor
    }

    public static Detail newInstance(String param1, String param2) {
        Detail fragment = new Detail();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        // Add click listener to the view or any UI element that should trigger the navigation
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to MainActivity
                Intent intent = new Intent(getActivity(), studentManagementsActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}