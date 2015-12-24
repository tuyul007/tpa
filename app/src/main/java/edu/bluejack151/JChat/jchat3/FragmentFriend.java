package edu.bluejack151.JChat.jchat3;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shiperus.ark.jchat3.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by komputer on 12/22/2015.
 */
public class FragmentFriend  extends android.support.v4.app.Fragment{



    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    @Override
       public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstance)
    {
        View view=inflater.inflate(R.layout.fragmentfriend,container,false);
        ExpandableListView elv = (ExpandableListView) view.findViewById(R.id.expandableListView);
        elv.setAdapter(new SavedTabsListAdapter(getActivity()));

        return  view;
    }


    //class buat setting expandedviewlist nya
    public class SavedTabsListAdapter extends BaseExpandableListAdapter {

        private  Context context;
        public SavedTabsListAdapter(Activity context)
        {
            this.context=context;
        }

        //ini buat tab header nya
        private String[] header = { "Friends", "Groups"};


        //ini buat isi header nya
        private String[][] child = {
                { "Arnold", "Barry" }, // -> ini pas dijalanin bakal jadi anaknya header friends
                { "Group 1", "Group 2"} // -> ini pas dijalanin bakal jadi anaknya header groups
        };

        @Override
        public int getGroupCount() {
            return header.length;
        }

        @Override
        public int getChildrenCount(int i) {
            return child[i].length;
        }

        @Override
        public Object getGroup(int i) {
            return header[i];
        }

        @Override
        public Object getChild(int i, int i1) {
            return child[i][i1];
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i1;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }


        //buat ngeset header expandable nya nya
        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {

            String headerTitle = (String) getGroup(i);
            if (view == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = infalInflater.inflate(R.layout.list_friend_group, null);
            }

            TextView lblListHeader = (TextView) view.findViewById(R.id.headerFriend);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            return view;
        }


        //buat set child expandable nya
        @Override
        public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
            String headerTitle = (String) getChild(i, i1);
            if (view == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = infalInflater.inflate(R.layout.list_friend_item, null);
            }

            TextView lblListHeader = (TextView) view.findViewById(R.id.itemFriend);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);
            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }

    }
}
