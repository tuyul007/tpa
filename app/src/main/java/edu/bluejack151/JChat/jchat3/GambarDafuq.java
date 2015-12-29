package edu.bluejack151.JChat.jchat3;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.shiperus.ark.jchat3.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import edu.bluejack151.JChat.jchat3.Helper.UserAccount;

public class GambarDafuq extends AppCompatActivity {
    Firebase uploadedImageTesRef;
    String image;
    int i=0;
    ArrayList<Bitmap> listImageDBBitmap=new ArrayList<Bitmap>();
    private static int RESULT_LOAD_IMAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_gambar_dafuq);

        uploadedImageTesRef = new Firebase("https://tespict.firebaseio.com/picture");

        uploadedImageTesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                Toast.makeText(GambarDafuq.this, "azzz", Toast.LENGTH_SHORT).show();
                if (i == 0) {
                    i = (int) dataSnapshot.getChildrenCount();
//                    Toast.makeText(GambarDafuq.this, ""+i, Toast.LENGTH_SHORT).show();
                    int a=0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        if(ds!=null || a<2) {
                            byte[] imageAsBytes = Base64.decode(ds.getValue().toString(),Base64.DEFAULT);

                            Bitmap bmp=BitmapFactory.decodeByteArray(imageAsBytes, 0,imageAsBytes.length);

                            listImageDBBitmap.add(bmp);
                            Toast.makeText(GambarDafuq.this, listImageDBBitmap.size()+"", Toast.LENGTH_SHORT).show();
                            ImageView imageView = (ImageView) findViewById(R.id.imgTesUpload);
                            imageView.setImageBitmap(listImageDBBitmap.get(a));
                            a++;

//                            (ds.child(a+"").toString());
                        }
                    }
//                    Toast.makeText(GambarDafuq.this, listImageDB.get(0), Toast.LENGTH_SHORT).show();

                }
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    friendAccountList.put(ds.getKey(), ds.getValue(UserAccount.class));
//                }
//                loadingHandler.setText("event_trigger_u");
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        Button btnChoose=(Button)findViewById(R.id.chooseImageButton);
        Button btnUpload=(Button) findViewById(R.id.uploadImageButton);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                uploadedImageTesRef.setValue(image);
//                uploadedImageTesRef.push();


                uploadedImageTesRef.push().setValue(image);

                Toast.makeText(GambarDafuq.this, "Image" + image.toString() + " Uploaded", Toast.LENGTH_SHORT).show();
                i++;
            }
        });

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);

                    ImageView imageView = (ImageView) findViewById(R.id.imgTesUpload);
                    imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

                    Bitmap bmp = (Bitmap) BitmapFactory.decodeFile(picturePath);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    bmp.recycle();
                    byte[] byteArray = stream.toByteArray();
//                    String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);

                    image= Base64.encodeToString(byteArray,Base64.DEFAULT);



                }
                cursor.close();
            }


        }


    }


}
