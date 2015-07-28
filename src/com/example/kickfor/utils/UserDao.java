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

import java.util.List;
import java.util.Map;

import android.content.Context;


import com.example.kickfor.utils.User;

public class UserDao {
	public static final String TABLE_NAME = "uers";
	public static final String COLUMN_NAME_ID = "username";
	public static final String COLUMN_NAME_NICK = "nick";
	public static final String COLUMN_NAME_AVATAR = "avatar";
	
	public static final String PREF_TABLE_NAME = "pref";
	public static final String COLUMN_NAME_DISABLED_GROUPS = "disabled_groups";
	public static final String COLUMN_NAME_DISABLED_IDS = "disabled_ids";

	public UserDao(Context context) {
	    KickforDBManager.getInstance().onInit(context);
	}

	/**
	 * 淇濆瓨濂藉弸list
	 * 
	 * @param contactList
	 */
	public void saveContactList(List<User> contactList) {
	    KickforDBManager.getInstance().saveContactList(contactList);
	}

	/**
	 * 鑾峰彇濂藉弸list
	 * 
	 * @return
	 */
	public Map<String, User> getContactList() {
		
	    return KickforDBManager.getInstance().getContactList();
	}
	
	/**
	 * 鍒犻櫎涓�涓仈绯讳�?
	 * @param username
	 */
	public void deleteContact(String username){
	    KickforDBManager.getInstance().deleteContact(username);
	}
	
	/**
	 * 淇濆瓨涓�涓仈绯讳�?
	 * @param user
	 */
	public void saveContact(User user){
	    KickforDBManager.getInstance().saveContact(user);
	}
	
	public void setDisabledGroups(List<String> groups){
	    KickforDBManager.getInstance().setDisabledGroups(groups);
    }
    
    public List<String>  getDisabledGroups(){       
        return KickforDBManager.getInstance().getDisabledGroups();
    }
    
    public void setDisabledIds(List<String> ids){
        KickforDBManager.getInstance().setDisabledIds(ids);
    }
    
    public List<String> getDisabledIds(){
        return KickforDBManager.getInstance().getDisabledIds();
    }
}
