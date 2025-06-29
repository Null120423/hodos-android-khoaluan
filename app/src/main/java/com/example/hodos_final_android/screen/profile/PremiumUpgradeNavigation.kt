package com.example.hodos_final_android.screen.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.LinearCardBg
import com.example.hodos_final_android.di.UserViewModelEntryPoint
import com.example.hodos_final_android.navigateWithAnimation
import dagger.hilt.android.EntryPointAccessors
import java.text.NumberFormat
import java.util.Locale

// Navigation routes
object PremiumRoutes {
    const val UPGRADE_OVERVIEW = "upgrade_overview"
    const val PLAN_SELECTION = "plan_selection"
    const val PAYMENT_METHOD = "payment_method"
    const val PAYMENT_PROCESSING = "payment_processing"
    const val SUCCESS = "success"
    const val TRIAL_ACTIVATION = "trial_activation"
    const val PREMIUM_ONBOARDING = "premium_onboarding"
}

// Data models
data class PremiumPlan(
    val id: String,
    val name: String,
    val price: String,
    val originalPrice: String? = null,
    val currency: String,
    val billingCycle: String,
    val features: List<String>,
    val trialDays: Int,
    val isPopular: Boolean = false,
    val discount: String? = null
)

data class PaymentMethod(
    val id: String,
    val name: String,
    val icon: ImageVector,
    val description: String,
    val isEnabled: Boolean = true
)


// 1. Upgrade Overview Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpgradeOverviewScreen() {

    val navController = LocalNavController.current
    val context = LocalContext.current
    val userViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, UserViewModelEntryPoint::class.java)
            .userViewModel()
    }

    val pricingPlanSug = userViewModel.getSuggestPricingPlan()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Choose a plan") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                if (pricingPlanSug != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Suggested Plan",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF8B5CF6)
                            )

                            Text(
                                text = pricingPlanSug.name,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = pricingPlanSug.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )

                            Text(
                                text = "${pricingPlanSug.price}${pricingPlanSug.currency} / ${pricingPlanSug.billingCycle}",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF8B5CF6)
                            )

                            if (pricingPlanSug.trialPeriodDays > 0) {
                                Text(
                                    text = "${pricingPlanSug.trialPeriodDays}-day Free Trial available",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF10B981)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Includes:",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )

                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                pricingPlanSug.features.forEach {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color(0xFF10B981),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = it, style = MaterialTheme.typography.bodySmall)
                                    }
                                }
                            }
                        }
                    }
                }
            }


//            item {
//                // Trial CTA
//                Card(
//                    colors = CardDefaults.cardColors(
//                        containerColor = Color(0xFFF0FDF4)
//                    )
//                ) {
//                    Row(
//                        modifier = Modifier.padding(16.dp),
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            imageVector = Icons.Default.Schedule,
//                            contentDescription = null,
//                            tint = Color(0xFF10B981),
//                            modifier = Modifier.size(24.dp)
//                        )
//
//                        Spacer(modifier = Modifier.width(12.dp))
//
//                        Column(modifier = Modifier.weight(1f)) {
//                            Text(
//                                text = "7-day Free Trial",
//                                style = MaterialTheme.typography.titleSmall,
//                                fontWeight = FontWeight.SemiBold,
//                                color = Color(0xFF065F46)
//                            )
//                            Text(
//                                text = "Cancel anytime, no charges",
//                                style = MaterialTheme.typography.bodySmall,
//                                color = Color(0xFF065F46).copy(alpha = 0.8f)
//                            )
//                        }
//
//                        TextButton(
//                            onClick = { navController.navigateWithAnimation(Screen.TrialActivationScreen.route) }
//                        ) {
//                            Text(
//                                "Start",
//                                color = Color(0xFF10B981),
//                                fontWeight = FontWeight.SemiBold
//                            )
//                        }
//                    }
//                }
//            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanSelectionScreen() {
    val navController = LocalNavController.current

    val plans = listOf(
        PremiumPlan(
            id = "monthly",
            name = "Monthly Plan",
            price = "100000",
            currency = "VND",
            billingCycle = "monthly",
            features = listOf("All Premium features", "Priority support", "7-day free trial"),
            trialDays = 7
        ),
        PremiumPlan(
            id = "yearly",
            name = "Yearly Plan",
            price = "960000",
            originalPrice = "1200000",
            currency = "VND",
            billingCycle = "yearly",
            features = listOf("All Premium features", "Priority support", "14-day free trial", "Save 20%"),
            trialDays = 14,
            isPopular = true,
            discount = "Save 20%"
        )
    )

    var selectedPlan by remember { mutableStateOf(plans[1]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
    ) {
        TopAppBar(
            title = { Text("Choose a Premium Plan") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(plans) { plan ->
                PlanCard(
                    plan = plan,
                    isSelected = selectedPlan.id == plan.id,
                    onSelect = { selectedPlan = plan }
                )
            }

            item {
                Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "All plans include:",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        val allFeatures = listOf(
                            "Unlimited trips",
                            "Unlimited collaborators",
                            "Advanced analytics",
                            "24/7 priority support",
                            "Multi-device sync",
                            "Export to PDF",
                            "Ad-free experience"
                        )

                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            allFeatures.forEach { feature ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color(0xFF10B981),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = feature,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = selectedPlan.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${formatPrice(selectedPlan.price, selectedPlan.currency)}/${
                                if (selectedPlan.billingCycle == "monthly") "month" else "year"
                            }",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }

                    Button(
                        onClick = {
                            navController.navigateWithAnimation(Screen.PaymentMethodScreen.route)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6))
                    ) {
                        Text("Continue")
                    }
                }
            }
        }
    }
}


