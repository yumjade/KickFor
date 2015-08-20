package com.example.kickfor.lobby;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes.Name;

import com.example.kickfor.ClientWrite;
import com.example.kickfor.HomePageActivity;
import com.example.kickfor.R;
import com.example.kickfor.Tools;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.jpush.android.data.l;

public class LobbyTeamAdapter extends BaseAdapter{

	private Context context=null;
	private List<LobbyTeamEntity> mList=null;
	private LayoutInflater inflater=null;
	
	public LobbyTeamAdapter(Context context, List<LobbyTeamEntity> mList){
		this.mList=mList;
		this.context=context;
		this.inflater=LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		WaitHolder waitHolder=null;
		LobbyTeamEntity item=mList.get(position);
		if(convertView==null){
			if(item.getThemeKey().equals("-1")){
				convertView=inflater.inflate(R.layout.refresh_item, null);
				waitHolder=new WaitHolder();
				waitHolder.loadClick=(ImageView)convertView.findViewById(R.id.refresh_click);
				waitHolder.loadText=(TextView)convertView.findViewById(R.id.refresh_text);
				waitHolder.loadWiat=(ProgressBar)convertView.findViewById(R.id.refresh_wait);
				waitHolder.l=(RelativeLayout)convertView.findViewById(R.id.refresh_layout);
				waitHolder.l.setOnClickListener(new MyOnClickListener(waitHolder));
				convertView.setTag(waitHolder);
			}
			else{
				convertView=inflater.inflate(R.layout.lobby_team_item, null);
				viewHolder=new ViewHolder();
				viewHolder.image=(ImageView)convertView.findViewById(R.id.lobby_team_photo);
				viewHolder.name=(TextView)convertView.findViewById(R.id.lobby_team_name);
				viewHolder.city=(TextView)convertView.findViewById(R.id.lobby_team_city);
				viewHolder.date=(TextView)convertView.findViewById(R.id.lobby_team_date);
				viewHolder.type=(TextView)convertView.findViewById(R.id.lobby_team_type);
				viewHolder.text=(TextView)convertView.findViewById(R.id.lobby_team_text);
				viewHolder.cancel=(TextView)convertView.findViewById(R.id.lobby_team_cancel);
				viewHolder.reply=(TextView)convertView.findViewById(R.id.lobby_team_reply);
				viewHolder.l=(LinearLayout)convertView.findViewById(R.id.lobby_team_layout);
				
				viewHolder.cancel.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						
					}
					
				});
				
