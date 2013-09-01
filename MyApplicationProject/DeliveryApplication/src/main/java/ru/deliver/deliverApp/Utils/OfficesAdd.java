package ru.deliver.deliverApp.Utils;

/**
 * Created by 1 on 01.09.13.
 *
 * Дополнительная информация по городам
 */
public final class OfficesAdd
{
    private String EMail;
    private String Address;
    private String Phone;
    private String Name;
    private String Fax;

    public OfficesAdd()
    {}


    public String getEMail()
    {
        return EMail;
    }

    public void setEMail(String EMail)
    {
        this.EMail = EMail;
    }

    public String getAddress()
    {
        return Address;
    }

    public void setAddress(String address)
    {
        Address = address;
    }

    public String getPhone()
    {
        return Phone;
    }

    public void setPhone(String phone)
    {
        Phone = phone;
    }

    public String getName()
    {
        return Name;
    }

    public void setName(String name)
    {
        Name = name;
    }

    public String getFax()
    {
        return Fax;
    }

    public void setFax(String fax)
    {
        Fax = fax;
    }
}
