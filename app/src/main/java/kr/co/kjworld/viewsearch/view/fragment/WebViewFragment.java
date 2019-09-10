package kr.co.kjworld.viewsearch.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import kr.co.kjworld.viewsearch.R;

public class WebViewFragment extends Fragment {
    public WebViewFragment(){}
    public WebViewFragment(String urlString, String title) {
        mURLString = urlString;
        mTitle = title;
    }
    private WebView mWebView;
    private Context mContext;

    private String mURLString;
    private String mTitle;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_webview, container, false);

        mWebView = view.findViewById(R.id.webview);

        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(mURLString);

        Toolbar toolbar = view.findViewById(R.id.toolbar_detail);
        ((AppCompatActivity)mContext).setSupportActionBar(toolbar);
        ((AppCompatActivity)mContext).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)mContext).getSupportActionBar().setTitle(mTitle);
        return view;
    }
}
