package kr.co.kjworld.viewsearch.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kr.co.kjworld.viewsearch.R;
import kr.co.kjworld.viewsearch.data.response.data.Document;
import kr.co.kjworld.viewsearch.data.response.data.KakaoData;
import kr.co.kjworld.viewsearch.data.response.data.Meta;
import kr.co.kjworld.viewsearch.util.DateUtil;
import kr.co.kjworld.viewsearch.view.fragment.SearchFragment;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    public final static int ALL = 0;
    public final static int BLOG = 1;
    public final static int CAFE = 2;

    public final static String LABEL_CAFE = "Cafe";
    public final static String LABEL_BLOG = "Blog";

    public final static int SORT_TITLE = 0;
    public final static int SORT_DATETIME = 1;

    private List<Document> mSearcList;
    private List<Document> mFilteringList;
    private Context mContext;
    private SearchFragment.ItemClickListener mItemClicklister;

    private int mLabelType;
    private int mSortType;

    private boolean mIsBlogSearchEnd;
    private boolean mIsCafeSearchEnd;

    private boolean mIsSearchingBlog;
    private boolean mIsSearchingCafe;

    public boolean isSearchingBlog() {
        return mIsSearchingBlog;
    }

    public void setIsSearchingBlog(boolean mIsSearchingBlog) {
        this.mIsSearchingBlog = mIsSearchingBlog;
    }

    public boolean isSearchingCafe() {
        return mIsSearchingCafe;
    }

    public void setIsSearchingCafe(boolean mIsSearchingCafe) {
        this.mIsSearchingCafe = mIsSearchingCafe;
    }



    public boolean isBlogSearchEnd() {
        return mIsBlogSearchEnd;
    }

    public void setIsBlogSearchEnd(boolean mIsBlogSearchEnd) {
        this.mIsBlogSearchEnd = mIsBlogSearchEnd;
    }

    public void setIsCafeSearchEnd(boolean mIsCafeSearchEnd) {
        this.mIsCafeSearchEnd = mIsCafeSearchEnd;
    }

    public boolean isCafeSearchEnd() {
        return mIsCafeSearchEnd;
    }

    public SearchAdapter(Context context, ArrayList<Document> list, SearchFragment.ItemClickListener itemClickListener)
    {
        mContext = context;
        mSearcList = list;
        mItemClicklister = itemClickListener;
    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.search_item, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public int getItemCount() {
        int size = 0;
        int fullSize = mSearcList.size();

        switch (mLabelType)
        {
            case BLOG:
            case CAFE:
                Document temp;
                for(int i = 0; i < fullSize; i ++)
                {
                    temp = mSearcList.get(i);
                    if (mLabelType == BLOG)
                    {
                        if (temp.isCafe == false)
                            size++;
                    } else {
                        if (temp.isCafe == true)
                            size++;
                    }
                }
                break;

            case ALL:
            default:
                size = fullSize;
                break;

        }
        return size;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder holder, int position) {
        Document temp;

        int itemIndex = 0;
        int fullSize = mSearcList.size();

        Document data = null;
        if (mLabelType == ALL)
        {
            data = mSearcList.get(position);
        }

        for (int i = 0; i < fullSize; i++)
        {
            temp = mSearcList.get(i);
            if (mLabelType == BLOG && temp.isCafe == false)
            {
                if (position == itemIndex) {
                    if (mFilteringList == null)
                        mFilteringList = new ArrayList<>();

                    data = temp;
                    mFilteringList.add(data);
                    break;
                }
                itemIndex++;
            } else if (mLabelType == CAFE && temp.isCafe == true){
                if (position == itemIndex) {
                    if (mFilteringList == null)
                        mFilteringList = new ArrayList<>();

                    data = temp;
                    mFilteringList.add(data);
                    break;
                }

                itemIndex++;
            }
        }

        holder.mLabelView.setText(data.label);
        holder.mNameView.setText(data.name);
        holder.mTitleView.setText(data.title);
        String dateStr = DateUtil.changeDateString(data.datetime, "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", "yyyy년 MM월 dd일");
        holder.mDateTimeView.setText(dateStr);

        Glide.with(mContext)
                .load(data.thumbnail)
                .into(holder.mThumbnailView);

        if (data.isSawItem)
            holder.mDimView.setVisibility(View.VISIBLE);
        else
            holder.mDimView.setVisibility(View.INVISIBLE);
    }

    public void updateBlogData(KakaoData data)
    {
        Meta metaData = data.meta;
        mIsBlogSearchEnd = metaData.isEnd;
        for (Document document : data.documents)
        {
            Document newDocument = new Document();
            newDocument.thumbnail = document.thumbnail;
            newDocument.isCafe = false;
            newDocument.label = LABEL_BLOG;
            newDocument.datetime = document.datetime;
            newDocument.name = document.name;
            newDocument.title = document.title;
            newDocument.contents = document.contents;
            newDocument.url = document.url;
            newDocument.isSawItem = false;
            mSearcList.add(newDocument);
        }
        sort(SORT_TITLE);
        notifyDataSetChanged();
        mIsSearchingBlog = false;
    }

    public void updateCafeData(KakaoData data)
    {
        Meta metaData = data.meta;
        mIsCafeSearchEnd = metaData.isEnd;
        for (Document document : data.documents)
        {
            Document newDocument = new Document();
            newDocument.thumbnail = document.thumbnail;
            newDocument.isCafe = true;
            newDocument.label = LABEL_CAFE;
            newDocument.datetime = document.datetime;
            newDocument.name = document.name;
            newDocument.title = document.title;
            newDocument.url = document.url;
            newDocument.contents = document.contents;
            newDocument.isSawItem = false;
            mSearcList.add(newDocument);
        }
        sort(SORT_TITLE);
        notifyDataSetChanged();
        mIsSearchingCafe = false;
    }

    public void setType(int type) {
        if (mFilteringList != null)
            mFilteringList.clear();

        mLabelType = type;
    }

    public void sort(int type) {
        mSortType = type;
        Collections.sort(mSearcList, new Comparator<Document>() {
            @Override
            public int compare(Document document, Document document2) {
                if (mSortType == SORT_TITLE)
                    return document.title.compareTo(document2.title);
                else
                    return document2.datetime.compareTo(document.datetime);
            }
        });
        notifyDataSetChanged();
    }

    public void clear() {
        int size = mSearcList.size();
        mSearcList.clear();
        notifyItemRangeRemoved(0, size);
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {
        TextView mLabelView;
        TextView mNameView;
        TextView mTitleView;
        TextView mDateTimeView;
        ImageView mThumbnailView;
        View mDimView;

        public SearchViewHolder(@NonNull View itemView){
            super(itemView);
            mLabelView = itemView.findViewById(R.id.cardview_label);
            mNameView = itemView.findViewById(R.id.cardview_name);
            mTitleView = itemView.findViewById(R.id.cardview_title);
            mDateTimeView = itemView.findViewById(R.id.careview_date_time);
            mThumbnailView = itemView.findViewById(R.id.cardview_thumbnail);
            mDimView = itemView.findViewById(R.id.cardview_dim);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Document document = null;
                    if (mLabelType != ALL && mFilteringList != null)
                        document = mFilteringList.get(position);
                    else
                        document = mSearcList.get(position);
                    mItemClicklister.onItemClicked(document);
                }
            });

        }
    }
}
