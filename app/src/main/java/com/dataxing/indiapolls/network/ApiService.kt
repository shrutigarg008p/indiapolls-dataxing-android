package com.dataxing.indiapolls.network

import com.dataxing.indiapolls.data.ApiResult
import com.dataxing.indiapolls.data.DashboardDto
import com.dataxing.indiapolls.data.SurveyItemDto
import com.dataxing.indiapolls.data.address.CountryDto
import com.dataxing.indiapolls.data.address.ZipCodeResponseDto
import com.dataxing.indiapolls.data.auth.DeviceTokenRequestDto
import com.dataxing.indiapolls.data.auth.ResendOtpRequestDto
import com.dataxing.indiapolls.data.auth.VerifyOtpRequestDto
import com.dataxing.indiapolls.data.auth.login.LoginWithFacebookRequestDto
import com.dataxing.indiapolls.data.auth.login.LoginWithOtpRequestDto
import com.dataxing.indiapolls.data.auth.login.LoginWithOtpUserResponseDto
import com.dataxing.indiapolls.data.auth.login.LoginWithPasswordRequestDto
import com.dataxing.indiapolls.data.auth.login.UserInfo
import com.dataxing.indiapolls.data.auth.onboarding.OnBoardingRequestDto
import com.dataxing.indiapolls.data.auth.onboarding.OnBoardingResponseDto
import com.dataxing.indiapolls.data.auth.register.RegisterRequestDto
import com.dataxing.indiapolls.data.auth.register.RegisterResponseDto
import com.dataxing.indiapolls.data.contactus.ContactUsRequestDto
import com.dataxing.indiapolls.data.contactus.ContactUsResponseDto
import com.dataxing.indiapolls.data.password.ChangePasswordRequestDto
import com.dataxing.indiapolls.data.password.ForgotPasswordRequestDto
import com.dataxing.indiapolls.data.profile.ProfileDto
import com.dataxing.indiapolls.data.profiles.ProfilesDto
import com.dataxing.indiapolls.data.profiles.ProfilesSurveyQuestionsDto
import com.dataxing.indiapolls.data.profiles.SubmitAnswersRequestDto
import com.dataxing.indiapolls.data.referral.ReferralRequestDto
import com.dataxing.indiapolls.data.referral.ReferralResponseDto
import com.dataxing.indiapolls.data.reward.RedeemPointsRequestDto
import com.dataxing.indiapolls.data.reward.RedemptionModeDto
import com.dataxing.indiapolls.data.reward.RewardDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {
    @POST("auth/user/login")
    suspend fun loginWithPassword(@Body requestDto: LoginWithPasswordRequestDto) : Response<ApiResult<UserInfo>>

    @POST("auth/user/continueWithMobile")
    suspend fun loginWithMobile(@Body requestDto: LoginWithOtpRequestDto) : Response<ApiResult<LoginWithOtpUserResponseDto>>

    @POST("auth/user/login")
    suspend fun loginWithFacebook(@Body requestDto: LoginWithFacebookRequestDto) : Response<ApiResult<UserInfo>>

    @POST("auth/user/signup")
    suspend fun register(@Body requestDto: RegisterRequestDto) : Response<ApiResult<RegisterResponseDto>>

    @POST("auth/user/reset-password")
    suspend fun forgotPassword(@Body requestDto: ForgotPasswordRequestDto) : Response<ApiResult<Any?>>

    @POST("auth/user/change-password")
    suspend fun changePassword(@Body requestDto: ChangePasswordRequestDto) : Response<ApiResult<Any?>>

    @POST("auth/user/verify-mobile")
    suspend fun verifyOtp(@Body requestDto: VerifyOtpRequestDto) : Response<ApiResult<Any?>>

    @POST("auth/user/resendOtp")
    suspend fun resendOtp(@Body requestDto: ResendOtpRequestDto) : Response<ApiResult<Any?>>

    @PUT("auth/user/update-basic-profile/{userId}")
    suspend fun updateBasicProfile(@Path("userId") userId: String, @Body requestDto: OnBoardingRequestDto) : Response<ApiResult<OnBoardingResponseDto>>

    @POST("auth/user/updateDeviceToken")
    suspend fun updateDeviceToken(@Body requestDto: DeviceTokenRequestDto) : Response<ApiResult<Any?>>

    @Multipart
    @POST("auth/user/uploadProfile")
    suspend fun uploadProfilePicture(@Part("userId") description: RequestBody,@Part photo: MultipartBody.Part) : Response<ApiResult<String?>>

    @GET("country/getAll/1000")
    suspend fun getCountries() : Response<ApiResult<List<CountryDto>>>

    @GET("surveys/panelist-surveys/{userId}")
    suspend fun getSurveys(@Path("userId") userId: String) : Response<ApiResult<List<SurveyItemDto>>>

    @GET("auth/user/respondentProfileOverview/{userId}")
    suspend fun getProfiles(@Path("userId") userId: String) : Response<ApiResult<ProfilesDto>>

    @GET("profileManagement/getOneDetails/{id}/{userId}")
    suspend fun getProfilesSurveyQuestions(@Path("id") id: String, @Path("userId") userId: String) : Response<ApiResult<ProfilesSurveyQuestionsDto>>

    @POST("profileManagement/createUserProfiles")
    suspend fun submitProfilesSurveyQuestions(@Body requestDto: SubmitAnswersRequestDto) : Response<ApiResult<Any?>>

    @GET("rewards/getAllByUserId/{userId}/1000")
    suspend fun getRewards(@Path("userId") userId: String) : Response<ApiResult<RewardDto>>

    @GET("redemption/getAll/100")
    suspend fun getAllRedemptionModes() : Response<ApiResult<List<RedemptionModeDto>>>

    @POST("redemptionRequest/create")
    suspend fun redeemPoints(@Body requestDto: RedeemPointsRequestDto) : Response<ApiResult<Any?>>

    @GET("auth/user/get-user/{userId}")
    suspend fun getUser(@Path("userId") userId: String) : Response<ApiResult<ProfileDto>>

    @GET("surveys/userRespondentDashboard/{userId}")
    suspend fun getDashboard(@Path("userId") userId: String) : Response<ApiResult<List<DashboardDto>>>

    @POST("messages/create")
    suspend fun contactUs(@Body requestDto: ContactUsRequestDto) : Response<ApiResult<ContactUsResponseDto>>

    @POST("auth/user/unSubscribeUser/{userId}")
    suspend fun unsubscribeUser(@Path("userId") userId: String) : Response<ApiResult<Any?>>

    @POST("auth/user/permanentlyDelete/{userId}/user")
    suspend fun deleteAccount(@Path("userId") userId: String) : Response<ApiResult<Any?>>

    @POST("referrals/create")
    suspend fun refer(@Body requestDto: ReferralRequestDto) : Response<ApiResult<ReferralResponseDto>>

    @GET("country/getAllStatesAndCitiesByZipCode/{zipCode}/1000")
    suspend fun getAllStatesAndCitiesByZipCode(@Path("zipCode") zipCode: String) : Response<ApiResult<ZipCodeResponseDto>>
}