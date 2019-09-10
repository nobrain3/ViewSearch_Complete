package kr.co.kjworld.viewsearch.view.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import kr.co.kjworld.viewsearch.R;
import kr.co.kjworld.viewsearch.data.network.RetrofitCreator;
import kr.co.kjworld.viewsearch.data.response.KakaoSearchService;
import kr.co.kjworld.viewsearch.data.response.data.Document;
import kr.co.kjworld.viewsearch.view.adapter.SearchAdapter;

public class SearchFragment extends Fragment {

    private static final String REQUEST_SORT = "accuracy";
    private static final int REQUEST_SIZE = 25;

    public interface ItemClickListener {
        void onItemClicked(Document document);
    }

    public interface FragmentCreateListener {
        void onCreated();
    }

    private ItemClickListener mItemclickLister;
    private FragmentCreateListener mCreateListener;

    private RecyclerView mSearchView;
    private SearchAdapter mAdapter;
    private Spinner mFilterSpinner;
    private Context mContext;
    private ImageButton mImageButton;

    private int mSelecteditem;

    private String mCurrentSearchText;
    private int mSearchPageCount;

    private boolean mIsSearching;

    @Override
    public void onAttach(Context context) {
        mContext = context;
        mItemclickLister = (ItemClickListener) context;
        mCreateListener = (FragmentCreateListener) context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_layout, container, false);

        mSearchView = view.findViewById(R.id.search_listview);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mSearchView.setLayoutManager(layoutManager);
        mAdapter = new SearchAdapter(mContext, new ArrayList<Document>(), mItemclickLister);
        mSearchView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(mSearchView.getContext(),new LinearLayoutManager(view.getContext()).getOrientation());
        mSearchView.addItemDecoration(dividerItemDecoration);

        mSearchView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                int itemTotalCount = mAdapter.getItemCount();

                if (lastVisibleItemPosition+1 == itemTotalCount) {
                    if (!mAdapter.isSearchingBlog() && !mAdapter.isSearchingCafe())
                        searchContinue();
                }
            }
        });

        mFilterSpinner = view.findViewById(R.id.filter_spinner);
        mFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                mAdapter.setType(i);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mSelecteditem = -1;
        mImageButton = view.findViewById(R.id.sort_button);
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] sortItemList = view.getResources().getStringArray(R.array.sort_dialog);
                AlertDialog.Builder oDialog = new AlertDialog.Builder(mContext, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                oDialog.setTitle(R.string.dialog_title)
                        .setSingleChoiceItems(sortItemList, mSelecteditem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mSelecteditem = i;
                            }
                        })
                        .setNeutralButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (mSelecteditem >= 0)
                                    mAdapter.sort(mSelecteditem);
                            }
                        })
                        .setNegativeButton(R.string.dialog_cancle, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mCreateListener.onCreated();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void searchStart(String aText, Context context)
    {
        View view = getView();

        mCurrentSearchText = aText;
        mSearchPageCount = 1;

        mAdapter.clear();

        mAdapter.setIsBlogSearchEnd(false);
        mAdapter.setIsCafeSearchEnd(false);
        mAdapter.setIsSearchingBlog(true);
        mAdapter.setIsSearchingCafe(true);

        mIsSearching = true;

        KakaoSearchService kakaoService = RetrofitCreator.getInstance().create(KakaoSearchService.class);

        kakaoService.getBlogData("KakaoAK 158ecb4f39933422a476a6ce06120296", aText, REQUEST_SORT, mSearchPageCount, REQUEST_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> mAdapter.updateBlogData(item));

        kakaoService.getCafeData("KakaoAK 158ecb4f39933422a476a6ce06120296", aText, REQUEST_SORT, mSearchPageCount, REQUEST_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> mAdapter.updateCafeData(item));
    }

    public void searchContinue() {
        if (!mAdapter.isBlogSearchEnd() || !mAdapter.isCafeSearchEnd())
            mSearchPageCount++;

        KakaoSearchService kakaoService = RetrofitCreator.getInstance().create(KakaoSearchService.class);

        if (mAdapter.isBlogSearchEnd() == false) {
            mAdapter.setIsSearchingBlog(true);
            kakaoService.getBlogData("KakaoAK 158ecb4f39933422a476a6ce06120296", mCurrentSearchText, REQUEST_SORT, mSearchPageCount, REQUEST_SIZE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(item -> mAdapter.updateBlogData(item));
        }

        if (mAdapter.isCafeSearchEnd() == false) {
            mAdapter.setIsSearchingCafe(true);
            kakaoService.getCafeData("KakaoAK 158ecb4f39933422a476a6ce06120296", mCurrentSearchText, REQUEST_SORT, mSearchPageCount, REQUEST_SIZE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(item -> mAdapter.updateCafeData(item));
        }
    }

    public void notifyDim() {
        mAdapter.notifyDataSetChanged();
    }

}
