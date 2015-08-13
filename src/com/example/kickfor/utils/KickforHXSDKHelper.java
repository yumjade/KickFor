/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.kickfor.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMChatRoomChangeListener;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.example.kickfor.applib.HXSDKHelper;
import com.example.kickfor.ChatFragment;
import com.example.kickfor.HomePageActivity;
import com.example.kickfor.PreferenceData;
import com.example.kickfor.R;
import com.example.kickfor.SQLHelper;
import com.example.kickfor.applib.HXNotifier;
import com.example.kickfor.applib.HXNotifier.HXNotificationInfoProvider;
import com.example.kickfor.applib.HXSDKModel;
import com.easemob.chat.CmdMessageBody;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.TextMessageBody;
//import com.easemob.chatuidemo.activity.ChatActivity;
//import com.easemob.chatuidemo.activity.MainActivity;
import com.example.kickfor.utils.User;
import com.example.kickfor.utils.CallReceiver;
import com.example.kickfor.utils.CommonUtils;
import com.easemob.util.EMLog;
import com.easemob.util.EasyUtils;

/**
 * Demo UI HX SDK helper class which subclass HXSDKHelper
 * @author easemob
 *
 */
public class KickforHXSDKHelper extends HXSDKHelper{

    private static final String TAG = "KickforHXSDKHelper";
    
    
    

	/**
     * EMEventListener
     */
    protected EMEventListener eventListener = null;

    /**
     * contact list in cache
     */
    private Map<String, User> contactList;
    private CallReceiver callReceiver;
    
    /**
     * 閻€劍娼电拋鏉跨秿foreground Activity
     */
    private List<Activity> activityList = new ArrayList<Activity>();
    
    public void pushActivity(Activity activity){
        if(!activityList.contains(activity)){
            activityList.add(0,activity); 
        }
    }
    
    public void popActivity(Activity activity){
        activityList.remove(activity);
    }
    
    @Override
    protected void initHXOptions(){
        super.initHXOptions();

        // you can also get EMChatOptions to set related SDK options
        EMChatOptions options = EMChatManager.getInstance().getChatOptions();
        options.allowChatroomOwnerLeave(getModel().isChatroomOwnerLeaveAllowed());  
    }

    @Override
    protected void initListener(){
        super.initListener();
        IntentFilter callFilter = new IntentFilter(EMChatManager.getInstance().getIncomingCallBroadcastAction());
        if(callReceiver == null){
            callReceiver = new CallReceiver();
        }

        //濞夈劌鍞介柅姘崇樈楠炴寧鎸遍幒銉︽暪閼帮拷
        appContext.registerReceiver(callReceiver, callFilter);    
        //濞夈劌鍞藉☉鍫熶紖娴滃娆㈤惄鎴濇儔
        initEventListener();
    }
    
