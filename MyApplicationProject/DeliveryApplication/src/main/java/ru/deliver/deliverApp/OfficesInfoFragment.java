package ru.deliver.deliverApp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.deliver.deliverApp.Adapters.OfficesInfoAdapter;
import ru.deliver.deliverApp.Utils.OfficesAdd;

/**
 * Created by Evgenij on 19.09.13.
 *
 * Фиговая идея для этой проги
 */
public class OfficesInfoFragment extends Fragment
{
    //-----------------------------
    //Constants
    //-----------------------------

    //-----------------------------
    //Variables
    //-----------------------------

    //private ListView mList;
    //private TextView mTitle;

    //private String mTitleName;
    //private int mPosition;

    //-----------------------------
    //Ctors
    //-----------------------------

    public OfficesInfoFragment()
    {
        super();
    }

    //-----------------------------
    //Methods
    //-----------------------------

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (container == null)
            return null;

        View view = inflater.inflate(R.layout.offices_fragment, container, false);
        TextView mTitle = (TextView)view.findViewById(R.id.Offices_Title);
        ListView mList = (ListView)view.findViewById(R.id.Offices_List);

        int mPosition = getArguments().getInt("position");
        String mTitleName = getArguments().getString("title");

        OfficesInfoAdapter mSecondAdapter = new OfficesInfoAdapter();
        ArrayList<OfficesAdd> mOfficesInfo = ((Main)getActivity()).mOffices.get(mPosition).getContacts();
        mSecondAdapter.addItems(mOfficesInfo);
        mList.setAdapter(mSecondAdapter);

        mTitle.setText(mTitleName);

        return view;
    }

    //-----------------------------
    //Getters/Setters
    //-----------------------------

    //-----------------------------
    //Inner Classes
    //-----------------------------
}
