package vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.chatproject.R;
import vn.edu.hcmute.ms14110050.chatproject.base.fragment.BaseFragment;
import vn.edu.hcmute.ms14110050.chatproject.base.recyclerview.OnClickItemVHListener;
import vn.edu.hcmute.ms14110050.chatproject.databinding.FragmentHomeBinding;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.chatter.Chatter;
import vn.edu.hcmute.ms14110050.chatproject.module.chat.ChatActivity;
import vn.edu.hcmute.ms14110050.chatproject.module.main_screen.activity.MainActivity;
import vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home.adapter.ChatItemAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeContract.View, HomeViewModel>
        implements HomeContract.View, SearchView.OnQueryTextListener {

    ChatItemAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new HomeViewModel();
        View view = setAndBindContentView(inflater, container, R.layout.fragment_home, this);
        binding.setViewmodel(viewModel);

        // gán listener lắng nghe thay đổi thông tin người dùng trên Firebase
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.setOnUpdateUserListener(viewModel);
        }

        setHasOptionsMenu(true);

        initRecyclerView();

        return view;
    }

    private void initRecyclerView() {
        createAdapter();

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerview.setLayoutManager(manager);
        binding.recyclerview.setHasFixedSize(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_search, menu);
        final MenuItem searchMenuItem = menu.findItem(R.id.search_view);
        final SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(this);

        // nút close (X) của SearchView
        ImageView closeButton = searchView.findViewById(R.id.search_close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // khóa search
                // không search khi tắt SearchView
                viewModel.blockSearch();

                EditText editText = searchView.findViewById(R.id.search_src_text);
                editText.setText("");
                searchView.setQuery("", false);
                searchView.onActionViewCollapsed();
                searchMenuItem.collapseActionView();

                // gỡ khóa search
                viewModel.unblockSearch();
            }
        });
    }

    /**
     * IMPLEMENT SEARCHVIEW
     */

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(getContext(), "Query: " + query, Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (viewModel != null) {
            viewModel.search(newText);
        }
        return false;
    }

    /**
     * IMPLEMENT HOMECONTRACT.VIEW
     * */
    @Override
    public void onLoadChatters(ArrayList<Chatter> list) {
        if (adapter == null) {
            createAdapter();
        }
        adapter.setData(list);
    }

    private void createAdapter() {
        OnClickItemVHListener<Chatter> listener = new OnClickItemVHListener<Chatter>() {
            @Override
            public void onClick(Chatter chatter) {
                ChatActivity.startActivity(getActivity(), chatter.getUid());
            }
        };
        adapter = new ChatItemAdapter(listener);

        binding.recyclerview.setAdapter(adapter);
    }
}
