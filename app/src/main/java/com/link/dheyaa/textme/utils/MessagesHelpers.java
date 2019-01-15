/* TextMe Team
 * Jan 2019
 * MyFirebaseInstanceIDService class:
 * this class maintain how the token for each device is loaded and checked
 */
package com.link.dheyaa.textme.utils;


public class MessagesHelpers {

    /**
     * this method is taking to user ids and return them in acceding order so it gives us the room id
     * @param a : first user id
     * @param b : second user id
     * @return : return the room id
     */
    public static String getRoomId(String a , String b){
        /* checking whether the a is before b in alphabetic order */
        if(!Sorting.checkAlphabetOrder(a,b,true)){
            return a+b;
        }else{
            return b+a;
        }
    }

}
