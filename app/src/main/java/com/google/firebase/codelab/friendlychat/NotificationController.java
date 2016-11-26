package com.google.firebase.codelab.friendlychat;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


import static com.google.firebase.crash.FirebaseCrash.log;

/**
 * Created by ideomobile on 11/11/16.
 */

public class NotificationController {


    Context mContext;
    OkHttpClient mClient;

    public NotificationController(Context mContext) {
        this.mContext = mContext;
        mClient = new OkHttpClient();
    }

    // This snippet takes the simple approach of using the first returned Google account,
// but you can pick any Google account on the device.
    private String getAccount() {
        if (ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "default";
        }
        Account[] accounts = AccountManager.get(mContext).
                getAccountsByType("com.google");
        if (accounts.length == 0) {
            return null;
        }
        return accounts[0].name;
    }


    private String getTokenId() {
        String accountName = getAccount();

// Initialize the scope using the client ID you got from the Console.
        final String scope = "audience:server:client_id:"
                + "309117443195-oh0h2goptqnpvnh6lil7er843fjg7pg8.apps.googleusercontent.com";
        String idToken = null;
        try {
            idToken = GoogleAuthUtil.getToken(mContext, accountName, scope);
        } catch (Exception e) {
            log("exception while getting idToken: " + e);
        }
        return idToken;
    }

//
//    public void signToGroup() {
//
//        String sendToDeviceTokenString = "dSTD1uvHd60:APA91bHLdwLcFtmt-Ee6wEiaXSGpk7flxrD5UwNklH9uxBljYWli9X0bW1pRUOiE6fbCGD40yDqoj-xdgNsRVL-p7xvBQo0z9AF-BEDtguuhNhDMnP8-MsbNV1MqdzPQBVO9tNn4M37O";
//        String notifKey = "";
//
//        try {
//            notifKey = addNotificationKey("309117443195", getAccount(), sendToDeviceTokenString, getTokenId());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        sendNotificationToUserGroup(notifKey);
//    }
//
//    public String addNotificationKey(
//            String senderId, String userEmail, String registrationId, String idToken)
//            throws IOException, JSONException {
//        URL url = new URL("https://android.googleapis.com/gcm/googlenotification");
//        HttpURLConnection con = (HttpURLConnection) url.openConnection();
//        con.setDoOutput(true);
//
//        // HTTP request header
//        con.setRequestProperty("project_id", senderId);//////////////
//        con.setRequestProperty("Content-Type", "application/json");
//        con.setRequestProperty("Accept", "application/json");
//        con.setRequestMethod("POST");
//        con.connect();
//
//        // HTTP request
//        JSONObject data = new JSONObject();
//        data.put("operation", "add");
//        data.put("notification_key_name", userEmail);///////////////////
//        data.put("registration_ids", new JSONArray(Arrays.asList(registrationId)));/////
//        data.put("id_token", idToken);//////////
//
//        OutputStream os = con.getOutputStream();
//        os.write(data.toString().getBytes("UTF-8"));
//        os.close();
//
//        // Read the response into a string
//        InputStream is = con.getInputStream();
//        String responseString = new Scanner(is, "UTF-8").useDelimiter("\\A").next();
//        is.close();
//
//        // Parse the JSON string and return the notification key
//        JSONObject response = new JSONObject(responseString);
//        return response.getString("notification_key");//////
//
//    }


//    public String removeNotificationKey(
//            String senderId, String userEmail, String registrationId, String idToken)
//            throws IOException, JSONException {
//        URL url = new URL("https://android.googleapis.com/gcm/googlenotification");
//        HttpURLConnection con = (HttpURLConnection) url.openConnection();
//        con.setDoOutput(true);
//
//        // HTTP request header
//        con.setRequestProperty("project_id", senderId);
//        con.setRequestProperty("Content-Type", "application/json");
//        con.setRequestProperty("Accept", "application/json");
//        con.setRequestMethod("POST");
//        con.connect();
//
//        // HTTP request
//        JSONObject data = new JSONObject();
//        data.put("operation", "remove");
//        data.put("notification_key_name", userEmail);
//        data.put("registration_ids", new JSONArray(Arrays.asList(registrationId)));
//        data.put("id_token", idToken);
//
//        OutputStream os = con.getOutputStream();
//        os.write(data.toString().getBytes("UTF-8"));
//        os.close();
//
//        // Read the response into a string
//        InputStream is = con.getInputStream();
//        String responseString = new Scanner(is, "UTF-8").useDelimiter("\\A").next();
//        is.close();
//
//        // Parse the JSON string and return the notification key
//        JSONObject response = new JSONObject(responseString);
//        return response.getString("notification_key");
//
//    }
//
//    public void sendNotificationToUserGroup(String aUniqueKey) {
//        FirebaseMessaging fm = FirebaseMessaging.getInstance();
//        String to = aUniqueKey; // the notification key
//        AtomicInteger msgId = new AtomicInteger();
//        fm.send(new RemoteMessage.Builder(to)
//                .setMessageId(msgId.toString())
//                .addData("hello", "world")
//                .build());
//    }

//
//    class SendNotifAssyncTask extends AsyncTask<String, Void, Boolean> {
//
//        private Exception exception;
//
//        protected Boolean doInBackground(String... urls) {
//            signToGroup();
//
//            return true;
//        }
//
//
//        protected void onPostExecute(Boolean feed) {
//            // TODO: check this.exception
//            // TODO: do something with the feed
//        }
//    }
//
//
//
//    public void sendNotif(){
//        new SendNotifAssyncTask().execute();
//    }
//

