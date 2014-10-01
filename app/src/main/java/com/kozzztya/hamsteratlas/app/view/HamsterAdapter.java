package com.kozzztya.hamsteratlas.app.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kozzztya.hamsteratlas.app.R;
import com.kozzztya.hamsteratlas.app.model.Hamster;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HamsterAdapter extends ArrayAdapter<Hamster> {

    private int mResource;
    private LayoutInflater mInflater;

    public HamsterAdapter(Context context, int resource, List<Hamster> objects) {
        super(context, resource, objects);
        mResource = resource;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        View view = convertView;

        if (view == null) {
            viewHolder = new ViewHolder();
            view = mInflater.inflate(mResource, parent, false);

            viewHolder.textViewTitle = (TextView) view.findViewById(R.id.title);
            viewHolder.textViewDescription = (TextView) view.findViewById(R.id.description);
            viewHolder.imageViewHamster = (ImageView) view.findViewById(R.id.image);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Hamster hamster = getItem(position);

        Picasso.with(getContext())
                .load(hamster.getImage())
                .resize(viewHolder.imageViewHamster.getLayoutParams().width,
                        viewHolder.imageViewHamster.getLayoutParams().height)
                .centerCrop()
                .placeholder(R.drawable.ic_unknown_hamster)
                .error(R.drawable.ic_unknown_hamster)
                .into(viewHolder.imageViewHamster);

        viewHolder.textViewTitle.setText(hamster.getTitle());
        viewHolder.textViewDescription.setText(hamster.getDescription());

        return view;
    }

    public class ViewHolder {
        TextView textViewTitle;
        TextView textViewDescription;
        ImageView imageViewHamster;
    }
}
