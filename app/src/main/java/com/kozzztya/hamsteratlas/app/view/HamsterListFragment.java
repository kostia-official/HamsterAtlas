package com.kozzztya.hamsteratlas.app.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.kozzztya.hamsteratlas.app.Constants;
import com.kozzztya.hamsteratlas.app.R;
import com.kozzztya.hamsteratlas.app.model.Hamster;
import com.kozzztya.hamsteratlas.app.model.HamsterContent;
import com.kozzztya.hamsteratlas.app.service.RefreshService;

public class HamsterListFragment extends ListFragment {

    private static final String TAG = "my" + HamsterListFragment.class.getSimpleName();

    private MenuItem mRefreshMenuItem;
    private HamsterListCallbacks mCallbacks;

    public HamsterListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        HamsterAdapter hamsterAdapter = new HamsterAdapter(getActivity(),
                R.layout.list_item_hamster, HamsterContent.ITEMS);
        setListAdapter(hamsterAdapter);

        // Receive download actions to show/hide progressbar
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_REFRESH);
        intentFilter.addAction(Constants.ACTION_FETCH);
        getActivity().registerReceiver(mReceiver, intentFilter);

        // Start refresh of hamster data
        getActivity().startService(new Intent(getActivity(), RefreshService.class));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getListAdapter().isEmpty()) setListShown(false);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        mCallbacks.onHamsterSelected(HamsterContent.ITEMS.get(position));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.hamster_list, menu);
        mRefreshMenuItem = menu.findItem(R.id.action_refresh);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            getActivity().sendBroadcast(new Intent(Constants.ACTION_REFRESH));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        getActivity().stopService(new Intent(getActivity(), RefreshService.class));
        getActivity().unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.v(TAG, action);

            if (action.equals(Constants.ACTION_REFRESH)) {
                // Downloading started
                // Show Progressbar on MenuItem
                if (mRefreshMenuItem != null) {
                    MenuItemCompat.setActionView(mRefreshMenuItem, R.layout.menu_item_progressbar);
                    MenuItemCompat.expandActionView(mRefreshMenuItem);
                }

            } else if (action.equals(Constants.ACTION_FETCH)) {
                ((BaseAdapter) getListAdapter()).notifyDataSetChanged();

                if (isVisible()) setListShown(true);
                if (mRefreshMenuItem != null) {
                    MenuItemCompat.setActionView(mRefreshMenuItem, null);
                    MenuItemCompat.collapseActionView(mRefreshMenuItem);
                }
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (HamsterListCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement "
                    + HamsterListCallbacks.class.getSimpleName());
        }
    }

    public interface HamsterListCallbacks {
        public void onHamsterSelected(Hamster hamster);
    }
}
