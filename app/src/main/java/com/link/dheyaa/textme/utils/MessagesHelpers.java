package com.link.dheyaa.textme.utils;

public class MessagesHelpers {

    public static String getRoomId(String a , String b){
        if(!Sorting.checkAlphabetOrder(a,b,true)){
            return a+b;
        }else{
            return b+a;
        }
    }

}
