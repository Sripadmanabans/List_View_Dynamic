package com.example.sripadmanaban.listviewdynamic;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;

/**
 * Update Page
 * Created by Sripadmanaban on 12/18/2014.
 */
public class UpdatePageFragment extends Fragment
{
    private static final String UPDATE_ROW_ID = "UPDATE_ROW_ID";

    private GamesDatabaseAdapter db;

    private int rowId;

    private Games game = new Games();

    private EditText gameName, developerName;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        rowId = bundle.getInt(UPDATE_ROW_ID);
        db = GamesDatabaseAdapter.getInstance(getActivity().getApplicationContext());
        try
        {
            db.open();
            Cursor c = db.getGame(rowId);
            game.setGameId(c.getInt(0));
            game.setGameName(c.getString(1));
            game.setDeveloperName(c.getString(2));
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_update_page, container, false);

        gameName = (EditText) view.findViewById(R.id.update_game_name);
        developerName = (EditText) view.findViewById(R.id.update_developer_name);

        Button button = (Button) view.findViewById(R.id.update_button);

        gameName.setText(game.getGameName());
        developerName.setText(game.getDeveloperName());

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    db.open();
                    if(db.updateGame(rowId, gameName.getText().toString(), developerName.getText().toString()))
                    {
                        Toast.makeText(getActivity(), "Update Successful", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Update Unsuccessful", Toast.LENGTH_SHORT).show();
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
            }
        });

        return view;
    }
}
