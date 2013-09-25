package ru.deliver.deliverApp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.deliver.deliverApp.R;

/**
 * Created by 1 on 22.08.13.
 *
 * Адаптер с текстом и стрелочкой вконце
 */
public class TextPicAdapter extends BaseAdapter
{
    //-------------------------------
    //CONSTANTS
    //-------------------------------

    //-------------------------------
    //VARIABLES
    //-------------------------------

    private ArrayList<String> mItems;

    //-------------------------------
    //CONSTRUCTORS
    //-------------------------------

    public TextPicAdapter()
    {
        this.mItems = new ArrayList<String>();
    }

    //-------------------------------
    //SUPER METHODS
    //-------------------------------

    //-------------------------------
    //METHODS
    //-------------------------------

    public void addItem(String item)
    {
        if(mItems == null)
            return;

        mItems.add(item);
        notifyDataSetChanged();
    }

    public void addAllItems(ArrayList<String> items)
    {
        if(mItems == null)
            return;

        mItems.addAll(items);
        notifyDataSetChanged();
    }

    public void deleteItem(int position)
    {
        if(mItems == null)
            return;

        mItems.remove(position);
        notifyDataSetChanged();
    }

    public void clear()
    {
        if(mItems == null)
            return;

        mItems.clear();
        notifyDataSetChanged();
    }

    //-------------------------------
    //GETTERS/SETTERS
    //-------------------------------

    @Override
    public int getCount()
    {
        return mItems.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.text_pic_adapter, parent, false);
        }

        ((TextView)convertView.findViewById(R.id.TPAdapter_Name)).setText(mItems.get(position));

        return convertView;
    }

    //-------------------------------
    //INNER CLASSES
    //-------------------------------
}
