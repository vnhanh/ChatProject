package vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.chatproject.R;
import vn.edu.hcmute.ms14110050.chatproject.base.recyclerview.OnClickItemVHListener;
import vn.edu.hcmute.ms14110050.chatproject.databinding.ItemChatterMyFriendBinding;
import vn.edu.hcmute.ms14110050.chatproject.databinding.ItemChatterNotFriendBinding;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.chatter.Chatter;
import vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home.adapter.viewholder.ChatItemVH;
import vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home.adapter.viewholder.FriendChatItemVH;
import vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home.adapter.viewholder.NotFriendChatterItemVH;


/**
 * Created by Vo Ngoc Hanh on 5/14/2018.
 */

public class ChatItemAdapter extends RecyclerView.Adapter<ChatItemVH> {
    private final int VIEW_TYPE_FRIEND = 0;
    private final int VIEW_TYPE_NOT_FRIEND = 1;

    private OnClickItemVHListener<Chatter> listener;

    private ArrayList<Chatter> list = new ArrayList<>();
    // tạm thời chưa dùng
    private ArrayList<ChatItemVH> viewholders = new ArrayList<>();

    public ChatItemAdapter(OnClickItemVHListener<Chatter> listener) {
        this.listener = listener;
    }

    public void setData(ArrayList<Chatter> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).isFriend())
            return VIEW_TYPE_FRIEND;
        else
            return VIEW_TYPE_NOT_FRIEND;
    }

    @NonNull
    @Override
    public ChatItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_FRIEND) {
            FriendChatItemVH vh = createFriendViewHolder(inflater, parent);
            viewholders.add(vh);
            return vh;
        }else if(viewType == VIEW_TYPE_NOT_FRIEND){
            NotFriendChatterItemVH vh = createNotFriendViewHolder(inflater, parent);
            viewholders.add(vh);
            return vh;
        }else{
            return null;
        }
    }

    private NotFriendChatterItemVH createNotFriendViewHolder(LayoutInflater inflater, ViewGroup parent) {
        ItemChatterNotFriendBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.item_chatter_not_friend, parent, false);
        NotFriendChatterItemVH vh = new NotFriendChatterItemVH(binding);
        vh.setListener(listener);
        return vh;
    }

    private FriendChatItemVH createFriendViewHolder(LayoutInflater inflater, ViewGroup parent) {
        ItemChatterMyFriendBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.item_chatter_my_friend, parent, false);
        FriendChatItemVH vh = new FriendChatItemVH(binding);
        vh.setListener(listener);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatItemVH holder, int position) {
        if (holder instanceof FriendChatItemVH) {
            FriendChatItemVH viewholder = (FriendChatItemVH) holder;
            viewholder.bind(list.get(position));
        } else if (holder instanceof NotFriendChatterItemVH) {
            NotFriendChatterItemVH viewholder = (NotFriendChatterItemVH) holder;
            viewholder.bind(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }
}
