package com.nutriai.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Inventory2
import androidx.compose.material.icons.outlined.LocalDining
import androidx.compose.material.icons.outlined.RestaurantMenu
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nutriai.app.data.AnalyzeResponse
import com.nutriai.app.data.CoachCard
import com.nutriai.app.data.EatOption
import com.nutriai.app.data.FoodItem
import com.nutriai.app.data.FoodLog
import com.nutriai.app.data.InventoryAddRequest
import com.nutriai.app.data.InventoryItem
import com.nutriai.app.data.LogsForDateResponse
import com.nutriai.app.data.MacroFoodItem
import com.nutriai.app.data.MealPlanResponse
import com.nutriai.app.data.PlanMeal
import com.nutriai.app.data.SchedulerJob
import com.nutriai.app.data.SignupRequest
import com.nutriai.app.data.UpdateItemRequest
import com.nutriai.app.data.UpdateProfileRequest
import com.nutriai.app.data.UserProfile
import com.nutriai.app.data.WaterResponse
import kotlin.math.roundToInt

@Composable
fun NutriAiApp(viewModel: NutriViewModel) {
    val ui by viewModel.state

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when {
            !viewModel.hasSession && ui.me == null -> AuthScreen(viewModel)
            ui.me == null && ui.loading -> LoadingScreen()
            ui.me == null -> AuthScreen(viewModel)
            else -> MainShell(viewModel)
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthScreen(viewModel: NutriViewModel) {
    val ui by viewModel.state
    var signup by rememberSaveable { mutableStateOf(false) } // Default to Login
    var step by rememberSaveable { mutableStateOf(1) } // 1: Login/Basic, 2: Body Info, 3: Goals

    var email by rememberSaveable { mutableStateOf("demo@nutriai.app") }
    var password by rememberSaveable { mutableStateOf("testpass123") }
    var name by rememberSaveable { mutableStateOf("Demo User") }
    var age by rememberSaveable { mutableStateOf("25") }
    var weight by rememberSaveable { mutableStateOf("70") }
    var height by rememberSaveable { mutableStateOf("175") }
    var goal by rememberSaveable { mutableStateOf("maintain") }
    var gender by rememberSaveable { mutableStateOf("male") }
    var activityLevel by rememberSaveable { mutableStateOf("moderate") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(Modifier.height(40.dp))
            Text("NutriAI", style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
            Text(
                "Your AI-Powered Nutrition Coach",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
            Spacer(Modifier.height(24.dp))
        }

        item {
            CardBlock {
                if (step == 1) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { signup = false },
                            colors = if (!signup) ButtonDefaults.buttonColors() else ButtonDefaults.filledTonalButtonColors(),
                            modifier = Modifier.weight(1f)
                        ) { Text("Login") }
                        Button(
                            onClick = { signup = true },
                            colors = if (signup) ButtonDefaults.buttonColors() else ButtonDefaults.filledTonalButtonColors(),
                            modifier = Modifier.weight(1f)
                        ) { Text("Join") }
                    }

                    Text(if (signup) "Welcome!" else "Welcome Back", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(if (signup) "Create an account to start your journey." else "Sign in to continue.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(email, { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    OutlinedTextField(
                        password,
                        { password = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    if (signup) {
                        OutlinedTextField(name, { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    }
                } else if (step == 2) {
                    Text("Tell us about you", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("This helps us calculate your daily targets.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(age, { age = it }, label = { Text("Age") }, modifier = Modifier.weight(1f), singleLine = true)
                        OutlinedTextField(weight, { weight = it }, label = { Text("Weight (kg)") }, modifier = Modifier.weight(1f), singleLine = true)
                    }
                    OutlinedTextField(height, { height = it }, label = { Text("Height (cm)") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                    
                    Spacer(Modifier.height(16.dp))
                    Text("Gender", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("male", "female", "other").forEach { g ->
                            FilterChip(
                                selected = gender == g,
                                onClick = { gender = g },
                                label = { Text(g.replaceFirstChar { it.uppercase() }) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                } else if (step == 3) {
                    Text("Set your goals", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text("Choose what you want to achieve.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(Modifier.height(16.dp))

                    Text("Goal", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("lose_weight", "maintain", "gain_muscle").forEach { g ->
                            FilterChip(
                                selected = goal == g,
                                onClick = { goal = g },
                                label = { Text(g.replace("_", " ").replaceFirstChar { it.uppercase() }) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    Text("Activity Level", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf("sedentary", "light", "moderate", "active", "very_active").forEach { a ->
                            FilterChip(
                                selected = activityLevel == a,
                                onClick = { activityLevel = a },
                                label = { Text(a.replace("_", " ").replaceFirstChar { it.uppercase() }) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        if (!signup) {
                            viewModel.login(email.trim(), password)
                        } else {
                            if (step < 3) step++
                            else {
                                viewModel.signup(
                                    SignupRequest(
                                        email = email.trim(),
                                        password = password,
                                        name = name,
                                        age = age.toIntOrNull() ?: 25,
                                        weightKg = weight.toDoubleOrNull() ?: 70.0,
                                        heightCm = height.toDoubleOrNull() ?: 175.0,
                                        gender = gender,
                                        goal = goal,
                                        activityLevel = activityLevel,
                                        dietaryRestrictions = emptyList()
                                    )
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !ui.loading
                ) {
                    Text(if (!signup) "Sign In" else if (step < 3) "Continue" else "Get Started")
                }

                if (signup && step > 1) {
                    TextButton(onClick = { step-- }, modifier = Modifier.fillMaxWidth()) {
                        Text("Back")
                    }
                }
            }
        }
        
        item {
            ErrorText(ui.error)
        }
    }
}

@Composable
private fun MainShell(viewModel: NutriViewModel) {
    val ui by viewModel.state
    Scaffold(
        bottomBar = {
            NavigationBar {
                AppTab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = ui.selectedTab == tab,
                        onClick = { viewModel.selectTab(tab) },
                        icon = { Icon(tab.icon(), contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            when (ui.selectedTab) {
                AppTab.Home -> HomeScreen(viewModel)
                AppTab.Log -> LogScreen(viewModel)
                AppTab.Inventory -> InventoryScreen(viewModel)
                AppTab.Plan -> PlanScreen(viewModel)
                AppTab.History -> HistoryScreen(viewModel)
                AppTab.Account -> SettingsScreen(viewModel)
                AppTab.Admin -> AdminScreen(viewModel)
            }
            if (ui.loading) {
                LinearProgressIndicator(Modifier.fillMaxWidth().align(Alignment.TopCenter))
            }
        }
    }
}

private fun AppTab.icon(): ImageVector = when (this) {
    AppTab.Home -> Icons.Outlined.Home
    AppTab.Log -> Icons.Outlined.LocalDining
    AppTab.Inventory -> Icons.Outlined.Inventory2
    AppTab.Plan -> Icons.Outlined.RestaurantMenu
    AppTab.History -> Icons.Outlined.Analytics
    AppTab.Account -> Icons.Outlined.Person
    AppTab.Admin -> Icons.Outlined.Terminal
}

@Composable
private fun HomeScreen(viewModel: NutriViewModel) {
    val ui by viewModel.state
    val me = ui.me
    val coach = ui.coach

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.weight(1f)) {
                    Text(
                        text = "Hello, ${me?.name?.split(" ")?.firstOrNull() ?: "there"}!",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Let's hit your targets today.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            me?.name?.firstOrNull()?.toString() ?: "U",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }

        item {
            CardBlock {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
                    val progress = coach?.progress
                    val meTargets = me?.targets
                    val calTarget = meTargets?.calories ?: 2000.0
                    val proteinTarget = meTargets?.proteinG ?: 100.0

                    // Use remaining values from coach, otherwise show full target if nothing logged
                    val remainingCals = progress?.remainingCalories ?: calTarget
                    val remainingProtein = progress?.remainingProteinG ?: proteinTarget

                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(120.dp)) {
                        CircularProgressIndicator(
                            progress = { (progress?.caloriesPct ?: 0) / 100f },
                            modifier = Modifier.size(110.dp),
                            strokeWidth = 10.dp,
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        )
                        CircularProgressIndicator(
                            progress = { (progress?.proteinPct ?: 0) / 100f },
                            modifier = Modifier.size(80.dp),
                            strokeWidth = 8.dp,
                            color = MaterialTheme.colorScheme.tertiary,
                            trackColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.1f)
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val pct = progress?.caloriesPct ?: 0
                            Text(
                                "$pct%",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Text("of goal", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                    Spacer(Modifier.width(20.dp))
                    Column(Modifier.weight(1f)) {
                        TargetMiniRow(
                            label = "Calories",
                            value = "${remainingCals.roundToInt()} kcal left",
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(8.dp))
                        TargetMiniRow(
                            label = "Protein",
                            value = "${remainingProtein.roundToInt()}g left",
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(Modifier.height(8.dp))
                        TargetMiniRow(
                            label = "Activity",
                            value = "${progress?.mealsLogged ?: 0} meals logged",
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                HorizontalDivider(Modifier.padding(vertical = 12.dp))

                // Macro Tracking Row
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    val logs = ui.logsForDate?.logs ?: emptyList()
                    val totalCarbs = logs.sumOf { it.totalMacros.carbsG }
                    val totalFats = logs.sumOf { it.totalMacros.fatG }
                    val totalFiber = logs.sumOf { it.totalMacros.fiberG }

                    MacroMiniBox("Carbs", totalCarbs.roundToInt(), "g", Modifier.weight(1f))
                    MacroMiniBox("Fats", totalFats.roundToInt(), "g", Modifier.weight(1f))
                    MacroMiniBox("Fiber", totalFiber.roundToInt(), "g", Modifier.weight(1f))
                }

                HorizontalDivider(Modifier.padding(vertical = 12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(60.dp, 80.dp)) {
                        WaterJar(progress = (ui.water?.progressPct ?: 0) / 100f)
                    }
                    Spacer(Modifier.width(16.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Hydration", fontWeight = FontWeight.Bold)
                        val water = ui.water
                        if (water != null) {
                            Text(
                                "${water.totalMl.roundToInt()} / ${water.targetMl.roundToInt()} ml",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 4.dp)) {
                            OutlinedButton(
                                onClick = { viewModel.logWater(250.0) },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text("+250ml", style = MaterialTheme.typography.labelSmall)
                            }
                            OutlinedButton(
                                onClick = { viewModel.loadWaterToday() },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text("Refresh", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }
        }

        coach?.card?.let { card ->
            item {
                CoachCardView(card, viewModel)
            }
        }

        val suggestions = ui.suggestions
        if (suggestions != null) {
            item {
                Spacer(Modifier.height(8.dp))
                Text("Smart Suggestions", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            items(suggestions.options) { option ->
                EatOptionCard(option)
            }
            item {
                TextButton(onClick = { viewModel.refreshHome() }, modifier = Modifier.fillMaxWidth()) {
                    Text("Clear Suggestions")
                }
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                var showBingeInfo by remember { mutableStateOf(false) }
                SignalChip(
                    label = if (coach?.signals?.bingeRecoveryMode == true) "Binge Recovery" else "Steady",
                    icon = Icons.Outlined.Analytics,
                    active = coach?.signals?.bingeRecoveryMode == true,
                    color = Color(0xFFE57373),
                    onClick = { showBingeInfo = true }
                )
                if (showBingeInfo) {
                    AlertDialog(
                        onDismissRequest = { showBingeInfo = false },
                        title = { Text("Binge Recovery Mode") },
                        text = {
                            Text(
                                if (coach?.signals?.bingeRecoveryMode == true)
                                    "Your weekly intake has exceeded your target. NutriAI is currently in recovery mode, adjusting your remaining daily targets to keep you on track for the week."
                                else
                                    "You are currently in steady mode. Your targets are based on your normal profile settings."
                            )
                        },
                        confirmButton = {
                            TextButton(onClick = { showBingeInfo = false }) { Text("Got it") }
                        }
                    )
                }
                SignalChip(
                    label = "${coach?.signals?.inventoryCount ?: 0} in Kitchen",
                    icon = Icons.Outlined.Inventory2,
                    active = (coach?.signals?.inventoryCount ?: 0) > 0,
                    onClick = { viewModel.selectTab(AppTab.Inventory) }
                )
            }
        }

        item {
            MicrosView(viewModel)
        }

        item { ErrorText(ui.error) }
    }
}

@Composable
private fun MacroMiniBox(label: String, value: Int, unit: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("$value$unit", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun MicrosView(viewModel: NutriViewModel) {
    val ui by viewModel.state
    val micros = ui.micros

    CardBlock {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Micronutrients", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            TextButton(onClick = { viewModel.loadMicros() }) {
                Text("Refresh")
            }
        }
        
        if (micros == null) {
            Text("Load today's micronutrient breakdown from your meals.", style = MaterialTheme.typography.bodySmall)
        } else {
            if (micros.allGood) {
                Text("✅ All key nutrients are on track today!", color = Color(0xFF43A047), style = MaterialTheme.typography.bodyMedium)
            }
            
            if (micros.topDeficits.isNotEmpty()) {
                Text("Top Deficits Today:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                for (deficit in micros.topDeficits) {
                    NutrientRow(deficit.nutrient, deficit.pct)
                }
            }

            if (micros.breakdown.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Text("All Nutrients:", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                micros.breakdown.forEach { (name, data) ->
                    NutrientRow(name, data.pct)
                }
            }
        }
    }
}

@Composable
private fun NutrientRow(name: String, pct: Int) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
        Text(name.replace("_", " ").replaceFirstChar { it.uppercase() }, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall)
        LinearProgressIndicator(
            progress = { pct / 100f },
            modifier = Modifier.width(100.dp).height(6.dp).clip(RoundedCornerShape(3.dp)),
            color = when {
                pct < 30 -> MaterialTheme.colorScheme.error
                pct < 70 -> MaterialTheme.colorScheme.secondary
                else -> Color(0xFF43A047)
            }
        )
        Spacer(Modifier.width(8.dp))
        Text("$pct%", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun WaterJar(progress: Float) {
    val color = MaterialTheme.colorScheme.primary
    androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val jarPath = Path().apply {
            moveTo(width * 0.1f, 0f)
            lineTo(width * 0.9f, 0f)
            lineTo(width * 0.85f, height * 0.9f)
            quadraticTo(width * 0.85f, height, width * 0.5f, height)
            quadraticTo(width * 0.15f, height, width * 0.15f, height * 0.9f)
            close()
        }

        // Draw Jar Background (empty state)
        drawPath(jarPath, color = color.copy(alpha = 0.1f))
        
        // Draw Water
        clipPath(jarPath) {
            val waterHeight = height * progress
            drawRect(
                color = color,
                topLeft = androidx.compose.ui.geometry.Offset(0f, height - waterHeight),
                size = androidx.compose.ui.geometry.Size(width, waterHeight)
            )
        }

        // Draw Jar Outline
        drawPath(jarPath, color = color, style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx()))
    }
}

@Composable
private fun TargetMiniRow(label: String, value: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(shape = CircleShape, color = color, modifier = Modifier.size(8.dp)) {}
        Spacer(Modifier.width(8.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SignalChip(label: String, icon: ImageVector, active: Boolean, color: Color = MaterialTheme.colorScheme.secondary, onClick: () -> Unit = {}) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (active) color.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, if (active) color.copy(alpha = 0.5f) else Color.Transparent)
    ) {
        Row(
            Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, modifier = Modifier.size(16.dp), tint = if (active) color else MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.width(6.dp))
            Text(label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun CoachCardView(card: CoachCard, viewModel: NutriViewModel) {
    val bgColor = when (card.priority) {
        "high" -> MaterialTheme.colorScheme.errorContainer
        "medium" -> MaterialTheme.colorScheme.primaryContainer
        else -> MaterialTheme.colorScheme.secondaryContainer
    }
    val contentColor = when (card.priority) {
        "high" -> MaterialTheme.colorScheme.onErrorContainer
        "medium" -> MaterialTheme.colorScheme.onPrimaryContainer
        else -> MaterialTheme.colorScheme.onSecondaryContainer
    }

    Column {
        Text("Coach Recommendation", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(8.dp))
        ElevatedCard(
            colors = CardDefaults.elevatedCardColors(containerColor = bgColor),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        if (card.priority == "high") Icons.Outlined.Analytics else Icons.Outlined.Check,
                        null,
                        tint = contentColor
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(card.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = contentColor)
                }
                Spacer(Modifier.height(8.dp))
                Text(card.message, style = MaterialTheme.typography.bodyLarge, color = contentColor.copy(alpha = 0.8f))
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = {
                        when (card.action.endpoint) {
                            "/api/v1/meal/what-can-i-eat-now" -> viewModel.whatCanIEatNow()
                            "/api/v1/plan/generate" -> viewModel.generatePlan()
                            "/api/v1/meal/analyze" -> viewModel.selectTab(AppTab.Log)
                            "/api/v1/utils/water" -> viewModel.logWater(250.0)
                            "/api/v1/plan/revise-week" -> viewModel.reviseWeek()
                            else -> viewModel.whatCanIEatNow()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = contentColor,
                        contentColor = bgColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(card.action.label, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun LogScreen(viewModel: NutriViewModel) {
    val ui by viewModel.state
    var description by rememberSaveable { mutableStateOf("") }
    var mealType by rememberSaveable { mutableStateOf("lunch") }
    var imageBase64 by remember { mutableStateOf<String?>(null) }
    val analysis = ui.pendingAnalysis

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Log Your Meal", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Snap a photo or type what you ate.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        item {
            CardBlock {
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("What did you eat?") },
                    placeholder = { Text("e.g., Grilled chicken salad with avocado") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
                
                Spacer(Modifier.height(8.dp))
                
                Text("Meal Type", style = MaterialTheme.typography.labelMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    listOf("breakfast", "lunch", "dinner", "snack").forEach { type ->
                        FilterChip(
                            selected = mealType == type,
                            onClick = { mealType = type },
                            label = { Text(type.replaceFirstChar { it.uppercase() }) }
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = { 
                            // Simulate camera
                            imageBase64 = "simulated_food_image_base64"
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Outlined.Add, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Add Photo")
                    }
                    Button(
                        onClick = { viewModel.analyze(description, mealType, imageBase64) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !ui.loading && (description.isNotBlank() || imageBase64 != null)
                    ) {
                        Text("Analyze")
                    }
                }
                
                if (imageBase64 != null) {
                    Spacer(Modifier.height(8.dp))
                    Text("Photo attached", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        if (analysis != null) {
            item {
                ElevatedCard(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("AI Analysis", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            Surface(
                                shape = CircleShape,
                                color = if (analysis.aiConfidence > 0.8) Color(0xFF81C784) else Color(0xFFFFB74D),
                                modifier = Modifier.size(8.dp)
                            ) {}
                            Spacer(Modifier.width(6.dp))
                            Text("${(analysis.aiConfidence * 100).roundToInt()}% confident", style = MaterialTheme.typography.labelSmall)
                        }
                        
                        if (analysis.aiNotes.isNotBlank()) {
                            Text(analysis.aiNotes, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Spacer(Modifier.height(12.dp))
                        HorizontalDivider()
                        Spacer(Modifier.height(12.dp))

                        for (food in analysis.foodItems) {
                            MacroFoodRow(food)
                        }

                        Spacer(Modifier.height(12.dp))
                        HorizontalDivider()
                        Spacer(Modifier.height(12.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text("Total Calories", style = MaterialTheme.typography.labelMedium)
                                Text("${analysis.totalMacros.calories.roundToInt()} kcal", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                            }
                            Button(
                                onClick = { viewModel.logReviewed(description, mealType) },
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Outlined.Check, null)
                                Spacer(Modifier.width(8.dp))
                                Text("Confirm Log")
                            }
                        }
                    }
                }
            }
        }

        ui.lastLog?.let { log ->
            item {
                CardBlock(container = MaterialTheme.colorScheme.tertiaryContainer) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Check, null, tint = MaterialTheme.colorScheme.onTertiaryContainer)
                        Spacer(Modifier.width(8.dp))
                        Text("Meal Logged Successfully", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onTertiaryContainer)
                    }
                    Text("Total today: ${log.dailyTotals.calories.roundToInt()} kcal", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        item { ErrorText(ui.error) }
    }
}

@Composable
private fun InventoryScreen(viewModel: NutriViewModel) {
    val ui by viewModel.state
    var name by rememberSaveable { mutableStateOf("") }
    var quantity by rememberSaveable { mutableStateOf("") }
    var expiry by rememberSaveable { mutableStateOf("") }
    var category by rememberSaveable { mutableStateOf("protein") }
    var editingItem by rememberSaveable { mutableStateOf<InventoryItem?>(null) }
    var showScanPrompt by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("My Kitchen", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text("Manage your inventory for better AI suggestions.")
                }
                IconButton(onClick = { showScanPrompt = true }) {
                    Icon(Icons.Outlined.Search, "Scan Fridge", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }

        if (showScanPrompt) {
            item {
                CardBlock(container = MaterialTheme.colorScheme.primaryContainer) {
                    Text("Scan Fridge or Groceries", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                    Text("Take a photo to bulk-add items using AI.", style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = {
                            // In real app, launch camera. Here we simulate base64
                            viewModel.scanFridge("simulated_base64_fridge_image")
                            showScanPrompt = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Simulate Camera Scan")
                    }
                    TextButton(onClick = { showScanPrompt = false }, modifier = Modifier.fillMaxWidth()) {
                        Text("Cancel")
                    }
                }
            }
        }

        item {
            CardBlock {
                Text(if (editingItem != null) "Edit Item" else "Add New Item", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(name, { name = it }, label = { Text("Food Name") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(quantity, { quantity = it }, label = { Text("Quantity") }, modifier = Modifier.weight(1f), singleLine = true)
                    OutlinedTextField(expiry, { expiry = it }, label = { Text("Expiry (YYYY-MM-DD)") }, modifier = Modifier.weight(1f), singleLine = true)
                }
                
                Spacer(Modifier.height(8.dp))
                Text("Category", style = MaterialTheme.typography.labelMedium)
                val categories = listOf("protein", "vegetable", "dairy", "grain", "spice", "fruit", "other")
                Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    categories.forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat.replaceFirstChar { it.uppercase() }) }
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = {
                        if (editingItem != null) {
                            viewModel.updateInventoryItem(
                                editingItem!!.id,
                                UpdateItemRequest(
                                    quantity = quantity.ifBlank { null },
                                    expiryDate = expiry.ifBlank { null }
                                )
                            )
                            editingItem = null
                        } else {
                            viewModel.addInventory(name, quantity, expiry, category)
                        }
                        name = ""
                        quantity = ""
                        expiry = ""
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(if (editingItem != null) "Update Kitchen" else "Add to Kitchen")
                }
                if (editingItem != null) {
                    TextButton(onClick = { editingItem = null; name = ""; quantity = ""; expiry = ""; category = "protein" }, modifier = Modifier.fillMaxWidth()) {
                        Text("Cancel")
                    }
                }
            }
        }

        items(ui.inventory?.items.orEmpty()) { item ->
            InventoryCard(
                item = item,
                onEdit = {
                    editingItem = item
                    name = item.name
                    quantity = item.quantity ?: ""
                    expiry = item.expiryDate ?: ""
                    category = item.category ?: "protein"
                },
                onDelete = {
                    viewModel.deleteInventoryItem(item.id)
                }
            )
        }
        item { ErrorText(ui.error) }
    }
}

@Composable
private fun PlanScreen(viewModel: NutriViewModel) {
    val ui by viewModel.state
    val activePlan = ui.activePlan
    val pendingPlan = ui.plan

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Meal Planning", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("AI-generated plans tailored to your kitchen and goals.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        if (activePlan != null) {
            item {
                Text("Today's Active Plan", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            item {
                ActivePlanView(activePlan)
            }
        } else {
            item {
                CardBlock(container = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Outlined.Restaurant, null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
                        Spacer(Modifier.width(12.dp))
                        Text("No active plan for today", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
                    }
                }
            }
        }

        item {
            Button(
                onClick = { viewModel.generatePlan() },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                enabled = !ui.loading
            ) {
                Icon(Icons.Outlined.Add, null)
                Spacer(Modifier.width(8.dp))
                Text("Generate New Plan")
            }
        }

        if (pendingPlan != null) {
            item {
                HorizontalDivider(Modifier.padding(vertical = 8.dp))
                Text("Review Suggestion", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            item {
                CardBlock {
                    Text(pendingPlan.planSummary, style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(12.dp))
                    for (meal in pendingPlan.meals) {
                        PlanMealCard(meal)
                        Spacer(Modifier.height(8.dp))
                    }
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { viewModel.acceptPlan() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Follow This Plan")
                    }
                }
            }
        }

        // Binge Recovery / Revision section
        item {
            CardBlock(container = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)) {
                Text("Weekly Rebalancing", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("If you've had a high-calorie day, NutriAI can redistribute your remaining weekly budget.", style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { viewModel.reviseWeek() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Revise Remainder of Week")
                }
            }
        }

        item { ErrorText(ui.error) }
    }
}

@Composable
private fun ActivePlanView(plan: MealPlanResponse) {
    CardBlock(container = MaterialTheme.colorScheme.primaryContainer) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.Check, null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
            Spacer(Modifier.width(8.dp))
            Text("Currently Following", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
        Spacer(Modifier.height(8.dp))
        Text(plan.planSummary, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
        
        Spacer(Modifier.height(16.dp))
        for (meal in plan.meals) {
            ListItem(
                headlineContent = { Text(meal.name, fontWeight = FontWeight.Bold) },
                supportingContent = { Text("${meal.mealType.replaceFirstChar { it.uppercase() }} • ${meal.prepTimeMins} mins") },
                overlineContent = { if (meal.usesExpiringItems) Text("USES EXPIRING ITEMS", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelSmall) },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
            )
        }
    }
}

@Composable
private fun HistoryScreen(viewModel: NutriViewModel) {
    val ui by viewModel.state
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("History & Trends", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text("Streak: ${ui.history?.streak ?: 0} days 🔥")
                }
            }
        }

        item {
            CardBlock {
                Text("Weekly Nutrients", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                MacroTrendChart(ui.history?.days ?: emptyList())
            }
        }

        item {
            CardBlock {
                Text("Calorie Trend", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                CalorieTrendChart(ui.history?.days ?: emptyList())
            }
        }

        item {
            CardBlock {
                Text("Daily Micronutrient Signature", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("RDI % for key nutrients by day", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(16.dp))
                DailyMicroHeatmap(ui.history?.days ?: emptyList())
            }
        }

        item {
            Text("Daily History", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        for (day in ui.history?.days ?: emptyList()) {
            item {
                CardBlock {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(day.date, fontWeight = FontWeight.Bold)
                            Text("${day.mealsLogged} meals logged", style = MaterialTheme.typography.bodySmall)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("${day.calories.roundToInt()} kcal", fontWeight = FontWeight.Bold, color = if (day.isBingeDay) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary)
                            val diff = day.vsTarget
                            Text(
                                if (diff > 0) "+${diff.roundToInt()} over" else "${diff.roundToInt()} left",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (diff > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                    
                    val logsForThisDay = if (ui.selectedHistoryDate == day.date) {
                        ui.logsForDate?.logs ?: emptyList()
                    } else {
                        emptyList()
                    }

                    if (logsForThisDay.isNotEmpty()) {
                        HorizontalDivider(Modifier.padding(vertical = 12.dp))
                        Text("Meals for ${day.date}:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(8.dp))
                        logsForThisDay.forEach { log ->
                            Column(Modifier.padding(vertical = 4.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(log.description, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, modifier = Modifier.weight(1f))
                                    Text("${log.totalMacros.calories.roundToInt()} kcal", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Text("P: ${log.totalMacros.proteinG.roundToInt()}g", style = MaterialTheme.typography.labelSmall)
                                    Text("C: ${log.totalMacros.carbsG.roundToInt()}g", style = MaterialTheme.typography.labelSmall)
                                    Text("F: ${log.totalMacros.fatG.roundToInt()}g", style = MaterialTheme.typography.labelSmall)
                                    if (log.totalMacros.fiberG > 0) {
                                        Text("Fiber: ${log.totalMacros.fiberG.roundToInt()}g", style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                                if (log.micros.isNotEmpty()) {
                                    Text(
                                        text = "Micros: " + log.micros.entries.joinToString(", ") { "${it.key}: ${it.value.roundToInt()}" },
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Text("${log.mealType.uppercase()} • ${log.loggedAt.split("T").lastOrNull()?.take(5) ?: ""}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }

                    Button(
                        onClick = { viewModel.loadLogsForDate(day.date) },
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.filledTonalButtonColors()
                    ) {
                        Text(if (logsForThisDay.isNotEmpty()) "Refresh Meals" else "View Meals for this Day", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

@Composable
private fun DailyMicroHeatmap(days: List<com.nutriai.app.data.DailyEntry>) {
    val last7Days = days.takeLast(7)
    if (last7Days.isEmpty()) {
        Text("No data available for heatmap", style = MaterialTheme.typography.bodySmall)
        return
    }

    // Get list of unique micros across all days
    // Make names more robust by title-casing them if they are all caps or lowercase
    val microNames = last7Days.flatMap { it.micros.keys }
        .map { it.lowercase().replaceFirstChar { char -> char.uppercase() } }
        .distinct().sorted().take(10)
    
    if (microNames.isEmpty()) {
        Text("No micronutrient data recorded yet. Log meals with micro details to see your signature.", style = MaterialTheme.typography.bodySmall, color = Color.Gray, modifier = Modifier.padding(8.dp))
        return
    }

    // Mock RDI targets for calculation
    val rdiTargets = mapOf(
        "Iron" to 18.0,
        "Calcium" to 1000.0,
        "Vitamin C" to 90.0,
        "Zinc" to 11.0,
        "Magnesium" to 400.0,
        "Potassium" to 3500.0,
        "Vitamin D" to 20.0,
        "Vitamin B12" to 2.4,
        "Fiber" to 30.0,
        "Sodium" to 2300.0
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        // Header Row (Dates)
        Row(modifier = Modifier.padding(bottom = 8.dp)) {
            Spacer(Modifier.width(80.dp))
            last7Days.forEach { day ->
                val label = try {
                    val parts = day.date.split("-")
                    if (parts.size == 3) {
                        // MM/DD/YY
                        "${parts[1]}/${parts[2]}/${parts[0].takeLast(2)}"
                    } else day.date
                } catch(e: Exception) { "?? " }
                Text(
                    text = label,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Nutrient Rows
        microNames.forEach { micro ->
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = micro,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.width(80.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                last7Days.forEach { day ->
                    // Find key in case-insensitive way
                    val consumed = day.micros.entries.find { it.key.lowercase() == micro.lowercase() }?.value ?: 0.0
                    val target = rdiTargets[micro] ?: 100.0
                    val pct = ((consumed / target) * 100).toInt()
                    
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(30.dp)
                            .padding(1.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                color = when {
                                    consumed == 0.0 -> Color.LightGray.copy(alpha = 0.2f)
                                    pct < 50 -> Color(0xFFE57373).copy(alpha = 0.8f)
                                    pct < 90 -> Color(0xFFFFD54F).copy(alpha = 0.8f)
                                    else -> Color(0xFF81C784).copy(alpha = 0.8f)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (consumed > 0.0) {
                            Text(
                                "$pct%",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                                color = Color.Black.copy(alpha = 0.7f),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
        
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            LegendDot("Low (<50%)", Color(0xFFE57373))
            Spacer(Modifier.width(16.dp))
            LegendDot("Mid (50-90%)", Color(0xFFFFD54F))
            Spacer(Modifier.width(16.dp))
            LegendDot("Target (>90%)", Color(0xFF81C784))
        }
    }
}

@Composable
private fun LegendDot(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(8.dp).clip(CircleShape).background(color))
        Spacer(Modifier.width(4.dp))
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun MacroTrendChart(days: List<com.nutriai.app.data.DailyEntry>) {
    val maxMacro = (days.maxOfOrNull { maxOf(it.proteinG, it.carbsG, it.fatG) } ?: 100.0).coerceAtLeast(100.0).toFloat()
    
    androidx.compose.foundation.Canvas(
        modifier = Modifier.fillMaxWidth().height(150.dp).padding(horizontal = 8.dp)
    ) {
        val width = size.width
        val height = size.height
        val spacing = width / 7f
        val barWidth = spacing / 4f

        days.takeLast(7).forEachIndexed { index, day ->
            val xBase = index * spacing
            
            // Protein (Blue)
            val pHeight = (day.proteinG.toFloat() / maxMacro * height).coerceAtLeast(2f)
            drawRect(Color(0xFF64B5F6), androidx.compose.ui.geometry.Offset(xBase, height - pHeight), androidx.compose.ui.geometry.Size(barWidth, pHeight))
            
            // Carbs (Yellow)
            val cHeight = (day.carbsG.toFloat() / maxMacro * height).coerceAtLeast(2f)
            drawRect(Color(0xFFFFD54F), androidx.compose.ui.geometry.Offset(xBase + barWidth, height - cHeight), androidx.compose.ui.geometry.Size(barWidth, cHeight))
            
            // Fat (Red)
            val fHeight = (day.fatG.toFloat() / maxMacro * height).coerceAtLeast(2f)
            drawRect(Color(0xFFE57373), androidx.compose.ui.geometry.Offset(xBase + barWidth * 2, height - fHeight), androidx.compose.ui.geometry.Size(barWidth, fHeight))

            // Draw date label
            drawContext.canvas.nativeCanvas.drawText(
                formatDateForChart(day.date),
                xBase + barWidth * 1.5f,
                height + 20.dp.toPx(),
                android.graphics.Paint().apply {
                    color = android.graphics.Color.GRAY
                    textSize = 24f
                    textAlign = android.graphics.Paint.Align.CENTER
                }
            )
        }
    }
    Spacer(Modifier.height(28.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 8.dp)) {
        LegendItem("Protein", Color(0xFF64B5F6))
        LegendItem("Carbs", Color(0xFFFFD54F))
        LegendItem("Fats", Color(0xFFE57373))
    }
    Spacer(Modifier.height(16.dp))
}

@Composable
private fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(8.dp).clip(CircleShape).background(color))
        Spacer(Modifier.width(4.dp))
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
private fun CalorieTrendChart(days: List<com.nutriai.app.data.DailyEntry>) {
    val maxCal = (days.maxOfOrNull { it.calories } ?: 2500.0).coerceAtLeast(2500.0).toFloat()
    val targetCal = days.firstOrNull()?.targetCalories?.toFloat() ?: 2000f
    
    androidx.compose.foundation.Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(horizontal = 8.dp)
    ) {
        val width = size.width
        val height = size.height
        val barWidth = width / 14f
        val spacing = width / 7f
        
        // Draw Target Line
        val targetY = height - (targetCal / maxCal * height)
        drawLine(
            color = Color.Gray.copy(alpha = 0.3f),
            start = androidx.compose.ui.geometry.Offset(0f, targetY),
            end = androidx.compose.ui.geometry.Offset(width, targetY),
            strokeWidth = 2.dp.toPx(),
            pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )

        days.takeLast(7).forEachIndexed { index, day ->
            val barHeight = (day.calories.toFloat() / maxCal * height).coerceAtLeast(5f)
            val x = index * spacing + (spacing - barWidth) / 2
            val y = height - barHeight
            
            drawRoundRect(
                color = if (day.isBingeDay) Color(0xFFE57373) else Color(0xFF81C784),
                topLeft = androidx.compose.ui.geometry.Offset(x, y),
                size = androidx.compose.ui.geometry.Size(barWidth, barHeight),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx())
            )
            
            // Draw day label
            drawContext.canvas.nativeCanvas.drawText(
                formatDateForChart(day.date),
                x + barWidth/2,
                height + 20.dp.toPx(),
                android.graphics.Paint().apply {
                    color = android.graphics.Color.GRAY
                    textSize = 24f
                    textAlign = android.graphics.Paint.Align.CENTER
                }
            )
        }
    }
    Spacer(Modifier.height(28.dp))
}

private fun formatDateForChart(dateStr: String): String {
    return try {
        // Assuming YYYY-MM-DD
        val parts = dateStr.split("-")
        if (parts.size == 3) {
            val year = parts[0].takeLast(2)
            val month = parts[1].toInt()
            val day = parts[2].toInt()
            val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
            "${months[month - 1]} $day, '$year"
        } else dateStr
    } catch (e: Exception) {
        dateStr
    }
}

@Composable
private fun EatOptionCard(option: EatOption) {
    CardBlock {
        Text(option.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(option.whyThisFits, maxLines = 2, overflow = TextOverflow.Ellipsis)
        Text("${option.estimatedMacros.calories.roundToInt()} kcal, ${option.estimatedMacros.proteinG.roundToInt()}g protein")
        if (option.usesInventory.isNotEmpty()) {
            Text("Uses: ${option.usesInventory.joinToString()}")
        }
    }
}

@Composable
private fun FoodRow(food: FoodItem) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(food.name, fontWeight = FontWeight.Medium)
            Text(food.quantity, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text("${food.calories.roundToInt()} kcal", fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun MacroFoodRow(item: MacroFoodItem) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f)) {
            Text(item.name, fontWeight = FontWeight.Medium)
            Text(item.quantity, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text("${item.calories.roundToInt()} kcal", fontWeight = FontWeight.Bold)
            Text("P: ${item.proteinG.roundToInt()}g  C: ${item.carbsG.roundToInt()}g", style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun InventoryCard(item: InventoryItem, onEdit: () -> Unit, onDelete: () -> Unit) {
    ElevatedCard(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(item.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                IconButton(onClick = onEdit) { Icon(Icons.Outlined.Edit, null, tint = MaterialTheme.colorScheme.primary) }
                IconButton(onClick = onDelete) { Icon(Icons.Outlined.Delete, null, tint = MaterialTheme.colorScheme.error) }
            }
            
            Text("${item.quantity ?: "Amount not set"} • ${item.category?.replaceFirstChar { it.uppercase() } ?: "Other"}")
            
            if (item.expiryDate != null) {
                Spacer(Modifier.height(4.dp))
                Surface(
                    color = if (item.expiringSoon) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = if (item.expiringSoon) "🚨 Expiring: ${item.expiryDate}" else "📅 Expires: ${item.expiryDate}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (item.expiringSoon) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            if (item.daysUntilExpiry != null) {
                Text("${item.daysUntilExpiry} days left", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
private fun PlanMealCard(meal: PlanMeal) {
    CardBlock {
        Text(meal.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(meal.mealType)
        Text("${meal.estimatedMacros.calories.roundToInt()} kcal, ${meal.estimatedMacros.proteinG.roundToInt()}g protein")
        Text(meal.ingredients.joinToString(), maxLines = 2, overflow = TextOverflow.Ellipsis)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdminScreen(viewModel: NutriViewModel) {
    val ui by viewModel.state

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Admin Tools", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text("Manual overrides and system status.")
        }

        item {
            CardBlock {
                Text("Coach Agent", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Text("Manually trigger the coach logic to analyze your recent logs.")
                Spacer(Modifier.height(12.dp))
                Button(onClick = { viewModel.triggerCoach() }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                    Text("Trigger My Coach")
                }
            }
        }

        item {
            CardBlock {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Scheduler Status", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(if (ui.schedulerStatus?.schedulerRunning == true) "Running" else "Stopped")
                    }
                    Button(onClick = { viewModel.loadSchedulerStatus() }) {
                        Text("Refresh")
                    }
                }

                ui.schedulerStatus?.jobs?.forEach { job ->
                    HorizontalDivider(Modifier.padding(vertical = 8.dp))
                    Text(job.name, fontWeight = FontWeight.Bold)
                    Text("ID: ${job.id}", style = MaterialTheme.typography.bodySmall)
                    Text("Next: ${job.nextRunTime}", style = MaterialTheme.typography.bodySmall)
                    Text("Trigger: ${job.trigger}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        item { ErrorText(ui.error) }
    }
}

@Composable
private fun CardBlock(
    container: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.surface,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = container),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = content
        )
    }
}

@Composable
private fun ErrorText(error: String?) {
    if (!error.isNullOrBlank()) {
        Text(error, color = MaterialTheme.colorScheme.error)
    }
}

@Composable
private fun SettingsScreen(viewModel: NutriViewModel) {
    val ui by viewModel.state
    val profile = ui.profile

    var name by remember(profile?.name) { mutableStateOf(profile?.name ?: "") }
    var age by remember(profile?.age) { mutableStateOf(profile?.age?.toString() ?: "") }
    var weight by remember(profile?.weightKg) { mutableStateOf(profile?.weightKg?.toString() ?: "") }
    var height by remember(profile?.heightCm) { mutableStateOf(profile?.heightCm?.toString() ?: "") }
    
    // Backup gender, goal, and activity from ui.me if profile is missing them
    var gender by remember(profile?.gender, ui.me?.gender) { mutableStateOf(profile?.gender ?: ui.me?.gender ?: "male") }
    var goal by remember(profile?.goal, ui.me?.goal) { mutableStateOf(profile?.goal ?: ui.me?.goal ?: "maintain") }
    var activityLevel by remember(profile?.activityLevel, ui.me?.activityLevel) { mutableStateOf(profile?.activityLevel ?: ui.me?.activityLevel ?: "moderate") }

    var calorieTarget by remember(profile?.targets?.calories) { mutableStateOf(profile?.targets?.calories?.roundToInt()?.toString() ?: "") }
    var proteinTarget by remember(profile?.targets?.proteinG) { mutableStateOf(profile?.targets?.proteinG?.roundToInt()?.toString() ?: "") }
    var fiberTarget by remember(profile?.targets?.fiberG) { mutableStateOf(profile?.targets?.fiberG?.roundToInt()?.toString() ?: "") }
    var waterTarget by remember(profile?.waterTargetMl, ui.water?.targetMl) {
        mutableStateOf(ui.water?.targetMl?.roundToInt()?.toString() ?: profile?.waterTargetMl?.roundToInt()?.toString() ?: "2500") 
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Profile Settings", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                IconButton(onClick = { viewModel.logout() }) {
                    Icon(Icons.AutoMirrored.Outlined.Logout, null, tint = MaterialTheme.colorScheme.error)
                }
            }
        }

        if (profile != null) {
            item {
                CardBlock {
                    Text("Basic Information", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    
                    OutlinedTextField(name, { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth())
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(age, { age = it }, label = { Text("Age") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(weight, { weight = it }, label = { Text("Weight (kg)") }, modifier = Modifier.weight(1f))
                    }
                    OutlinedTextField(height, { height = it }, label = { Text("Height (cm)") }, modifier = Modifier.fillMaxWidth())
                    
                    Spacer(Modifier.height(8.dp))
                    Text("Gender", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("male", "female", "other").forEach { g ->
                            FilterChip(
                                selected = gender == g,
                                onClick = { gender = g },
                                label = { Text(g.replaceFirstChar { it.uppercase() }) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            viewModel.updateProfile(
                                UpdateProfileRequest(
                                    name = name,
                                    age = age.toIntOrNull(),
                                    weightKg = weight.toDoubleOrNull(),
                                    heightCm = height.toDoubleOrNull(),
                                    gender = gender
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Update Profile")
                    }
                }
            }

            item {
                CardBlock {
                    Text("Activity & Goals", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))

                    Text("Goal", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("lose_weight", "maintain", "gain_muscle").forEach { g ->
                            FilterChip(
                                selected = goal == g,
                                onClick = { goal = g },
                                label = { Text(g.replace("_", " ").replaceFirstChar { it.uppercase() }) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    Text("Activity Level", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf("sedentary", "light", "moderate", "active", "very_active").forEach { a ->
                            FilterChip(
                                selected = activityLevel == a,
                                onClick = { activityLevel = a },
                                label = { Text(a.replace("_", " ").replaceFirstChar { it.uppercase() }) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            viewModel.updateProfile(
                                UpdateProfileRequest(
                                    goal = goal,
                                    activityLevel = activityLevel
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Update Goals")
                    }
                }
            }

            item {
                CardBlock {
                    Text("Manual Target Overrides", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Override the AI's calculated targets.", style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(12.dp))
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(calorieTarget, { calorieTarget = it }, label = { Text("Daily Cals") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(proteinTarget, { proteinTarget = it }, label = { Text("Protein (g)") }, modifier = Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(fiberTarget, { fiberTarget = it }, label = { Text("Fiber (g)") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(waterTarget, { waterTarget = it }, label = { Text("Water (ml)") }, modifier = Modifier.weight(1f))
                    }
                    
                    Spacer(Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = {
                            viewModel.updateProfile(
                                UpdateProfileRequest(
                                    calorieTarget = calorieTarget.toDoubleOrNull(),
                                    proteinTargetG = proteinTarget.toDoubleOrNull(),
                                    fiberTargetG = fiberTarget.toDoubleOrNull(),
                                    waterTargetMl = waterTarget.toDoubleOrNull()
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Save Custom Targets")
                    }
                }
            }
        }
        item { ErrorText(ui.error) }
    }
}
