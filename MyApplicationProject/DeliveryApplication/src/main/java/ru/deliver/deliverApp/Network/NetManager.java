package ru.deliver.deliverApp.Network;

import android.support.v4.app.FragmentActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import ru.deliver.deliverApp.Main;
import ru.deliver.deliverApp.R;
import ru.deliver.deliverApp.Setup.Logs;
import ru.deliver.deliverApp.Setup.Settings;
import ru.deliver.deliverApp.Utils.Favourite;
import ru.deliver.deliverApp.Utils.InfoFavouriteItem;

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

    @Override
    public void GetResponce(ResponceTask task)
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
                        if(text.equals(Settings.CITY_ERROR))
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
                        text = text.substring(1, text.length()-1);
                        Logs.i("text="+text);
                        JSONObject json = new JSONObject(text);

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

                        ((Main)mActivity).mBufDeparture = mDeparture;
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
            }
            else
                mAnswer.ResponceError(task.mResponceTag, task.mErrorText);
        }
    }
}