// 3. Payment Method Screen
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodScreen() {
    val navController = LocalNavController.current
    val paymentMethods = listOf(
        PaymentMethod("momo", "MoMo", Icons.Default.AccountBalanceWallet, "MoMo e-wallet"),
        PaymentMethod("vnpay", "VNPay", Icons.Default.CreditCard, "Bank card via VNPay"),
        PaymentMethod("zalopay", "ZaloPay", Icons.Default.Payment, "ZaloPay e-wallet"),
        PaymentMethod("banking", "Internet Banking", Icons.Default.AccountBalance, "Bank transfer")
    )

    var selectedMethod by remember { mutableStateOf(paymentMethods[0]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
    ) {
        TopAppBar(
            title = { Text("Payment Method") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White
            )
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "Select a payment method",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            items(paymentMethods) { method ->
                PaymentMethodCard(
                    method = method,
                    isSelected = selectedMethod.id == method.id,
                    onSelect = { selectedMethod = method }
                )
            }

            item {
                // Security Note
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF0F9FF)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = Color(0xFF0369A1),
                            modifier = Modifier.size(24.dp)
                        )

                        Column {
                            Text(
                                text = "Secure Payment",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0C4A6E)
                            )
                            Text(
                                text = "Payment information is encrypted and securely protected",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF0C4A6E).copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }
        }

        // Bottom Payment Summary
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Payment Summary",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Premium Plan (Monthly)")
                    Text("100,000₫")
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Free Trial")
                    Text("7 days", color = Color(0xFF10B981))
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Total",
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "0₫ today",
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF10B981)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigateWithAnimation(Screen.PaymentProcessingScreen.route) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8B5CF6)
                    )
                ) {
                    Text("Confirm Payment")
                }

                Text(
                    text = "You will be charged after the trial period ends",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
            }
        }
    }
}


// 4. Payment Processing Screen
@Composable
fun PaymentProcessingScreen() {
    val navController = LocalNavController.current
    var progress by remember { mutableStateOf(0f) }
    var currentStep by remember { mutableStateOf(0) }

    val steps = listOf(
        "Verifying Information",
        "Connecting Payment Gateway",
        "Processing Transaction",
        "Activating Premium"
    )

    LaunchedEffect(Unit) {
        for (i in 0..100) {
            progress = i / 100f
            if (i % 25 == 0 && currentStep < steps.size - 1) {
                currentStep++
            }
            kotlinx.coroutines.delay(50)
        }
        navController.navigateWithAnimation(Screen.SuccessScreen.route)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Loading Animation
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    Color(0xFF8B5CF6).copy(alpha = 0.1f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = progress,
                modifier = Modifier.size(80.dp),
                color = Color(0xFF8B5CF6),
                strokeWidth = 6.dp
            )

            Icon(
                imageVector = Icons.Default.CreditCard,
                contentDescription = null,
                tint = Color(0xFF8B5CF6),
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Processing Payment",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Please do not close the app",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Progress Steps
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            steps.forEachIndexed { index, step ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(
                                if (index <= currentStep) Color(0xFF10B981) else Color(0xFFE5E7EB),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (index <= currentStep) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Text(
                        text = step,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (index <= currentStep) Color.Black else Color.Gray
                    )
                }
            }
        }
    }
}

