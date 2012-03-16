package ua.dod.picload.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {
	
	public String format() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yy_HH_mm_ss_SSS"); 
		return sdf.format(date);
	}
}
