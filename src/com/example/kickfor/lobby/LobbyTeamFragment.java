package com.example.kickfor.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.kickfor.ClientWrite;
import com.example.kickfor.HandlerListener;
import com.example.kickfor.HomePageActivity;
import com.example.kickfor.R;
import com.example.kickfor.RealTimeHandler;
import com.example.kickfor.Tools;
import com.example.kickfor.pullableview.PullToRefreshLayout;
import com.example.kickfor.pullableview.PullableListView;
import com.example.kickfor.utils.IdentificationInterface;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LobbyTeamFragment extends Fragment implements IdentificationInterface, LobbyInterface, HandlerListener{

	private int num = 5;
	private int start = 0;

	private PullableListView mListView = null;
	private PullToRefreshLayout pullToRefreshLayout = null;
	private List<LobbyTeamEntity> mList = null;
	private LobbyTeamAdapter adapter = null;
	private Context context = null;
	

	@Override
	public int getFragmentLevel() {
		return IdentificationInterface.MAIN_LEVEL;
	}

	@Override
	public void setEnable(boolean enable) {

	}

	private void init() {
		this.context = getActivity();
		mList = new ArrayList<LobbyTeamEntity>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		RealTimeHandler.getInstance().regist(this);
		init();
		View view = inflater.inflate(R.layout.fragment_lobby_team, container,
				false);
		pullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.lobby_refresh_view);
		mListView = (PullableListView) view.findViewById(R.id.lobby_content_view);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("request", "get themelist");
		map.put("pstart", start);
		map.put("pnum", num);
		Runnable r = new ClientWrite(Tools.JsonEncode(map));
		new Thread(r).start();
		adapter = new LobbyTeamAdapter(context, mList);
		mListView.setAdapter(adapter);
		
		pullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {

			@Override
			public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("request", "get themelist");
				map.put("pstart", 0);
				start=0;
				map.put("pnum", num);
				Runnable r = new ClientWrite(Tools.JsonEncode(map));
				new Thread(r).start();
			}

			@Override
			public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("request", "get themelist");
				map.put("pstart", String.valueOf(start));
				map.put("pnum", String.valueOf(num));
				Runnable r=new ClientWrite(Tools.JsonEncode(map));
				new Thread(r).start();
				new Handler() {
					@Override
					public void handleMessage(Message msg) {
						pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
					}
				}.sendEmptyMessageDelayed(0, 1000);
			}
			
		}, 4);
		
		return view;
	}

	
	
	@Override
	public void onDestroy() {
		RealTimeHandler.getInstance().unRegist(this);
		super.onDestroy();
	}

	public int getIndex() {
		return start;
	}

	public void setData(List<LobbyTeamEntity> mList) {
		if(start==0){
			this.mList.clear();
		}
		else{
			LobbyTeamEntity item=this.mList.get(this.mList.size()-1);
			if(item.getThemeKey().equals("-1")){
				this.mList.remove(item);
			}
		}
		Iterator<LobbyTeamEntity> iter = mList.iterator();
		while (iter.hasNext()) {
			this.mList.add(iter.next());
		}
		int end=start+num;
		start=start + mList.size();
		
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
		new Handler() {
			@Override
			public void handleMessage(Message msg) {
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}
		}.sendEmptyMessageDelayed(0, 1000);
	}

	@Override
	public void onChange(Message msg) {
		if(msg.what==HomePageActivity.LOBBY_TEAM){
			((HomePageActivity)getActivity()).removeVague();
			int index=msg.arg1;
			@SuppressWarnings("unchecked")
			List<LobbyTeamEntity> list=(List<LobbyTeamEntity>)msg.obj;
			if(index==0){
				start=0;
			}
			if(index==getIndex()){
				setData(list);
			}
		}
	}
	
	

}
