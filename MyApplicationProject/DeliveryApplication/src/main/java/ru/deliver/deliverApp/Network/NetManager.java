package ru.deliver.deliverApp.Network;

import org.json.JSONArray;
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
        JSONArray jArray = new JSONArray();
        jArray.put(to);
        jArray.put(from);
        jArray.put(weight);
        jArray.put(type);
        if(type.equals("2"))
        {
            jArray.put(height);
            jArray.put(width);
            jArray.put(length);
        }
        Logs.i(jArray.toString());

        Logs.i("Start task");
        Request request = new Request(new RequestTask("CALC", jArray));
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
