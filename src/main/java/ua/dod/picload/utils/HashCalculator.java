package ua.dod.picload.utils;

import java.io.*;
import java.math.BigInteger;
import java.security.*;

public class HashCalculator {
	public String calculateSHA(InputStream inputStream){
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			DigestInputStream dis = new DigestInputStream(inputStream, md);
			BufferedInputStream bis = new BufferedInputStream(dis);
			
			while (true) {
				int b = bis.read();
				if (b == -1)
					break;
			}
			
			BigInteger bi = new BigInteger(md.digest());
			
			return bi.toString();
		} catch (Exception e) {e.printStackTrace();}
		
		return "";
	}
}
