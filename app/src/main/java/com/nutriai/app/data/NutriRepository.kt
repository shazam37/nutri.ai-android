package com.nutriai.app.data

class NutriRepository(
    private val api: NutriApi,
    private val authStore: AuthStore
) {
    val userId: String?
        get() = authStore.userId

    val hasSession: Boolean
        get() = !authStore.accessToken.isNullOrBlank()

    fun logout() {
        authStore.clear()
    }

    suspend fun signup(request: SignupRequest): AuthResponse {
        val response = api.signup(request)
        authStore.saveSession(response.userId, response.accessToken, response.refreshToken)
        return response
    }

    suspend fun login(email: String, password: String): AuthResponse {
        val response = api.login(LoginRequest(email, password))
        authStore.saveSession(response.userId, response.accessToken, response.refreshToken)
        return response
    }

    suspend fun me(): MeResponse = api.me()

    suspend fun coachToday(): CoachTodayResponse = api.coachToday()

    suspend fun analyzeMeal(description: String, mealType: String, imageBase64: String? = null): AnalyzeResponse {
        return api.analyzeMeal(MealAnalyzeRequest(description = description, mealType = mealType, imageBase64 = imageBase64))
    }

    suspend fun logReviewed(
        analysis: AnalyzeResponse,
        mealType: String,
        description: String
    ): LogMealResponse {
        val request = analysis.suggestedLogPayload ?: ReviewedLogRequest(
            foodItems = analysis.foodItems,
            mealType = mealType,
            totalMacros = analysis.totalMacros,
            micros = analysis.micros,
            aiConfidence = analysis.aiConfidence,
            aiNotes = analysis.aiNotes,
            usdaValidation = analysis.usdaValidation,
            description = description,
            imageUrl = analysis.imageUrl,
            source = if (analysis.imageUrl != null) "image" else "text"
        )

        return api.logReviewedMeal(request)
    }

    suspend fun logQuickMeal(description: String, mealType: String, imageBase64: String? = null): LogMealResponse {
        return api.logMeal(MealAnalyzeRequest(description = description, mealType = mealType, imageBase64 = imageBase64))
    }

    suspend fun whatCanIEatNow(mealType: String? = null): WhatCanIEatResponse {
        return api.whatCanIEatNow(WhatCanIEatRequest(mealType = mealType))
    }

    suspend fun inventory(): InventoryResponse {
        return api.inventory(requireNotNull(authStore.userId))
    }

    suspend fun addInventory(name: String, quantity: String, expiry: String, category: String, imageBase64: String? = null): InventoryAddResponse {
        return api.addInventory(
            InventoryAddRequest(
                name = name,
                quantity = quantity.ifBlank { null },
                expiryDate = expiry.ifBlank { null },
                category = category.ifBlank { null },
                imageBase64 = imageBase64
            )
        )
    }

    suspend fun generatePlan(): MealPlanResponse {
        return api.generatePlan(GeneratePlanRequest())
    }

    suspend fun acceptPlan(planId: String): AcceptPlanResponse {
        return api.acceptPlan(planId)
    }

    suspend fun history(days: Int = 7): DailyHistoryResponse {
        return api.dailyHistory(requireNotNull(authStore.userId), days)
    }

    suspend fun logsForDate(date: String? = null): LogsForDateResponse {
        return api.logsForDate(requireNotNull(authStore.userId), date ?: java.time.LocalDate.now().toString())
    }

    suspend fun deleteLog(logId: String): Map<String, String> {
        return api.deleteLog(logId)
    }

    suspend fun updateLog(logId: String, request: EditLogRequest): Boolean {
        return api.editLog(logId, request)["updated"] == true
    }

    suspend fun getProfile(): UserProfile {
        return api.getProfile(requireNotNull(authStore.userId))
    }

    suspend fun updateProfile(request: UpdateProfileRequest): UpdateProfileResponse {
        return api.updateProfile(requireNotNull(authStore.userId), request)
    }

    suspend fun getActivePlan(): ActivePlanResponse {
        return api.getActivePlan(requireNotNull(authStore.userId))
    }

    suspend fun reviseWeek(): ReviseWeekResponse {
        return api.reviseWeek()
    }

    suspend fun logWater(amountMl: Double): WaterResponse {
        return api.logWater(WaterLogRequest(amountMl))
    }

    suspend fun getWaterToday(): WaterResponse {
        return api.getWaterToday(requireNotNull(authStore.userId))
    }

    suspend fun getMicros(): MicrosResponse {
        return api.getMicros(requireNotNull(authStore.userId))
    }

    suspend fun registerDeviceToken(token: String): Map<String, String> {
        return api.registerDeviceToken(DeviceTokenRequest(token))
    }

    suspend fun updateInventoryItem(itemId: String, request: UpdateItemRequest): Boolean {
        return api.updateInventoryItem(itemId, request)["updated"] == true
    }

    suspend fun scanInventoryImage(imagePreviewBase64: String): ScanImageResponse {
        return api.scanInventoryImage(ScanImageRequest(imagePreviewBase64))
    }

    suspend fun deleteInventoryItem(itemId: String): Map<String, String> {
        return api.deleteInventoryItem(itemId)
    }

    suspend fun triggerCoach(): TriggerCoachResponse {
        return api.triggerCoach()
    }

    suspend fun triggerCoachAll(): TriggerCoachResponse {
        return api.triggerCoachAll()
    }

    suspend fun getSchedulerStatus(): SchedulerStatusResponse {
        return api.getSchedulerStatus()
    }
}
