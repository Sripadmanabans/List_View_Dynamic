package com.example.sripadmanaban.listviewdynamic;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Custom Adapter for the data
 * Created by Sripadmanaban on 12/15/2014.
 */
public class GamesListAdapter extends ArrayAdapter<Games>
{
    private final Activity context;
    private final List<Games> gamesList;

    public GamesListAdapter(Activity context, List<Games> list)
    {
        super(context, R.layout.list_item, list);
        this.context = context;
        this.gamesList = list;
    }

    private static class ViewHolder
    {
        public TextView gameName, developerName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View rowView = convertView;

        if(rowView == null)
        {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.gameName = (TextView) rowView.findViewById(R.id.game_location);
            viewHolder.developerName = (TextView) rowView.findViewById(R.id.developer_location);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();

        Games games = gamesList.get(position);

        holder.gameName.setText(games.getGameName());
        holder.developerName.setText(games.getDeveloperName());

        return rowView;
    }
}
