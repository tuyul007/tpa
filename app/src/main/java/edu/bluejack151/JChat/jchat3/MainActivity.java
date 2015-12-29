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
        }else {
            loginPreferences = getSharedPreferences("user_session",MODE_PRIVATE);
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

    boolean loginUserAccount(String email,String password){
        for (Map.Entry<String,UserAccount> data : userAccounts.entrySet()){
            loginAccount = data.getValue();
            if(loginAccount.getEmail().equals(email)
                    && loginAccount.getPassword().equals(password)){
                return true;
            }
        }
        return false;
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

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        userRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                userAccounts.put(dataSnapshot.getKey(), dataSnapshot.getValue(UserAccount.class));
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        //manual-login



        manualLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "";
                if(email.getText().toString().equals("") || password.getText().toString().equals("")){
                    message = "All field must be filled";
                }else if(!loginUserAccount(email.getText().toString(),password.getText().toString())){
                    message = "Invalid username/password";
                }else{
                    message = "Login Success";
                }
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                if(message.equals("Login Success")){
                    userSessionEditor = loginPreferences.edit();
                    String user_session = new Gson().toJson(loginAccount);
                    userSessionEditor.putString("user_session", user_session);
                    userSessionEditor.commit();

                    Intent i = new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(i);
                    finish();
                }
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
                    String idUs;

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                                //Log.i("LoginActivity", graphResponse.toString());
                                try {
                                    //buat tampilin data facebook sesuai kolom nya
                                    //Toast.makeText(getApplicationContext(), jsonObject.getString("id"), Toast.LENGTH_LONG).show();
                                    editor.putString("userID", jsonObject.getString("id").toString());
                                    editor.commit();
                                    // Toast.makeText(getApplicationContext(), idUs, Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                startActivity(pageLoginFacebook);

                            }
                        });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                        request.setParameters(parameters);
                        request.executeAsync();

                        LoginManager.getInstance().logOut();

                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(getApplicationContext(), "Asu Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(getApplicationContext(), "Asu Error", Toast.LENGTH_LONG).show();
                    }
                });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