				viewHolder.reply.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						final View vi=v;
						LobbyTeamEntity item=(LobbyTeamEntity)vi.getTag();
						HomePageActivity h=(HomePageActivity)context;
						Bundle bundle=new Bundle();
						bundle.putString("type", item.getType());
						bundle.putString("themeKey", item.getThemeKey());
						h.openLobbyTeamReply(bundle);
					}
					
				});
				
				convertView.setTag(viewHolder);
			}
		}
		else{
			if(item.getThemeKey().equals("-1")){
				if(convertView.getTag() instanceof WaitHolder){
					waitHolder=(WaitHolder)convertView.getTag();
				}
				else{
					convertView=inflater.inflate(R.layout.refresh_item, null);
					waitHolder=new WaitHolder();
					waitHolder.loadClick=(ImageView)convertView.findViewById(R.id.refresh_click);
					waitHolder.loadText=(TextView)convertView.findViewById(R.id.refresh_text);
					waitHolder.loadWiat=(ProgressBar)convertView.findViewById(R.id.refresh_wait);
					waitHolder.l=(RelativeLayout)convertView.findViewById(R.id.refresh_layout);
					waitHolder.l.setOnClickListener(new MyOnClickListener(waitHolder));
					convertView.setTag(waitHolder);
				}
			}
			else{
				if(convertView.getTag() instanceof ViewHolder){
					viewHolder=(ViewHolder)convertView.getTag();
				}
				else{
					convertView=inflater.inflate(R.layout.lobby_team_item, null);
					viewHolder=new ViewHolder();
					viewHolder.image=(ImageView)convertView.findViewById(R.id.lobby_team_photo);
					viewHolder.name=(TextView)convertView.findViewById(R.id.lobby_team_name);
					viewHolder.city=(TextView)convertView.findViewById(R.id.lobby_team_city);
					viewHolder.date=(TextView)convertView.findViewById(R.id.lobby_team_date);
					viewHolder.type=(TextView)convertView.findViewById(R.id.lobby_team_type);
					viewHolder.text=(TextView)convertView.findViewById(R.id.lobby_team_text);
					viewHolder.cancel=(TextView)convertView.findViewById(R.id.lobby_team_cancel);
					viewHolder.reply=(TextView)convertView.findViewById(R.id.lobby_team_reply);
					viewHolder.l=(LinearLayout)convertView.findViewById(R.id.lobby_team_layout);
					
					viewHolder.cancel.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							
						}
						
					});
					
					viewHolder.reply.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							final View vi=v;
							LobbyTeamEntity item=(LobbyTeamEntity)vi.getTag();
							HomePageActivity h=(HomePageActivity)context;
							Bundle bundle=new Bundle();
							bundle.putString("type", item.getType());
							bundle.putString("themeKey", item.getThemeKey());
							h.openLobbyTeamReply(bundle);
						}
						
					});
					
					convertView.setTag(viewHolder);
				}
			}
		}
		if(!item.getThemeKey().equals("-1")){
			if(Tools.isManager(context, item.getTeamid())){
				viewHolder.cancel.setVisibility(View.VISIBLE);
			}
			else{
				viewHolder.cancel.setVisibility(View.GONE);
			}
			viewHolder.image.setImageBitmap(item.getImage());
			viewHolder.name.setText(item.getName());
			viewHolder.city.setText(item.getCity());
			viewHolder.date.setText(item.getDate());
			viewHolder.type.setText(item.getType());
			viewHolder.text.setText(item.getText());
			List<String> fromName=item.getFromNameList();
			List<String> content=item.getContentList();
			viewHolder.l.removeAllViews();
			for(int i=0; i<fromName.size(); i++){
				TextView t=new TextView(context);
				t.setText(fromName.get(i)+":"+content.get(i));
				viewHolder.l.addView(t);
			}
			for (int i = 0; i < mList.size(); i++) {
				if (viewHolder.l.getChildCount() != 0) {
					viewHolder.l.setVisibility(View.VISIBLE);
				}
			}
			
			viewHolder.cancel.setTag(item);
			viewHolder.reply.setTag(item);
		}
		else{
			waitHolder.l.setTag(item);
		}
		return convertView;
	}

	static class ViewHolder{
		ImageView image=null;
		TextView name=null;
		TextView city=null;
		TextView date=null;
		TextView type=null;
		TextView text=null;
		TextView cancel=null;
		TextView reply=null;
		LinearLayout l=null;
	}
	
	static class WaitHolder{
		ImageView loadClick=null;
		TextView loadText=null;
		ProgressBar loadWiat=null;
		RelativeLayout l=null;
	}
	
	class MyOnClickListener implements OnClickListener{
		private WaitHolder waitHolder;
		
		public MyOnClickListener(WaitHolder waitHolder){
			this.waitHolder=waitHolder;
		}

		@Override
		public void onClick(View v) {
			if(waitHolder.loadText.getText().toString().equals("加载更多")){
				waitHolder.loadClick.setVisibility(View.GONE);
				waitHolder.loadText.setText("正在加载");
				waitHolder.loadWiat.setVisibility(View.VISIBLE);
				final View vi=v;
				LobbyTeamEntity load=(LobbyTeamEntity)vi.getTag();
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("request", "get themelist");
				map.put("pstart", load.getStart());
				map.put("pnum", load.getNum());
				Runnable r=new ClientWrite(Tools.JsonEncode(map));
				new Thread(r).start();
			}
			else{
				waitHolder.loadClick.setVisibility(View.VISIBLE);
				waitHolder.loadText.setText("加载更多");
				waitHolder.loadWiat.setVisibility(View.GONE);
			}
		}
		
		
		
	}
	
	
}