    /**
     * 鍏ㄥ眬浜嬩欢鐩戝惉
     * 鍥犱负鍙兘浼氭湁UI椤甸潰鍏堝鐞嗗埌杩欎釜娑堟伅锛屾墍浠ヤ竴鑸鏋淯I椤甸潰宸茬粡澶勭悊锛岃繖閲屽氨涓嶉渶瑕佸啀娆″锟�?
     * activityList.size() <= 0 鎰忓懗锟�?锟�?鏈夐〉闈㈤兘宸茬粡鍦ㄥ悗鍙拌繍琛岋紝鎴栵拷?锟藉凡缁忕锟�?Activity Stack
     */
    protected void initEventListener() {
    	System.out.println("actuaally done");
        eventListener = new EMEventListener() {
            private BroadcastReceiver broadCastReceiver = null;
            
            @Override
            public void onEvent(EMNotifierEvent event) {
            	System.out.println("notificationnnnnnnnnnnnnnnnnnn");
                EMMessage message = null;
                if(event.getData() instanceof EMMessage){
                    message = (EMMessage)event.getData();
                    EMLog.d(TAG, "receive the event : " + event.getEvent() + ",id : " + message.getMsgId());
                }
                
                switch (event.getEvent()) {
                case EventNewMessage:
                    //鎼存梻鏁ら崷銊ユ倵閸欏府绱濇稉宥夋付鐟曚礁鍩涢弬鐧營,闁氨鐓￠弽蹇斿絹锟�?鐑樻煀濞戝牊锟�?
                    if(activityList.size() <= 0){
                        HXSDKHelper.getInstance().getNotifier().onNewMsg(message);
                    }
                    break;
                case EventOfflineMessage:
                    if(activityList.size() <= 0){
                        EMLog.d(TAG, "received offline messages");
                        List<EMMessage> messages = (List<EMMessage>) event.getData();
                        HXSDKHelper.getInstance().getNotifier().onNewMesg(messages);
                    }
                    break;
                // below is just giving a example to show a cmd toast, the app should not follow this
                // so be careful of this
                case EventNewCMDMessage:
                {
                    
                    EMLog.d(TAG, "锟�?璺哄煂闁繋绱跺☉鍫熶紖");
                    //閼惧嘲褰囧☉鍫熶紖body
                    CmdMessageBody cmdMsgBody = (CmdMessageBody) message.getBody();
                    final String action = cmdMsgBody.action;//閼惧嘲褰囬懛顏勭暰娑斿¨ction
                    
                    //閼惧嘲褰囬幍鈺佺潔鐏炵偞锟斤拷 濮濄倕顦╅惇浣烘殣
                    //message.getStringAttribute("");
                    EMLog.d(TAG, String.format("閫忎紶娑堟伅锛歛ction:%s,message:%s", action,message.toString()));
                    final String str = appContext.getString(R.string.receive_the_passthrough);
                    
                    final String CMD_TOAST_BROADCAST = "easemob.demo.cmd.toast";
                    IntentFilter cmdFilter = new IntentFilter(CMD_TOAST_BROADCAST);
                    
                    if(broadCastReceiver == null){
                        broadCastReceiver = new BroadcastReceiver(){

                            @Override
                            public void onReceive(Context context, Intent intent) {
                                // TODO Auto-generated method stub
                                Toast.makeText(appContext, intent.getStringExtra("cmd_value"), Toast.LENGTH_SHORT).show();
                            }
                        };
                        
                      //濞夈劌鍞介獮鎸庢尡閹恒儲鏁归懓锟�?
                        appContext.registerReceiver(broadCastReceiver,cmdFilter);
                    }

                    Intent broadcastIntent = new Intent(CMD_TOAST_BROADCAST);
                    broadcastIntent.putExtra("cmd_value", str+action);
                    appContext.sendBroadcast(broadcastIntent, null);
                    
                    break;
                }
                case EventDeliveryAck:
                    message.setDelivered(true);
                    break;
                case EventReadAck:
                    message.setAcked(true);
                    break;
                // add other events in case you are interested in
                default:
                    break;
                }
                
            }
        };
        
        EMChatManager.getInstance().registerEventListener(eventListener);
        
        EMChatManager.getInstance().addChatRoomChangeListener(new EMChatRoomChangeListener(){
            private final static String ROOM_CHANGE_BROADCAST = "easemob.demo.chatroom.changeevent.toast";
            private final IntentFilter filter = new IntentFilter(ROOM_CHANGE_BROADCAST);
            private boolean registered = false;
            
            private void showToast(String value){
                if(!registered){
                  //濞夈劌鍞介獮鎸庢尡閹恒儲鏁归懓锟�?
                    appContext.registerReceiver(new BroadcastReceiver(){

                        @Override
                        public void onReceive(Context context, Intent intent) {
                            Toast.makeText(appContext, intent.getStringExtra("value"), Toast.LENGTH_SHORT).show();
                        }
                        
                    }, filter);
                    
                    registered = true;
                }
                
                Intent broadcastIntent = new Intent(ROOM_CHANGE_BROADCAST);
                broadcastIntent.putExtra("value", value);
                appContext.sendBroadcast(broadcastIntent, null);
            }
            
            @Override
            public void onChatRoomDestroyed(String roomId, String roomName) {
                showToast(" room : " + roomId + " with room name : " + roomName + " was destroyed");
                Log.i("info","onChatRoomDestroyed="+roomName);
            }

            @Override
            public void onMemberJoined(String roomId, String participant) {
                showToast("member : " + participant + " join the room : " + roomId);
                Log.i("info", "onmemberjoined="+participant);
                
            }

            @Override
            public void onMemberExited(String roomId, String roomName,
                    String participant) {
                showToast("member : " + participant + " leave the room : " + roomId + " room name : " + roomName);
                Log.i("info", "onMemberExited="+participant);
                
            }

            @Override
            public void onMemberKicked(String roomId, String roomName,
                    String participant) {
                showToast("member : " + participant + " was kicked from the room : " + roomId + " room name : " + roomName);
                Log.i("info", "onMemberKicked="+participant);
                
            }

        });
    }

