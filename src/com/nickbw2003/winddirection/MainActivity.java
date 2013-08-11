package com.nickbw2003.winddirection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.nickbw2003.winddirection.fragments.CompassFragment;

public class MainActivity extends SherlockFragmentActivity implements ListView.OnItemClickListener {
	private Fragment _contentFragment;
	
	private DrawerLayout _drawerLayout;
	private ListView _drawerList;
	private ActionBarDrawerToggle _drawerToggle;
	private CharSequence _title;
	
	private String[] _drawerItems;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
		
		_drawerItems = getResources().getStringArray(R.array.drawerItems);
        _drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        _drawerList = (ListView) findViewById(R.id.drawerList);
        _drawerList.setAdapter(new DrawerListAdapter(this, R.layout.drawer_list_item, _drawerItems));
        _drawerList.setOnItemClickListener(this);
        _title = getTitle();
                
        _drawerToggle = new ActionBarDrawerToggle(this, _drawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(_title);
                supportInvalidateOptionsMenu();
            }
 
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(R.string.app_name);
                supportInvalidateOptionsMenu();
            }
        };
        _drawerLayout.setDrawerListener(_drawerToggle);
        
		FragmentManager fm = getSupportFragmentManager();
		
		if (savedInstanceState != null) {
			_contentFragment = fm.getFragment(savedInstanceState, "contentFragment");
		}
		
		if (_contentFragment == null) {
			_contentFragment = new CompassFragment();
		}
		
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.contentFrame, _contentFragment);
		ft.commit();
	}
	
	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        _drawerToggle.syncState();
    }
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {		
		super.onSaveInstanceState(outState);
		
		if (_contentFragment != null) {
			getSupportFragmentManager().putFragment(outState, "contentFragment", _contentFragment);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (position == 1) {
			showPreferenceActivity();
		}
		
		_drawerLayout.closeDrawer(_drawerList);
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (_drawerLayout.isDrawerOpen(_drawerList)) {
                _drawerLayout.closeDrawer(_drawerList);
            } else {
                _drawerLayout.openDrawer(_drawerList);
            }
        }
        
        return super.onOptionsItemSelected(item);
    }
	
	private void showPreferenceActivity() {
		startActivity(new Intent(this, SettingsActivity.class));
	}
	
	private class DrawerListAdapter extends ArrayAdapter<String> {
		public DrawerListAdapter(Context context, int textViewResourceId, String[] objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView textView = (TextView)super.getView(position, convertView, parent);
			int drawableId = -1;			
			
			if (position == 0) {
				drawableId = R.drawable.ic_cloud;
			}
			else if (position == 1) {
				drawableId = R.drawable.ic_settings;
			}
			
			if (drawableId != -1) {
				textView.setCompoundDrawablesWithIntrinsicBounds(drawableId, 0, 0, 0);
				textView.setCompoundDrawablePadding(20);
			}
			
			return textView;
		}
	}
}