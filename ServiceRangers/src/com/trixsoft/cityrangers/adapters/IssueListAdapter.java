package com.trixsoft.cityrangers.adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.trixsoft.cityrangers.activity.IssueListCustom;
import com.trixsoft.cityrangers.activity.R;
import com.trixsoft.cityrangers.model.IssuesModel;

/********* Adapter class extends with BaseAdapter and implements with OnClickListener ************/
public class IssueListAdapter extends BaseAdapter implements OnClickListener {

	private Activity activity;
	private ArrayList<IssuesModel> customListValuesArray;
	private static LayoutInflater inflater = null;
	public Resources res;
	IssuesModel tempValues = null;

	public IssueListAdapter(Activity a, ArrayList<IssuesModel> customListValuesArray,Resources res) {
		this.activity = a;
		this.customListValuesArray = customListValuesArray;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.res=res;
		Log.d("IssueListAdapter", "Entered Constructor");
	}

	@Override
	public int getCount() {
		if (customListValuesArray.size() <= 0)
			return -1;
		return customListValuesArray.size();
	}

	@Override
	public Object getItem(int position) {
		return customListValuesArray.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {

		public TextView issueId;
		public TextView issueDesc;
		public TextView wardNumber;
		public TextView textWide;
		//public ImageView image;

	}

	 /****** Depends upon data size called for each row , Create each ListView row *****/	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		try {
			View vi = convertView;
			ViewHolder holder;
			Log.d("IssueListAdapter-getView","Position value is:"+position);
			if (convertView == null) {
				Log.d("IssueListAdapter", "converView is null");
				
				//Inflate tabitem.xml file for each row  
				vi = inflater.inflate(R.layout.custom_issue_display_list, null);
				
				//ViewHolder Object to hold model elements
				holder = new ViewHolder();
				holder.issueId=(TextView) vi.findViewById(R.id.issueId);
				holder.issueDesc = (TextView) vi.findViewById(R.id.issueDesc);
				holder.wardNumber = (TextView) vi.findViewById(R.id.wardNumber);
				vi.setTag(holder);
			} else
			{
				holder = (ViewHolder) vi.getTag();
				Log.d("IssueListAdapter", "converView is not null");
			}

			if (customListValuesArray.size() <= 0) {
				holder.issueDesc.setText("No Issues Assigned/Open");
			} else {
				tempValues = null;
				
				tempValues = (IssuesModel) customListValuesArray.get(position);
				Log.d("IssueListAdapter", "t1");
				holder.issueId.setText(tempValues.getIS_ID());
				Log.d("IssueListAdapter", "t2");
				holder.issueDesc.setText(tempValues.getIS_TITLE());
				Log.d("IssueListAdapter", "t3");
				holder.wardNumber.setText(tempValues.getIS_LOC());
				Log.d("IssueListAdapter", "t4");

				vi.setOnClickListener(new OnIssueClickListener(position));
			}
			Log.d("IssueListAdapter", "Exited Get view method");
			return vi;
		} catch (Exception e) {
			// TODO Auto-generated catch block			
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void onClick(View v) {
		Log.v("CustomAdapter", "=====Row button clicked=====");
	}

	private class OnIssueClickListener implements OnClickListener {
		private int mPosition;

		OnIssueClickListener(int position) {
			mPosition = position;
		}

		@Override
		public void onClick(View arg0) {

			IssueListCustom sct = (IssueListCustom) activity;

			/****
			 * Call onItemClick Method inside CustomListViewAndroidExample Class
			 * ( See Below )
			 ****/

			sct.onItemClick(mPosition);
		}
	}
}
