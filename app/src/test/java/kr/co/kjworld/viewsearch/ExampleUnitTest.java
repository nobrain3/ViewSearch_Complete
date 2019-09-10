package kr.co.kjworld.viewsearch;

import org.junit.Test;

import kr.co.kjworld.viewsearch.data.network.RetrofitCreator;
import kr.co.kjworld.viewsearch.data.response.data.KakaoData;
import kr.co.kjworld.viewsearch.util.DateUtil;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void DateChange_isCorrect() {
        String beforeStr = "2019-07-31T16:31:41.000+09:00";
        String afterStr = "2019년 07월 31일";
        String result = DateUtil.changeDateString(beforeStr, "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", "yyyy년 MM월 dd일" );
        assertEquals(result, afterStr);
    }

    @Test
    public void NotNULL_isCorrect(){
        KakaoData data = new KakaoData();
        assertNotNull(data.documents);
    }

    @Test
    public void NotNULL_Retrofit() {
        assertNotNull(RetrofitCreator.getInstance());
    }

    /*
    @Test
    public void Retrofit_isCorrect() {
        KakaoSearchService kakaoService = RetrofitCreator.getInstance().create(KakaoSearchService.class);

        kakaoService.getBlogData("KakaoAK 158ecb4f39933422a476a6ce06120296", "설현", "accuracy", 1, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> updateBlogData(item));
    }

    @Test
    public void updateBlogData(KakaoData kakaodata)
    {
        String url = "http://dudrms606.tistory.com/525";
        assertEquals(url, kakaodata.documents.get(0).url);
    }
    */

}