    /**
     * 閼奉亜鐣炬稊澶愶拷姘辩叀閺嶅繑褰佺粈鍝勫敶鐎癸拷
     * @return
     */
    @Override
    protected HXNotificationInfoProvider getNotificationListener() {
        //閸欘垯浜掔憰鍡欐磰姒涙顓婚惃鍕啎缂冿拷
        return new HXNotificationInfoProvider() {
            
            @Override
            public String getTitle(EMMessage message) {
              //娣囶喗鏁奸弽鍥暯,鏉╂瑩鍣锋担璺ㄦ暏姒涙锟�?
            	String name="";
            	SQLHelper helper=SQLHelper.getInstance(appContext);
            	if(message.getChatType().equals(ChatType.Chat)){
            		String phone=message.getFrom();
            		Cursor cursor=helper.select("friends", new String[]{"name"}, "phone=?", new String[]{phone}, null);
            		if(cursor.moveToNext()){
            			name=cursor.getString(0);
            		}
            	}
            	else if(message.getChatType().equals(ChatType.GroupChat)){
            		String groupid=message.getTo();
            		PreferenceData pd=new PreferenceData(appContext);
            		Map<String, Object> map=pd.getData();
					Iterator<String> iter=map.keySet().iterator();
					while(iter.hasNext()){
						String key=iter.next();
						if(map.get(key) instanceof String && map.get(key).toString().equals(groupid)){
							name=key;
							Cursor cursor=helper.select("teams", new String[]{"name"}, "teamid=?", new String[]{name}, null);
							if(cursor.moveToNext()){
								name=cursor.getString(0);
							}
							break;
						}
					}
            	}
                return name;
            }
            
            @Override
            public int getSmallIcon(EMMessage message) {
              //鐠佸墽鐤嗙亸蹇撴禈閺嶅浄绱濇潻娆撳櫡娑撴椽绮拋锟�?
                return R.drawable.ic_launcher;
            }
            
            @Override
            public String getDisplayedText(EMMessage message) {
                // 鐠佸墽鐤嗛悩鑸碉拷浣圭埉閻ㄥ嫭绉烽幁顖涘絹锟�?鐚寸礉閸欘垯浜掗弽瑙勫祦message閻ㄥ嫮琚崹瀣粵閻╃绨查幓鎰仛
            	String name="";
            	SQLHelper helper=SQLHelper.getInstance(appContext);
            	if(message.getChatType().equals(ChatType.Chat)){
            		String phone=message.getFrom();
            		Cursor cursor=helper.select("friends", new String[]{"name"}, "phone=?", new String[]{phone}, null);
            		if(cursor.moveToNext()){
            			name=cursor.getString(0);
            		}
            	}
            	else if(message.getChatType().equals(ChatType.GroupChat)){
            		String groupid=message.getTo();
            		PreferenceData pd=new PreferenceData(appContext);
            		Map<String, Object> map=pd.getData();
					Iterator<String> iter=map.keySet().iterator();
					while(iter.hasNext()){
						String key=iter.next();
						if(map.get(key) instanceof String && map.get(key).toString().equals(groupid)){
							name=key;
							Cursor cursor=helper.select("teams", new String[]{"name"}, "teamid=?", new String[]{name}, null);
							if(cursor.moveToNext()){
								name=cursor.getString(0);
							}
							break;
						}
					}
            	}
            	String text="";
                String ticker = CommonUtils.getMessageDigest(message, appContext);
                if(message.getType() == Type.TXT){
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[琛ㄦ儏]");
                    text=((TextMessageBody)message.getBody()).getMessage();
                }
                
                return name+":"+text;
            }
            
            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
            	return ((TextMessageBody)message.getBody()).getMessage();
                // return fromUsersNum + "涓熀鍙嬶紝鍙戞潵锟�?" + messageNum + "鏉℃秷锟�?";
            }

            
            @Override
            public Intent getLaunchIntent(EMMessage message) {
                //璁剧疆鐐瑰嚮閫氱煡鏍忚烦杞簨锟�?
                Intent intent = new Intent(appContext, HomePageActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                ChatType chatType = message.getChatType();
                if(chatType == ChatType.Chat) { // 鍗曡亰淇℃伅
                    intent.putExtra("phone", message.getFrom());
                    intent.putExtra("type", ChatFragment.PEORSON_CHAT);
                } 
                else if(chatType == ChatType.GroupChat){
                	intent.putExtra("groupid", message.getTo());
                    intent.putExtra("type", ChatFragment.GROUP_CHAT);
                }
                return intent;
            }
        };
    }
    
    
    
//    @Override
//    protected void onConnectionConflict(){
//        Intent intent = new Intent(appContext, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("conflict", true);
//        appContext.startActivity(intent);
//    }
//    
//    @Override
//    protected void onCurrentAccountRemoved(){
//    	Intent intent = new Intent(appContext, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra(Constant.ACCOUNT_REMOVED, true);
//        appContext.startActivity(intent);
//    }
    

