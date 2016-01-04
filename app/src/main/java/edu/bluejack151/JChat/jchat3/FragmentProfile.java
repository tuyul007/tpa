package edu.bluejack151.JChat.jchat3;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.shiperus.ark.jchat3.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by komputer on 12/22/2015.
 */
public class FragmentProfile extends android.support.v4.app.Fragment{
    private static int RESULT_LOAD_IMAGE = 1;
    View view;
    private ArrayList<String> editableProfileHeader=new ArrayList<String>();
    private  ArrayList<String> editableProfileContent=new ArrayList<String>();

    RoundImage roundImage;
    Dialog dialogDisplayNameChange,dialogGenderChange;
    EditableProfileAdapter edP;
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstance)
    {
         view=inflater.inflate(R.layout.fragmentprofile,container,false);
        Button btnChangePictureProfile=(Button)view.findViewById(R.id.btnProfileChangePic);
        ImageView imgProfPicture=(ImageView) view.findViewById(R.id.profileFragmentPicture);
        ListView listChangeableProfile=(ListView) view.findViewById(R.id.listEditableProfile);
        editableProfileHeader.add("Display Name");
        editableProfileHeader.add("Gender");
        initModalDialog();

        editableProfileContent.add(HomeActivity.userSessionAccount.getDisplayName());
        editableProfileContent.add(HomeActivity.userSessionAccount.getGender());



        byte[] imageAsBytes = Base64.decode(
                HomeActivity.userSessionAccount.getProfilePicture()
                , Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);

        Bitmap bmp2 = BitmapFactory.decodeResource(getResources(), R.drawable.com_facebook_profile_picture_blank_portrait);
        Bitmap bmp3=Bitmap.createScaledBitmap(bmp2, 150, 150, false);


        if(bmp!=null) {
            roundImage=new RoundImage(bmp,150,150);
            imgProfPicture.setImageDrawable(roundImage);
        }
        else
        {
            roundImage=new RoundImage(bmp3,150,150);
            imgProfPicture.setImageDrawable(roundImage);
        }




         edP=new EditableProfileAdapter(getActivity(), editableProfileHeader, editableProfileContent);
        listChangeableProfile.setAdapter(edP);

//            HomeActivity.userSessionAccount.getUserId();

        TextView txtUserId=(TextView) view.findViewById(R.id.userIdProfileTxt);
        TextView txtProfileEmail=(TextView) view.findViewById(R.id.emailProfileTxt);

        txtUserId.setText("User ID: "+HomeActivity.userSessionAccount.getUserId());
        txtProfileEmail.setText("Email: "+HomeActivity.userSessionAccount.getEmail());
        btnChangePictureProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });


        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                // AdapterView is the parent class of ListView
                ListView lv1 = (ListView) arg0;
                switch (position)
                {
                    case 0:
                        changeDisplayName();
                        break;
                    case 1:
                        changeGender();
                        break;

                }


            }
        };

        // Setting the ItemClickEvent listener for the listview
        listChangeableProfile.setOnItemClickListener(itemClickListener);

        //setText buat 3 text dibawah gambar

        //setAdapter buat gender sama display name

        return  view ;



    }
    void initModalDialog(){
        dialogDisplayNameChange = new Dialog(getContext());
        dialogDisplayNameChange.setContentView(R.layout.profile_edit_displayname);

        dialogGenderChange=new Dialog(getContext());
        dialogGenderChange.setContentView(R.layout.profile_edit_gender);

    }

    public  void changeDisplayName()
    {
        dialogDisplayNameChange.setTitle("Change Display Name");
        dialogDisplayNameChange.show();

        Button btnSubmitDisplayName=(Button) dialogDisplayNameChange.findViewById(R.id.submitEditProfile);

        btnSubmitDisplayName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextDisplayName=(EditText) dialogDisplayNameChange.findViewById(R.id.editTextChangeDisplayNameProfile);


                if(!editTextDisplayName.equals("")) {
                    HomeActivity.userSessionAccount.setDisplayName(editTextDisplayName.getText().toString());
                    HomeActivity.userRef.child(HomeActivity.userSessionAccount.getUserId()).setValue(HomeActivity.userSessionAccount);
                    editableProfileContent.set(0, HomeActivity.userSessionAccount.getDisplayName());

                    HomeActivity.refresehNavigationDrawer();


                    edP.notifyDataSetChanged();
                    dialogDisplayNameChange.dismiss();
                }
                else
                    Toast.makeText(getActivity(), "Display Name Must Be Filled", Toast.LENGTH_SHORT).show();
            }
        });

    }
    RadioButton rbMale,rbFemale;
    public  void changeGender()
    {
        rbMale =(RadioButton) dialogGenderChange.findViewById(R.id.radioUpdateProfileMalerd);
         rbFemale=(RadioButton) dialogGenderChange.findViewById(R.id.radioUpdateProfileFemalerd);
        rbMale.setChecked(true);
        dialogGenderChange.setTitle("Change Gender");
        dialogGenderChange.show();
        Button btnSubmitGender=(Button) dialogGenderChange.findViewById(R.id.submitEditProfileGender);

        btnSubmitGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String gender;
                if(rbMale.isChecked())
                    gender="Male";
                else
                    gender="Female";


                HomeActivity.userSessionAccount.setGender(gender);
                HomeActivity.userRef.child(HomeActivity.userSessionAccount.getUserId()).setValue(HomeActivity.userSessionAccount);
                editableProfileContent.set(1, HomeActivity.userSessionAccount.getGender());
                edP.notifyDataSetChanged();
                HomeActivity.refresehNavigationDrawer();
                dialogGenderChange.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == getActivity().RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    ImageView imageView = (ImageView) view.findViewById(R.id.profileFragmentPicture);
                    Bitmap bmpChoosenImg = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(picturePath), 150, 150, false);
                    Bitmap bmpForUpload = Bitmap.createScaledBitmap(BitmapFactory.decodeFile(picturePath), 150, 150, false);
                    roundImage=new RoundImage(bmpChoosenImg,150,150);

                    ImageView imgNav=(ImageView) getActivity().findViewById(R.id.profImageSlide);


                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmpForUpload.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    bmpForUpload.recycle();
                    byte[] byteArray = stream.toByteArray();
                    String imageStore;
                    imageStore= Base64.encodeToString(byteArray, Base64.DEFAULT);
                    HomeActivity.userSessionAccount.setProfilePicture(imageStore);
                    HomeActivity.userRef.child(HomeActivity.userSessionAccount.getUserId()).setValue(HomeActivity.userSessionAccount);
                    HomeActivity.refresehNavigationDrawer();


//                    NotificationManager notif=(NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//                    Notification notify=new Notification(R.drawable.button_boarder_new,"EHMMM",System.currentTimeMillis());
//                    PendingIntent pending= PendingIntent.getActivity(getActivity().getApplicationContext(), 0, new Intent(), 0);
//
//
//                    notif.notify(0,notify);
////                    notify.setLatestEventInfo(getApplicationContext(), subject, body, pending);
//                    notif.notify(0, notify);


                    Toast.makeText(getActivity(), "Image Changed", Toast.LENGTH_SHORT).show();

                        imageView.setImageDrawable(roundImage);
                }
                cursor.close();
            }


        }


    }


}
