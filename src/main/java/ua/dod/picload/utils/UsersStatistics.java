package ua.dod.picload.utils;

import java.util.HashMap;
import java.util.Map;

import ua.dod.picload.utils.UserAccess;

public class UsersStatistics {
    
    private volatile static UsersStatistics usersStatistics;
   
    public static UsersStatistics getInstance() {
          if (usersStatistics == null) {
                        synchronized (UsersStatistics.class) {
                               if (usersStatistics == null) {
                            	   usersStatistics = new UsersStatistics();
                               }
                        }
          }
          return usersStatistics;
    }
    
    public Map<String, UserAccess> usersAccess = new HashMap<String, UserAccess>();
}