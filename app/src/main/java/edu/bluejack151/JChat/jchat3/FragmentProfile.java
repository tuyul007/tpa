package edu.bluejack151.JChat.jchat3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shiperus.ark.jchat3.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by komputer on 12/22/2015.
 */
public class FragmentProfile extends android.support.v4.app.Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstance)
    {
        View view=inflater.inflate(R.layout.fragmentprofile,container,false);
        return  view;
    }
}