// 5. Success Screen
@Composable
fun SuccessScreen() {
    val navController = LocalNavController.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Success Animation
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(
                    Color(0xFF10B981).copy(alpha = 0.1f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF10B981),
                modifier = Modifier.size(64.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Congratulations! 🎉",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = "You are now a Premium member",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = "Your 7-day free trial is now active. You can cancel at any time.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Benefits Summary
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF0FDF4)
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "What you get:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF065F46)
                )

                Spacer(modifier = Modifier.height(12.dp))

                val benefits = listOf(
                    "✈️ Unlimited trips",
                    "📊 Advanced analytics",
                    "🎧 24/7 priority support",
                    "☁️ Cross-device sync"
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    benefits.forEach { benefit ->
                        Text(
                            text = benefit,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF065F46)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Action Buttons
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { navController.navigateWithAnimation(Screen.PremiumOnboardingScreen.route) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8B5CF6)
                )
            ) {
                Text("Explore Premium Features")
            }

            OutlinedButton(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to Home")
            }
        }
    }
}


// 6. Trial Activation Screen
@Composable
fun TrialActivationScreen() {
    val navController = LocalNavController.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // Trial Icon
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    Color(0xFFFBBF24).copy(alpha = 0.1f),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Schedule,
                contentDescription = null,
                tint = Color(0xFFFBBF24),
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Free Premium Trial",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Text(
            text = "First 7 days are completely free",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFF8B5CF6),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Trial Benefits
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC))
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                val trialBenefits = listOf(
                    "🚀 Instant access to all Premium features",
                    "💳 No credit card required to start",
                    "⏰ Reminder before trial ends",
                    "❌ Cancel anytime without charges"
                )

                trialBenefits.forEach { benefit ->
                    Text(
                        text = benefit,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // CTA Buttons
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { navController.navigateWithAnimation(Screen.PlanSelectionScreen.route) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8B5CF6)
                )
            ) {
                Text("Start Free Trial")
            }

            TextButton(
                onClick = { navController.navigateWithAnimation(Screen.PlanSelectionScreen.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View All Premium Plans")
            }
        }

        Text(
            text = "By continuing, you agree to the Terms of Use and Privacy Policy",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}


// 7. Premium Onboarding Screen
@Composable
fun PremiumOnboardingScreen() {
    val navController = LocalNavController.current
    val onboardingSteps = listOf(
        Triple(
            Icons.Default.AllInclusive,
            "Unlimited Trips",
            "Create and manage unlimited trips for work and travel"
        ),
        Triple(
            Icons.Default.Analytics,
            "Detailed Insights",
            "View detailed reports on expenses, time, and efficiency"
        ),
        Triple(
            Icons.Default.CloudSync,
            "Multi-device Sync",
            "Access data from your phone, tablet, and web"
        )
    )

    var currentStep by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        // Progress Indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(onboardingSteps.size) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .background(
                            if (index <= currentStep) Color(0xFF8B5CF6) else Color(0xFFE5E7EB),
                            RoundedCornerShape(2.dp)
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        Color(0xFF8B5CF6).copy(alpha = 0.1f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = onboardingSteps[currentStep].first,
                    contentDescription = null,
                    tint = Color(0xFF8B5CF6),
                    modifier = Modifier.size(56.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = onboardingSteps[currentStep].second,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = onboardingSteps[currentStep].third,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
            )
        }

        // Navigation Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (currentStep > 0) {
                TextButton(
                    onClick = { currentStep-- }
                ) {
                    Text("Back")
                }
            } else {
                Spacer(modifier = Modifier.width(1.dp))
            }

            if (currentStep < onboardingSteps.size - 1) {
                Button(
                    onClick = { currentStep++ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8B5CF6)
                    )
                ) {
                    Text("Next")
                }
            } else {
                Button(
                    onClick = {
                        // Navigate back to main app
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8B5CF6)
                    )
                ) {
                    Text("Get Started")
                }
            }
        }
    }
}

