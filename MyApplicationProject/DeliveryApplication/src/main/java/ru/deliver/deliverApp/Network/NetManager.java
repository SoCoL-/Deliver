package ru.deliver.deliverApp.Network;

import org.json.JSONObject;

import java.util.HashMap;

import ru.deliver.deliverApp.Setup.Logs;

/**
 * Created by Evgenij on 28.08.13.
 *
 * Менеджер запросов на сервак
 */
public final class NetManager implements IProgress
{
    private AnswerServer mAnswer;

    public void setInterface(AnswerServer answer)
    {
        this.mAnswer = answer;
    }

    public void sendCalcReq(String from, String to, String weight, String type, String height, String width, String length)
    {
        //TODO
        HashMap<String, String> items = new HashMap<String, String>();
        items.put("from", from);
        items.put("to", to);
        items.put("weigth", weight);
        items.put("type", type);
        if(type.equals("2"))
        {
            items.put("d1", height);
            items.put("d2", width);
            items.put("d3", length);
        }

        JSONObject object = new JSONObject(items);
        Logs.i("json = " + object.toString());

        Logs.i("Start task");
        Request request = new Request(new RequestTask("CALC", object));
        request.setInterface(this);
        Logs.i("execute");
        request.execute();
    }

    public void sendCall(String date, String time, String from, String to, String type, String name, String address, String person, String phone, String mail, String comment, String d1, String d2, String d3, String weight)
    {
        HashMap<String, String> items = new HashMap<String, String>();
        items.put("date", date);
        items.put("time", time);
        items.put("from", from);
        items.put("to", to);
        items.put("name", name);
        items.put("address", address);
        items.put("person", person);
        items.put("phone", phone);
        items.put("mail", mail);
        items.put("type", type);
        items.put("weight", weight);
        items.put("comment", comment);
        if(type.equals("2"))
        {
            items.put("d1", d1);
            items.put("d2", d2);
            items.put("d3", d3);
        }

        JSONObject object = new JSONObject(items);
        Logs.i("json = " + object.toString());

        Logs.i("Start task");
        Request request = new Request(new RequestTask("DELIVERY", object));
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
                Logs.i(task.mJSON.toString());
                mAnswer.ResponceOK(task.mResponceTag);
            }
            else
                mAnswer.ResponceError(task.mResponceTag);
        }
    }
}
