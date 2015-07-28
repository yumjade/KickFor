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

public class InviteMessage {
	private String from;
	//鏃堕�?
	private long time;
	//娣诲姞鐞嗙敱
	private String reason;
	
	//鏈獙璇侊紝宸插悓鎰忕瓑鐘舵��
	private InviteMesageStatus status;
	//缇d
	private String groupId;
	//缇ゅ悕绉�?
	private String groupName;
	

	private int id;
	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}


	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public InviteMesageStatus getStatus() {
		return status;
	}

	public void setStatus(InviteMesageStatus status) {
		this.status = status;
	}

	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}



	public enum InviteMesageStatus{
		/**琚個璇�?*/
		BEINVITEED,
		/**琚嫆缁�?*/
		BEREFUSED,
		/**瀵规柟鍚屾剰*/
		BEAGREED,
		/**瀵规柟鐢宠*/
		BEAPPLYED,
		/**鎴戝悓鎰忎簡瀵规柟鐨勮姹�*/
		AGREED,
		/**鎴戞嫆缁濅簡瀵规柟鐨勮姹�*/
		REFUSED
		
	}
	
}



