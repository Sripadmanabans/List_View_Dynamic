package com.example.sripadmanaban.listviewdynamic;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Update List Fragment
 * Created by Sripadmanaban on 12/17/2014.
 */
public class UpdateFragment extends Fragment
{
    private static final String ORDERBY_TYPE = "ORDERING_TYPE";

    private List<Games> listOfValues = new ArrayList<>();

    private ListView listView;

    private int rowId = -1;

    private UpdateFragmentCallback mCallback;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            mCallback = (UpdateFragmentCallback) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException("The activity should implement UpdateFragmentCallback");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_update, container, false);

        Button button = (Button) view.findViewById(R.id.button_update);

        GamesDatabaseAdapter db = GamesDatabaseAdapter.getInstance(getActivity().getApplicationContext());
        ArrayAdapter<Games> adapter;

        boolean orderAsc = true;

        Bundle bundle = getArguments();
        if(bundle != null)
        {
            orderAsc = bundle.getBoolean(ORDERBY_TYPE);
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
                    Log.d("Bug", ""+ games.getGameId());
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

        listView = (ListView) view.findViewById(R.id.update_list);

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, listOfValues);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                rowId = listOfValues.get(position).getGameId();
                Log.d("BugClick", "" + rowId);
                if(listView.getCheckedItemCount() == 0)
                {
                    rowId = -1;
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(rowId == -1)
                {
                    Toast.makeText(getActivity(), "Please select a record to update", Toast.LENGTH_LONG).show();
                }
                else
                {
                    timeToUpdate(rowId);
                }
            }
        });

        return view;
    }

    private void timeToUpdate(int row)
    {
        if(mCallback != null)
        {
            mCallback.onUpdateButtonListener(row);
        }
    }

    public interface UpdateFragmentCallback
    {
        public void onUpdateButtonListener(int row);
    }
}
