package com.example.myinvoice;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;


public class Main extends Fragment {
    private static final String TAG = "Main";

    private ListView listView;
    SQLiteHandler db;
    ArrayList<Invoice> invoices;
    ArrayAdapter<Invoice> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.invoices, container, false);
       //((MainActivity)getActivity()).setViewPager(1);
        db=new SQLiteHandler(getContext());
        invoices=db.getRecords();
        listView=(ListView) view.findViewById(R.id.listView);
        adapter=new ArrayAdapter<>(this.getActivity(),android.R.layout.simple_list_item_1,invoices);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MainActivity.index=i;
                ((MainActivity)getActivity()).setViewPager(3);
            }
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        db=new SQLiteHandler(getContext());
        invoices=db.getRecords();
        adapter.addAll();
        adapter.notifyDataSetChanged();
    }

}
