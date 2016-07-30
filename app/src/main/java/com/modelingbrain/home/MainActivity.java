package com.modelingbrain.home;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.modelingbrain.home.about.ActivityAbout;
import com.modelingbrain.home.archiveModel.FragmentArchive;
import com.modelingbrain.home.chooseModel.ActivityChooseModel;
import com.modelingbrain.home.folderModel.FragmentFolder;
import com.modelingbrain.home.main.ModelSort;
import com.modelingbrain.home.model.ContentManagerModel;
import com.modelingbrain.home.opensave.OpenActivity;
import com.modelingbrain.home.opensave.SaveActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{

    @SuppressWarnings("unused")
    protected final String TAG = this.getClass().toString();

//    static public final String CHOOSE_MODEL_RESULT = "CHOOSE_MODEL_RESULT";

    private ContentManagerModel contentManager = new ContentManagerModel(this);

    private FloatingActionButton fab;

    private FragmentFolder fragmentFolder;
    private FragmentArchive fragmentArchive;
    private FragmentTransaction transaction;

    public static final int REQUEST_FRAGMENT = 800;

    private enum PageStatus{
        Folder,
        Archive,
        Games
    }
    private PageStatus pageStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ActivityChooseModel.class);
                startActivityForResult(intent, REQUEST_FRAGMENT);
            }
        });

        fragmentFolder = new FragmentFolder();
        fragmentFolder.setFab(fab);
        fragmentArchive = new FragmentArchive();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Default NavigationView position
        onNavigationItemSelected(navigationView.getMenu().findItem(R.id.nav_folder));
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case (R.id.action_save_models):{
                Intent intent = new Intent(this, SaveActivity.class);
                startActivity(intent);
                break;
            }
            case (R.id.action_open_models):{
                Intent intent = new Intent(this, OpenActivity.class);
                startActivityForResult(intent,REQUEST_FRAGMENT);
                break;
            }
            // TODO: 7/30/16 create good list view with indication of present sort state
            case (R.id.action_sort):{
                final PopupMenu popupMenu = new PopupMenu(this, this.findViewById(R.id.action_sort));
                popupMenu.inflate(R.menu.menu_popup);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        ModelSort modelSort;
                        switch (item.getItemId()) {
                            case(R.id.item_sort_alpha):
                                modelSort = ModelSort.SortAlphabet;
                                break;
                            case(R.id.item_sort_alpha_invert):
                                modelSort = ModelSort.SortAlphabetInverse;
                                break;
                            case(R.id.item_sort_name):
                                modelSort = ModelSort.SortName;
                                break;
                            case(R.id.item_sort_name_invert):
                                modelSort = ModelSort.SortNameInverse;
                                break;
                            case(R.id.item_sort_date):
                                modelSort = ModelSort.SortDate;
                                break;
                            case(R.id.item_sort_date_invert):
                                modelSort = ModelSort.SortDateInverse;
                                break;
                            default:
                                return false;
                        }
                        switch (pageStatus){
                            case Folder:
                                fragmentFolder.changeSort(modelSort);
                                break;
                            case Archive:
                                fragmentArchive.changeSort(modelSort);
                                break;
                            default:
                        }
                        return true;
                    }
                });
                popupMenu.show();
                return true;
            }
            default:
                return false;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        transaction = getFragmentManager().beginTransaction();
        Fragment currentFragment = getFragmentManager().findFragmentById(R.id.detail_fragment);
        boolean createFragment = false;
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_folder: {
                fab.show();
                pageStatus = PageStatus.Folder;

                if(currentFragment != null)
                    transaction.remove(currentFragment);
                transaction.replace(R.id.detail_fragment, fragmentFolder);
                createFragment = true;
                break;
            }
            case R.id.nav_archive: {
                fab.hide();
                pageStatus = PageStatus.Archive;

                if(currentFragment != null)
                    transaction.remove(currentFragment);
                transaction.replace(R.id.detail_fragment, fragmentArchive);
                createFragment = true;
                break;
            }
            // TODO: 1/28/16 add setting
            /*case R.id.nav_settings: {
                Intent intent = new Intent(this, ActivitySetting.class);
                startActivity(intent);
                break;
            }*/
            case R.id.nav_help: {
                Intent intent = new Intent(this, ActivityAbout.class);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
        if(createFragment){
            transaction.addToBackStack(null);
            transaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getFragmentManager().findFragmentById(R.id.detail_fragment);
        if(fragment != null){
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
