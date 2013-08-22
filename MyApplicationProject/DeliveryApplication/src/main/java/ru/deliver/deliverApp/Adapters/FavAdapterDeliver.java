package ru.deliver.deliverApp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ru.deliver.deliverApp.R;
import ru.deliver.deliverApp.Utils.ItemFav;

/**
 * Created by Evgenij on 22.08.13.
 *
 * Класс отображения элементов избранного
 */
public class FavAdapterDeliver extends BaseAdapter
{
	//---------------------------------
	//CONSTANTS
	//---------------------------------

	//---------------------------------
	//VARIABLES
	//---------------------------------

	private ArrayList<ItemFav> mItems;

	//---------------------------------
	//SUPER
	//---------------------------------

	public FavAdapterDeliver()
	{
		mItems = new ArrayList<ItemFav>();
	}

	//---------------------------------
	//METHODS
	//---------------------------------

	public void addItem(ItemFav item)
	{
		if(mItems == null)
			return;

		mItems.add(item);
		notifyDataSetChanged();
	}

	public void addAllItems(ArrayList<ItemFav> items)
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

	//---------------------------------
	//GETTERS/SETTERS
	//---------------------------------

	@Override
	public int getCount()
	{
		return mItems.size();
	}

	@Override
	public Object getItem(int i)
	{
		return mItems.get(i);
	}

	@Override
	public long getItemId(int i)
	{
		return i;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup)
	{
		View itemView = view;

		if(view == null)
		{
			LayoutInflater inflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			itemView = inflater.inflate(R.layout.fav_item, null);
		}

		((TextView)itemView.findViewById(R.id.FavItem_Number)).setText(mItems.get(position).getNumber());
		((TextView)itemView.findViewById(R.id.FavItem_State)).setText(mItems.get(position).getState());

		return itemView;
	}

	//---------------------------------
	//INNER CLASSES
	//---------------------------------        
}
