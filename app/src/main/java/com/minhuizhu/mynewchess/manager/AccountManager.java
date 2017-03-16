package com.minhuizhu.mynewchess.manager;

/**
 * Created by zhuminh on 2017/3/16.
 */

public class AccountManager {
    public static AccountManager getInstance(){
        return inner.instance;
    }

    private static class inner{
       static AccountManager instance=new AccountManager();
    }






    private class UserBaseData{
        String uin;
        String account;
        String loginSession;
        String salt;
        String nickName;
        String level;
        String score;
    }
}
