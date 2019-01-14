
/* TextMe Team
 * Jan 2019
 * Friend adapter:
 * controls the friends' listing and layout
 */

package com.link.dheyaa.textme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.link.dheyaa.textme.R;
import com.link.dheyaa.textme.models.User;
import com.link.dheyaa.textme.utils.Sorting;
import com.link.dheyaa.textme.viewHolders.FriendsViewHolder;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class FriendAdapter extends RecyclerView.Adapter<FriendsViewHolder> {
    //attributes of FriendAdapter
    public ArrayList<User> friends;
    private Context context;
    private int itemResource;

    /**
     * default constructor
     * @param context = Context value of the activities
     * @param itemResource = int value of the layouts
     * @param friends = ArrayList<User> of friends
     */
    public FriendAdapter(Context context, int itemResource, ArrayList<User> friends) {
        //Initialize our adapter
        this.friends = friends;
        this.context = context;
        this.itemResource = itemResource;
    }

    /**
     * Create a new FriendsViewHolder
     * Override the onCreateViewHolder method
     * @param parent = ViewGroup of parent Views
     * @param viewType = int value of the view type
     * @return a new FriendsViewHolder based on the FriendAdapter
     */
    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Inflate the view and return the new ViewHolder
        View view = LayoutInflater.from (parent.getContext ())
                .inflate (R.layout.friends_list_item, parent, false);
        return new FriendsViewHolder (this.context, view);
    }

    /**
     * bind the position-th friend to the FriendViewHolder
     * Override the onBindViewHolder method
     * @param holder = FriendsViewHolder to bind the friend
     * @param position = int value of the position index
     */
    @Override
    public void onBindViewHolder(FriendsViewHolder holder, int position) {

        //Use position to access the correct friend
        User friend = this.friends.get (position);
        if (friend != null) {
            holder.bindFriend (friend);
            //Bind the friend to the holder
        }
    }

    /**
     * add a new friend to the the ArrayList<User> friends
     * @param friend = User to be added as a new friend
     * @param sortingAscending = a boolean value to indicate the sorting order, true = ascending, false = descending
     */
    public void addFriend(User friend, boolean sortingAscending) {
        removeOld (friend);
        friends.add(friend);
        if (sortingAscending) {
            Sorting.quickSortByAlphabet (friends, true);
        } else {
        }
        notifyItemInserted (friends.size () - 1);
    }

    /**
     * remove the User friend from the ArrayList<User> friends
     * @param friend = User to be removed from friends
     */
    public void removeOld(User friend) {
        for (int i = 0; i < friends.size (); i++) {
            if (friends.get (i).getId ().equals (friend.getId ())) {
                friends.remove (i);
            }
        }
    }

    /**
     * remove the User friend with UserId id from the ArrayList<User> friends
     * @param id = String value of the User to be removed from friends
     */
    public void removeOldByID(String id) {
        for (int i = 0; i < friends.size (); i++) {
            if (friends.get (i).getId ().equals (id)) {
                friends.remove (i);
            }
        }
        this.notifyDataSetChanged ();

    }

    /**
     * set list of friends
     * @param friends = ArrayList<User> of friends
     */
    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }

    /**
     * get the number of friends
     * @return the size of ArrayList<User> friends
     */
    @Override
    public int getItemCount() {
        return this.friends.size ();
    }

    /**
     * sort the ArrayList<User> friends according to alphabet order
     * @param ascending = a boolean value to indicate the sorting order, true = ascending, false = descending
     */
    public void sortFriends(boolean ascending) {
        System.out.println ("sorting ->> " + ascending);
        Sorting.quickSortByAlphabet (this.friends, ascending);
        this.notifyDataSetChanged ();

    }
}