    @Override
    protected HXSDKModel createModel() {
        return new KickforHXSDKModel(appContext);
    }
    
    @Override
    public HXNotifier createNotifier(){
        return new HXNotifier(){
            public synchronized void onNewMsg(final EMMessage message) {
                if(EMChatManager.getInstance().isSlientMessage(message)){
                    return;
                }
                
                String chatUsename = null;
                List<String> notNotifyIds = null;
                // 閼惧嘲褰囩拋鍓х枂閻ㄥ嫪绗夐幓鎰仛閺傜増绉烽幁顖滄畱閻€劍鍩涢幋鏍拷鍛參缂佸埇ds
                if (message.getChatType() == ChatType.Chat) {
                    chatUsename = message.getFrom();
                    notNotifyIds = ((KickforHXSDKModel) hxModel).getDisabledGroups();
                } else {
                    chatUsename = message.getTo();
                    notNotifyIds = ((KickforHXSDKModel) hxModel).getDisabledIds();
                }

                if (notNotifyIds == null || !notNotifyIds.contains(chatUsename)) {
                    // 閸掋倖鏌嘺pp閺勵垰鎯侀崷銊ユ倵閸欙拷
                    if (!EasyUtils.isAppRunningForeground(appContext)) {
                        EMLog.d(TAG, "app is running in backgroud");
                        sendNotification(message, false);
                    } else {
                        sendNotification(message, true);

                    }
                    
                    viberateAndPlayTone(message);
                }
            }
        };
    }
    
    /**
     * get demo HX SDK Model
     */
    public KickforHXSDKModel getModel(){
        return (KickforHXSDKModel) hxModel;
    }
    
    /**
     * 閼惧嘲褰囬崘鍛摠娑擃厼銈介崣濯er list
     *
     * @return
     */
    public Map<String, User> getContactList() {
        if (getHXId() != null && contactList == null) {
            contactList = ((KickforHXSDKModel) getModel()).getContactList();
        }
        
        return contactList;
    }

    /**
     * 鐠佸墽鐤嗘總钘夊几user list閸掓澘鍞达拷?锟芥ü锟�?
     *
     * @param contactList
     */
    public void setContactList(Map<String, User> contactList) {
        this.contactList = contactList;
    }
    
    @Override
    public void logout(final EMCallBack callback){
        endCall();
        super.logout(new EMCallBack(){

            @Override
            public void onSuccess() {
                // TODO Auto-generated method stub
                setContactList(null);
                getModel().closeDB();
                if(callback != null){
                    callback.onSuccess();
                }
            }

            @Override
            public void onError(int code, String message) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void onProgress(int progress, String status) {
                // TODO Auto-generated method stub
                if(callback != null){
                    callback.onProgress(progress, status);
                }
            }
            
        });
    }   
    
    void endCall(){
        try {
            EMChatManager.getInstance().endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
