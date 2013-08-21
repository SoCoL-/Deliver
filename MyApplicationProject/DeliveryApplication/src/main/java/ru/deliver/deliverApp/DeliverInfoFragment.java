package ru.deliver.deliverApp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.deliver.deliverApp.Utils.EditWithDrawable;

/**
 * Created by Evgenij on 21.08.13.
 *
 * Информация об отправлении
 */
public class DeliverInfoFragment extends Fragment
{
	//---------------------------------
	//CONSTANTS
	//---------------------------------

	//---------------------------------
	//VARIABLES
	//---------------------------------

	private boolean isNew;

	//---------------------------------
	//SUPER
	//---------------------------------

	public DeliverInfoFragment(boolean isNew)
	{
		this.isNew = isNew;
	}

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        if (container == null)
            return null;

        View view = inflater.inflate(R.layout.deliver_info_fragment, container, false);

        return view;
    }

	//---------------------------------
	//METHODS
	//---------------------------------

	//---------------------------------
	//GETTERS/SETTERS
	//---------------------------------

	//---------------------------------
	//INNER CLASSES
	//---------------------------------        
}
