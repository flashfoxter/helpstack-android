package com.tenmiles.helpstack.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.tenmiles.helpstack.R;
import com.tenmiles.helpstack.activities.HSActivityManager;
import com.tenmiles.helpstack.helper.HSBaseExpandableListAdapter;
import com.tenmiles.helpstack.logic.HSEmailGear;
import com.tenmiles.helpstack.model.HSKBItem;

/**
 * Initial Fragment that contains FAQ and Tickets
 * 
 * @author Nalin Chhajer
 *
 */
public class HomeFragment extends HSFragmentParent {

	private ExpandableListView mExpandableListView;
	private LocalAdapter mAdapter;
	
	private HSEmailGear emailGear;
	boolean isNewUser = true;
	
	public HomeFragment() {
		
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
             Bundle savedInstanceState) {
		 
         View rootView = inflater.inflate(R.layout.fragment_home, container, false);
         
         mExpandableListView = (ExpandableListView) rootView.findViewById(R.id.expandableList); 
         mAdapter = new LocalAdapter(getActivity());
         
         mExpandableListView.setAdapter(mAdapter);
         
         
         View report_an_issue_view = inflater.inflate(R.layout.expandable_footer_report_issue, null);
         report_an_issue_view.findViewById(R.id.button1).setOnClickListener(reportIssueClickListener);
         mExpandableListView.addFooterView(report_an_issue_view);
         
         emailGear = new HSEmailGear();
         
         Button reportIssueButton = (Button)rootView.findViewById(R.id.reportIssueButton);
         reportIssueButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(isNewUser) {
					HSActivityManager.startNewUserActivity(getActivity());
				}else {
					HSActivityManager.startNewIssueActivity(getActivity());
				}
			}
		});
         
         initializeView();
         
         return rootView;
    }
	 
	private void initializeView() {
		
		mAdapter.clearAll();
		
		mAdapter.addParent(0, "FAQ");
		
		{
			int count = emailGear.getKBArticleCount();
			for (int i = 0; i < count; i++) {
				HSKBItem item = emailGear.getKBItemAtPosition(i);
				mAdapter.addChild(0, item);
			}
		}
		
		
		mAdapter.addParent(1, "ISSUES");
		
		mAdapter.notifyDataSetChanged();
		
		expandAll();
	}
	
	private void expandAll() {
		int count = mAdapter.getGroupCount();
		for (int i = 0; i < count; i++) {
			mExpandableListView.expandGroup(i);
		}
		
	}
	
	private OnClickListener reportIssueClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
		}
	};

	private class LocalAdapter extends HSBaseExpandableListAdapter
	 {

		public LocalAdapter(Context context) {
			super(context);
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			
			ChildViewHolder holder;
			
			if (convertView == null) {
				convertView = mLayoutInflater.inflate(R.layout.expandable_child_home_default, null);
				holder = new ChildViewHolder();
				
				holder.textView1 = (TextView) convertView.findViewById(R.id.textView1);
				
				
				convertView.setTag(holder);
			}
			else {
				holder = (ChildViewHolder) convertView.getTag();
			}
			
			if (groupPosition == 0) {
				HSKBItem item = (HSKBItem) getChild(groupPosition, childPosition);
				holder.textView1.setText(item.getSubject());
			}
			
			return convertView;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			ParentViewHolder holder;
			
			if (convertView == null) {
				convertView = mLayoutInflater.inflate(R.layout.expandable_parent_home_default, null);
				holder = new ParentViewHolder();
				
				holder.textView1 = (TextView) convertView.findViewById(R.id.textView1);
				
				convertView.setTag(holder);
			}
			else {
				holder = (ParentViewHolder) convertView.getTag();
			}
			
			String text = (String) getGroup(groupPosition);
			
			holder.textView1.setText(text);
			
			return convertView;
		}
		
		private class ParentViewHolder {
			TextView textView1;
		}
		
		private class ChildViewHolder {
			TextView textView1;
		}
	 }
}
