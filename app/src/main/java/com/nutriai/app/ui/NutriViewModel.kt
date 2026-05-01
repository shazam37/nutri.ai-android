package com.nutriai.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nutriai.app.data.AnalyzeResponse
import com.nutriai.app.data.AuthStore
import com.nutriai.app.data.CoachTodayResponse
import com.nutriai.app.data.DailyHistoryResponse
import com.nutriai.app.data.EditLogRequest
import com.nutriai.app.data.InventoryAddRequest
import com.nutriai.app.data.InventoryResponse
import com.nutriai.app.data.LogMealResponse
import com.nutriai.app.data.LogsForDateResponse
import com.nutriai.app.data.MeResponse
import com.nutriai.app.data.MealPlanResponse
import com.nutriai.app.data.MicrosResponse
import com.nutriai.app.data.Network
import com.nutriai.app.data.NutriRepository
import com.nutriai.app.data.ScanImageResponse
import com.nutriai.app.data.SchedulerStatusResponse
import com.nutriai.app.data.SignupRequest
import com.nutriai.app.data.TriggerCoachResponse
import com.nutriai.app.data.UpdateItemRequest
import com.nutriai.app.data.UpdateProfileRequest
import com.nutriai.app.data.UserProfile
import com.nutriai.app.data.WaterResponse
import com.nutriai.app.data.WhatCanIEatResponse
import kotlinx.coroutines.launch

data class UiState(
    val loading: Boolean = false,
    val error: String? = null,
    val me: MeResponse? = null,
    val coach: CoachTodayResponse? = null,
    val inventory: InventoryResponse? = null,
    val plan: MealPlanResponse? = null,
    val history: DailyHistoryResponse? = null,
    val suggestions: WhatCanIEatResponse? = null,
    val pendingAnalysis: AnalyzeResponse? = null,
    val lastLog: LogMealResponse? = null,
    val selectedTab: AppTab = AppTab.Home,
    val profile: UserProfile? = null,
    val activePlan: MealPlanResponse? = null,
    val water: WaterResponse? = null,
    val micros: MicrosResponse? = null,
    val logsForDate: LogsForDateResponse? = null,
    val schedulerStatus: SchedulerStatusResponse? = null,
    val lastTriggerResult: TriggerCoachResponse? = null,
    val scanResult: ScanImageResponse? = null,
    val selectedHistoryDate: String? = null
)

enum class AppTab(val label: String) {
    Home("Home"),
    Log("Log"),
    Inventory("Kitchen"),
    Plan("Plan"),
    History("History"),
    Account("Me"),
    Admin("Admin")
}

class NutriViewModel(application: Application) : AndroidViewModel(application) {
    private val authStore = AuthStore(application)
    private val repository = NutriRepository(Network.createApi(authStore), authStore)

    var state = androidx.compose.runtime.mutableStateOf(UiState())
        private set

    val hasSession: Boolean
        get() = repository.hasSession

    init {
        if (hasSession) {
            refreshHome()
        }
    }

    fun selectTab(tab: AppTab) {
        state.value = state.value.copy(selectedTab = tab)
        when (tab) {
            AppTab.Home -> refreshHome()
            AppTab.Inventory -> loadInventory()
            AppTab.Plan -> loadActivePlan()
            AppTab.History -> loadHistory()
            AppTab.Account -> loadProfile()
            AppTab.Admin -> loadSchedulerStatus()
            AppTab.Log -> Unit
        }
    }

    fun login(email: String, password: String) = runLoading {
        repository.login(email, password)
        refreshHomeInternal()
    }

    fun signup(request: SignupRequest) = runLoading {
        repository.signup(request)
        refreshHomeInternal()
    }

    fun logout() {
        repository.logout()
        state.value = UiState()
    }

    fun refreshHome() = runLoading {
        state.value = state.value.copy(suggestions = null)
        refreshHomeInternal()
    }

    fun analyze(description: String, mealType: String, imageBase64: String? = null) = runLoading {
        val analysis = repository.analyzeMeal(description, mealType, imageBase64)
        android.util.Log.d("NutriAI", "Analyze Response URL: ${analysis.imageUrl}")
        state.value = state.value.copy(pendingAnalysis = analysis, error = null)
    }

    fun clearAnalysis() {
        state.value = state.value.copy(pendingAnalysis = null)
    }

    fun logReviewed(description: String, mealType: String) = runLoading {
        val analysis = requireNotNull(state.value.pendingAnalysis)
        val log = repository.logReviewed(analysis, mealType, description)
        state.value = state.value.copy(pendingAnalysis = null, lastLog = log)
        refreshHomeInternal()
    }

    fun logQuick(description: String, mealType: String, imageBase64: String? = null) = runLoading {
        val log = repository.logQuickMeal(description, mealType, imageBase64)
        state.value = state.value.copy(lastLog = log)
        refreshHomeInternal()
    }

