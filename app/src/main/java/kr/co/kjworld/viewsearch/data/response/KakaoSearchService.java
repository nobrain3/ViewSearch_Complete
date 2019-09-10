package kr.co.kjworld.viewsearch.data.response;

import io.reactivex.Single;
import kr.co.kjworld.viewsearch.data.response.data.KakaoData;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface KakaoSearchService {
    @GET("/v2/search/blog")
    Single<KakaoData> getBlogData(@Header("Authorization") String auth, @Query("query") String query, @Query("sort") String sort, @Query("page") int page, @Query("size") int size);

    @GET("/v2/search/cafe")
    Single<KakaoData> getCafeData(@Header("Authorization") String auth, @Query("query") String query, @Query("sort") String sort, @Query("page") int page, @Query("size") int size);
}
