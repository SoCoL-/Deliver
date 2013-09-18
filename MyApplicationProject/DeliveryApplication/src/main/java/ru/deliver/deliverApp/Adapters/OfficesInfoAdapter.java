package ru.deliver.deliverApp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.deliver.deliverApp.R;
import ru.deliver.deliverApp.Utils.OfficesAdd;

/**
 * Created by Evgenij on 18.09.13.
 *
 * Отображение списка филиалов в городе
 */
public class OfficesInfoAdapter extends BaseAdapter
{

    //-----------------------------
    //Constants
    //-----------------------------

    //-----------------------------
    //Variables
    //-----------------------------

    private ArrayList<OfficesAdd> mInfos;

    //-----------------------------
    //Ctors
    //-----------------------------

    public OfficesInfoAdapter()
    {
        this.mInfos = new ArrayList<OfficesAdd>();
    }

    //-----------------------------
    //Methods
    //-----------------------------

    public void addItems(ArrayList<OfficesAdd> items)
    {
        if(mInfos == null)
            return;

        this.mInfos.addAll(items);
    }

    public void clear()
    {
        if(mInfos == null)
            return;

        this.mInfos.clear();
    }

    @Override
    public int getCount()
    {
        return mInfos.size();
    }

    @Override
    public Object getItem(int i)
    {
        return mInfos.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.child_view, null);
        }

        if(mInfos.get(i).getName() != null && mInfos.get(i).getName().length() > 0)
            ((TextView)view.findViewById(R.id.child_name)).setText(mInfos.get(i).getName());
        else
            ((TextView)view.findViewById(R.id.child_name)).setText("");
        if(mInfos.get(i).getPhone() != null && mInfos.get(i).getPhone().length() > 0)
            ((TextView)view.findViewById(R.id.child_phone)).setText(viewGroup.getContext().getString(R.string.Info_Exp_Phone) + mInfos.get(i).getPhone());
        else
            ((TextView)view.findViewById(R.id.child_phone)).setText(viewGroup.getContext().getString(R.string.Info_Exp_Phone) + " - ");
        if(mInfos.get(i).getAddress() != null && mInfos.get(i).getAddress().length() > 0)
            ((TextView)view.findViewById(R.id.child_address)).setText(viewGroup.getContext().getString(R.string.Info_Exp_Address) + mInfos.get(i).getAddress());
        else
            ((TextView)view.findViewById(R.id.child_address)).setText(viewGroup.getContext().getString(R.string.Info_Exp_Address) + " - ");
        if(mInfos.get(i).getEMail() != null && mInfos.get(i).getEMail().length() > 0)
            ((TextView)view.findViewById(R.id.child_email)).setText(viewGroup.getContext().getString(R.string.Info_Exp_Email) + mInfos.get(i).getEMail());
        else
            ((TextView)view.findViewById(R.id.child_email)).setText(viewGroup.getContext().getString(R.string.Info_Exp_Email) + " - ");
        if(mInfos.get(i).getFax() != null && mInfos.get(i).getFax().length() > 0)
            ((TextView)view.findViewById(R.id.child_fax)).setText(viewGroup.getContext().getString(R.string.Info_Exp_Fax) + mInfos.get(i).getFax());
        else
            ((TextView)view.findViewById(R.id.child_fax)).setText(viewGroup.getContext().getString(R.string.Info_Exp_Fax) + " - ");

        return view;
    }
    //-----------------------------
    //Getters/Setters
    //-----------------------------

    //-----------------------------
    //Inner Classes
    //-----------------------------
}