    fun whatCanIEatNow(mealType: String? = null) = runLoading {
        val suggestions = repository.whatCanIEatNow(mealType)
        state.value = state.value.copy(suggestions = suggestions, selectedTab = AppTab.Home)
    }

    fun loadInventory() = runLoading {
        state.value = state.value.copy(inventory = repository.inventory())
    }

    fun addInventory(name: String, quantity: String, expiry: String, category: String) = runLoading {
        repository.addInventory(
            InventoryAddRequest(
                name = name,
                quantity = quantity.ifBlank { null },
                expiryDate = expiry.ifBlank { null },
                category = category.ifBlank { null }
            )
        )
        state.value = state.value.copy(inventory = repository.inventory())
    }

    fun generatePlan() = runLoading {
        state.value = state.value.copy(plan = repository.generatePlan())
    }

    fun acceptPlan() = runLoading {
        state.value.plan?.planId?.let { 
            repository.acceptPlan(it)
            state.value = state.value.copy(plan = null)
            loadActivePlan()
        }
    }

    fun loadHistory() = runLoading {
        state.value = state.value.copy(history = repository.history())
    }

    fun loadLogsForDate(date: String) = runLoading {
        state.value = state.value.copy(logsForDate = repository.logsForDate(date), selectedHistoryDate = date)
    }

    fun deleteLog(logId: String) = runLoading {
        repository.deleteLog(logId)
        // Refresh history or logs
        state.value = state.value.copy(history = repository.history())
    }

    fun updateLog(logId: String, request: EditLogRequest) = runLoading {
        repository.updateLog(logId, request)
        // Refresh history
        state.value = state.value.copy(history = repository.history())
    }

    fun loadProfile() = runLoading {
        state.value = state.value.copy(profile = repository.getProfile())
    }

    fun updateProfile(request: UpdateProfileRequest) = runLoading {
        repository.updateProfile(request)
        loadProfile()
        refreshHomeInternal()
        state.value = state.value.copy(error = "Profile updated!")
    }

    fun loadActivePlan() = runLoading {
        val response = repository.getActivePlan()
        state.value = state.value.copy(activePlan = response.activePlan)
    }

    fun clearActivePlan() {
        state.value = state.value.copy(activePlan = null)
    }

    fun reviseWeek() = runLoading {
        val response = repository.reviseWeek()
        // Handle revised plans, perhaps show a message
        state.value = state.value.copy(error = response.message)
    }

    fun logWater(amountMl: Double) = runLoading {
        val water = repository.logWater(amountMl)
        state.value = state.value.copy(water = water)
    }

    fun loadWaterToday() = runLoading {
        state.value = state.value.copy(water = repository.getWaterToday())
    }

    fun loadMicros() = runLoading {
        state.value = state.value.copy(micros = repository.getMicros())
    }

    fun registerDeviceToken(token: String) = runLoading {
        repository.registerDeviceToken(token)
    }

    fun updateInventoryItem(itemId: String, request: UpdateItemRequest) = runLoading {
        repository.updateInventoryItem(itemId, request)
        state.value = state.value.copy(inventory = repository.inventory())
    }

    fun scanFridge(imageBase64: String) = runLoading {
        val result = repository.scanInventoryImage(imageBase64)
        state.value = state.value.copy(scanResult = result)
        state.value = state.value.copy(inventory = repository.inventory())
    }

    fun deleteInventoryItem(itemId: String) = runLoading {
        repository.deleteInventoryItem(itemId)
        state.value = state.value.copy(inventory = repository.inventory())
    }

    fun triggerCoach() = runLoading {
        repository.triggerCoach()
        refreshHomeInternal()
        state.value = state.value.copy(error = "Coach updated! Check your home screen.")
    }

    fun triggerCoachAll() = runLoading {
        repository.triggerCoachAll()
        state.value = state.value.copy(error = "Fleet coach trigger successful.")
    }

    fun loadSchedulerStatus() = runLoading {
        val status = repository.getSchedulerStatus()
        state.value = state.value.copy(schedulerStatus = status)
    }

    private fun runLoading(block: suspend () -> Unit) {
        viewModelScope.launch {
            state.value = state.value.copy(loading = true, error = null)
            try {
                block()
            } catch (e: Exception) {
                state.value = state.value.copy(error = e.message)
            } finally {
                state.value = state.value.copy(loading = false)
            }
        }
    }

    private suspend fun refreshHomeInternal() {
        val today = java.time.LocalDate.now().toString()
        state.value = state.value.copy(
            me = repository.me(),
            coach = repository.coachToday(),
            water = repository.getWaterToday(),
            micros = repository.getMicros(),
            logsForDate = repository.logsForDate(today)
        )
    }
}
