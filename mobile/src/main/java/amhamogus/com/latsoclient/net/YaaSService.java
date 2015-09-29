package amhamogus.com.latsoclient.net;


import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * HTTP requests that are made to the Lotso Product Repository
 * hosted on YaaS.
 */
public class YaaSService {

    // Url to YaaS powered Product API
    //private String productEndpoint = "https://lotso-nfc.cf3.hybris.com/productInstances/";

    // New endpoint as pf 09/28/2015
    private String productEndpoint = "http://lotso.cfapps.io/productInstances/";
    // A product from the Latso Store front stored in YaaS.
    JSONObject product;

    public JSONObject getProduct(String productID) {

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request =
                new Request.Builder().url(productEndpoint + "" + productID).build();


        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                String jsonArray = body.string();

                try {
                    product = new JSONObject(jsonArray);
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            } else {
                //TODO:
                Log.d("LOTSO OUT", "CALL FAIL");
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
        return product;
    }
}