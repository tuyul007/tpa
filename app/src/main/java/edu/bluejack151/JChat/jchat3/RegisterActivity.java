package edu.bluejack151.JChat.jchat3;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.client.snapshot.Index;
import com.firebase.client.snapshot.IndexedNode;
import com.firebase.client.snapshot.Node;
import com.shiperus.ark.jchat3.R;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import edu.bluejack151.JChat.jchat3.Helper.UserAccount;
import edu.bluejack151.JChat.jchat3.Helper.Validator;

public class RegisterActivity extends AppCompatActivity {
    //register components
    EditText userId;
    EditText displayName;
    EditText email;
    EditText password;
    RadioButton radioMale;
    RadioButton radioFemale;
    Button btnSubmit;
    Button btnReset;
    String message;

    private void initComponent(){
        userId = (EditText)findViewById(R.id.fieldRegisUserID);
        displayName = (EditText)findViewById(R.id.fieldRegisDisplayName);
        email = (EditText)findViewById(R.id.fieldRegisEmail);
        password = (EditText)findViewById(R.id.fieldRegisPassword);
        radioMale = (RadioButton)findViewById(R.id.regisRadioMale);
        radioFemale = (RadioButton)findViewById(R.id.regisRadioFemale);

        btnSubmit = (Button)findViewById(R.id.submitRegisButton);
        btnReset = (Button)findViewById(R.id.resetRegisButton);

        message = "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_register);
        initComponent();

        final Firebase userRef = new Firebase("https://jchatapps.firebaseio.com/user");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        message = "";
                        if (!Validator.checkUserID(userId.getText().toString())) {
                            message = "User ID must be alphabet or numeric, 6-8 character";
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        } else if (!Validator.checkLength(displayName.getText().toString(), 3, 20) ||
                                !Validator.isAlpha(displayName.getText().toString())) {
                            message = "Display Name must be alphabet, 3-20 character";
                        } else if (!Validator.validateEmail(email.getText())) {
                            message = "Email format is wrong";
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        } else if (!Validator.checkLength(password.getText().toString(), 4, 20)
                                || !Validator.isAlphaNumeric(password.getText().toString())) {
                            message = "Password must be alphanumeric, 4-20 character";
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        } else {
                                    message = "Register Success";
                                    if (dataSnapshot.hasChild(userId.getText().toString())) {
                                        message = "User ID has already used";
                                    }

                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    if (message.equals("Register Success")) {
                                        //insertDatabase
                                        String gender = "Male";
                                        if (radioFemale.isChecked()) gender = "Female";

                                        userRef.child(userId.getText().toString())
                                                .setValue(new UserAccount(
                                                        userId.getText().toString(),
                                                        displayName.getText().toString(),
                                                        email.getText().toString(),
                                                        password.getText().toString(),
                                                        gender,
                                                        "",
                                                        1,
                                                        1,
                                                        0,
                                                        0
                                                ));
                                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
                                        startActivity(i);
                                        finish();
                                    }

                        }
                    }
                });
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userId.setText("");
                displayName.setText("");
                email.setText("");
                password.setText("");
                radioMale.setChecked(true);

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(i);
        finish();
    }
}
