package com.example.kickfor.team;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.easemob.chat.EMGroupManager;
import com.example.kickfor.ClientWrite;
import com.example.kickfor.HomePageActivity;
import com.example.kickfor.PreferenceData;
import com.example.kickfor.R;
import com.example.kickfor.Tools;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ChangingRoomAdapter extends BaseAdapter implements OnClickListener{
	
	protected static final boolean JOIN_IN=false;
	protected static final boolean MEMBER=true;
	
	private LayoutInflater mInflater=null;
	private List<ChangingRoomEntity> mList=null;
	private boolean type;
	private int authority;
	private Context context=null;
	private ListView mListView=null;
	
	private String phone=null;
	
	public ChangingRoomAdapter(Context context, List<ChangingRoomEntity> mList, boolean type, int authority, ListView mListView){
		this.context=context;
		mInflater=LayoutInflater.from(context);
		this.mList=mList;
		this.type=type;
		this.authority=authority;
		this.mListView=mListView;
		PreferenceData pd=new PreferenceData(context);
		phone=pd.getData(new String[]{"phone"}).get("phone").toString();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ChangingRoomEntity item=mList.get(position);
		ViewHolder viewHolder=null;
		if(convertView==null){
			//System.out.println("yes, "+position+" is null");
			convertView=mInflater.inflate(R.layout.changing_room_manage_item, null);
			viewHolder=new ViewHolder();
			viewHolder.number=(EditText)convertView.findViewById(R.id.manage_number);
			viewHolder.name=(EditText)convertView.findViewById(R.id.manage_name);
			viewHolder.left=(TextView)convertView.findViewById(R.id.manage_authority);
			viewHolder.right=(TextView)convertView.findViewById(R.id.manage_kick);
			viewHolder.pb1=(ProgressBar)convertView.findViewById(R.id.manage_pb1);
			viewHolder.pb2=(ProgressBar)convertView.findViewById(R.id.manage_pb2);
			viewHolder.layout=(LinearLayout)convertView.findViewById(R.id.manage_layout);
			
			class MyTextWatcher implements TextWatcher{
				private ViewHolder mHolder;
				private boolean status;
				public MyTextWatcher(ViewHolder holder, boolean status){
					mHolder=holder;
					this.status=status;
				}
				
				@Override
				public void afterTextChanged(Editable arg0) {
					// TODO Auto-generated method stub
					if(!TextUtils.isEmpty(arg0)){
						if(status==true){
							int position=(Integer)mHolder.name.getTag();
							String str=mList.get(position).getName();
							if(!str.equals(arg0.toString())){
								mList.get(position).changedData();
								mList.get(position).setName(arg0.toString());
								System.out.println(str+"-has-changed-his-name-to-"+arg0+"-already");
							}
						}
						else{
							int position=(Integer)mHolder.number.getTag();
							String str=mList.get(position).getNumber();
							if(!str.equals(arg0.toString())){
								mList.get(position).changedData();
								mList.get(position).setNumber(arg0.toString());
								System.out.println(str+"-has-changed-his-number-to-"+arg0+"-already");
							}
						}
					}
				}
				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void onTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					// TODO Auto-generated method stub
					
				}
				
			}
			viewHolder.name.addTextChangedListener(new MyTextWatcher(viewHolder, true));
			viewHolder.number.addTextChangedListener(new MyTextWatcher(viewHolder, false));
			convertView.setTag(viewHolder);
			
		}
		else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		viewHolder.setEnable(false);
		viewHolder.name.setTag(position);
		viewHolder.number.setTag(position);
		viewHolder.left.setTag(position);
		viewHolder.right.setTag(position);
		viewHolder.number.setText(item.getNumber());
		viewHolder.name.setText(item.getName());
		viewHolder.phone=item.getPhone();
		viewHolder.teamid=item.getTeamid();
		viewHolder.layout.setBackgroundColor(Color.parseColor("#ffffff"));

		if(item.pb1==true){
			viewHolder.pb1.setVisibility(View.VISIBLE);
			viewHolder.left.setAlpha((float) 0.6);
		}
		else{
			viewHolder.pb1.setVisibility(View.GONE);
			viewHolder.left.setAlpha(1);
		}
		if(item.pb2==true){
			viewHolder.pb2.setVisibility(View.VISIBLE);
			viewHolder.right.setAlpha((float) 0.6);
		}
		else{
			viewHolder.pb2.setVisibility(View.GONE);
			viewHolder.right.setAlpha(1);
		}
		
		if(phone.equals(item.getPhone())){
			viewHolder.left.setVisibility(View.GONE);
			viewHolder.right.setVisibility(View.VISIBLE);
			viewHolder.right.setText("离开球队");
		}
		else{
			viewHolder.left.setVisibility(View.VISIBLE);
			viewHolder.right.setVisibility(View.VISIBLE);
			viewHolder.right.setText("剔除");
			viewHolder.left.setText("授权");
		}
		
		if(authority==4){
			if(type==JOIN_IN){
				viewHolder.left.setText("同意");
				viewHolder.right.setText("否决");
				viewHolder.name.setEnabled(false);
				viewHolder.number.setEnabled(false);
			}
			else{
				if(item.getAuthority().equals("2")){
					viewHolder.left.setText("已授权");
					viewHolder.layout.setBackgroundColor(Color.parseColor("#f4a460"));
				}
			}
		}
		else{
			viewHolder.left.setVisibility(View.GONE);
			viewHolder.right.setVisibility(View.GONE);
			if(phone.equals(item.getPhone())){
				viewHolder.right.setVisibility(View.VISIBLE);
				viewHolder.right.setText("离开球队");
			}
			if(item.getAuthority().equals("2")){
				viewHolder.layout.setBackgroundColor(Color.parseColor("#f4a460"));
			}
		}
		viewHolder.left.setOnClickListener(this);
		viewHolder.right.setOnClickListener(this);
		viewHolder.setEnable(true);
		return convertView;
	}
	
	public static class ViewHolder{
		LinearLayout layout=null;
		EditText number=null;
		EditText name=null;
		TextView left=null;
		TextView right=null;
		ProgressBar pb1=null;
		ProgressBar pb2=null;
		String phone=null;
		String teamid=null;
		
		
		public void setEnable(boolean enable){
			number.setEnabled(enable);
			name.setEnabled(enable);
			left.setEnabled(enable);
			right.setEnabled(enable);
			if(enable==true){
				
				pb1.setVisibility(View.GONE);
				pb2.setVisibility(View.GONE);
				left.setAlpha(1);
				right.setAlpha(1);
			}
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		ViewHolder v=(ViewHolder)((View)view.getParent().getParent()).getTag();
		v.setEnable(false);
		int id=view.getId();
		int position=(Integer)view.getTag();
		Map<String, Object> tmp=new HashMap<String, Object>();
		if(id==R.id.manage_authority){
			if(type==JOIN_IN){
				boolean isSuccess=true;
				try{
					EMGroupManager.getInstance().addUsersToGroup(new PreferenceData(context).getData(new String[]{v.teamid}).get(v.teamid).toString(), new String[]{v.phone});
				}catch(Exception e){
					e.printStackTrace();
					isSuccess=false;
				}
				if(isSuccess){
					((TextView)view).setAlpha((float) 0.6);
					mList.get(position).pb2=true;
					v.pb2.setVisibility(View.VISIBLE);
					HomePageActivity ac=(HomePageActivity)context;;
					ac.openProgressBarWait(HomePageActivity.WAIT_PROGRESSBAR, v, mList.get(position));
					tmp.put("request", "agree join");
					tmp.put("phone", v.phone);
					tmp.put("teamid", v.teamid);
					Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
					new Thread(r).start();
				}
				else{
					Toast.makeText(context, "允许加入失败，请重试", Toast.LENGTH_LONG).show();
					v.setEnable(true);
				}
			}
			else{
				String str=v.name.getText().toString();
				String str1=v.phone;
				if(!str.isEmpty() && !str1.isEmpty()){
					((TextView)view).setAlpha((float) 0.6);
					mList.get(position).pb2=true;
					v.pb2.setVisibility(View.VISIBLE);
					HomePageActivity ac=(HomePageActivity)context;;
					ac.openProgressBarWait(HomePageActivity.WAIT_PROGRESSBAR, v, mList.get(position));
					if(mList.get(position).getAuthority().equals("0")){
						tmp.put("request", "authority");
						tmp.put("phone", v.phone);
						tmp.put("name", v.name.getText());
						tmp.put("number", v.number.getText());
						tmp.put("teamid", v.teamid);
						Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
						new Thread(r).start();
					}
					else if(mList.get(position).getAuthority().equals("2")){
						tmp.put("request", "decline authority");
						tmp.put("phone", v.phone);
						tmp.put("name", v.name.getText());
						tmp.put("number", v.number.getText());
						tmp.put("teamid", v.teamid);
						Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
						new Thread(r).start();
					}
				}
				else if(str.isEmpty()){
					Toast.makeText(context, "姓名为空怎么授权", Toast.LENGTH_SHORT).show();
					v.setEnable(true);
				}
				else if(str1.isEmpty()){
					Toast.makeText(context, "此球员尚未存在，请保存退出后重新操作", Toast.LENGTH_SHORT).show();
					v.setEnable(true);
				}
			}
		}
		else if(id==R.id.manage_kick){
			if(type==JOIN_IN){
				((TextView)view).setAlpha((float) 0.6);
				mList.get(position).pb1=true;
				v.pb1.setVisibility(View.VISIBLE);
				HomePageActivity ac=(HomePageActivity)context;;
				ac.openProgressBarWait(HomePageActivity.WAIT_PROGRESSBAR, v, mList.get(position));
				tmp.put("request", "refuse join");
				tmp.put("phone", v.phone);
				tmp.put("teamid", v.teamid);
				Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
				new Thread(r).start();
			}
			else{
				if(authority==4 && v.phone.equals(phone)){
					Toast.makeText(context, "暂不支持队长离队", Toast.LENGTH_SHORT).show();
					v.setEnable(true);
				}
			    else if(v.phone.isEmpty()){
					mList.remove(position);
					this.notifyDataSetChanged();
					Tools.setListViewHeight(mListView);
				}
				else{
					boolean isSuccess=true;
					if(authority==4){
						try{
							EMGroupManager.getInstance().removeUserFromGroup(new PreferenceData(context).getData(new String[]{v.teamid}).get(v.teamid).toString(), v.phone);//需异步处理
						}catch(Exception e){
							isSuccess=false;
						}
					}
					else{
						try{
							EMGroupManager.getInstance().exitFromGroup(new PreferenceData(context).getData(new String[]{v.teamid}).get(v.teamid).toString());//需异步处理
						}catch(Exception e){
							isSuccess=false;
						}
					}
					if(isSuccess){
						((TextView)view).setAlpha((float) 0.6);
						if(mList.size()>position){
							mList.get(position).pb1=true;
							v.pb1.setVisibility(View.VISIBLE);
							HomePageActivity ac=(HomePageActivity)context;;
							ac.openProgressBarWait(HomePageActivity.WAIT_PROGRESSBAR, v, mList.get(position));
							tmp.put("request", "fuck off");
							tmp.put("phone", v.phone);
							tmp.put("name", v.name.getText());
							tmp.put("number", v.number.getText());
							tmp.put("teamid", v.teamid);
							Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
							new Thread(r).start();
						}
					}
					else{
						Toast.makeText(context, "操作未成功，请重试", Toast.LENGTH_LONG).show();
						v.setEnable(true);
					}
					
				}
			}
		}
		
	}
	
	
	
	
	

}
