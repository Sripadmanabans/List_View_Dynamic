package com.example.sripadmanaban.listviewdynamic;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the fragment that displays the list of elements
 * Created by Sripadmanaban on 12/11/2014.
 */
public class DisplayListFragment extends Fragment
{
    private static final String ORDERBY_TYPE = "ORDERING_TYPE";
    private static final String LIST_VIEW_LINES = "LIST_VIEW_LINES";

    private DisplayListFragmentCallback mCallback;

    private List<Games> listOfValues = new ArrayList<>();

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            mCallback = (DisplayListFragmentCallback) activity;
        }
        catch(ClassCastException e)
        {
            throw new ClassCastException("The class has to implement DisplayFragmentCallback");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_display_list, container, false);

        Button button = (Button) view.findViewById(R.id.button_fab);

        GamesDatabaseAdapter db = GamesDatabaseAdapter.getInstance(getActivity().getApplicationContext());
        ArrayAdapter<Games> adapter;

        boolean orderAsc = true;

        int adapterChoice = 1;

        Bundle bundle = getArguments();
        if(bundle != null)
        {
            orderAsc = bundle.getBoolean(ORDERBY_TYPE);
            adapterChoice = bundle.getInt(LIST_VIEW_LINES);
        }

        try
        {
            Games games;
            Cursor cursor;
            db.open();
            if(orderAsc)
            {
               cursor = db.getAllGames(" ASC");
            }
            else
            {
                cursor = db.getAllGames(" DESC");
            }
            if(cursor.moveToFirst())
            {
                do
                {
                    games = new Games();
                    games.setGameId(cursor.getInt(0));
                    games.setGameName(cursor.getString(1));
                    games.setDeveloperName(cursor.getString(2));
                    listOfValues.add(games);

                } while (cursor.moveToNext());
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.close();
        }

        ListView listView = (ListView) view.findViewById(R.id.display_list);

        if(adapterChoice == 1)
        {
            adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listOfValues);
        }
        else
        {
            adapter = new GamesListAdapter(getActivity(), listOfValues);
        }
        listView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openCreateFragment();
            }
        });

        return view;
    }

    private void openCreateFragment()
    {
        if(mCallback != null)
        {
            mCallback.onButtonClickCallCreateFragment();
        }
    }

    public static interface DisplayListFragmentCallback
    {
        public void onButtonClickCallCreateFragment();
    }
}
