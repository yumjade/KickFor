package com.example.kickfor.lobby;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Attributes.Name;

import com.example.kickfor.ClientWrite;
import com.example.kickfor.HomePageActivity;
import com.example.kickfor.R;
import com.example.kickfor.Tools;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
		LobbyTeamEntity item=mList.get(position);
		if(convertView==null){
			convertView=inflater.inflate(R.layout.lobby_team_item, null);
			viewHolder=new ViewHolder();
			viewHolder.image=(ImageView)convertView.findViewById(R.id.lobby_team_photo);
			viewHolder.name=(TextView)convertView.findViewById(R.id.lobby_team_name);
			viewHolder.city=(TextView)convertView.findViewById(R.id.lobby_team_city);
			viewHolder.type=(TextView)convertView.findViewById(R.id.lobby_team_type);
			viewHolder.text=(TextView)convertView.findViewById(R.id.lobby_team_text);
			viewHolder.date=(TextView)convertView.findViewById(R.id.lobby_team_date);
			viewHolder.cancel=(TextView)convertView.findViewById(R.id.lobby_team_cancel);
			viewHolder.reply=(TextView)convertView.findViewById(R.id.lobby_team_reply);
			viewHolder.l=(LinearLayout)convertView.findViewById(R.id.lobby_team_layout);
				
			viewHolder.cancel.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(final View v) {
					new AlertDialog.Builder(context)
					.setTitle("是否确定删除")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							LobbyTeamEntity item=(LobbyTeamEntity)v.getTag();
							Map<String, Object> tmp=new HashMap<String, Object>();
							tmp.put("request", "del theme");
							tmp.put("themekey", item.getThemeKey());
							Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
							new Thread(r).start();
						}
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
						}
					}).show();
					
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
		else{
			viewHolder=(ViewHolder)convertView.getTag();
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
//			for (int i = 0; i < mList.size(); i++) {
//				if (viewHolder.l.getChildCount() != 0) {
//					viewHolder.l.setVisibility(View.VISIBLE);
//				}
//			}
			
			viewHolder.cancel.setTag(item);
			viewHolder.reply.setTag(item);
		}
		return convertView;
	}

	static class ViewHolder{
		ImageView image=null;
		TextView name=null;
		TextView city=null;
		TextView type=null;
		TextView text=null;
		TextView date=null;
		TextView cancel=null;
		TextView reply=null;
		LinearLayout l=null;
	}
	

}
