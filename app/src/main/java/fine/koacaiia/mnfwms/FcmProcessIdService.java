package fine.koacaiia.mnfwms;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FcmProcessIdService extends FirebaseInstanceIdService {
    String token;
    @Override
    public void onTokenRefresh() {

        Log.i("koacaiiiib",token);
    }
    public String tokenValue(){
        token= FirebaseInstanceId.getInstance().getToken();
        return token;
    }
}
