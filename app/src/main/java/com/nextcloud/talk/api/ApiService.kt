
package com.nextcloud.talk.api

import com.nextcloud.talk.models.kikaoutitilies.RequestToActionGenericResult
import io.reactivex.Observable
import io.reactivex.Observer
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {



//    @POST("apps/kikaoutilities/api/0.1/activities")
    @POST("index.php/apps/kikaoutilities/api/0.1/activities")
    fun requestToSpeak(@Header("Authorization") authorization: String?, @Body body: RequestBody?): Observable<RequestToActionGenericResult?>?

    @PUT("index.php/apps/kikaoutilities/api/0.1/activities/{id}")
    fun cancelRequestToSpeak(@Header("Authorization") authorization: String?,  @Path("id") id:
    Int, @Query("token") token:String, @Body body: RequestBody?):
        Observable<RequestToActionGenericResult?>?

    @POST("index.php/apps/kikaoutilities/api/0.1/activities")
    fun requestToIntervene(@Header("Authorization") authorization: String?, @Body body: RequestBody?): Observable<RequestToActionGenericResult?>?

    @PUT("index.php/apps/kikaoutilities/api/0.1/activities/{id}")
    fun cancelRequestToIntervene(@Header("Authorization") authorization: String?,  @Path("id") id:
    Int, @Query("token") token:String, @Body body: RequestBody?):
        Observable<RequestToActionGenericResult?>?

    @GET("index.php/apps/kikaoutilities/api/0.1/activities")
    fun getSpeakerActionResponses(@Header("Authorization") authorization: String?,  @Query("token") token:
    String,):
        Observable<List<RequestToActionGenericResult>?>?

    @PUT("index.php/apps/kikaoutilities/api/0.1/activities/{id}")
    fun userUnMuted(@Header("Authorization") authorization: String?, @Path("id") id:
    Int, @Query("token") token:String, @Body body: RequestBody?): Observable<RequestToActionGenericResult?>?

    // VOTE //
    // fetch votes GET
    @GET("index.php/apps/polls/api/v1.0/fetchOpenVote/{token}")
    fun fetchVote(@Header("Authorization") authorization: String?,  @Path("token") token: String): Observable<ResponseBody?>?

    // fetch polls GET
    @GET("index.php/apps/polls/api/v1.0/polls")
    fun getPolls(@Header("Authorization") authorization: String?): Observable<ResponseBody>?

    // fetch polls option
    @GET("index.php/apps/polls/api/v1.0/poll/{id}/options")
    fun getPollsOptions(@Header("Authorization") authorization: String?, @Path("id") pollId: Int): Observable<ResponseBody?>?

    // sendOtpSmsForUser
    @POST("index.php/apps/polls/api/v1.0/sendOtpSms")
    fun sendOtp(@Header("Authorization") authorization: String?, @Body body: RequestBody?): Observable<ResponseBody?>?

    // verifyOtp
    @POST("index.php/apps/polls/api/v1.0/verifyOtp")
    fun verifyOtp(@Header("Authorization") authorization: String?, @Body body: RequestBody?): Observable<ResponseBody?>?

    // setVote
//    NSDictionary *parameters = @{
//        @"optionId" : optionId,
//        @"setTo" : option
//    };
    @PUT("index.php/apps/polls/vote")
    fun setVote(@Header("Authorization") authorization: String?, @Body body: RequestBody?): Observable<ResponseBody?>?

    // getVotes
    @GET("index.php/apps/polls/api/v1.0/poll/{id}/votes")
    fun getVoteResults(@Header("Authorization") authorization: String?, @Path("id") pollId: Int): Observable<ResponseBody?>?

    // getShares
    @GET("index.php/apps/polls/api/v1.0/poll/{id}/shares")
    fun getShares(@Header("Authorization") authorization: String?, @Path("id") pollId: Int): Observable<ResponseBody?>?

    // getComments

    // addComment

    // deleteComment

}