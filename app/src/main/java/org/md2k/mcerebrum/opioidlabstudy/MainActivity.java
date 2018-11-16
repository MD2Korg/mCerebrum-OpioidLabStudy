package org.md2k.mcerebrum.opioidlabstudy;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.design.widget.TabLayout;
import android.widget.Toast;

import org.md2k.datakitapi.DataKitAPI;
import org.md2k.datakitapi.exception.DataKitException;
import org.md2k.datakitapi.messagehandler.OnConnectionListener;
import org.md2k.mcerebrum.commons.permission.PermissionInfo;
import org.md2k.mcerebrum.commons.permission.ResultCallback;

public class MainActivity extends AppCompatActivity {
    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(Tab1Fragment.newInstance("Pre"), "Pre");
        adapter.addFragment(Tab1Fragment.newInstance("During"), "During");
        adapter.addFragment(Tab1Fragment.newInstance("Post"), "Post");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        PermissionInfo permissionInfo = new PermissionInfo();
        permissionInfo.getPermissions(this, new ResultCallback<Boolean>() {
            @Override
            public void onResult(Boolean result) {
                if (!result) {
                    Toast.makeText(getApplicationContext(), "!PERMISSION DENIED !!! Could not continue...", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                }
            }
        });
        try {
            DataKitAPI.getInstance(this).connect(new OnConnectionListener() {
                @Override
                public void onConnected() {

                }
            });
        } catch (DataKitException e) {
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_example, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_settings:
                Intent intent = new Intent(this, ActivitySettings.class);
                startActivity(intent);
                return true;
            case R.id.menu_clear:
                MySharedPreference.clear(this);
                viewPager.getAdapter().notifyDataSetChanged();

//                Toast.makeText(getApplicationContext(),"Item 2 Selected",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onDestroy(){
        DataKitAPI.getInstance(this).disconnect();
        super.onDestroy();
    }
}
