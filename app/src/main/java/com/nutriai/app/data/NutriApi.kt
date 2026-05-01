package com.nutriai.app.data

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface NutriApi {
    @POST("auth/signup")
    suspend fun signup(@Body request: SignupRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/refresh")
    suspend fun refresh(@Body request: RefreshRequest): RefreshResponse

    @GET("auth/me")
    suspend fun me(): MeResponse

    @GET("coach/today")
    suspend fun coachToday(): CoachTodayResponse

    @POST("meal/analyze")
    suspend fun analyzeMeal(@Body request: MealAnalyzeRequest): AnalyzeResponse

    @POST("meal/log-reviewed")
    suspend fun logReviewedMeal(@Body request: ReviewedLogRequest): LogMealResponse

    @POST("meal/log")
    suspend fun logMeal(@Body request: MealAnalyzeRequest): LogMealResponse

    @POST("meal/what-can-i-eat-now")
    suspend fun whatCanIEatNow(@Body request: WhatCanIEatRequest): WhatCanIEatResponse

    @POST("inventory/add")
    suspend fun addInventory(@Body request: InventoryAddRequest): InventoryAddResponse

    @GET("inventory/{userId}")
    suspend fun inventory(@Path("userId") userId: String): InventoryResponse

    @POST("plan/generate")
    suspend fun generatePlan(@Body request: GeneratePlanRequest): MealPlanResponse

    @POST("plan/accept/{planId}")
    suspend fun acceptPlan(@Path("planId") planId: String): AcceptPlanResponse

    @GET("history/{userId}/daily")
    suspend fun dailyHistory(
        @Path("userId") userId: String,
        @Query("days") days: Int = 7
    ): DailyHistoryResponse

    @GET("history/{userId}/logs")
    suspend fun logsForDate(
        @Path("userId") userId: String,
        @Query("date") date: String
    ): LogsForDateResponse

    @DELETE("history/log/{logId}")
    suspend fun deleteLog(@Path("logId") logId: String): Map<String, String>

    @PATCH("history/log/{logId}")
    suspend fun updateLog(
        @Path("logId") logId: String,
        @Body request: ReviewedLogRequest
    ): LogMealResponse

    @GET("users/{userId}")
    suspend fun getProfile(@Path("userId") userId: String): UserProfile

    @PUT("users/{userId}")
    suspend fun updateProfile(
        @Path("userId") userId: String,
        @Body request: UpdateProfileRequest
    ): UpdateProfileResponse

    @GET("plan/active/{userId}")
    suspend fun getActivePlan(@Path("userId") userId: String): ActivePlanResponse

    @POST("plan/revise-week")
    suspend fun reviseWeek(): ReviseWeekResponse

    @POST("utils/water")
    suspend fun logWater(@Body request: WaterLogRequest): WaterResponse

    @GET("utils/water/{userId}")
    suspend fun getWaterToday(@Path("userId") userId: String): WaterResponse

    @GET("utils/micros/{userId}")
    suspend fun getMicros(@Path("userId") userId: String): MicrosResponse

    @POST("utils/device-token")
    suspend fun registerDeviceToken(@Body request: DeviceTokenRequest): Map<String, String>

    @PATCH("inventory/{itemId}")
    suspend fun updateInventoryItem(
        @Path("itemId") itemId: String,
        @Body request: UpdateItemRequest
    ): Map<String, Boolean>

    @POST("inventory/scan-image")
    suspend fun scanInventoryImage(@Body request: ScanImageRequest): ScanImageResponse

    @DELETE("inventory/{itemId}")
    suspend fun deleteInventoryItem(@Path("itemId") itemId: String): Map<String, String>

    // Agents
    @POST("agents/trigger-coach")
    suspend fun triggerCoach(): TriggerCoachResponse

    @POST("agents/trigger-coach-all")
    suspend fun triggerCoachAll(): TriggerCoachResponse

    @GET("agents/scheduler-status")
    suspend fun getSchedulerStatus(): SchedulerStatusResponse
    @PATCH("history/log/{log_id}")
    suspend fun editLog(
        @Path("log_id") logId: String,
        @Body request: EditLogRequest
    ): Map<String, Boolean>
}
