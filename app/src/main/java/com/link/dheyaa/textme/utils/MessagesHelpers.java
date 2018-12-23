package com.link.dheyaa.textme.utils;

public class MessagesHelpers {

    public static String getRoomId(String a , String b){
        if(a.compareToIgnoreCase(b) < 0 ){
            return  a+b;
        }else if (a.compareToIgnoreCase(b) > 0 ){
            return  b+a;
        }
        return a+b;
    }

}
