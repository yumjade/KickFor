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

import java.util.Map;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.example.kickfor.utils.User;

public class KickforApplication extends Application {

	public static Context applicationContext;
	private static KickforApplication instance;
	// login user name
	public final String PREF_USERNAME = "username";
	
	public static String currentUserNick = "";
	public static KickforHXSDKHelper hxSDKHelper = new KickforHXSDKHelper();

	@Override
	public void onCreate() {
		super.onCreate();
		
        applicationContext = this;
        instance = this;

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush

        hxSDKHelper.onInit(applicationContext);
        
        
        
	}

	public static KickforApplication getInstance() {
		return instance;
	}
 
	public Map<String, User> getContactList() {
	    return hxSDKHelper.getContactList();
	}

	public void setContactList(Map<String, User> contactList) {
	    hxSDKHelper.setContactList(contactList);
	}

	public String getUserName() {
	    return hxSDKHelper.getHXId();
	}

	public String getPassword() {
		return hxSDKHelper.getPassword();
	}

	public void setUserName(String username) {
	    hxSDKHelper.setHXId(username);
	}

	public void setPassword(String pwd) {
	    hxSDKHelper.setPassword(pwd);
	}

	public void logout(final EMCallBack emCallBack) {
	    hxSDKHelper.logout(emCallBack);
	}
}
