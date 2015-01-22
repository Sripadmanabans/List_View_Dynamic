package com.example.sripadmanaban.listviewdynamic;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;

/**
 * This is the fragment that takes the data from the user
 * Created by Sripadmanaban on 12/11/2014.
 */
public class CreateFragment extends Fragment
{
    private static final String STYLE_BOLD = "STYLE_BOLD";
    private static final String STYLE_ITALIC = "STYLE_ITALIC";
    private static final String CREATE_SUCCESSFUL = "New Entry Was Created";
    private static final String CREATE_FAILED_BOTH_NAMES = "Please enter values for both 'Game Name' and 'Developer Name'";
    private static final String CREATE_FAILED_GAME_NAME = "Please enter values for 'Game Name'";
    private static final String CREATE_FAILED_DEVELOPER_NAME = "Please enter values for 'Developer Name'";

    private EditText gameName;
    private EditText developerName;
    private GamesDatabaseAdapter db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_create, container, false);

        db = GamesDatabaseAdapter.getInstance(getActivity().getApplicationContext());

        Bundle bundle = getArguments();
        boolean bold = false;
        boolean italic = false;

        if(bundle != null)
        {
            bold = bundle.getBoolean(STYLE_BOLD);
            italic = bundle.getBoolean(STYLE_ITALIC);
        }

        gameName = (EditText) view.findViewById(R.id.game_name);
        developerName = (EditText) view.findViewById(R.id.developer_name);
        Button button = (Button) view.findViewById(R.id.create_button);

        if(bold && italic)
        {
            gameName.setTypeface(null, Typeface.BOLD_ITALIC);
            developerName.setTypeface(null, Typeface.BOLD_ITALIC);
        }
        else if(bold)
        {
            gameName.setTypeface(null, Typeface.BOLD);
            developerName.setTypeface(null, Typeface.BOLD);
        }
        else if(italic)
        {
            gameName.setTypeface(null, Typeface.ITALIC);
            developerName.setTypeface(null, Typeface.ITALIC);
        }


        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String nameGame = gameName.getText().toString();
                String nameDeveloper = developerName.getText().toString();
                Log.d("Bug", nameGame);
                if(nameGame.equals("") && nameDeveloper.equals(""))
                {
                    Toast.makeText(getActivity(), CREATE_FAILED_BOTH_NAMES, Toast.LENGTH_LONG).show();
                }
                else if(nameGame.equals(""))
                {
                    Toast.makeText(getActivity(), CREATE_FAILED_GAME_NAME, Toast.LENGTH_LONG).show();
                }
                else if(nameDeveloper.equals(""))
                {
                    Toast.makeText(getActivity(), CREATE_FAILED_DEVELOPER_NAME, Toast.LENGTH_LONG).show();
                }
                else
                {
                    try
                    {
                        db.open();
                        db.insertGame(nameGame, nameDeveloper);
                        db.close();
                    }
                    catch (SQLException e)
                    {
                        e.printStackTrace();
                    }

                    Toast.makeText(getActivity(), CREATE_SUCCESSFUL, Toast.LENGTH_SHORT).show();

                    gameName.setText("");
                    developerName.setText("");
                    gameName.setHint(R.string.hint_game_name);
                    developerName.setHint(R.string.hint_developer);
                }
            }
        });

        return view;
    }
}
