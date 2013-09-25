package ru.deliver.deliverApp.Network;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.deliver.deliverApp.R;
import ru.deliver.deliverApp.Setup.Logs;
import ru.deliver.deliverApp.Setup.Settings;

/**
 * Created by Evgenij on 28.08.13.
 *
 * Асинхронный запрос
 */
public final class Request extends AsyncTask<String, Integer, ResponceTask>
{
    private static final String ADDRESS = "http://exmail.ws/mobile/mac.php";
    //private static final String ADDRESS = "http://exmail.ws/mobile/uagent.php";

    private static final int HTTP_OK = 200;
    private static final String TYPE = "TYPE";
    private static final String DATA = "DATA";

    private RequestTask mReqTask;
    private ResponceTask mRespTask;

    private IProgress progress;
    private FragmentActivity mActivity;

    public Request(RequestTask task, FragmentActivity a)
    {
        Logs.i("Create Request");
        this.mReqTask = task;
        this.mActivity = a;
    }

    public void setInterface(IProgress p)
    {
        Logs.i("Set Interface");
        this.progress = p;

        if(progress != null)
            if(mRespTask != null)
                progress.GetResponce(mRespTask);
    }

    @Override
    protected ResponceTask doInBackground(String... strings)
    {
        Logs.i("Start doInBackground");
        HttpPost mPost = new HttpPost(ADDRESS);

        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setSoTimeout(httpParameters, 60000);
        HttpConnectionParams.setConnectionTimeout(httpParameters, 60000);

        DefaultHttpClient client = new DefaultHttpClient(httpParameters);

        if(mReqTask == null)
        {
            Logs.i("mReqTask == null");
        }

        try
        {
            List<BasicNameValuePair> nvps = new ArrayList<BasicNameValuePair>();
            nvps.add(new BasicNameValuePair(TYPE, mReqTask.mRquestTag));
            if(mReqTask.mRquestTag.equals(Settings.REQ_TAG_DEPA))
                nvps.add(new BasicNameValuePair(DATA, mReqTask.mJSON.get("items").toString()));
            else
                nvps.add(new BasicNameValuePair(DATA, mReqTask.mJSON.toString()));
            UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);

            mPost.setEntity(p_entity);
            Logs.i("request = " + nvps.toString());

            long time = System.currentTimeMillis();
            Logs.i("1Начало запроса: " + System.currentTimeMillis());
            HttpResponse response = client.execute(mPost);
            Logs.i(response.getStatusLine().toString());
            if(response.getStatusLine().getStatusCode() != HTTP_OK)
            {
                Logs.e("Вернули не с 200, а с "+response.getStatusLine().getStatusCode());
                return new ResponceTask(response.getStatusLine().getStatusCode(), mReqTask.mRquestTag, null, mActivity.getString(R.string.Error_Server) + response.getStatusLine().getStatusCode());
            }
            HttpEntity responseEntity = response.getEntity();
            Logs.i("2Конец запроса: "+(System.currentTimeMillis()-time));

            return new ResponceTask(HTTP_OK, mReqTask.mRquestTag, responseEntity.getContent(), null);
        }
        catch(UnknownHostException e)
        {
            Logs.e(e.toString());
            return new ResponceTask(-1, mReqTask.mRquestTag, null, mActivity.getString(R.string.Error_Server_NoInternet));
        }
        catch(Exception e)
        {
            Logs.e(e.toString());
            return new ResponceTask(-2, mReqTask.mRquestTag, null, mActivity.getString(R.string.Error_Server_Wait));
        }
    }

    @Override
    protected void onPostExecute(ResponceTask responce)
    {
        Logs.i("Data is received");
        mRespTask = responce;

        if(progress != null && mRespTask != null)
        {
            Logs.i("Send data to netManager");
            progress.GetResponce(mRespTask);
        }

        progress = null;
    }
}