// Helper Composables
@Composable
fun FeatureItem(icon: ImageVector, title: String, description: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        Color(0xFF8B5CF6).copy(alpha = 0.1f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF8B5CF6),
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanCard(
    plan: PremiumPlan,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        onClick = onSelect,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF8B5CF6).copy(alpha = 0.1f) else Color.White
        ),
        border = if (isSelected) BorderStroke(2.dp, Color(0xFF8B5CF6)) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 2.dp)
    ) {
       LinearCardBg {
           Box {
               Column(
                   modifier = Modifier.padding(20.dp)
               ) {
                   Row(
                       modifier = Modifier.fillMaxWidth(),
                       horizontalArrangement = Arrangement.SpaceBetween,
                       verticalAlignment = Alignment.CenterVertically
                   ) {
                       Column {
                           Text(
                               text = plan.name,
                               style = MaterialTheme.typography.titleLarge,
                               fontWeight = FontWeight.Bold,
                               color = Color.White,
                           )

                           Row(
                               verticalAlignment = Alignment.Bottom,
                               horizontalArrangement = Arrangement.spacedBy(8.dp)
                           ) {
                               Text(
                                   text = formatPrice(plan.price, plan.currency),
                                   style = MaterialTheme.typography.headlineMedium,
                                   fontWeight = FontWeight.Bold,
                                   color = Color.White,
                               )

                               if (plan.originalPrice != null) {
                                   Text(
                                       text = formatPrice(plan.originalPrice, plan.currency),
                                       style = MaterialTheme.typography.bodyMedium,
                                       color = Color.White,
                                       textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                                   )
                               }
                           }

                           Text(
                               text = "/${if (plan.billingCycle == "monthly") "month" else "year"}",
                               style = MaterialTheme.typography.bodyMedium,
                               color = Color.White,
                           )
                       }

                       RadioButton(
                           selected = isSelected,
                           onClick = onSelect,
                           colors = RadioButtonDefaults.colors(
                               selectedColor = Color(0xFF8B5CF6)
                           )
                       )
                   }

                   if (plan.discount != null) {
                       Spacer(modifier = Modifier.height(8.dp))
                       Card(
                           colors = CardDefaults.cardColors(
                               containerColor = Color(0xFF10B981).copy(alpha = 0.1f)
                           )
                       ) {
                           Text(
                               text = plan.discount,
                               style = MaterialTheme.typography.labelSmall,
                               color = Color(0xFF065F46),
                               fontWeight = FontWeight.SemiBold,
                               modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                           )
                       }
                   }

                   Spacer(modifier = Modifier.height(16.dp))

                   Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                       plan.features.forEach { feature ->
                           Row(
                               verticalAlignment = Alignment.CenterVertically,
                               horizontalArrangement = Arrangement.spacedBy(8.dp)
                           ) {
                               Icon(
                                   imageVector = Icons.Default.Check,
                                   contentDescription = null,
                                   tint = Color(0xFF10B981),
                                   modifier = Modifier.size(16.dp)
                               )
                               Text(
                                   text = feature,
                                   style = MaterialTheme.typography.bodyMedium,
                                   color = Color.White,
                               )
                           }
                       }
                   }
               }
           }
       }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodCard(
    method: PaymentMethod,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        onClick = onSelect,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF8B5CF6).copy(alpha = 0.1f) else Color.White
        ),
        border = if (isSelected) BorderStroke(2.dp, Color(0xFF8B5CF6)) else null
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        Color(0xFF8B5CF6).copy(alpha = 0.1f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = method.icon,
                    contentDescription = null,
                    tint = Color(0xFF8B5CF6),
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = method.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = method.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            RadioButton(
                selected = isSelected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF8B5CF6)
                )
            )
        }
    }
}

// Helper function
fun formatPrice(price: String, currency: String): String {
    val numPrice = price.toDoubleOrNull() ?: 0.0
    return if (currency == "VND") {
        NumberFormat.getCurrencyInstance(Locale("vi", "VN")).format(numPrice)
    } else {
        NumberFormat.getCurrencyInstance(Locale.US).format(numPrice)
    }
}
