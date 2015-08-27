package com.example.kickfor.lobby;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.example.kickfor.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

public class TeamPopupWindow extends PopupWindow {

	private ListView mListView = null;
	private View view = null;
	private String selected = null;
	private List<String> mList = null;

	public TeamPopupWindow(Context context, List<String> mList1, int width, int height) {
		
		super(context);
		this.mList=new ArrayList<String>();
		Iterator<String> iter = mList1.iterator();
		while (iter.hasNext()) {
			this.mList.add(iter.next());
		}
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(R.layout.fragment_normal_list, null);
		mListView = (ListView) view.findViewById(R.id.normal_list);
		setContentView(view);
		setWidth(width);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);

		ListAdapter catalogsAdapter = new ArrayAdapter<String>(context,
				R.layout.match_type_item, mList);
		mListView.setAdapter(catalogsAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				selected = mList.get(arg2);
				dismiss();
			}

		});

		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int height = view.findViewById(R.id.btns).getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}

		});
	}

	public String getSelect() {
		return selected;
	}

}
