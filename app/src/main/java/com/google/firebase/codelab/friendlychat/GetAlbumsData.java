//package com.google.firebase.codelab.friendlychat;
//
//import android.os.AsyncTask;
//import android.view.View;
//import android.widget.LinearLayout;
//import android.widget.Toast;
//
//import com.facebook.internal.Utility;
//import com.google.firebase.auth.FirebaseAuth;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
//import static com.facebook.FacebookSdk.getApplicationContext;
//
///**
// * Created by zeevi on 4/1/2017.
// */
//
//public class GetAlbumsData extends AsyncTask<Void, Void, Void> {
//
//    LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
//
//    @Override
//    protected void onPreExecute() {
//
//        // SHOW THE PROGRESS BAR (SPINNER) WHILE LOADING ALBUMS
//        linlaHeaderProgress.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    protected Void doInBackground(Void... params) {
//
//        // CHANGE THE LOADING MORE STATUS TO PREVENT DUPLICATE CALLS FOR
//        // MORE DATA WHILE LOADING A BATCH
//        loadingMore = true;
//
//        // SET THE INITIAL URL TO GET THE FIRST LOT OF ALBUMS
//        URL = "https://graph.facebook.com/" + FirebaseAuth.getInstance().getCurrentUser().getUid()
//                + "/albums&access_token="
//                + Utility.mFacebook.getAccessToken() + "?limit=10";
//
//        try {
//
//            HttpClient hc = new DefaultHttpClient();
//            HttpGet get = new HttpGet(URL);
//            HttpResponse rp = hc.execute(get);
//
//            if (rp.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                String queryAlbums = EntityUtils.toString(rp.getEntity());
//
//                JSONObject JOTemp = new JSONObject(queryAlbums);
//
//                JSONArray JAAlbums = JOTemp.getJSONArray("data");
//
//                if (JAAlbums.length() == 0) {
//                    stopLoadingData = true;
//                    Runnable run = new Runnable() {
//
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(),
//                                    "No more Albums", Toast.LENGTH_SHORT)
//                                    .show();
//                        }
//                    };
//                    Albums.this.runOnUiThread(run);
//
//                } else {
//                    // PAGING JSONOBJECT
//                    if (JOTemp.has("paging"))   {
//                        JSONObject JOPaging = JOTemp.getJSONObject("paging");
//
//                        if (JOPaging.has("next")) {
//                            String initialpagingURL = JOPaging
//                                    .getString("next");
//
//                            String[] parts = initialpagingURL.split("limit=10");
//                            String getLimit = parts[1];
//
//                            pagingURL = "https://graph.facebook.com/"
//                                    + initialUserID + "/albums&access_token="
//                                    + Utility.mFacebook.getAccessToken()
//                                    + "?limit=10" + getLimit;
//
//                        } else {
//                            stopLoadingData = true;
//                            Runnable run = new Runnable() {
//
//                                @Override
//                                public void run() {
//                                    Toast.makeText(getApplicationContext(),
//                                            "No more Albums",
//                                            Toast.LENGTH_SHORT).show();
//                                }
//                            };
//                            Albums.this.runOnUiThread(run);
//                        }
//                    } else {
//                        stopLoadingData = true;
//                        Runnable run = new Runnable() {
//
//                            @Override
//                            public void run() {
//                                Toast.makeText(getApplicationContext(),
//                                        "No more Albums",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        };
//                        Albums.this.runOnUiThread(run);
//
//                    }
//
//                    getAlbums albums;
//
//                    for (int i = 0; i < JAAlbums.length(); i++) {
//                        JSONObject JOAlbums = JAAlbums.getJSONObject(i);
//
//                        if (JOAlbums.has("link")) {
//
//                            albums = new getAlbums();
//
//                            // GET THE ALBUM ID
//                            if (JOAlbums.has("id")) {
//                                albums.setAlbumID(JOAlbums.getString("id"));
//                            } else {
//                                albums.setAlbumID(null);
//                            }
//
//                            // GET THE ALBUM NAME
//                            if (JOAlbums.has("name")) {
//                                albums.setAlbumName(JOAlbums
//                                        .getString("name"));
//                            } else {
//                                albums.setAlbumName(null);
//                            }
//
//                            // GET THE ALBUM COVER PHOTO
//                            if (JOAlbums.has("cover_photo")) {
//                                albums.setAlbumCover("https://graph.facebook.com/"
//                                        + JOAlbums.getString("cover_photo")
//                                        + "/picture?type=normal"
//                                        + "&access_token="
//                                        + Utility.mFacebook
//                                        .getAccessToken());
//                            } else {
//                                albums.setAlbumCover("https://graph.facebook.com/"
//                                        + JOAlbums.getString("id")
//                                        + "/picture?type=album"
//                                        + "&access_token="
//                                        + Utility.mFacebook
//                                        .getAccessToken());
//                            }
//
//                            // GET THE ALBUM'S PHOTO COUNT
//                            if (JOAlbums.has("count")) {
//                                albums.setAlbumPhotoCount(JOAlbums
//                                        .getString("count"));
//                            } else {
//                                albums.setAlbumPhotoCount("0");
//                            }
//
//                            arrAlbums.add(albums);
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(Void result) {
//
//        // SET THE ADAPTER TO THE LISTVIEW
//        lv.setAdapter(adapter);
//
//        // CHANGE THE LOADING MORE STATUS
//        loadingMore = false;
//
//        // HIDE THE PROGRESS BAR (SPINNER) AFTER LOADING ALBUMS
//        linlaHeaderProgress.setVisibility(View.GONE);
//    }
//
//}