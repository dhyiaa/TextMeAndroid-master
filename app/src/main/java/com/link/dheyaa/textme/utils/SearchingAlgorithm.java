package com.link.dheyaa.textme.utils;

import com.link.dheyaa.textme.models.User;

import java.util.ArrayList;

/**
 *
 * @author zhuxiaoyu
 */
public class SearchingAlgorithm {

    /**
     * @param args the command line arguments
     */
    static ArrayList <User> data=new ArrayList();

    public SearchingAlgorithm(){

    }

    public static ArrayList<User> search(ArrayList<User> users,String input){
        ArrayList <User> relatedResult=new ArrayList();
        ArrayList <User> result=new ArrayList();
        String key=input.toLowerCase().trim();

        for(int i=0;i<users.size();i++){
            String username=users.get(i).getUsername().toLowerCase().trim();
            String email=users.get(i).getEmail().toLowerCase().trim();

            if(key.equals(username)){
                result.add(users.get(i));
            }
            else if(key.equals(email)){
                result.add(users.get(i));
            }
            else if(username.startsWith(key)){
                relatedResult.add(users.get(i));
            }
            else if(email.startsWith(key)){
                relatedResult.add(users.get(i));
            }
        }
        Sorting.quickSortByAlphabet(result);
        Sorting.quickSortByAlphabet(relatedResult);

        for(int i=0;i<result.size();i++){
            relatedResult.add(i,result.get(i));
        }

        return relatedResult;
    }

}
