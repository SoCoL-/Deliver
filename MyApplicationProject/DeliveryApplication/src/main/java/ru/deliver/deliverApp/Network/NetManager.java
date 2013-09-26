package ru.deliver.deliverApp.Network;

import android.support.v4.app.FragmentActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import ru.deliver.deliverApp.DateBase.DBHelper;
import ru.deliver.deliverApp.Main;
import ru.deliver.deliverApp.R;
import ru.deliver.deliverApp.Setup.Logs;
import ru.deliver.deliverApp.Setup.Settings;
import ru.deliver.deliverApp.Utils.Favourite;
import ru.deliver.deliverApp.Utils.InfoFavouriteItem;
import ru.deliver.deliverApp.Utils.Offices;
import ru.deliver.deliverApp.Utils.OfficesAdd;

/**
 * Created by Evgenij on 28.08.13.
 *
 * Менеджер запросов на сервак
 */
public final class NetManager implements IProgress
{
    private AnswerServer mAnswer;
    private final FragmentActivity mActivity;

    public NetManager(FragmentActivity a)
    {
        this.mActivity = a;
    }

    public void setInterface(AnswerServer answer)
    {
        this.mAnswer = answer;
    }

    public void sendCalcReq(String from, String to, String weight, String type, String height, String width, String length)
    {
        HashMap<String, String> items = new HashMap<String, String>();
        items.put(Settings.FIELD_FROM, from);
        items.put(Settings.FIELD_TO, to);
        items.put(Settings.FIELD_WEIGHT, weight);
        items.put(Settings.FIELD_TYPE, type);
        if(type.equals("2"))
        {
            items.put(Settings.FIELD_D1, width);
            items.put(Settings.FIELD_D2, height);
            items.put(Settings.FIELD_D3, length);
        }

        JSONObject object = new JSONObject(items);
        Logs.i("json = " + object.toString());

        Logs.i("Start task");
        Request request = new Request(new RequestTask(Settings.REQ_TAG_CALC, object), mActivity);
        request.setInterface(this);
        Logs.i("execute");
        request.execute();
    }

    public void sendCall(String date, String time, String from, String to, String type, String name, String address, String person, String phone, String mail, String comment, String d1, String d2, String d3, String weight)
    {
        HashMap<String, String> items = new HashMap<String, String>();
        items.put(Settings.FIELD_DATA, date);
        items.put(Settings.FIELD_TIME, time);
        items.put(Settings.FIELD_FROM, from);
        items.put(Settings.FIELD_TO, to);
        items.put(Settings.FIELD_NAME, name);
        items.put(Settings.FIELD_ADDRESS, address);
        items.put(Settings.FIELD_PERSON, person);
        items.put(Settings.FIELD_PHONE, phone);
        items.put(Settings.FIELD_EMAIL, mail);
        items.put(Settings.FIELD_TYPE, type);
        items.put(Settings.FIELD_WEIGHT, weight);
        items.put(Settings.FIELD_COMMENT, comment);
        if(type.equals("2"))
        {
            items.put(Settings.FIELD_D1, d1);
            items.put(Settings.FIELD_D2, d2);
            items.put(Settings.FIELD_D3, d3);
        }

        JSONObject object = new JSONObject(items);
        Logs.i("json = " + object.toString());

        Logs.i("Start task");
        Request request = new Request(new RequestTask(Settings.REQ_TAG_CALL, object), mActivity);
        request.setInterface(this);
        Logs.i("execute");
        request.execute();
    }

    public void sendDeparture(ArrayList<String> numbers)
    {
        JSONObject object = new JSONObject();
        JSONArray items = new JSONArray(numbers);
        try
        {
            Logs.i("json = " + items.toString());
            Logs.i("Start task");
            object.put("items", items);
            Request request = new Request(new RequestTask(Settings.REQ_TAG_DEPA, object), mActivity);
            request.setInterface(this);
            Logs.i("execute");
            request.execute();
        }
        catch(JSONException e)
        {
            Logs.e(e.toString());
        }
    }

