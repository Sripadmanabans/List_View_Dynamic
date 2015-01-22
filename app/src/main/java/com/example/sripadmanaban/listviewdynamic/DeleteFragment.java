package com.example.sripadmanaban.listviewdynamic;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Delete the entry from the list
 * Created by Sripadmanaban on 12/11/2014.
 */
public class DeleteFragment extends Fragment
{
    private static final String ORDERBY_TYPE = "ORDERING_TYPE";

    private List<Games> listOfValues = new ArrayList<>();

    private ListView listView;

    private GamesDatabaseAdapter db;

    private SparseBooleanArray booleanArray;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_delete, container, false);

        boolean orderAsc = true;

        Bundle bundle = getArguments();
        orderAsc = bundle.getBoolean(ORDERBY_TYPE);

        listView = (ListView) view.findViewById(R.id.delete_list);

        db = GamesDatabaseAdapter.getInstance(getActivity().getApplicationContext());

        Games games;
        Cursor cursor;
        try
        {
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
                    Log.d("Bug", "" + games.getGameId());
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

        final ArrayAdapter<Games> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_multiple_choice, listOfValues);
        listView.setAdapter(adapter);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        Button button = (Button) view.findViewById(R.id.button_delete);

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                booleanArray = new SparseBooleanArray();
                booleanArray = listView.getCheckedItemPositions();
                if(booleanArray.size() < 1)
                {
                    Toast.makeText(getActivity(), "Not Rows selected for Deletion", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    for (int i = 0; i < booleanArray.size(); i++)
                    {
                        int position = booleanArray.keyAt(i);
                        boolean valueAt = booleanArray.valueAt(i);
                        if (valueAt)
                        {
                            Games game = listOfValues.get(position);
                            int rowId = game.getGameId();
                            try
                            {
                                db.open();
                                db.deleteGame(rowId);
                            }
                            catch (SQLException e)
                            {
                                e.printStackTrace();
                            }
                            finally
                            {
                                db.close();
                            }
                        }
                    }

                    for(int j = 0; j < booleanArray.size(); j++)
                    {
                        int position = booleanArray.keyAt(j);
                        listOfValues.remove(position - j);
                        adapter.notifyDataSetChanged();
                    }

                }
            }
        });

        return view;
    }
}
