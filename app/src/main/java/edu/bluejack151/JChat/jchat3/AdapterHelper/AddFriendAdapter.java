package edu.bluejack151.JChat.jchat3.AdapterHelper;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shiperus.ark.jchat3.R;

import java.util.ArrayList;

/**
 * Created by shiperus on 12/27/2015.
 */
public class AddFriendAdapter  extends ArrayAdapter<String> {

    private Activity context;
    private ArrayList<String> searchFriendName=new ArrayList<String>();
    private  ArrayList<Integer> imgSearchFriend=new ArrayList<>();
    public AddFriendAdapter(Activity context,ArrayList<String> searchFriendName,ArrayList<Integer> imgSearchFriend) {
        super(context, R.layout.list_search_add_friend, searchFriendName);

        this.context=context;

        this.searchFriendName=searchFriendName;
        this.imgSearchFriend=imgSearchFriend;
    }
    @Override
    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.list_search_add_friend, null, true);

        TextView txtNameSearch = (TextView) rowView.findViewById(R.id.friendNameSearch);

        ImageView profileSearch = (ImageView) rowView.findViewById(R.id.profileSearchPicture);


        txtNameSearch.setText(searchFriendName.get(position));

        profileSearch.setImageResource(imgSearchFriend.get(position));



        return rowView;

    };
}
