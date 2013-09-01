package ru.deliver.deliverApp.Adapters;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.deliver.deliverApp.R;
import ru.deliver.deliverApp.Utils.OfficesAdd;

/**
 * Created by 1 on 01.09.13.
 *
 * Адаптер для отображения списка офисов
 */
public class ExpandAdapter extends BaseExpandableListAdapter
{
    private ArrayList<ArrayList<OfficesAdd>> mItems;
    private ArrayList<String> mNames;
    private LayoutInflater mInflater;

    public ExpandAdapter(FragmentActivity fa)
    {
        mItems = new ArrayList<ArrayList<OfficesAdd>>();
        mNames = new ArrayList<String>();
        mInflater = LayoutInflater.from(fa);
    }

    public void addItems(ArrayList<ArrayList<OfficesAdd>> items, ArrayList<String> names)
    {
        if(mItems == null || mNames == null)
            return;

        mNames.addAll(names);
        mItems.addAll(items);

        notifyDataSetChanged();
    }

    public void clear()
    {
        if(mItems == null || mNames == null)
            return;

        mNames.clear();
        mItems.clear();

        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount()
    {
        return mItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return mItems.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return mNames.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return mItems.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return 0;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        if(convertView == null)
            convertView = mInflater.inflate(R.layout.group_view, null);

        ((TextView)convertView.findViewById(R.id.group_name)).setText(mNames.get(groupPosition));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        if(convertView == null)
            convertView = mInflater.inflate(R.layout.child_view, null);

        if(mItems.get(groupPosition).get(childPosition).getName() != null && mItems.get(groupPosition).get(childPosition).getName().length() > 0)
            ((TextView)convertView.findViewById(R.id.child_name)).setText(mItems.get(groupPosition).get(childPosition).getName());
        else
            ((TextView)convertView.findViewById(R.id.child_name)).setVisibility(View.GONE);
        if(mItems.get(groupPosition).get(childPosition).getPhone() != null && mItems.get(groupPosition).get(childPosition).getPhone().length() > 0)
            ((TextView)convertView.findViewById(R.id.child_phone)).setText(mItems.get(groupPosition).get(childPosition).getPhone());
        else
            ((TextView)convertView.findViewById(R.id.child_phone)).setVisibility(View.GONE);
        if(mItems.get(groupPosition).get(childPosition).getAddress() != null && mItems.get(groupPosition).get(childPosition).getAddress().length() > 0)
            ((TextView)convertView.findViewById(R.id.child_address)).setText(mItems.get(groupPosition).get(childPosition).getAddress());
        else
            ((TextView)convertView.findViewById(R.id.child_address)).setVisibility(View.GONE);
        if(mItems.get(groupPosition).get(childPosition).getEMail() != null && mItems.get(groupPosition).get(childPosition).getEMail().length() > 0)
            ((TextView)convertView.findViewById(R.id.child_email)).setText(mItems.get(groupPosition).get(childPosition).getEMail());
        else
            ((TextView)convertView.findViewById(R.id.child_email)).setVisibility(View.GONE);
        if(mItems.get(groupPosition).get(childPosition).getFax() != null && mItems.get(groupPosition).get(childPosition).getFax().length() > 0)
            ((TextView)convertView.findViewById(R.id.child_fax)).setText(mItems.get(groupPosition).get(childPosition).getFax());
        else
            ((TextView)convertView.findViewById(R.id.child_fax)).setVisibility(View.GONE);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
}
