package com.nutriai.app.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

@Serializable
data class SignupRequest(
    val email: String,
    val password: String,
    val name: String,
    val age: Int,
    @SerialName("weight_kg") val weightKg: Double,
    @SerialName("height_cm") val heightCm: Double,
    val gender: String,
    val goal: String,
    @SerialName("activity_level") val activityLevel: String,
    @SerialName("dietary_restrictions") val dietaryRestrictions: List<String> = emptyList()
)

@Serializable
data class LoginRequest(val email: String, val password: String)

@Serializable
data class RefreshRequest(@SerialName("refresh_token") val refreshToken: String)

@Serializable
data class AuthResponse(
    @SerialName("user_id") val userId: String,
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String? = null,
    @SerialName("token_type") val tokenType: String = "bearer",
    val targets: MacroTargets? = null
)

@Serializable
data class RefreshResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("token_type") val tokenType: String = "bearer"
)

@Serializable
data class MeResponse(
    @SerialName("user_id") val userId: String,
    val name: String,
    val email: String,
    val targets: MacroTargets,
    @SerialName("dietary_restrictions") val dietaryRestrictions: List<String> = emptyList(),
    val goal: String = "maintain",
    @SerialName("activity_level") val activityLevel: String = "moderate",
    val gender: String = "male" // Added to match likely backend storage even if missing in /me
)

@Serializable
data class MacroTargets(
    val calories: Double = 0.0,
    @SerialName("protein_g") val proteinG: Double = 0.0,
    @SerialName("carbs_g") val carbsG: Double = 0.0,
    @SerialName("fat_g") val fatG: Double = 0.0,
    @SerialName("fiber_g") val fiberG: Double = 0.0
)

@Serializable
data class FoodItem(
    val name: String,
    val quantity: String = "1 serving",
    val calories: Double = 0.0,
    @SerialName("protein_g") val proteinG: Double = 0.0,
    @SerialName("carbs_g") val carbsG: Double = 0.0,
    @SerialName("fat_g") val fatG: Double = 0.0,
    @SerialName("fiber_g") val fiberG: Double = 0.0
)

@Serializable
data class MealAnalyzeRequest(
    @SerialName("image_base64") val imageBase64: String? = null,
    val description: String = "",
    @SerialName("meal_type") val mealType: String = "lunch",
    @SerialName("image_url") val imageUrl: String? = null
)

@Serializable
data class AnalyzeResponse(
    @SerialName("review_required") val reviewRequired: Boolean = true,
    @SerialName("food_items") val foodItems: List<MacroFoodItem> = emptyList(),
    @SerialName("total_macros") val totalMacros: MacroTotals = MacroTotals(),
    val micros: JsonObject = JsonObject(emptyMap()),
    @SerialName("ai_confidence") val aiConfidence: Double = 0.0,
    @SerialName("ai_notes") val aiNotes: String = "",
    @SerialName("agent_steps") val agentSteps: List<JsonElement> = emptyList(),
    @SerialName("agent_invoked") val agentInvoked: Boolean = false,
    @SerialName("usda_validation") val usdaValidation: JsonObject = JsonObject(emptyMap()),
    @SerialName("suggested_log_payload") val suggestedLogPayload: ReviewedLogRequest? = null
)

@Serializable
data class MacroFoodItem(
    val name: String,
    val quantity: String = "1 serving",
    val calories: Double = 0.0,
    @SerialName("protein_g") val proteinG: Double = 0.0,
    @SerialName("carbs_g") val carbsG: Double = 0.0,
    @SerialName("fat_g") val fatG: Double = 0.0,
    @SerialName("fiber_g") val fiberG: Double = 0.0
)

@Serializable
data class ReviewedLogRequest(
    @SerialName("food_items") val foodItems: List<MacroFoodItem>,
    @SerialName("meal_type") val mealType: String,
    @SerialName("total_macros") val totalMacros: MacroTotals,
    val micros: JsonObject = JsonObject(emptyMap()),
    @SerialName("ai_confidence") val aiConfidence: Double,
    @SerialName("ai_notes") val aiNotes: String = "",
    @SerialName("usda_validation") val usdaValidation: JsonObject = JsonObject(emptyMap()),
    val description: String = "",
    @SerialName("image_url") val imageUrl: String? = null,
    val source: String = "quick_form"
)