    public static final MediaType JSON  = MediaType.parse("application/json; charset=utf-8");;

    public static final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    public void sendMessage(final JSONArray recipients, final String title, final String body, final String icon, final String message) {


        new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    JSONObject root = new JSONObject();
                    JSONObject notification = new JSONObject();
                    notification.put("body", body);
                    notification.put("title", title);
                    notification.put("icon", icon);

                    JSONObject data = new JSONObject();
                    data.put("message", message);
                    root.put("notification", notification);
                    root.put("data", data);
                    root.put("registration_ids", recipients);

                    String result = postToFCM(root.toString());
                    Log.d("notif doInBackground", "Result: " + result);
                    return result;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                try {
                    JSONObject resultJson = new JSONObject(result);
                    int success, failure;
                    success = resultJson.getInt("success");
                    failure = resultJson.getInt("failure");
                    Toast.makeText(mContext, "Message Success: " + success + "Message Failed: " + failure, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "Message Failed, Unknown error occurred.", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
    }

    String postToFCM(String bodyString) throws IOException {
        //String fromKey = "dSTD1uvHd60:APA91bHLdwLcFtmt-Ee6wEiaXSGpk7flxrD5UwNklH9uxBljYWli9X0bW1pRUOiE6fbCGD40yDqoj-xdgNsRVL-p7xvBQo0z9AF-BEDtguuhNhDMnP8-MsbNV1MqdzPQBVO9tNn4M37O";

        String nexus6p = "dSTD1uvHd60:APA91bHLdwLcFtmt-Ee6wEiaXSGpk7flxrD5UwNklH9uxBljYWli9X0bW1pRUOiE6fbCGD40yDqoj-xdgNsRVL-p7xvBQo0z9AF-BEDtguuhNhDMnP8-MsbNV1MqdzPQBVO9tNn4M37O";
        String nexusS ="dWswpCvgpyc:APA91bHdmJzphQgHeT1VvePeIhagqmltsjZ1yhQ_7FpIp-mL79fqzL8X87EiYOX7D7o7XddZ2VLe4Uo_QV8EQwe1yoOcyxYeYxYS8UjPLQm7S7KLyYYB81FobI5TunpAJCh6W1K-DEbw";


        RequestBody body = RequestBody.create(JSON, bodyString);
        Request request = new Request.Builder()
                .url(FCM_MESSAGE_URL)
                .post(body)
                .addHeader("Authorization", "key=" + "AIzaSyAQ5hGFihfNubVsP4f6YFhXseBrNg6fSio")
                .build();
        Response response = mClient.newCall(request).execute();
        return response.body().string();
    }
}


