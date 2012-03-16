
package ua.dod.picload.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserAccess {
	
	private String id;
	private int count = 0;
	Date date = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmm");
	UsersStatistics usersStatistics = new UsersStatistics();
	
	public UserAccess(String id){
		setId(id);
	}
	
	private Long time = Long.parseLong(sdf.format(date));
	
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public Long getTime() {
		
		return time;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
}
