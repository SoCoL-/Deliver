package ru.deliver.deliverApp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

	//---------------------------------
	//SUPER
	//---------------------------------
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (container == null)
			return null;

		View view = inflater.inflate(R.layout.deliver_fragment, container, false);

		EditWithDrawable mEdit = (EditWithDrawable)view.findViewById(R.id.Deliver_number);
		mEdit.requestFocus();

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
