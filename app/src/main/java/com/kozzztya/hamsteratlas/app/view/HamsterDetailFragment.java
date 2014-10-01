package com.kozzztya.hamsteratlas.app.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kozzztya.hamsteratlas.app.R;
import com.kozzztya.hamsteratlas.app.model.Hamster;
import com.squareup.picasso.Picasso;

public class HamsterDetailFragment extends Fragment {

    private static final String TAG = "my" + HamsterDetailFragment.class.getSimpleName();

    public static final String KEY_HAMSTER = "hamster";

    private Hamster mHamster;

    public static Fragment newInstance(Hamster hamster) {
        HamsterDetailFragment fragment = new HamsterDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_HAMSTER, hamster);
        fragment.setArguments(bundle);

        return fragment;
    }

    public HamsterDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            retrieveData(savedInstanceState);
        } else {
            retrieveData(getArguments());
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hamster_detail, container, false);
        bindData(view);
        return view;
    }

    /**
     * Bind data to Views
     *
     * @param view View of the Fragment's layout
     */
    private void bindData(View view) {
        getActivity().setTitle(mHamster.getTitle());

        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        TextView textViewDescription = (TextView) view.findViewById(R.id.description);

        Picasso.with(getActivity()).load(mHamster.getImage())
                .placeholder(R.drawable.ic_unknown_hamster)
                .error(R.drawable.ic_unknown_hamster)
                .into(imageView);

        textViewDescription.setText(mHamster.getDescription());
        textViewDescription.setMovementMethod(new ScrollingMovementMethod());
    }

    /**
     * Retrieve data from savedInstanceState or arguments
     */
    private void retrieveData(Bundle bundle) {
        if (bundle != null) {
            mHamster = bundle.getParcelable(KEY_HAMSTER);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_HAMSTER, mHamster);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.hamster_detail, menu);

        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(
                menu.findItem(R.id.action_share));
        shareActionProvider.setShareIntent(getDefaultShareIntent());
    }

    /**
     * Returns a share intent
     */
    private Intent getDefaultShareIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, mHamster.getTitle());
        intent.putExtra(Intent.EXTRA_TEXT, mHamster.getImage());
        return intent;
    }
}
