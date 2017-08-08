package multiplexer.contentcreator.Helper;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by U on 7/9/2017.
 */

public class EndPoints {
    public  String TEMPLTATES_LINK = "http://ws.sharedtoday.com:4000/ws/wili/gettemplates";

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

}
