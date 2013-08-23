package ru.deliver.deliverApp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by 1 on 20.08.13.
 *
 * Окно расчета стоимости заказа
 */
public final class CalculatorFragment extends Fragment
{
    //-------------------------------
    //CONSTANTS
    //-------------------------------

    //-------------------------------
    //VARIABLES
    //-------------------------------

    //-------------------------------
    //CONSTRUCTORS
    //-------------------------------

	public CalculatorFragment()
	{
		super();
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.calculator_fragment, container, false);

		final EditText mEditWidth 	= (EditText)view.findViewById(R.id.Calc_Width);
		final EditText mEditHeight 	= (EditText)view.findViewById(R.id.Calc_Height);
		final EditText mEditLength 	= (EditText)view.findViewById(R.id.Calc_Length);
		final CheckBox mCheck		= (CheckBox)view.findViewById(R.id.Calc_Check);
		AutoCompleteTextView mFrom 	= (AutoCompleteTextView)view.findViewById(R.id.Calc_Auto1);
		AutoCompleteTextView mTo 	= (AutoCompleteTextView)view.findViewById(R.id.Calc_Auto2);
		Spinner mWeight 			= (Spinner)view.findViewById(R.id.Calc_Spin1);

		String[] towns = getActivity().getResources().getStringArray(R.array.towns);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, towns);
		String[] weights = getActivity().getResources().getStringArray(R.array.weights);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, weights);
		mFrom.setAdapter(adapter1);
		mTo.setAdapter(adapter1);
		mWeight.setAdapter(adapter2);

		mCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b)
			{
				if(b)//Письмо
				{
					mCheck.setText(R.string.Calculator_CheckLetter);
					mEditHeight.setVisibility(View.GONE);
					mEditWidth.setVisibility(View.GONE);
					mEditLength.setVisibility(View.GONE);
				}
				else//Посылка
				{
					mCheck.setText(R.string.Calculator_CheckParcel);
					mEditHeight.setVisibility(View.VISIBLE);
					mEditWidth.setVisibility(View.VISIBLE);
					mEditLength.setVisibility(View.VISIBLE);
				}
			}
		});

        return view;
    }
    //-------------------------------
    //SUPER METHODS
    //-------------------------------

    //-------------------------------
    //METHODS
    //-------------------------------

    //-------------------------------
    //GETTERS/SETTERS
    //-------------------------------

    //-------------------------------
    //INNER CLASSES
    //-------------------------------
}
