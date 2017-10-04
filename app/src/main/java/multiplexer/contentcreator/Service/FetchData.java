package multiplexer.contentcreator.Service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import multiplexer.contentcreator.Helper.EndPoints;
import multiplexer.contentcreator.Helper.JsonConstants;
import multiplexer.contentcreator.Helper.WebRequest;

public class FetchData extends Service {
    SharedPreferences.Editor editor;
    SharedPreferences pref;
    public FetchData() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        new GetSuggestions().execute();
        //Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }



    private class GetSuggestions extends AsyncTask<Void, Void, Void> {

        // Hashmap for ListView
        //ArrayList<Headline> jsonHeadlineList;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            WebRequest webreq = new WebRequest();

            // Making a request to url and getting response
            String jsonHeadlineStr = webreq.makeWebServiceCall(new EndPoints().HEADLINE_SUGGESTION_LINK, WebRequest.GETRequest);
            String jsonSubHeadlineStr = webreq.makeWebServiceCall(new EndPoints().SUBHEADLINE_SUGGESTION_LINK, WebRequest.GETRequest);
            String jsonFrontlineStr = webreq.makeWebServiceCall(new EndPoints().FRONT_LINE_SUGGESTION_LINK, WebRequest.GETRequest);
            String jsonCallToActionStr = webreq.makeWebServiceCall(new EndPoints().CALL_TO_ACTION_LINK, WebRequest.GETRequest);
            Log.d("Response: ", "> " + jsonHeadlineStr);
            Log.d("Response: ", "> " + jsonSubHeadlineStr);
            Log.d("Response: ", "> " + jsonFrontlineStr);
            Log.d("Response: ", "> " + jsonCallToActionStr);
            editor.putString(new JsonConstants().JSON_HEADLINE, jsonHeadlineStr);
            editor.putString(new JsonConstants().JSON_SUBHEADLINE, jsonSubHeadlineStr);
            editor.putString(new JsonConstants().JSON_FRONTLINE, jsonFrontlineStr);
            editor.putString(new JsonConstants().JSON_CALL_TO_ACTION, jsonCallToActionStr);
            editor.commit();


            return null;
        }

        @Override
        protected void onPostExecute(Void requestresult) {
            super.onPostExecute(requestresult);
            // Dismiss the progress dialog
            Log.d("Response: ", "> " + "Fetch COmplete");
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }


}