@Serializable
data class LogMealResponse(
    @SerialName("log_id") val logId: String,
    @SerialName("food_items") val foodItems: List<FoodItem> = emptyList(),
    @SerialName("total_macros") val totalMacros: MacroTotals = MacroTotals(),
    @SerialName("daily_totals") val dailyTotals: MacroTotals = MacroTotals(),
    @SerialName("remaining_calories") val remainingCalories: Double = 0.0,
    @SerialName("remaining_protein_g") val remainingProteinG: Double = 0.0,
    @SerialName("meals_logged_today") val mealsLoggedToday: Int = 0,
    @SerialName("binge_alert") val bingeAlert: Boolean = false,
    val reviewed: Boolean = false
)

@Serializable
data class MacroTotals(
    val calories: Double = 0.0,
    @SerialName("protein_g") val proteinG: Double = 0.0,
    @SerialName("carbs_g") val carbsG: Double = 0.0,
    @SerialName("fat_g") val fatG: Double = 0.0,
    @SerialName("fiber_g") val fiberG: Double = 0.0
)

@Serializable
data class CoachTodayResponse(
    val card: CoachCard,
    val progress: CoachProgress,
    val signals: CoachSignals
)

@Serializable
data class CoachCard(
    val type: String,
    val priority: String,
    val title: String,
    val message: String,
    val action: CoachAction
)

@Serializable
data class CoachAction(val label: String, val method: String, val endpoint: String)

@Serializable
data class CoachProgress(
    @SerialName("calories_pct") val caloriesPct: Int,
    @SerialName("protein_pct") val proteinPct: Int,
    @SerialName("meals_logged") val mealsLogged: Int,
    @SerialName("remaining_calories") val remainingCalories: Double,
    @SerialName("remaining_protein_g") val remainingProteinG: Double
)

@Serializable
data class CoachSignals(
    @SerialName("binge_recovery_mode") val bingeRecoveryMode: Boolean,
    @SerialName("binge_days_count") val bingeDaysCount: Int,
    @SerialName("expiring_soon") val expiringSoon: List<String> = emptyList(),
    @SerialName("inventory_count") val inventoryCount: Int = 0
)

@Serializable
data class WhatCanIEatRequest(
    @SerialName("meal_type") val mealType: String? = null,
    @SerialName("max_options") val maxOptions: Int = 3
)

@Serializable
data class WhatCanIEatResponse(
    val summary: String = "",
    val options: List<EatOption> = emptyList(),
    val nudge: String = "",
    @SerialName("remaining_calories") val remainingCalories: Double = 0.0,
    @SerialName("remaining_protein_g") val remainingProteinG: Double = 0.0,
    @SerialName("expiring_soon") val expiringSoon: List<String> = emptyList()
)

@Serializable
data class EatOption(
    val name: String,
    @SerialName("meal_type") val mealType: String = "meal",
    @SerialName("why_this_fits") val whyThisFits: String = "",
    @SerialName("uses_inventory") val usesInventory: List<String> = emptyList(),
    @SerialName("missing_items") val missingItems: List<String> = emptyList(),
    @SerialName("estimated_macros") val estimatedMacros: MacroTotals = MacroTotals(),
    @SerialName("prep_time_mins") val prepTimeMins: Int = 0,
    @SerialName("log_hint") val logHint: String = ""
)

@Serializable
data class InventoryAddRequest(
    val name: String,
    val quantity: String? = null,
    @SerialName("expiry_date") val expiryDate: String? = null,
    val category: String? = null
)

@Serializable
data class InventoryAddResponse(
    @SerialName("item_id") val itemId: String,
    val name: String,
    @SerialName("days_until_expiry") val daysUntilExpiry: Int? = null,
    @SerialName("expiring_soon") val expiringSoon: Boolean = false
)

@Serializable
data class InventoryResponse(
    val items: List<InventoryItem> = emptyList(),
    @SerialName("total_items") val totalItems: Int = 0,
    @SerialName("expiring_soon_count") val expiringSoonCount: Int = 0
)

