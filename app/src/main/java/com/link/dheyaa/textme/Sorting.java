package com.link.dheyaa.textme;

import java.util.ArrayList;

public class Sorting {

    public Sorting() {

    }

    public static void bubbleSortByAlphabet(ArrayList<User> friendList) {

        for (int i = 0; i < friendList.size(); i++) {
            for (int j = 0; j < friendList.size() - i - 1; j++) {
                String username1 = friendList.get(j).getUsername();
                String username2 = friendList.get(j + 1).getUsername();

                if (checkAlphabetOrder(username1, username2)) {
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

    public static void quickSortByAlphabet(ArrayList<User> friendList) {
        quickSort(friendList, 0, friendList.size() - 1);
    }

    private static void quickSort(ArrayList<User> friendList, int begin, int end) {

        if (begin < end) {
            String comparingName = friendList.get(end).getUsername();
            int wall = begin - 1;
            for (int i = begin; i < end; i++) {
                if (checkAlphabetOrder(comparingName, friendList.get(i).getUsername())) {
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

            quickSort(friendList, begin, wall);
            quickSort(friendList, wall + 2, end);
        }
    }

    private static boolean checkAlphabetOrder(String username1, String username2) {

        boolean change = false;
        boolean same = true;
        int minLength = Math.min(username1.length(), username2.length());
        for (int o = 0; o < minLength; o++) {
            char charOfUser1 = username1.toLowerCase().trim().charAt(o);
            char charOfUser2 = username2.toLowerCase().trim().charAt(o);
            if ((charOfUser1 > 122 || charOfUser1 < 97) && (charOfUser2 > 122 || charOfUser2 < 97)) {
                if (charOfUser1 > charOfUser2) {
                    same = false;
                    change = true;
                    o = minLength;
                } else if (charOfUser1 < charOfUser2) {
                    same = false;
                    o = minLength;
                }
            } else if (charOfUser1 > 122 || charOfUser1 < 97) {
                same = false;
                change = true;
                o = minLength;
            } else if (charOfUser2 > 122 || charOfUser2 < 97) {
                same = false;
                o = minLength;
            } else if (charOfUser1 > charOfUser2) {
                same = false;
                change = true;
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
                    change = true;
                    o = minLength;
                }
            }
        }
        if (same && username1.length() > username2.length()) {
            change = true;
        }
        return change;
    }
}
