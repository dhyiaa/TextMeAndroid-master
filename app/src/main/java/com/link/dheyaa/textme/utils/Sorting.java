
/* TextMe Team
 * Jan 2019
 * Sorting methods class:
 * containing methods of bubble sort and quick sort to rearrange 
 */

package com.link.dheyaa.textme.utils;

import com.link.dheyaa.textme.models.User;

import java.util.ArrayList;

public class Sorting {

    /**
     * default constructor
     */
    public Sorting() {

    }

    /**
     * Sort the ArrayList of Users according to alphabet with bubble sorting
     * @param friendList = an ArrayList of Users
     * @param order = a boolean value to indicate the sorting order, true = ascending, false = descending
     */
    public static void bubbleSortByAlphabet(ArrayList<User> friendList,boolean order) {

        for (int i = 0; i < friendList.size(); i++) {
            for (int j = 0; j < friendList.size() - i - 1; j++) {
                String username1 = friendList.get(j).getUsername();
                String username2 = friendList.get(j + 1).getUsername();

                if (checkAlphabetOrder(username1, username2,order)) {
                    User user1 = friendList.get(j);
                    User user2 = friendList.get(j + 1);
                    friendList.remove(j);
                    friendList.add(j, user2);
                    friendList.remove(j + 1);
                    friendList.add(j + 1, user1);
                }
            }
        }
    }

    /**
     * call the method to sort the ArrayList of Users according to alphabet with quick sorting
     * @param friendList = an ArrayList of Users
     * @param order = a boolean value to indicate the sorting order, true = ascending, false = descending
     */
    public static void quickSortByAlphabet(ArrayList<User> friendList,boolean order) {
        quickSort(friendList, 0, friendList.size() - 1,order);
    }

    /**
     * Sort the ArrayList of Users according to alphabet with quick sorting
     * @param friendList = an ArrayList of Users
     * @param begin = the beginning index
     * @param end = the ending index
     * @param order = a boolean value to indicate the sorting order, true = ascending, false = descending
     */
    private static void quickSort(ArrayList<User> friendList, int begin, int end,boolean order) {

        if (begin < end) {
            String comparingName = friendList.get(end).getUsername();
            int wall = begin - 1;
            for (int i = begin; i < end; i++) {
                if (checkAlphabetOrder(comparingName, friendList.get(i).getUsername(),order)) {
                    User store = friendList.get(i);
                    friendList.remove(i);
                    friendList.add(i, friendList.get(wall + 1));
                    friendList.remove(wall + 1);
                    friendList.add(wall + 1, store);
                    wall++;
                }
            }
            User store = friendList.get(end);
            friendList.add(end, friendList.get(wall + 1));
            friendList.remove(end + 1);
            friendList.remove(wall + 1);
            friendList.add(wall + 1, store);

            quickSort(friendList, begin, wall,order);
            quickSort(friendList, wall + 2, end,order);
        }
    }

    /**
     * check the alphabet order of the Strings
     * @param username1 = String containing the first Username
     * @param username2 = String containing the second Username
     * @param order = a boolean value to indicate the sorting order, true = ascending, false = descending
     * @return true changes in order need to be made, false if the user names follow the @param order
     */
    public static boolean checkAlphabetOrder(String username1, String username2, boolean order) {

        boolean change =!order;
        boolean same = true;
        int minLength = Math.min(username1.trim().length(), username2.trim().length());
        for (int o = 0; o < minLength; o++) {
            char charOfUser1 = username1.toLowerCase().trim().charAt(o);
            char charOfUser2 = username2.toLowerCase().trim().charAt(o);
            if ((charOfUser1 > 122 || charOfUser1 < 97) && (charOfUser2 > 122 || charOfUser2 < 97)) {
                if (charOfUser1 > charOfUser2) {
                    same = false;
                    change = order;
                    o = minLength;
                } else if (charOfUser1 < charOfUser2) {
                    same = false;
                    o = minLength;
                }
            } else if (charOfUser1 > 122 || charOfUser1 < 97) {
                same = false;
                change = order;
                o = minLength;
            } else if (charOfUser2 > 122 || charOfUser2 < 97) {
                same = false;
                o = minLength;
            } else if (charOfUser1 > charOfUser2) {
                same = false;
                change = order;
                o = minLength;
            } else if (charOfUser1 < charOfUser2) {
                same = false;
                o = minLength;
            }

        }
        if (same) {
            for (int o = 0; o < minLength; o++) {
                char x = username1.charAt(o);
                char y = username2.charAt(o);
                if (x > y) {
                    same = false;
                    o = minLength;
                } else if (x < y) {
                    same = false;
                    change = order;
                    o = minLength;
                }
            }
        }
        if (same && username1.length() > username2.length()) {
            change = order;
        }
        return change;
    }
}