@Serializable
data class InventoryItem(
    val id: String,
    val name: String,
    val quantity: String? = null,
    val category: String? = null,
    @SerialName("expiry_date") val expiryDate: String? = null,
    @SerialName("days_until_expiry") val daysUntilExpiry: Int? = null,
    @SerialName("expiring_soon") val expiringSoon: Boolean = false
)

@Serializable
data class UpdateItemRequest(
    val quantity: String? = null,
    @SerialName("expiry_date") val expiryDate: String? = null,
    @SerialName("is_available") val isAvailable: Boolean? = null
)

@Serializable
data class ScanImageRequest(
    @SerialName("image_base64") val imageBase64: String
)

@Serializable
data class ScanImageResponse(
    val added: List<InventoryItem> = emptyList(),
    @SerialName("total_added") val totalAdded: Int = 0,
    @SerialName("scan_confidence") val scanConfidence: Double = 0.0,
    val notes: String = "",
    val message: String = ""
)

@Serializable
data class WaterLogRequest(@SerialName("amount_ml") val amountMl: Double)

@Serializable
data class WaterResponse(
    @SerialName("logged_ml") val loggedMl: Double? = null,
    @SerialName("total_ml") val totalMl: Double = 0.0,
    @SerialName("target_ml") val targetMl: Double = 2500.0,
    @SerialName("remaining_ml") val remainingMl: Double = 0.0,
    @SerialName("progress_pct") val progressPct: Int = 0
)

@Serializable
data class SchedulerStatusResponse(
    @SerialName("scheduler_running") val schedulerRunning: Boolean,
    val jobs: List<SchedulerJob>,
    @SerialName("total_jobs") val totalJobs: Int
)

@Serializable
data class SchedulerJob(
    val id: String,
    val name: String,
    @SerialName("next_run_time") val nextRunTime: String,
    val trigger: String
)

@Serializable
data class TriggerCoachResponse(
    @SerialName("triggered_for") val triggeredFor: String? = null,
    @SerialName("triggered_by") val triggeredBy: String? = null,
    @SerialName("total_users") val totalUsers: Int? = null,
    val outcome: JsonElement? = null,
    val outcomes: List<JsonElement>? = null
)

@Serializable
data class EditLogRequest(
    val calories: Double? = null,
    @SerialName("protein_g") val proteinG: Double? = null,
    @SerialName("carbs_g") val carbsG: Double? = null,
    @SerialName("fat_g") val fatG: Double? = null,
    @SerialName("fiber_g") val fiberG: Double? = null,
    @SerialName("meal_type") val mealType: String? = null,
    val micros: Map<String, Double>? = null
)

@Serializable
data class UpdateProfileRequest(
    val name: String? = null,
    val age: Int? = null,
    @SerialName("weight_kg") val weightKg: Double? = null,
    @SerialName("height_cm") val heightCm: Double? = null,
    val gender: String? = null,
    val goal: String? = null,
    @SerialName("activity_level") val activityLevel: String? = null,
    @SerialName("dietary_restrictions") val dietaryRestrictions: List<String>? = null,
    @SerialName("calorie_target") val calorieTarget: Double? = null,
    @SerialName("protein_target_g") val proteinTargetG: Double? = null,
    @SerialName("carbs_target_g") val carbsTargetG: Double? = null,
    @SerialName("fat_target_g") val fatTargetG: Double? = null,
    @SerialName("fiber_target_g") val fiberTargetG: Double? = null,
    @SerialName("water_target_ml") val waterTargetMl: Double? = null
)

@Serializable
data class UpdateProfileResponse(
    @SerialName("user_id") val userId: String,
    val updated: Boolean = false,
    val targets: MacroTargets? = null
)

@Serializable
data class UserProfile(
    @SerialName("user_id") val userId: String = "",
    val name: String = "",
    val email: String = "",
    val age: Int = 0,
    @SerialName("weight_kg") val weightKg: Double = 0.0,
    @SerialName("height_cm") val heightCm: Double = 0.0,
    val gender: String = "male",
    val goal: String = "maintain",
    @SerialName("activity_level") val activityLevel: String = "moderate",
    @SerialName("dietary_restrictions") val dietaryRestrictions: List<String> = emptyList(),
    val targets: MacroTargets = MacroTargets(),
    @SerialName("water_target_ml") val waterTargetMl: Double = 2500.0
)

