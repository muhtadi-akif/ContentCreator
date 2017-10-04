package multiplexer.contentcreator.Helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by U on 7/9/2017.
 */

public class EndPoints {
    public  String TEMPLTATES_LINK = "http://ws.sharedtoday.com:4000/ws/wili/gettemplates";
    public  String HEADLINE_SUGGESTION_LINK = "http://ws.sharedtoday.com:4000/ws/wili/getheadline";
    public  String SUBHEADLINE_SUGGESTION_LINK = "http://ws.sharedtoday.com:4000/ws/wili/getsubheadline";
    public  String FRONT_LINE_SUGGESTION_LINK = "http://ws.sharedtoday.com:4000/ws/wili/getfrontline";
    public  String CALL_TO_ACTION_LINK = "http://ws.sharedtoday.com:4000/ws/wili/getcalltoaction";

    public ProgressDialog showProgressDialog(Context context){
        ProgressDialog pd = new ProgressDialog(context);
        pd.setCancelable(false);
        pd.setMessage("Please wait...");
        pd.show();

        return pd;
    }
    public void cancelProgressDialog(ProgressDialog pd){
        if (pd.isShowing()){
            pd.dismiss();
        }

    }

    public void noInernetToast(Context context) {
        Toast.makeText(context, "Connection problem please try again later", Toast.LENGTH_LONG).show();
    }
}
