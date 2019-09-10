package kr.co.kjworld.viewsearch.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kr.co.kjworld.viewsearch.R;
import kr.co.kjworld.viewsearch.data.response.data.Document;
import kr.co.kjworld.viewsearch.util.DateUtil;

public class DetailFragment extends Fragment {

    public interface GoURLClickListener {
        void onGoURLClickListener(String URL, String title);
    }
    private Document mDocument;
    private Context mContext;
    private GoURLClickListener mClickListener;

    private ImageView mThumbnailView;
    private TextView mNameView;
    private TextView mTitle;
    private TextView mContents;
    private TextView mDateTime;
    private TextView mURLView;
    private Button mGoUrlButton;

    public DetailFragment(){}
    public DetailFragment(Document document)
    {
        mDocument = document;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mClickListener = (GoURLClickListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar_detail);
        ((AppCompatActivity)mContext).setSupportActionBar(toolbar);
        ((AppCompatActivity)mContext).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)mContext).getSupportActionBar().setTitle(mDocument.label);

        mThumbnailView  = view.findViewById(R.id.detail_thumbnail);
        mNameView = view.findViewById(R.id.detail_name);
        mTitle = view.findViewById(R.id.detail_title);
        mContents = view.findViewById(R.id.detail_contents);
        mDateTime = view.findViewById(R.id.detail_datetime);
        mURLView = view.findViewById(R.id.detail_url);
        mGoUrlButton = view.findViewById(R.id.detail_go_button);
        mGoUrlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDocument.url != null) {
                    mDocument.isSawItem = true;
                    mClickListener.onGoURLClickListener(mDocument.url, mDocument.title);
                }
            }
        });

        if (mDocument != null)
        {
            mNameView.setText(mDocument.name);
            mTitle.setText(mDocument.title);
            String dateStr = DateUtil.changeDateString(mDocument.datetime, "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", "yyyy년 MM월 dd일");
            mDateTime.setText(dateStr);
            mContents.setText(mDocument.contents);
            mURLView.setText(mDocument.url);

            Glide.with(mContext)
                .load(mDocument.thumbnail)
                .into(mThumbnailView);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
