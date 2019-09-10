package kr.co.kjworld.viewsearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import kr.co.kjworld.viewsearch.data.response.data.Document;
import kr.co.kjworld.viewsearch.view.fragment.DetailFragment;
import kr.co.kjworld.viewsearch.view.fragment.SearchFragment;
import kr.co.kjworld.viewsearch.view.fragment.WebViewFragment;

public class MainActivity extends AppCompatActivity implements SearchFragment.ItemClickListener, SearchFragment.FragmentCreateListener, DetailFragment.GoURLClickListener{
    EditText mSearchText;
    SearchFragment mSearchFragment;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mSearchFragment = new SearchFragment();
        transaction.add(R.id.fragment_container, mSearchFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_search:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mSearchText.getWindowToken(), 0);
                searchText();
                break;

            case android.R.id.home:
                super.onBackPressed();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void searchText()
    {
        mSearchFragment.searchStart(mSearchText.getText().toString(), this);
    }

    @Override
    public void onItemClicked(Document document) {
        getSupportFragmentManager().addOnBackStackChangedListener(getListener());;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        DetailFragment detailFragment = new DetailFragment(document);
        transaction.add(R.id.fragment_container, detailFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onCreated() {
        mToolbar= findViewById(R.id.toolbar);
        mSearchText = mToolbar.findViewById(R.id.actionbar_search);
        setSupportActionBar(mToolbar);
    }

    @Override
    public void onGoURLClickListener(String URL, String title) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        WebViewFragment webViewFragment = new WebViewFragment(URL, title);
        transaction.add(R.id.fragment_container, webViewFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private FragmentManager.OnBackStackChangedListener getListener()
    {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();
                if (manager != null)
                {
                    Fragment currentFragment = manager.findFragmentById(R.id.fragment_container);
                    if (currentFragment instanceof SearchFragment) {
                        setSupportActionBar(mToolbar);
                    }
                }
            }
        };
        return result;
    }
}
