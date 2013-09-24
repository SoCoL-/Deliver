package ru.deliver.deliverApp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.deliver.deliverApp.Network.AnswerServer;
import ru.deliver.deliverApp.Network.NetManager;
import ru.deliver.deliverApp.Setup.Logs;
import ru.deliver.deliverApp.Setup.Settings;

/**
 * Created by 1 on 20.08.13.
 *
 * Окно расчета стоимости заказа
 */
public final class CalculatorFragment extends Fragment implements AnswerServer
{
    //-------------------------------
    //CONSTANTS
    //-------------------------------

    //-------------------------------
    //VARIABLES
    //-------------------------------

	//private CheckBox mCheck;
    private RadioGroup mCheck;
	private EditText mEditWidth;
	private EditText mEditHeight;
	private EditText mEditLength;
	private AutoCompleteTextView mFrom;
	private AutoCompleteTextView mTo;
    private TextView mSummaText;
    private Spinner mWeight;

    private NetManager mNetManager;
    private String mType;

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

		mEditWidth 			= (EditText)view.findViewById(R.id.Calc_Width);
		mEditHeight 		= (EditText)view.findViewById(R.id.Calc_Height);
		mEditLength 		= (EditText)view.findViewById(R.id.Calc_Length);
        mCheck				= (RadioGroup)view.findViewById(R.id.Calc_RGroup);
		mFrom 				= (AutoCompleteTextView)view.findViewById(R.id.Calc_Auto1);
		mTo 				= (AutoCompleteTextView)view.findViewById(R.id.Calc_Auto2);
        mSummaText          = (TextView)view.findViewById(R.id.Calc_Summa);
		mWeight 	= (Spinner)view.findViewById(R.id.Calc_Spin1);
		Button mCalculate	= (Button)view.findViewById(R.id.Calc_calculate);

		String[] towns = getActivity().getResources().getStringArray(R.array.towns);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, towns);
		String[] weights = getActivity().getResources().getStringArray(R.array.weights);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, weights);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mFrom.setAdapter(adapter1);
		mTo.setAdapter(adapter1);
		mWeight.setAdapter(adapter2);

        mNetManager = new NetManager(getActivity());
        mNetManager.setInterface(this);

        mFrom.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                mFrom.showDropDown();
                return false;
            }
        });

        mTo.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                mTo.showDropDown();
                return false;
            }
        });

        //Default values
        setDefaultValues();
        //end

        mCheck.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i)
            {
                switch (i)
                {
                    case R.id.Calc_Radio1:
                        mEditHeight.setVisibility(View.GONE);
                        mEditWidth.setVisibility(View.GONE);
                        mEditLength.setVisibility(View.GONE);
                        mType = "1";
                        break;
                    case R.id.Calc_Radio2:
                        mEditHeight.setVisibility(View.VISIBLE);
                        mEditWidth.setVisibility(View.VISIBLE);
                        mEditLength.setVisibility(View.VISIBLE);
                        mType = "2";
                        break;
                }
            }
        });

		mCalculate.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View view)
			{
				if(!isFillAllFields())
					Toast.makeText(getActivity(), R.string.Error_FillFields, Toast.LENGTH_SHORT).show();

                mNetManager.sendCalcReq(mFrom.getText().toString(), mTo.getText().toString(), (String)mWeight.getSelectedItem(), mType, mEditHeight.getText().toString(), mEditWidth.getText().toString(), mEditLength.getText().toString());
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

    private void setDefaultValues()
    {
        mCheck.check(R.id.Calc_Radio1);
        mEditHeight.setVisibility(View.GONE);
        mEditWidth.setVisibility(View.GONE);
        mEditLength.setVisibility(View.GONE);
        mType = "1";
        mWeight.setSelection(0);
        mEditHeight.setText("");
        mEditLength.setText("");
        mEditWidth.setText("");
        mFrom.setText("");
        mTo.setText("");
        //TODO
    }

	/**
	 * Заполнены ли все поля на форме окна
	 * @return true - если заполнены, false - если нет
	 * */
	private boolean isFillAllFields()
	{
		boolean rez = true;

		if(mFrom.getText().toString().length() <= 0 || mTo.getText().length() <= 0)
			rez = false;

		if(mType.equals("2")) // Если посылка, то проверим габариты
		{
			if(mEditWidth.getText().length() <= 0 || mEditHeight.getText().length() <= 0 || mEditLength.getText().length() <= 0)
				rez = false;
		}

		return rez;
	}

    @Override
    public void ResponceOK(String TAG, final ArrayList<String> params)
    {
        if(!TAG.equals(Settings.REQ_TAG_CALC))
            return;

        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mSummaText.setVisibility(View.VISIBLE);
                mSummaText.setText(params.get(0) + " " + getString(R.string.Info_Rubles));
                setDefaultValues();
            }
        });
    }

    @Override
    public void ResponceError(String TAG, final String text)
    {
        Logs.e("Error in send; text = " + text);
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mSummaText.setVisibility(View.GONE);
                Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                //setDefaultValues();
            }
        });
    }

    //-------------------------------
    //GETTERS/SETTERS
    //-------------------------------

    //-------------------------------
    //INNER CLASSES
    //-------------------------------
}
