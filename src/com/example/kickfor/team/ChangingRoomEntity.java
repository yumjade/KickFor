package com.example.kickfor.team;

public class ChangingRoomEntity {

	private String phone = null;
	private String number = null;
	private String name = null;
	private String teamid = null;
	private String authority = "0";
	private boolean isChanged = false;

	public boolean pb1 = false;
	public boolean pb2 = false;

	public ChangingRoomEntity(String phone, String number, String name,
			String teamid) {
		this.phone = phone;
		this.number = number;
		this.name = name;
		this.teamid = teamid;
	}

	public ChangingRoomEntity(String teamid) {
		this.phone = "";
		this.name = "";
		this.number = "";
		this.teamid = teamid;
	}

	public void changedData() {
		isChanged = true;
	}

	public boolean isChangedData() {
		return isChanged;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getPhone() {
		return phone;
	}

	public String getNumber() {
		return number;
	}

	public String getName() {
		return name;
	}

	public String getTeamid() {
		return teamid;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getAuthority() {
		return authority;
	}

}
