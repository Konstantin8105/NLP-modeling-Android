package com.modelingbrain.home;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
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
import com.modelingbrain.home.archive.FragmentArchive;
import com.modelingbrain.home.chooseModel.ActivityChooseModel;
import com.modelingbrain.home.folderModel.FragmentFolder;
import com.modelingbrain.home.main.ModelSort;
import com.modelingbrain.home.opensave.OpenActivity;
import com.modelingbrain.home.opensave.SaveActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @SuppressWarnings("unused")
    protected final String TAG = this.getClass().toString();

    private FloatingActionButton fab;
    public static final int REQUEST_FRAGMENT = 800;

    private enum PageStatus {
        Folder(new FragmentFolder(), R.string.nav_folder),
        Archive(new FragmentArchive(), R.string.nav_archive);

        private final MainFragments fragment;
        private final int stringResource;

        PageStatus(MainFragments fragment, int stringResource) {
            this.fragment = fragment;
            this.stringResource = stringResource;
        }

        public MainFragments getMainFragment() {
            return fragment;
        }

        public int getStringResource() {
            return stringResource;
        }
    }

    private PageStatus pageStatus;
    private String pageStatusKey = "PageStatus";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(pageStatusKey, pageStatus.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState != null) {
            pageStatus = PageStatus.valueOf(savedInstanceState.getString(pageStatusKey));
            switch (pageStatus) {
                case Folder:
                    navigationView.getMenu().getItem(0).setChecked(true);
                    break;
                case Archive:
                    navigationView.getMenu().getItem(1).setChecked(true);
                    break;
            }
        } else {
            pageStatus = PageStatus.Folder;
            navigationView.getMenu().getItem(0).setChecked(true);
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), ActivityChooseModel.class);
                startActivityForResult(intent, REQUEST_FRAGMENT);
            }
        });


        createView();
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

        switch (id) {
            case (R.id.action_save_models): {
                Intent intent = new Intent(this, SaveActivity.class);
                startActivity(intent);
                break;
            }
            case (R.id.action_open_models): {
                Intent intent = new Intent(this, OpenActivity.class);
                startActivityForResult(intent, REQUEST_FRAGMENT);
                break;
            }
            case (R.id.action_sort): {
                final PopupMenu popupMenu = new PopupMenu(this, this.findViewById(R.id.action_sort));
                popupMenu.inflate(R.menu.menu_popup);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        ModelSort modelSort;
                        switch (item.getItemId()) {
                            case (R.id.item_sort_alpha):
                                modelSort = ModelSort.SortAlphabet;
                                break;
                            case (R.id.item_sort_alpha_invert):
                                modelSort = ModelSort.SortAlphabetInverse;
                                break;
                            case (R.id.item_sort_name):
                                modelSort = ModelSort.SortName;
                                break;
                            case (R.id.item_sort_name_invert):
                                modelSort = ModelSort.SortNameInverse;
                                break;
                            case (R.id.item_sort_date):
                                modelSort = ModelSort.SortDate;
                                break;
                            case (R.id.item_sort_date_invert):
                                modelSort = ModelSort.SortDateInverse;
                                break;
                            default:
                                return false;
                        }
                        pageStatus.getMainFragment().changeSort(modelSort);
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

    private void createView() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        switch (pageStatus) {
            case Folder: {
                transaction.remove(pageStatus.getMainFragment().getFragment());
                transaction.replace(R.id.detail_fragment, pageStatus.getMainFragment().getFragment());
                fab.show();
                FragmentFolder folder = (FragmentFolder) pageStatus.getMainFragment().getFragment();
                folder.setFab(fab);
                break;
            }
            case Archive: {
                transaction.remove(pageStatus.getMainFragment().getFragment());
                transaction.replace(R.id.detail_fragment, pageStatus.getMainFragment().getFragment());
                fab.hide();
                break;
            }
        }
        transaction.commit();

        Snackbar.make(findViewById(R.id.nav_view),
                getBaseContext().getString(pageStatus.getStringResource()),
                Snackbar.LENGTH_SHORT).
                setAction("Action", null).show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_folder: {
                if (pageStatus != PageStatus.Folder) {
                    pageStatus = PageStatus.Folder;
                    createView();
                }
                break;
            }
            case R.id.nav_archive: {
                if (pageStatus != PageStatus.Archive) {
                    pageStatus = PageStatus.Archive;
                    createView();
                }
                break;
            }
            case R.id.nav_help: {
                Intent intent = new Intent(this, ActivityAbout.class);
                startActivity(intent);
                break;
            }
            default:
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Fragment fragment = getFragmentManager().findFragmentById(R.id.detail_fragment);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}