    public void sendOffices()
    {
        if(((Main)mActivity).isInternet)  //Если есть интернет, то запросим список филиалов у сервака
        {
            HashMap<String, String> items = new HashMap<String, String>();
            items.put("get", "1"); // <----- ????????

            JSONObject object = new JSONObject(items);
            Logs.i("json = " + object.toString());

            Logs.i("Start task");
            Request request = new Request(new RequestTask(Settings.REQ_TAG_OFFI, object), mActivity);
            request.setInterface(this);
            Logs.i("execute");
            request.execute();
        }
        else        //Если нет инета, то запросим список филиалов у БД
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    DBHelper helper = new DBHelper(mActivity, null);
                    helper.openDB();

                    ((Main)mActivity).mOffices = helper.findAllContacts();

                    helper.closeDB();

                }
            }).start();
        }
    }

    @Override
    public void GetResponce(final ResponceTask task)
    {
        Logs.i("code = " + task.mResponceCode + ", tag = " + task.mResponceTag);

        if(mAnswer != null)
        {
            if(task.mResponceCode == 200)
            {
                if(task.mResponceTag.equals(Settings.REQ_TAG_CALC))
                {
                    try
                    {
                        String text = Settings.convertStreamToString(task.mJSON);
                        Logs.i("text="+text);
                        text = text.trim();
                        Logs.i("length = "+text.length());
                        Logs.i("CITY_ERROR length = "+Settings.CITY_ERROR.length());
                        if(text.equals(""))
                        {
                            Logs.i("Nothing");
                            mAnswer.ResponceError(task.mResponceTag, "Error");
                        }
                        else if(text.equals(Settings.CITY_ERROR))
                        {
                            Logs.i("Settings.CITY_ERROR");
                            mAnswer.ResponceError(task.mResponceTag, mActivity.getString(R.string.Error_Server_City));
                        }
                        else if(text.equals(Settings.WEIGHT_ERROR))
                        {
                            Logs.i("Settings.WEIGHT_ERROR");
                            mAnswer.ResponceError(task.mResponceTag, mActivity.getString(R.string.Error_Server_Weight));
                        }
                        else if(text.equals(Settings.DATA_ERROR))
                        {
                            Logs.i("Settings.DATA_ERROR");
                            mAnswer.ResponceError(task.mResponceTag, mActivity.getString(R.string.Error_Server_Data));
                        }
                        else
                        {
                            Logs.i("Not error, to json");
                            JSONObject object = new JSONObject(text);
                            String summa = object.getString("total");
                            Logs.i("Summa = " + summa);

                            ArrayList<String> params = new ArrayList<String>(1);
                            params.add(summa);
                            mAnswer.ResponceOK(task.mResponceTag, params);
                        }
                    }
                    catch (IOException e)
                    {
                        Logs.e(e.toString());
                    }
                    catch(JSONException e)
                    {
                        Logs.e(e.toString());
                    }
                }
                else if(task.mResponceTag.equals(Settings.REQ_TAG_CALL))
                {
                    try
                    {
                        String text = Settings.convertStreamToString(task.mJSON);
                        Logs.i("text="+text);
                        text = text.trim();
                        if(text.length() > 15)
                        {
                            //Все прошло успешно, сообщим пользователю
                            ArrayList<String> params = new ArrayList<String>(1);
                            params.add(mActivity.getString(R.string.Info_CallDone));
                            mAnswer.ResponceOK(task.mResponceTag, params);
                        }
                        else
                        {
                            //Ошибка
                            mAnswer.ResponceError(task.mResponceTag, mActivity.getString(R.string.Error_CallError));
                        }
                    }
                    catch (IOException e)
                    {
                        Logs.e(e.toString());
                    }
                }
                else if(task.mResponceTag.equals(Settings.REQ_TAG_DEPA))
                {
                    try
                    {
                        String text = Settings.convertStreamToString(task.mJSON);
                        Logs.i("text="+text);
                        text = text.trim();
                        if(text.equals(Settings.DEPARTURE_ERROR))
                        {
                            Logs.i("Settings.DEPARTURE_ERROR");
                            mAnswer.ResponceError(task.mResponceTag, mActivity.getString(R.string.Error_Server_Departure));
                        }
                        Logs.i("text="+text);
                        JSONArray jsons = new JSONArray(text);
                        ArrayList<Favourite> mDepartures = new ArrayList<Favourite>();
                        if(jsons != null)
                        {
                            for(int j = 0; j < jsons.length(); j++)
                            {
                                JSONObject json = jsons.getJSONObject(j);

                                Favourite mDeparture = new Favourite();
                                mDeparture.setNumber(json.getString("num"));
                                mDeparture.setFrom(json.getString("from"));
                                mDeparture.setTo(json.getString("to"));

                                JSONArray items = json.getJSONArray("cp");
                                if(items != null)
                                {
                                    ArrayList<InfoFavouriteItem> favItems = new ArrayList<InfoFavouriteItem>(items.length());
                                    for(int i = 0; i < items.length(); i++)
                                    {
                                        InfoFavouriteItem ifi = new InfoFavouriteItem();
                                        JSONObject item = items.getJSONObject(i);
                                        ifi.setDate(item.getString("FORMATDATE"));
                                        ifi.setTime(item.getString("POINTTIME"));
                                        ifi.setDescription(item.getString("HEAP"));
                                        favItems.add(ifi);
                                    }
                                    mDeparture.setFavItems(favItems);
                                }
                                mDepartures.add(mDeparture);
                            }
                        }

                        if(mDepartures.size() == 1)
                            ((Main)mActivity).mBufDeparture = mDepartures.get(0);
                        else if(mDepartures.size() > 1)
                            ((Main)mActivity).mFavourites = mDepartures;

                        mAnswer.ResponceOK(task.mResponceTag, null);
                    }
                    catch (IOException e)
                    {
                        Logs.e(e.toString());
                    }
                    catch (JSONException e)
                    {
                        Logs.e(e.toString());
                    }
                }
                else if(task.mResponceTag.equals(Settings.REQ_TAG_OFFI))
                {
                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                                String text = Settings.convertStreamToString(task.mJSON);
                                Logs.i("text="+text);
                                text = text.trim();
                                JSONArray jsons = new JSONArray(text);
                                ArrayList<Offices> mOffices = new ArrayList<Offices>();
                                Logs.i("jsons = " + jsons.toString());
                                if(jsons != null)
                                {
                                    for(int i = 0; i < jsons.length(); i++)
                                    {
                                        JSONObject json = jsons.getJSONObject(i);
                                        Offices office = new Offices();
                                        office.setCity(json.getString("city"));

                                        JSONArray infos = json.getJSONArray("contacts");
                                        if(infos != null)
                                        {
                                            ArrayList<OfficesAdd> offInfo = new ArrayList<OfficesAdd>();
                                            for(int j = 0; j < infos.length(); j++)
                                            {
                                                JSONObject info = infos.getJSONObject(j);
                                                OfficesAdd oa = new OfficesAdd();
                                                oa.setAddress(info.getString("address"));
                                                oa.setEMail(info.getString("email"));
                                                oa.setFax(getString(info, "fax"));
                                                oa.setName(info.getString("name"));
                                                oa.setPhone(info.getString("phone"));
                                                offInfo.add(oa);
                                            }
                                            office.setContacts(offInfo);
                                        }
                                        mOffices.add(office);
                                    }

                                    ((Main)mActivity).mOffices = mOffices;
                                    Logs.i("!!!!!!!!!!!!Receive offices!!!!!!!!!!!");
                                    mAnswer.ResponceOK(task.mResponceTag, null);

                                    if(((Main)mActivity).isInternet)
                                    {
                                        Logs.i("Save CONTACTS to BD");
                                        DBHelper helper = new DBHelper(mActivity, null);
                                        helper.openDB();
                                        helper.deleteContacts();

                                        //Сохраним автоматически все филиалы в БД
                                        for(Offices o : mOffices)
                                        {
                                            Logs.i("Save contact");
                                            helper.addContact(o);
                                            for(OfficesAdd oa : o.getContacts())
                                            {
                                                Logs.i("Save contactInfo");
                                                helper.addContactInfo(o.getCity(), oa);
                                            }
                                        }

                                        helper.closeDB();
                                    }
                                }
                            }
                            catch (IOException e)
                            {
                                Logs.e(e.toString());
                            }
                            catch (JSONException e)
                            {
                                Logs.e(e.toString());
                            }
                        }
                    }).start();
                }
            }
            else
            {
                if(task.mResponceCode == -1)
                    ((Main)mActivity).isInternet = false;
                mAnswer.ResponceError(task.mResponceTag, task.mErrorText);
            }
        }
    }

    private String getString(JSONObject o, String key)
    {
        String rez;
        try
        {
            rez = o.getString(key);
        }
        catch (JSONException e)
        {
            return "";
        }

        return rez;
    }
}
