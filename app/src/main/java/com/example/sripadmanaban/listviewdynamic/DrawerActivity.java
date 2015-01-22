package com.example.sripadmanaban.listviewdynamic;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class DrawerActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, DisplayListFragment.DisplayListFragmentCallback, UpdateFragment.UpdateFragmentCallback
{
    private static final String FRAGMENT_POSITION = "FRAGMENT_POSITION";
    private static final String ORDERBY_TYPE = "ORDERING_TYPE";
    private static final String FINALLYOPEN_FRAGMENT = "FINALLY";
    private static final String SETTINGS_OPEN = "SETTINGS";
    private static final String STYLE_BOLD = "STYLE_BOLD";
    private static final String STYLE_ITALIC = "STYLE_ITALIC";
    private static final String LIST_VIEW_LINES = "LIST_VIEW_LINES";
    private static final String UPDATE_ROW_ID = "UPDATE_ROW_ID";

    private static final String TITLE_HOME = "Home";
    private static final String TITLE_DISPLAY = "List of Games";
    private static final String TITLE_CREATE = "Game Entry";
    private static final String TITLE_DELETE = "Delete Games";
    private static final String TITLE_UPDATE = "Updating a Game";
    private static final String TITLE_UPDATE_PAGE = "Update Page";

    private String preferenceListValue;

    private boolean orderAsc, bold, italic;

    private int LOCATION = 0;

    private Toolbar toolbar;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        preferences = getSharedPreferences("com.example.sripadmanaban.listviewdynamic_preferences", MODE_PRIVATE);

        orderAsc = preferences.getBoolean(ORDERBY_TYPE, true);
        bold = preferences.getBoolean(STYLE_BOLD, false);
        italic = preferences.getBoolean(STYLE_ITALIC, false);
        preferenceListValue = preferences.getString(LIST_VIEW_LINES, "1");

        setUpNavigationDrawer();

        if(savedInstanceState !=  null)
        {
            LOCATION = savedInstanceState.getInt(FRAGMENT_POSITION);
        }

        if(preferences.getBoolean(SETTINGS_OPEN, false))
        {
            LOCATION = preferences.getInt(FINALLYOPEN_FRAGMENT, 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(SETTINGS_OPEN, false);
            editor.apply();
        }

        openFragmentByPosition(LOCATION);

    }

    private void setUpNavigationDrawer()
    {
        NavigationDrawerFragment mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.fragment_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationDrawerFragment.setUp(R.id.fragment_layout, drawerLayout, toolbar);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(SETTINGS_OPEN, true);
            editor.apply();
            Intent intent = new Intent("com.example.sripadmanaban.listviewdynamic.SettingsActivity");
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position)
    {
        openFragmentByPosition(position);
    }

    private void openFragmentByPosition(int position)
    {
        LOCATION = position;
        Bundle bundle = setBundle();
        Fragment fragment;
        switch (position)
        {
            case 0:
                toolbar.setTitle(TITLE_HOME);
                fragment = new HomeFragment();
                openFragment(fragment, "HomeFrag");
                break;

            case 1:
                toolbar.setTitle(TITLE_DISPLAY);
                fragment = new DisplayListFragment();
                fragment.setArguments(bundle);
                openFragment(fragment, "DisplayFrag");
                break;

            case 2:
                toolbar.setTitle(TITLE_CREATE);
                fragment = new CreateFragment();
                fragment.setArguments(bundle);
                openFragment(fragment, "CreateFrag");
                break;

            case 3:
                toolbar.setTitle(TITLE_UPDATE);
                fragment = new UpdateFragment();
                fragment.setArguments(bundle);
                openFragment(fragment, "UpdateFrag");
                break;

            case 4:
                toolbar.setTitle(TITLE_DELETE);
                fragment = new DeleteFragment();
                fragment.setArguments(bundle);
                openFragment(fragment, "DeleteFrag");
                break;
        }
    }

    private void openFragment(Fragment frag, String tag)
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment fragment = getFragmentManager().findFragmentByTag(tag);
        if(fragment == null)
        {
            transaction.replace(R.id.container, frag, tag);
        }
        transaction.commit();
    }

    private Bundle setBundle()
    {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ORDERBY_TYPE, orderAsc);
        bundle.putInt(LIST_VIEW_LINES, Integer.parseInt(preferenceListValue));
        bundle.putBoolean(STYLE_BOLD, bold);
        bundle.putBoolean(STYLE_ITALIC, italic);
        return bundle;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt(FRAGMENT_POSITION, LOCATION);
    }

    @Override
    public void onButtonClickCallCreateFragment()
    {
        toolbar.setTitle(TITLE_CREATE);
        Bundle bundle = new Bundle();
        bundle.putBoolean(STYLE_BOLD, bold);
        bundle.putBoolean(STYLE_ITALIC, italic);
        Fragment fragment = new CreateFragment();
        fragment.setArguments(bundle);
        openFragment(fragment, "CreateFrag");
    }

    @Override
    public void onUpdateButtonListener(int rowId)
    {
        toolbar.setTitle(TITLE_UPDATE_PAGE);
        Bundle bundle = new Bundle();
        bundle.putBoolean(STYLE_BOLD, bold);
        bundle.putBoolean(STYLE_ITALIC, italic);
        bundle.putInt(UPDATE_ROW_ID, rowId);
        Fragment fragment = new UpdatePageFragment();
        fragment.setArguments(bundle);
        openFragment(fragment, "UpdatePageFrag");
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(preferences.getBoolean(SETTINGS_OPEN, false))
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt(FINALLYOPEN_FRAGMENT, LOCATION);
            editor.apply();
        }

    }


}
