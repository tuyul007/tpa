package edu.bluejack151.JChat.jchat3.AdapterHelper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.shiperus.ark.jchat3.R;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

import edu.bluejack151.JChat.jchat3.AddFriend;
import edu.bluejack151.JChat.jchat3.FragmentFriend;
import edu.bluejack151.JChat.jchat3.Helper.Friend;
import edu.bluejack151.JChat.jchat3.Helper.UserAccount;
import edu.bluejack151.JChat.jchat3.HomeActivity;

/**
 * Created by shiperus on 12/27/2015.
 */
public class AddFriendAdapter  extends ArrayAdapter{

    private ArrayList<UserAccount> userAccounts;
    private Context context;
    public AddFriendAdapter(Activity context,ArrayList<UserAccount> userAccount) {
        super(context,R.layout.list_search_add_friend,userAccount);

        this.context=context;
        this.userAccounts = userAccount;
    }

    UserAccount getData(int position){
        return userAccounts.get(position);
    }

    @Override
    public View getView(final int position,View view,ViewGroup parent) {
        final View newView =  LayoutInflater.from(getContext()).inflate(R.layout.list_search_add_friend, null);

        TextView txtNameSearch = (TextView) newView.findViewById(R.id.friendNameSearch);

        ImageView profileSearch = (ImageView) newView.findViewById(R.id.profileSearchPicture);
        txtNameSearch.setText(getData(position).getDisplayName().toString());

        if(!getData(position).getProfilePicture().equals("")){
            byte[] imageAsBytes = Base64.decode(getData(position).getProfilePicture(), Base64.DEFAULT);
            //Toast.makeText(AddFriendAdapter.this, getData(position).getProfilePicture(), Toast.LENGTH_SHORT).show();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            profileSearch.setImageBitmap(bitmap);
        }

        ImageButton btn = (ImageButton)newView.findViewById(R.id.btnAdd);
        btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HomeActivity.friendRef.child(HomeActivity.userSessionAccount.getUserId() + "_" + userAccounts.get(position).getUserId()).setValue(
                                new Friend(userAccounts.get(position).getUserId(),HomeActivity.userSessionAccount.getUserId(),0)
                        );
                        HomeActivity.tempFriendList.get(1).getFriendList().add(new FriendListItem());
                        HomeActivity.tempFriendList.get(1).getFriendList().get(
                                HomeActivity.tempFriendList.get(1).getFriendList().size() - 1
                        ).setFriendIdentity(new Friend(userAccounts.get(position).getUserId(), HomeActivity.userSessionAccount.getUserId(), 0));
                        HomeActivity.tempFriendList.get(1).getFriendList().get(
                                HomeActivity.tempFriendList.get(1).getFriendList().size() - 1
                        ).setFriendDetail(userAccounts.get(position));

                        HomeActivity.userSessionAccount.setTotalFriend((HomeActivity.userSessionAccount.getTotalFriend() + 1));
                        HomeActivity.userRef.child(HomeActivity.userSessionAccount.getUserId()).setValue(HomeActivity.userSessionAccount);

                        FragmentFriend.adapter.setFriendAndGroupList(HomeActivity.tempFriendList);
                        FragmentFriend.adapter.notifyDataSetChanged();

                        HomeActivity.friendCount++;
                        ((Activity)context).finish();
                    }
                }
        );

        return newView;

    };
}
