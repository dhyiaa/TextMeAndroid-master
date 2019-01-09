
/* TextMe Team
 * Jan 2019
 * Friend adapter:
 * containing the attributes and functions of User
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
    //attributes of FriendAdapters
    public ArrayList<User> friends;
    private Context context;
    private int itemResource;

    /**
     * default constructor
     * @param context = Context value of the activities
     * @param itemResource = int value of the layouts
     * @param friends = User ArrayList of the Friends
     */
    public FriendAdapter(Context context, int itemResource, ArrayList<User> friends) {
        //Initialize our adapter
        this.friends = friends;
        this.context = context;
        this.itemResource = itemResource;
    }

    /**
     * Create a new FriendsViewHolder
     * @param parent
     * @param viewType = int value of the view type
     * @return a new FriendsViewHolder with
     */
    @Override//Override the onCreateViewHolder method
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // 3. Inflate the view and return the new ViewHolder
        View view = LayoutInflater.from (parent.getContext ())
                .inflate (R.layout.friends_list_item, parent, false);
        return new FriendsViewHolder (this.context, view);
    }

    // 4. Override the onBindViewHolder method
    @Override
    public void onBindViewHolder(FriendsViewHolder holder, int position) {

        // 5. Use position to access the correct Bakery object
        User friend = this.friends.get (position);
        if (friend != null) {
            holder.bindFriend (friend);

        }
        // 6. Bind the bakery object to the holder
    }

    public void addFriend(User friend, boolean sortingAccending) {
        removeOld (friend);
        friends.add (friend);
        if (sortingAccending) {
            Sorting.quickSortByAlphabet (friends, true);
        } else {
        }
        notifyItemInserted (friends.size () - 1);
    }

    public void removeOld(User friend) {
        for (int i = 0; i < friends.size (); i++) {
            if (friends.get (i).getId ().equals (friend.getId ())) {
                friends.remove (i);
            }
        }
    }

    public void removeOldbyID(String id) {
        for (int i = 0; i < friends.size (); i++) {
            if (friends.get (i).getId ().equals (id)) {
                friends.remove (i);
            }
        }
        this.notifyDataSetChanged ();

    }


    public void setFreinds(ArrayList<User> friends) {
        this.friends = friends;
    }

    @Override
    public int getItemCount() {
        return this.friends.size ();
    }

    public void sortFirends(boolean accending) {
        System.out.println ("sorting ->> " + accending);
        Sorting.quickSortByAlphabet (this.friends, accending);
        this.notifyDataSetChanged ();

    }
}

