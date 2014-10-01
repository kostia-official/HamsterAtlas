package com.kozzztya.hamsteratlas.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.kozzztya.hamsteratlas.app.R;
import com.kozzztya.hamsteratlas.app.model.Hamster;

public class HamsterListActivity extends ActionBarActivity implements
        HamsterListFragment.HamsterListCallbacks {

    private static final String TAG = "my" + HamsterListActivity.class.getSimpleName();

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hamster_list);

        // The detail container view will be present only in the
        // large-screen layouts (res/values-large and
        // res/values-sw600dp). If this view is present, then the
        // activity should be in two-pane mode.
        if (findViewById(R.id.hamster_detail_container) != null) {
            mTwoPane = true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onHamsterSelected(Hamster hamster) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Fragment hamsterDetailFragment = HamsterDetailFragment.newInstance(hamster);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.hamster_detail_container, hamsterDetailFragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent intent = new Intent(this, HamsterDetailActivity.class);
            intent.putExtra(HamsterDetailFragment.KEY_HAMSTER, hamster);
            startActivity(intent);
        }
    }
}