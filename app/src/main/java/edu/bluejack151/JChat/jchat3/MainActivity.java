package edu.bluejack151.JChat.jchat3;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.shiperus.ark.jchat3.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import edu.bluejack151.JChat.jchat3.Helper.UserAccount;


public class MainActivity extends Activity {

    SharedPreferences loginPreferences;
    File f;
    SharedPreferences.Editor userSessionEditor;
    UserAccount loginAccount;

    CallbackManager callbackManager;
    Intent pageLoginFacebook;
    AccessToken accessToken;

    //ProfileTracker profileTracker;
    LoginButton fbButton;
    public static String preferencesName="LoginPref";
    SharedPreferences.Editor editor;

    EditText email;
    EditText password;
    Firebase userRef;
    Button manualLoginButton;
    HashMap<String,UserAccount> userAccounts;
    void setSession(UserAccount user){
        loginPreferences = getSharedPreferences("user_session",MODE_PRIVATE);
        userSessionEditor = loginPreferences.edit();
        String user_session = new Gson().toJson(user);
        userSessionEditor.putString("user_session", user_session);
        userSessionEditor.commit();
    }
    void initComponent(){
        email = (EditText)findViewById(R.id.loginEmail);
        password = (EditText)findViewById(R.id.loginPassword);
        manualLoginButton = (Button)findViewById(R.id.manualLoginButton);
        userAccounts = new HashMap<>();

        f = new File("/data/data/"+getPackageName()+"/shared_prefs/user_session.xml");
        if(f.exists()){
            Intent i = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    public void register(View v)
    {
        Intent intent=new Intent(this,RegisterActivity.class);
        startActivity(intent);

    }

    Firebase uploadedImageTesRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //                    Intent i = new Intent(getApplicationContext(),HomeActivity.class);

//        Intent toGambar=new Intent(getApplicationContext(),GambarDafuq.class);

//        startActivity(toGambar);

        editor = getSharedPreferences(preferencesName, MODE_PRIVATE).edit();

//        this.setTitle("EHM"); ->ganti title
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true); ->munculin back button di title


        initComponent();

        userRef = new Firebase("https://jchatapps.firebaseio.com/user");

        //manual-login



        manualLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String message = "";
                        if (email.getText().toString().equals("") || password.getText().toString().equals("")) {
                            message = "All field must be filled";
                        } else {
                            message = "Invalid username/password";
                            for(DataSnapshot ds : dataSnapshot.getChildren()){
                                if(ds.getValue(UserAccount.class).getEmail().equals(email.getText().toString())
                                    && ds.getValue(UserAccount.class).getPassword().equals(password.getText().toString())){
                                    loginAccount =  ds.getValue(UserAccount.class);
                                    message = "Login Success";break;
                                }
                            }
                        }
                        if(message.equals("Login Success")){
                            setSession(loginAccount);
                            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            }
        });

        //facebook - login
        pageLoginFacebook =new Intent(this,HomeActivity.class);
        callbackManager = CallbackManager.Factory.create();

        //Toast.makeText(getApplicationContext(),"Asu DEFFFF",Toast.LENGTH_LONG).show();
        fbButton=(LoginButton)findViewById(R.id.login_button);
        fbButton.setReadPermissions(Arrays.asList("email", "user_photos", "public_profile"));


        fbButton.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                            onFacebookAccessTokenChange(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(getApplicationContext(), "Auth Error", Toast.LENGTH_LONG).show();
                    }
                });



    }

    private void onFacebookAccessTokenChange(AccessToken token) {
        if (token != null) {
            userRef.authWithOAuthToken("facebook", token.getToken(), new Firebase.AuthResultHandler() {
                @Override
                public void onAuthenticated(AuthData authData) {
                    if (authData != null) {
                        final String fbID[] = authData.getUid().split(":");
                        //UserAccount(String userId, String displayName, String email, String password, String gender, String profilePicture,
                        // int notification, int isPublic, int totalFriend, int totalGroup)
                        loginAccount = new UserAccount(
                                fbID[1],
                                authData.getProviderData().get("displayName").toString(),
                                authData.getProviderData().get("email").toString(),
                                authData.getProviderData().get("displayName").toString()
                                , "",
                                "", 1, 1, 0, 0);

                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild(fbID[1])){
                                    loginAccount = dataSnapshot.child(fbID[1]).getValue(UserAccount.class);
                                }
                                setSession(loginAccount);
                                startActivity(pageLoginFacebook);
                                LoginManager.getInstance().logOut();
                                finish();
                            }

                            @Override
                            public void onCancelled(FirebaseError firebaseError) {

                            }
                        });

                    }
                }
                @Override
                public void onAuthenticationError(FirebaseError firebaseError) {
                    // there was an error
                    Toast.makeText(getApplicationContext(), "there was an error "+firebaseError.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            userRef.unauth();
        }
    }

    void onFacebookAuth(AccessToken token){

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
