package ru.deliver.deliverApp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import ru.deliver.deliverApp.Utils.EditWithDrawable;

/**
 * Created by Evgenij on 20.08.13.
 *
 * Отображение первого таба
 */
public class DeliverFragment extends Fragment
{
	//---------------------------------
	//CONSTANTS
	//---------------------------------

	//---------------------------------
	//VARIABLES
	//---------------------------------

    private FragmentActivity mActivity;

	//---------------------------------
	//SUPER
	//---------------------------------

    public DeliverFragment(FragmentActivity activity)
    {
        this.mActivity = activity;
    }

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (container == null)
			return null;

		View view = inflater.inflate(R.layout.deliver_fragment, container, false);

		EditWithDrawable mEdit = (EditWithDrawable)view.findViewById(R.id.Deliver_number);
        Button mBtn = (Button)view.findViewById(R.id.Deliver_find);

		mEdit.requestFocus();
        mBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pushFragment();
            }
        });

		return view;
	}
	//---------------------------------
	//METHODS
	//---------------------------------

    private void pushFragment()
    {
        FragmentManager manager = mActivity.getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(android.R.id.tabcontent, new DeliverInfoFragment(true));
        ft.addToBackStack(null);
        ft.commit();
    }

	//---------------------------------
	//GETTERS/SETTERS
	//---------------------------------

	//---------------------------------
	//INNER CLASSES
	//---------------------------------        
}
