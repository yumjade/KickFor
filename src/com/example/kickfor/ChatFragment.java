package com.example.kickfor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.TextMessageBody;
import com.example.kickfor.pullableview.PullToRefreshLayout;
import com.example.kickfor.pullableview.PullableListView;
import com.example.kickfor.utils.IdentificationInterface;
import com.example.kickfor.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

public class ChatFragment extends Fragment implements HomePageInterface, IdentificationInterface{

	
	final String[]items ={"复制","删除"};
	
	public static String PEORSON_CHAT = "1";
	public static String GROUP_CHAT = "0";

	private TextView sendButton = null;
	private EditText edit = null;
	private PullableListView mListView = null;
	private PullToRefreshLayout pullToRefreshLayout = null;
	private ChatAdapter adapter = null;
	private List<MyChat> mList =null;
	private Context context;
	private String phone = null;
	private String type = null;
	private ImageView back = null;
	private TextView titleText = null;
	private ImageView chatManage = null;
	private int offset = 20;

	private Bitmap otherBitmap = null;
	private Bitmap myBitmap = null;

	private String startMsgId = null;

	private String authority = null;
	private String groupid=null;


	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.SECOND_LEVEL;
	}




	private void init(){
		mList=new ArrayList<MyChat>();
		this.context=getActivity();
		Bundle bundle=getArguments();
		this.phone=bundle.getString("phone");
		this.type=bundle.getString("type");
		if(type.equals(GROUP_CHAT)){
			groupid=new PreferenceData(context).getData(new String[]{phone}).get(phone).toString();
			authority=bundle.getString("authority");
		}
	}
	
	
	

	public void setMessage(EMMessage message) {
		String date=null;
		if(mList.size()>0){
			date = Tools.getDate(mList.get(mList.size()-1).getTime(), message.getMsgTime());
		}
		else{
			date = Tools.getDate(0, message.getMsgTime());
		}
		String str = ((TextMessageBody) message.getBody()).getMessage();
		if(type.equals(GROUP_CHAT)){
			setData(str, date, groupid, message.getMsgTime(), message.getMsgId());
		}
		else{
			setData(str, date, phone, message.getMsgTime(), message.getMsgId());
		}
	}

	
	

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		if(type.equals(GROUP_CHAT)){
			EMConversation conversation = EMChatManager.getInstance().getConversation(groupid);
			conversation.resetUnreadMsgCount();
		}
		else{
			EMConversation conversation = EMChatManager.getInstance().getConversation(phone);
			conversation.resetUnreadMsgCount();
		}
		if(getFragmentManager().findFragmentById(R.id.title) instanceof TitleFragment){
			TitleFragment tmp=(TitleFragment)getFragmentManager().findFragmentById(R.id.title);
			if(tmp.getState()==TitleFragment.HOMEPAGE_TITLE){
				tmp.remind(tmp.setMsgNumberChanged());
			}
		}
		if(getFragmentManager().findFragmentById(R.id.content) instanceof ListsFragment){
			ListsFragment tmp=(ListsFragment)getFragmentManager().findFragmentById(R.id.content);
			if(tmp.getState()==ListsFragment.TYPE_MESSAGE_LIST){
				tmp.changedData();
			}
		}
		super.onPause();
	}




	public String getType() {
		return type;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		init();
		System.out.println("dfgssaaaaaaaaa");
		View view = inflater.inflate(R.layout.fragment_msg, container, false);
		pullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
		sendButton = (TextView) view.findViewById(R.id.chat_send);
		edit = (EditText) view.findViewById(R.id.chat_edit);
		chatManage = (ImageView) view.findViewById(R.id.chat_manage);
		back = (ImageView) view.findViewById(R.id.chat_back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getActivity().onBackPressed();
			}

		});
		titleText = (TextView) view.findViewById(R.id.chat_text);

		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final String str = edit.getText().toString();
				if (!str.isEmpty()) {
					// 获取到与聊天人的会话对象。参数username为聊天人的userid或者groupid，后文中的username皆是如此
					EMConversation conversation = EMChatManager.getInstance().getConversation(phone);
					// 创建一条文本消息
					EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
					// 如果是群聊，设置chattype,默认是单聊
					if(type.equals(GROUP_CHAT)){
						message.setChatType(ChatType.GroupChat);
						message.setReceipt(groupid);
					}
					else{
						message.setChatType(ChatType.Chat);
						message.setReceipt(phone);
					}
					// 设置消息body
					TextMessageBody txtBody = new TextMessageBody(str);
					message.addBody(txtBody);
					// 设置接收人
					// 把消息加入到此会话对象中
					conversation.addMessage(message);
					send(message.getMsgTime(), message.getMsgId());
					// 发送消息
					EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

						@Override
						public void onError(int arg0, String arg1) {
							// TODO Auto-generated method stub
							System.out.println("失败！");
						}

						@Override
						public void onProgress(int arg0, String arg1) {
							// TODO Auto-generated method stub
							System.out.println("正在进行！");
						}

						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							System.out.println("成功！");
						}
					});
				}

			}

		});
		mListView = (PullableListView) view.findViewById(R.id.content_view);
		adapter = new ChatAdapter(context, mList);
		mListView.setAdapter(adapter);
		initiate();
		
				
		return view;
	}

	private void send(long time, String msgId) {
		String str = edit.getText().toString();
		if (str.length() > 0) {
			MyChat entity = new MyChat();
			String date=null;
			if(mList.size()>0){
				date = Tools.getDate(mList.get(mList.size()-1).getTime(), time);
			}
			else{
				date = Tools.getDate(0, time);
			}
			if(type.equals(GROUP_CHAT)){
				entity.setData(str, date, true, groupid, time);
			}
			else{
				entity.setData(str, date, true, phone, time);
			}
			entity.setImage(myBitmap);
			entity.setMsgId(msgId);
			mList.add(entity);
			adapter.notifyDataSetChanged();
			edit.setText("");
			mListView.setSelection(mListView.getCount() - 1);
			if(type.equals(PEORSON_CHAT)){
				Map<String, Object> temp=new HashMap<String, Object>();
				SQLHelper helper=SQLHelper.getInstance(context);
				Cursor cursor1=helper.select("friends", new String[]{"name", "image"}, "phone=?", new String[]{phone}, null);
				if(cursor1.moveToNext()){
					temp.put("id", phone);
					temp.put("type", String.valueOf(ListsFragment.TYPE_FRIEND_MESSAGE));
					temp.put("name", cursor1.getString(0));
					temp.put("date", date);
					temp.put("message", str);
					temp.put("result", "u");
					Cursor cursor=helper.select("systemtable", new String[]{"i"}, "id=? and type=?", new String[]{phone, String.valueOf(ListsFragment.TYPE_FRIEND_MESSAGE)}, null);
					if(cursor.moveToNext()){
						temp.put("i", cursor.getInt(0));
					}
					else{
						Cursor cursor3=helper.select("systemtable", new String[]{"max(i)"}, null, null, null);
						if(cursor3.moveToNext()){
							int maxid=cursor3.getInt(0);
							temp.put("i", maxid+1);
						}
					}
					String image=cursor1.getString(1).toString();
					if(!(image.equals("-1"))){
						temp.put("image", image);
					}
					else{
						temp.put("image", "-1");
					}
					helper.update(Tools.getContentValuesFromMap(temp, null), "systemtable", (Integer)temp.get("i"));
				}
			}
		}

	}

	private void initiate() {
		SQLHelper helper = SQLHelper.getInstance(context);
		if (!(this.type.equals("0"))) {
			chatManage.setVisibility(View.GONE);

			Cursor cursor1 = helper.select("friends", new String[] { "image", "name" }, "phone=?",
					new String[] { phone }, null);
			if (cursor1.moveToNext()) {
				String imgPath = cursor1.getString(0);
				if (!imgPath.equals("-1")) {
					otherBitmap = BitmapFactory.decodeFile(imgPath);
				} else {
					otherBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.team_default);
				}
				titleText.setText(cursor1.getString(1));
			}
			Cursor cursor2 = helper.select("ich", new String[] { "image" }, "phone=?", new String[] { "host" }, null);
			if (cursor2.moveToNext()) {
				String imgPath = cursor2.getString(0);
				if (!imgPath.equals("-1")) {
					myBitmap = BitmapFactory.decodeFile(imgPath);
				} else {
					myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.team_default);
				}
			}

			EMConversation conversation = EMChatManager.getInstance().getConversation(phone);
			// 获取此会话的所有消息
			List<EMMessage> messages = conversation.getAllMessages();
			// sdk初始化加载的聊天记录为20条，到顶时需要去db里获取更多
			Iterator<EMMessage> iter = messages.iterator();

			while (iter.hasNext()) {
				EMMessage message = iter.next();
				TextMessageBody t = (TextMessageBody) message.getBody();
				MyChat entity = new MyChat();
				if (message.getFrom().equals(phone)) {
					String date=null;
					if(mList.size()>0){
						date = Tools.getDate(mList.get(mList.size()-1).getTime(), message.getMsgTime());
					}
					else{
						date = Tools.getDate(0, message.getMsgTime());
					}
					entity.setData(t.getMessage(), date, false, phone, message.getMsgTime());
					entity.setImage(otherBitmap);
					entity.setMsgId(message.getMsgId());
				} else {
					String date=null;
					if(mList.size()>0){
						date = Tools.getDate(mList.get(mList.size()-1).getTime(), message.getMsgTime());
					}
					else{
						date = Tools.getDate(0, message.getMsgTime());
					}
					entity.setData(t.getMessage(), date, true, phone, message.getMsgTime());
					entity.setImage(myBitmap);
					entity.setMsgId(message.getMsgId());
				}
				mList.add(entity);
			}

			if (messages.size() > 0) {
				startMsgId = messages.get(0).getMsgId();
			}

			pullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {

				@Override
				public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
					try {
						EMConversation conversation = EMChatManager.getInstance().getConversation(phone);
						// 获取startMsgId之前的pagesize条消息，此方法获取的messages
						// sdk会自动存入到此会话中，app中无需再次把获取到的messages添加到会话中
						List<EMMessage> messages = conversation.loadMoreMsgFromDB(startMsgId, offset);
						Collections.reverse(messages);
						Iterator<EMMessage> iter = messages.iterator();
						
						while (iter.hasNext()) {
							EMMessage message = iter.next();
							int position=messages.indexOf(message);
							String date=null;
							if(position==messages.size()-1){
								date = Tools.getDate(0, message.getMsgTime());
							}
							else{
								position++;
								date = Tools.getDate(messages.get(position).getMsgTime(), message.getMsgTime());
							}
							TextMessageBody t = (TextMessageBody) message.getBody();
							MyChat entity = new MyChat();
							if (message.getFrom().equals(phone)) {
								entity.setData(t.getMessage(), date, false, phone, message.getMsgTime());
								entity.setImage(otherBitmap);
								entity.setMsgId(message.getMsgId());
							} else {
								entity.setData(t.getMessage(), date, true, phone, message.getMsgTime());
								entity.setImage(myBitmap);
								entity.setMsgId(message.getMsgId());
							}
							mList.add(0, entity);
						}
						if(messages.size()>0){
							startMsgId = messages.get(messages.size()-1).getMsgId();
						}
						adapter.notifyDataSetChanged();

					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						new Handler() {
							@Override
							public void handleMessage(Message msg) {
								pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
							}
						}.sendEmptyMessageDelayed(0, 1000);
					}

				}

				@Override
				public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
					// TODO Auto-generated method stub
					new Handler() {
						@Override
						public void handleMessage(Message msg) {
							pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
						}
					}.sendEmptyMessageDelayed(0, 1000);

				}
			}, 1);
		}
		
		else {
			chatManage.setVisibility(View.VISIBLE);
			Cursor cursor0 = helper.select("teams", new String[] { "name" }, "teamid=?", new String[] { phone }, null);
			if (cursor0.moveToNext()) {
				titleText.setText(cursor0.getString(0));
			}
			
			Cursor cursor2 = helper.select("ich", new String[] { "image" }, "phone=?", new String[] { "host" }, null);
			if (cursor2.moveToNext()) {
				String imgPath = cursor2.getString(0);
				if (!imgPath.equals("-1")) {
					myBitmap = BitmapFactory.decodeFile(imgPath);
				} else {
					myBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.team_default);
				}
			}
			
			System.out.println("group id= "+new PreferenceData(context).getData(new String[]{phone}).get(phone).toString());
			
			EMConversation conversation = EMChatManager.getInstance().getConversation(groupid);
			// 获取此会话的所有消息
			List<EMMessage> messages = conversation.getAllMessages();
			// sdk初始化加载的聊天记录为20条，到顶时需要去db里获取更多
			Iterator<EMMessage> iter = messages.iterator();
			
			while (iter.hasNext()) {
				EMMessage message = iter.next();
				TextMessageBody t = (TextMessageBody) message.getBody();
				MyChat entity = new MyChat();
				if(message.getFrom().equals(new PreferenceData(context).getData(new String[]{"phone"}).get("phone").toString())){
					String date=null;
					if(mList.size()>0){
						date = Tools.getDate(mList.get(mList.size()-1).getTime(), message.getMsgTime());
					}
					else{
						date = Tools.getDate(0, message.getMsgTime());
					}
					entity.setData(t.getMessage(), date, true, groupid, message.getMsgTime());
					entity.setImage(myBitmap);
					entity.setMsgId(message.getMsgId());
				}
				else{
					String otherPhone=message.getFrom();
					System.out.println("other's phone=="+otherPhone);
					Cursor cursor=helper.select("friends", new String[]{"image", "name"}, "phone=?", new String[]{otherPhone}, null);
					if(cursor.moveToNext()){
						String imgPath=cursor.getString(0);
						if(!imgPath.equals("-1")){
							otherBitmap=BitmapFactory.decodeFile(imgPath);
						}
						else{
							otherBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.team_default);
						}
					}
					String date=null;
					if(mList.size()>0){
						date = Tools.getDate(mList.get(mList.size()-1).getTime(), message.getMsgTime());
					}
					else{
						date = Tools.getDate(0, message.getMsgTime());
					}
					entity.setData(t.getMessage(), date, false, groupid, message.getMsgTime());
					entity.setImage(otherBitmap);
					entity.setMsgId(message.getMsgId());
				}
				mList.add(entity);
			}
			
			if (messages.size() > 0) {
				startMsgId = messages.get(0).getMsgId();
			}

			pullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {

				@Override
				public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
					try {
						SQLHelper helper = SQLHelper.getInstance(context);
						
						EMConversation conversation = EMChatManager.getInstance().getConversation(groupid);
						// 获取startMsgId之前的pagesize条消息，此方法获取的messages
						// sdk会自动存入到此会话中，app中无需再次把获取到的messages添加到会话中
						List<EMMessage> messages = conversation.loadMoreMsgFromDB(startMsgId, offset);
						Collections.reverse(messages);
						Iterator<EMMessage> iter = messages.iterator();
						
						while (iter.hasNext()) {
							EMMessage message = iter.next();
							int position=messages.indexOf(message);
							String date=null;
							if(position==messages.size()-1){
								date = Tools.getDate(0, message.getMsgTime());
							}
							else{
								position++;
								date = Tools.getDate(messages.get(position).getMsgTime(), message.getMsgTime());
							}
							TextMessageBody t = (TextMessageBody) message.getBody();
							MyChat entity = new MyChat();
							if (message.getFrom().equals(new PreferenceData(context).getData(new String[]{"phone"}).get("phone").toString())) {
								entity.setData(t.getMessage(), date, true, groupid, message.getMsgTime());
								entity.setImage(myBitmap);
								entity.setMsgId(message.getMsgId());
							} else {
								String otherPhone=message.getFrom();
								Cursor cursor=helper.select("friends", new String[]{"image", "name"}, "phone=?", new String[]{otherPhone}, null);
								if(cursor.moveToNext()){
									String imgPath=cursor.getString(0);
									if(!imgPath.equals("-1")){
										otherBitmap=BitmapFactory.decodeFile(imgPath);
									}
									else{
										otherBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.team_default);
									}
								}
								entity.setData(t.getMessage(), date, false, groupid, message.getMsgTime());
								entity.setImage(otherBitmap);
								entity.setMsgId(message.getMsgId());
							}
							mList.add(0, entity);
						}
						if(messages.size()>0){
							startMsgId = messages.get(messages.size()-1).getMsgId();
						}
						adapter.notifyDataSetChanged();

					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						new Handler() {
							@Override
							public void handleMessage(Message msg) {
								pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
							}
						}.sendEmptyMessageDelayed(0, 1000);
					}
				}

				@Override
				public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
					// TODO Auto-generated method stub
					new Handler() {
						@Override
						public void handleMessage(Message msg) {
							pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
						}
					}.sendEmptyMessageDelayed(0, 1000);

				}
			}, 2);
			

			chatManage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					((HomePageActivity) getActivity()).openChangingRoomManager(phone, authority);
				}

			});

		}

		adapter.notifyDataSetChanged();
		mListView.setSelection(mListView.getCount() - 1);
	}

	private void setData(String str, String date, String phone, long time, String msgId) {
		MyChat entity = new MyChat();
		entity.setData(str, date, false, phone, time);
		entity.setImage(otherBitmap);
		entity.setMsgId(msgId);
		mList.add(entity);
		adapter.notifyDataSetChanged();
		mListView.setSelection(mListView.getCount()-1);
	}

	
	public String getPhone() {
		return phone;
	}

	
	
}
