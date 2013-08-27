package ru.deliver.deliverApp.Utils;

import java.util.ArrayList;

/**
 * Created by Evgenij on 27.08.13.
 *
 * Структура хранения записи в базе данных
 */
public final class Favourite
{
    private int mNumber;
    private String mFrom;
    private String mTo;
    private ArrayList<InfoFavouriteItem> mFavItems;

    public Favourite()
    {
        this.mFavItems = new ArrayList<InfoFavouriteItem>();
    }

    public Favourite(int num, String from, String to)
    {
        this.mFavItems = new ArrayList<InfoFavouriteItem>();
        this.mNumber = num;
        this.mFrom = from;
        this.mTo = to;
    }

    public int getNumber()
    {
        return mNumber;
    }

    public void setNumber(int mNumber)
    {
        this.mNumber = mNumber;
    }

    public String getFrom()
    {
        return mFrom;
    }

    public void setFrom(String mFrom)
    {
        this.mFrom = mFrom;
    }

    public String getTo()
    {
        return mTo;
    }

    public void setTo(String mTo)
    {
        this.mTo = mTo;
    }

    public ArrayList<InfoFavouriteItem> getFavItems()
    {
        return mFavItems;
    }

    public void setFavItems(ArrayList<InfoFavouriteItem> mFavItems)
    {
        this.mFavItems = mFavItems;
    }
}
