package edu.bluejack151.JChat.jchat3;

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
 * Created by shiperus on 1/1/2016.
 */
public class EditableProfileAdapter extends ArrayAdapter<String> {

    private Activity context;
    private ArrayList<String> editableProfileHeader=new ArrayList<String>();
    private  ArrayList<String> editableProfileContent=new ArrayList<String>();
    public EditableProfileAdapter(Activity context,ArrayList<String> editableProfileHeader,ArrayList<String> editableProfileContent) {
        super(context, R.layout.fragmentprofile, editableProfileContent);

        this.context=context;

        this.editableProfileContent=editableProfileContent;
        this.editableProfileHeader=editableProfileHeader;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.editable_profile_layout, null, true);


        TextView txtHeaderEditableProfile = (TextView) rowView.findViewById(R.id.editableProfileHeader);

        TextView txtContentEditableProfile = (TextView) rowView.findViewById(R.id.editableProfileContent);


        txtHeaderEditableProfile.setText(editableProfileHeader.get(position));

        txtContentEditableProfile.setText(editableProfileContent.get(position));



        return rowView;

    };
}
