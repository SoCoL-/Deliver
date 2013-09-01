package ru.deliver.deliverApp.Utils;

import java.util.ArrayList;

/**
 * Created by 1 on 01.09.13.
 *
 * Описание элемента отделения
 */
public final class Offices
{
    private String City;
    private ArrayList<OfficesAdd> Contacts;

    public Offices()
    {
        this.Contacts = new ArrayList<OfficesAdd>();
    }

    public String getCity()
    {
        return City;
    }

    public void setCity(String city)
    {
        City = city;
    }

    public ArrayList<OfficesAdd> getContacts()
    {
        return Contacts;
    }

    public void setContacts(ArrayList<OfficesAdd> contacts)
    {
        Contacts = contacts;
    }
}