@Serializable
data class ActivePlanResponse(
    @SerialName("active_plan") val activePlan: MealPlanResponse? = null
)

@Serializable
data class ReviseWeekResponse(
    val message: String,
    val plans: List<RevisedPlanDay> = emptyList(),
    @SerialName("weekly_surplus_kcal") val weeklySurplusKcal: Double? = null,
    @SerialName("daily_adjustment") val dailyAdjustment: Double? = null,
    @SerialName("adjusted_daily_target") val adjustedDailyTarget: Double? = null,
    @SerialName("days_revised") val daysRevised: Int? = null
)

@Serializable
data class RevisedPlanDay(
    val date: String,
    @SerialName("adjusted_target") val adjustedTarget: Double,
    val plan: MealPlanResponse
)

@Serializable
data class LogsForDateResponse(
    val logs: List<FoodLog> = emptyList()
)

@Serializable
data class FoodLog(
    @SerialName("log_id") val id: String,
    @SerialName("meal_type") val mealType: String,
    @SerialName("logged_at") val loggedAt: String,
    val description: String,
    @SerialName("food_items") val foodItems: List<FoodItem> = emptyList(),
    @SerialName("macros") val totalMacros: MacroTotals = MacroTotals(),
    val micros: Map<String, Double> = emptyMap(),
    val source: String = "ai_coach",
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("ai_confidence") val aiConfidence: Double? = null
)

@Serializable
data class MicrosResponse(
    val breakdown: Map<String, NutrientBreakdown> = emptyMap(),
    @SerialName("top_deficits") val topDeficits: List<NutrientDeficit> = emptyList(),
    @SerialName("all_good") val allGood: Boolean = false,
    @SerialName("logs_counted") val logsCounted: Int = 0
)

@Serializable
data class NutrientBreakdown(
    val consumed: Double,
    val rdi: Double,
    val pct: Int,
    val status: String
)

@Serializable
data class NutrientDeficit(
    val nutrient: String,
    val consumed: Double,
    val rdi: Double,
    val pct: Int,
    val status: String
)

@Serializable
data class DeviceTokenRequest(val deviceToken: String)

@Serializable
data class GeneratePlanRequest(val trigger: String = "user_request", val save: Boolean = true)

@Serializable
data class MealPlanResponse(
    @SerialName("plan_summary") val planSummary: String = "",
    val meals: List<PlanMeal> = emptyList(),
    @SerialName("total_plan_macros") val totalPlanMacros: MacroTotals = MacroTotals(),
    val trigger: String = "user_request",
    @SerialName("plan_id") val planId: String? = null
)

@Serializable
data class PlanMeal(
    @SerialName("meal_type") val mealType: String,
    val name: String,
    val ingredients: List<String> = emptyList(),
    @SerialName("estimated_macros") val estimatedMacros: MacroTotals = MacroTotals(),
    @SerialName("prep_time_mins") val prepTimeMins: Int = 0,
    @SerialName("recipe_steps") val recipeSteps: List<String> = emptyList(),
    @SerialName("uses_expiring_items") val usesExpiringItems: Boolean = false
)

@Serializable
data class AcceptPlanResponse(val accepted: Boolean, @SerialName("plan_id") val planId: String)

@Serializable
data class DailyHistoryResponse(
    val days: List<DailyEntry> = emptyList(),
    val streak: Int = 0,
    val period: JsonElement? = null
)

@Serializable
data class DailyEntry(
    val date: String,
    val calories: Double = 0.0,
    @SerialName("protein_g") val proteinG: Double = 0.0,
    @SerialName("carbs_g") val carbsG: Double = 0.0,
    @SerialName("fat_g") val fatG: Double = 0.0,
    @SerialName("fiber_g") val fiberG: Double = 0.0,
    @SerialName("meals_logged") val mealsLogged: Int = 0,
    @SerialName("target_calories") val targetCalories: Double = 0.0,
    @SerialName("is_binge_day") val isBingeDay: Boolean = false,
    @SerialName("vs_target") val vsTarget: Double = 0.0,
    val micros: Map<String, Double> = emptyMap()
)
