package com.example.hodos_final_android.screen.profile

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.component.ErrorView
import com.example.hodos_final_android.component.ImgWithUrl
import com.example.hodos_final_android.component.Loading
import com.example.hodos_final_android.component.Seprate
import com.example.hodos_final_android.di.UserSubscriptionEntryPoint
import com.example.hodos_final_android.di.UserViewModelEntryPoint
import com.example.hodos_final_android.helper.downloadQrImage
import com.example.hodos_final_android.helper.formatPrice
import com.example.hodos_final_android.helper.getScreenWidth
import com.example.hodos_final_android.model.GetUserInfoModel
import com.example.hodos_final_android.model.PricingPlanModel
import com.example.hodos_final_android.navigateBackWithAnimation
import com.example.hodos_final_android.navigateWithAnimation
import com.example.hodos_final_android.view_model.AuthViewModel
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.delay


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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpgradeOverviewScreen() {
    val navController = LocalNavController.current
    val context = LocalContext.current
    val userSubscriptionModel = remember {
        EntryPointAccessors
            .fromApplication(context, UserSubscriptionEntryPoint::class.java)
            .userSubscriptionModel()
    }
    val plansState by userSubscriptionModel.activePlansState.collectAsState()
    val userSubState by userSubscriptionModel.state.collectAsState()
    val pricingPlanSubState by userSubscriptionModel.pricingPlanSubState.collectAsState()
    val selectedPlan = userSubState.selectPlan


    fun handlePayment() {
        userSubscriptionModel.pricingPlanSub()

    }
    // Load plans if empty
    LaunchedEffect(Unit) {
        if (plansState.planActives.isEmpty()) {
            userSubscriptionModel.getActivePlans()
        }
    }

    LaunchedEffect(pricingPlanSubState) {
        if(!pricingPlanSubState.isLoading && pricingPlanSubState.res != null) {
            navController.navigateWithAnimation(
                Screen.PaymentMethodScreen.route
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .navigationBarsPadding()
    ) {
        TopAppBar(
            title = {
                Text(
                    "Choose Your Plan",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
        )

        when {
            plansState.isLoading || pricingPlanSubState.isLoading -> {
                Loading()
            }
            plansState.error != null || pricingPlanSubState.error != null -> {
                plansState.error?.let {
                    ErrorView(
                        title = it.message
                    )
                }
                pricingPlanSubState.error?.let {
                    ErrorView(
                        title = it.message
                    )
                }
            }
            plansState.planActives.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(48.dp)
                        )
                        Text("No subscription plans available", color = Color.Gray)
                        Text("Please try again later", color = Color.Gray)
                    }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    // Plans list
                    items(plansState.planActives) { plan ->
                        PlanCard(
                            plan = plan,
                            isSelected = selectedPlan?.id == plan.id,
                            onSelect = {
                                userSubscriptionModel.selectPlan(
                                    plan
                                )
                            }
                        )
                    }

                    item {
                        Seprate(
                            height = 50
                        )
                    }
                }

                // Continue button
                selectedPlan?.let { plan ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(plan.name, fontWeight = FontWeight.Bold)
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            formatPrice(plan.price, currency = plan.currency),
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            "/${if (plan.billingCycle == "monthly") "month" else "year"}",
                                            color = Color.Gray
                                        )
                                    }
                                    if (plan.trialPeriodDays > 0) {
                                        Text(
                                            "${plan.trialPeriodDays}-day free trial included",
                                            color = Color(0xFF10B981)
                                        )
                                    }
                                }
                                Button(
                                    onClick = {
                                        handlePayment()
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary
                                    ),
                                    shape = RoundedCornerShape(100.dp),
                                    modifier = Modifier.height(48.dp)
                                ) {
                                    Text(
                                        "Continue",
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlanCard(
    plan: PricingPlanModel,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFF0F9FF) else Color.White
        ),
        border = if (isSelected) BorderStroke(2.dp, Color(0xFF0EA5E9)) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = plan.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color(0xFF0C4A6E) else Color.Black
                    )

                    if (plan.description.isNotEmpty()) {
                        Text(
                            text = plan.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = formatPrice(plan.price, currency = plan.currency),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color(0xFF0C4A6E) else Color.Black
                        )
                        Text(
                            text = "/${if (plan.billingCycle == "monthly") "month" else "year"}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }

                    if (plan.trialPeriodDays > 0) {
                        Text(
                            text = "${plan.trialPeriodDays}-day free trial",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF10B981),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier
                                .padding(top = 4.dp)
                                .background(
                                    Color(0xFF10B981).copy(alpha = 0.1f),
                                    RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                RadioButton(
                    selected = isSelected,
                    onClick = onSelect,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFF0EA5E9)
                    )
                )
            }

            // Features
            if (plan.features.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Features:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSelected) Color(0xFF0C4A6E) else Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))

                plan.features.forEach { feature ->
                    Row(
                        modifier = Modifier.padding(vertical = 2.dp),
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
                            color = Color.Gray
                        )
                    }
                }
            }

            // Limits (if available)
            plan.limits.let { limits ->
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    limits.maxTripsPerMonth?.let { maxTrips ->
                        Column {
                            Text(
                                text = "$maxTrips",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color(0xFF0C4A6E) else Color.Black
                            )
                            Text(
                                text = "trips/month",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }

                    limits.maxCollaboratorsPerTrip?.let { maxCollaborators ->
                        Column {
                            Text(
                                text = "$maxCollaborators",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color(0xFF0C4A6E) else Color.Black
                            )
                            Text(
                                text = "collaborators",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodScreen() {
    val navController = LocalNavController.current
    val context = LocalContext.current
    val paymentMethods = listOf(
        PaymentMethod("qr_scan", "QR Code Payment", Icons.Default.QrCode, "Scan QR code with your banking app"),
        PaymentMethod("bank_transfer", "Bank Transfer", Icons.Default.AccountBalance, "Manual bank transfer with account details")
    )

    var selectedMethod by remember { mutableStateOf<PaymentMethod?>(null) }
    var showTryFree by remember { mutableStateOf(true) }
    var showQRDialog by remember { mutableStateOf(false) }
    var showBankTransferDialog by remember { mutableStateOf(false) }
    val userSubscriptionModel = remember {
        EntryPointAccessors
            .fromApplication(context, UserSubscriptionEntryPoint::class.java)
            .userSubscriptionModel()
    }

    val userViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, UserViewModelEntryPoint::class.java)
            .userViewModel()
    }

    fun clearDataCheckTransaction() {
        userSubscriptionModel.clearTransactionCheck()
        showQRDialog = false
        showBankTransferDialog = false
        navController.navigateWithAnimation(Screen.PaymentProcessingScreen.route)
    }
    val transactionCheckState by userSubscriptionModel.transactionCheckState.collectAsState()

    LaunchedEffect(Unit) {
        while (transactionCheckState.isCompleted != true) {
            delay(1000)
            userSubscriptionModel.transactionCheck()
        }
        navController.navigateWithAnimation(Screen.SuccessScreen.route)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
    ) {
        TopAppBar(
            title = {
                Text(
                    "Payment Method",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )
            },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedMethod = null
                            showTryFree = true
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF0FDF4)
                    ),
                    border = BorderStroke(2.dp, Color(0xFF10B981))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .background(
                                    Color(0xFF10B981).copy(alpha = 0.1f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Stars,
                                contentDescription = null,
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Start Free Trial",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF065F46)
                                )
                                Text(
                                    text = "RECOMMENDED",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .background(
                                            Color(0xFF10B981),
                                            RoundedCornerShape(4.dp)
                                        )
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = "7 days free, no payment required now",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF065F46).copy(alpha = 0.8f)
                            )
                        }

                        RadioButton(
                            selected = selectedMethod == null && showTryFree,
                            onClick = {
                                selectedMethod = null
                                showTryFree = true
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFF10B981)
                            )
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Or pay now with:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
            }

            items(paymentMethods) { method ->
                PaymentMethodCard(
                    method = method,
                    isSelected = selectedMethod?.id == method.id,
                    onSelect = {
                        selectedMethod = method
                        showTryFree = false

                        // Show appropriate dialog based on selected method
                        when (method.id) {
                            "qr_scan" -> showQRDialog = true
                            "bank_transfer" -> showBankTransferDialog = true
                        }
                    }
                )
            }

            // Warning Card
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFEF3C7)
                    ),
                    border = BorderStroke(1.dp, Color(0xFFD97706))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(24.dp)
                        )

                        Column {
                            Text(
                                text = "Important Notice",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF92400E)
                            )
                            Text(
                                text = "After your free trial ends, you will need to complete payment to continue using premium features. You can cancel anytime during the trial period.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF92400E).copy(alpha = 0.9f),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            // Security Note
            item {
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
                                text = "Secure & Protected",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0C4A6E)
                            )
                            Text(
                                text = "All payment information is encrypted and securely processed",
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
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                if (selectedMethod == null && showTryFree) {
                    // Free Trial Summary
                    Text(
                        text = "Free Trial Summary",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Premium Plan (Monthly)")
                        Text("200,000₫")
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Free Trial Period")
                        Text("7 days", color = Color(0xFF10B981))
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Total Today",
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "FREE",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF10B981),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { navController.navigateWithAnimation(Screen.PaymentProcessingScreen.route) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF10B981)
                        )
                    ) {
                        Text("Start Free Trial", fontWeight = FontWeight.SemiBold)
                    }

                    Text(
                        text = "No payment required now. You'll be charged 200,000₫/month after 7 days unless you cancel.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    )
                } else {
                    // Payment Summary
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
                        Text("200,000₫")
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Payment Method")
                        Text(selectedMethod?.name ?: "Not selected", color = Color.Gray)
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
                            "200,000₫",
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF8B5CF6)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Payment method selected. Please follow the instructions in the dialog to complete payment.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }

    // QR Code Payment Dialog
    if (showQRDialog) {
        QRCodePaymentDialog(
            onDismiss = {
                showQRDialog = false
                selectedMethod = null
                showTryFree = true
            },
            onConfirm = {
                clearDataCheckTransaction()
            }
        )
    }

    // Bank Transfer Payment Dialog
    if (showBankTransferDialog) {
        BankTransferPaymentDialog(
            onDismiss = {
                showBankTransferDialog = false
                selectedMethod = null
                showTryFree = true
            },
            onConfirm = {
                clearDataCheckTransaction()
            }
        )
    }
}

@Composable
fun QRCodePaymentDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val context = LocalContext.current
    val userSubscriptionModel = remember {
        EntryPointAccessors
            .fromApplication(context, UserSubscriptionEntryPoint::class.java)
            .userSubscriptionModel()
    }
    val pricingPlanSubState by userSubscriptionModel.pricingPlanSubState.collectAsState()
    val dataTransaction = pricingPlanSubState.res?.result?.metaData
    val qr = dataTransaction?.metadata?.qrCode
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width((getScreenWidth() - 10).dp)
                    .padding(20.dp)
                    .background(Color.Transparent)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.QrCode, contentDescription = null, tint = Color(0xFF0EA5E9), modifier = Modifier.size(28.dp))
                    Text("QR Code Payment", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                }

                Text(
                    text = "Scan the QR code below with your banking app to complete the payment.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )

                if (qr != null) {
                    ImgWithUrl(
                        url = qr,
                        modifier = Modifier
                            .width((getScreenWidth() - 40).dp)
                            .height((getScreenWidth() - 40).dp)
                            .background(Color.Transparent),
                        contentScale = ContentScale.FillWidth
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(
                        onClick = {
                            // Replace with real download logic
                            if (qr != null) {
                                downloadQrImage(
                                    context = context,
                                    imageUrl = qr
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                }

                Seprate(height = 10)
                Button(
                    onClick = onConfirm,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("I've Completed Payment")
                }

                TextButton(onClick = onDismiss) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        }
    }
}



@Composable
fun BankTransferPaymentDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val context = LocalContext.current
    val userSubscriptionModel = remember {
        EntryPointAccessors
            .fromApplication(context, UserSubscriptionEntryPoint::class.java)
            .userSubscriptionModel()
    }
    val pricingPlanSubState by userSubscriptionModel.pricingPlanSubState.collectAsState()
    val receivingBank = pricingPlanSubState.res?.receivingBank
    val transactionMetaData = pricingPlanSubState.res?.result?.metaData

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(20.dp)
                    .widthIn((getScreenWidth() - 10).dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.AccountBalance, contentDescription = null, tint = Color(0xFF0EA5E9), modifier = Modifier.size(28.dp))
                    Text("Bank Transfer", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                }

                Text("Transfer the exact amount to the account below:", style = MaterialTheme.typography.bodyMedium, color = Color.Gray)

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0)),

                ) {
                    Column(
                        modifier = Modifier.padding(10.dp)
                    ) {
                        if (receivingBank != null) {
                            PaymentDetailRow("Bank Name", receivingBank.bankName)
                        }
                        if (receivingBank != null) {
                            PaymentDetailRow("Account Number", receivingBank.accountNumber, copyable = true)
                        }
                        if (receivingBank != null) {
                            PaymentDetailRow("Account Name", receivingBank.accountHolderName)
                        }
                        if (transactionMetaData != null) {
                            PaymentDetailRow("Amount", formatPrice(
                                transactionMetaData.amount,
                                currency = transactionMetaData.currency
                            ), copyable = true)
                            PaymentDetailRow("Transfer Content", transactionMetaData.gatewayTransactionId, copyable = true)
                        }
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                    border = BorderStroke(1.dp, Color(0xFFD97706))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(16.dp))
                        Column {
                            Text("Important:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = Color(0xFF92400E))
                            Text(
                                "• Transfer exact amount: 200,000₫\n• Include your phone number in content\n• Keep transaction receipt",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF92400E),
                                modifier = Modifier.padding(top = 2.dp)
                            )
                        }
                    }
                }

                Seprate(height = 10)

                Button(
                    onClick = onConfirm,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0EA5E9))
                ) {
                    Text("I've Completed Transfer")
                }

                TextButton(onClick = onDismiss) {
                    Text("Cancel", color = Color.Gray)
                }
            }
        }
    }
}



@Composable
fun PaymentDetailRow(label: String, value: String, copyable: Boolean = false) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text(value, color = Color.Gray, fontSize = 14.sp)
        }
        if (copyable) {
            IconButton(onClick = {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText(label, value)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "$label copied", Toast.LENGTH_SHORT).show()
            }) {
                Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = Color.Gray)
            }
        }
    }
}

@Composable
fun PaymentMethodCard(
    method: PaymentMethod,
    isSelected: Boolean,
    onSelect: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFF0F9FF) else Color.White
        ),
        border = if (isSelected) BorderStroke(2.dp, Color(0xFF0EA5E9)) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        if (isSelected) Color(0xFF0EA5E9).copy(alpha = 0.1f)
                        else Color(0xFFF3F4F6),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = method.icon,
                    contentDescription = null,
                    tint = if (isSelected) Color(0xFF0EA5E9) else Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = method.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSelected) Color(0xFF0C4A6E) else Color.Black
                )
                Text(
                    text = method.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            RadioButton(
                selected = isSelected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF0EA5E9)
                )
            )
        }
    }
}

// 4. Payment Processing Screen
@Composable
fun PaymentProcessingScreen() {
    val navController = LocalNavController.current
    var progress by remember { mutableStateOf(0f) }
    var currentStep by remember { mutableStateOf(0) }

    val context = LocalContext.current
    val userSubscriptionModel = remember {
        EntryPointAccessors
            .fromApplication(context, UserSubscriptionEntryPoint::class.java)
            .userSubscriptionModel()
    }
    val transactionCheckState by userSubscriptionModel.transactionCheckState.collectAsState()


    val steps = listOf(
        "Verifying Information",
        "Connecting Payment Gateway",
        "Processing Transaction",
        "Activating Premium"
    )

    fun onTryAgain() {
        userSubscriptionModel.clearTransactionCheck()
        navController.navigateBackWithAnimation()
    }

    LaunchedEffect(Unit) {
        for (i in 0..100) {
            progress = i / 100f
            if (i % 25 == 0 && currentStep < steps.size - 1) {
                currentStep++
            }
            delay(50)
        }
        userSubscriptionModel.transactionCheck()
    }

    LaunchedEffect(transactionCheckState) {
        if(transactionCheckState.isCompleted == true){
            navController.navigateWithAnimation(Screen.SuccessScreen.route)
        }
    }
    if(transactionCheckState.error != null) {
        ErrorView(
            title = transactionCheckState.error!!.message
        )
    }else if(transactionCheckState.isCompleted == false) {
        transactionCheckState.message?.let {
            ErrorView(
                title = it,
                onRetry = {
                    onTryAgain()
                }
            )
        }
    }else {
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
                                    if (index <= currentStep) Color(0xFF10B981) else Color(
                                        0xFFE5E7EB
                                    ),
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


}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun SuccessScreen(
) {
    val navController = LocalNavController.current
    val context = LocalContext.current

    val userSubscriptionModel = remember {
        EntryPointAccessors
            .fromApplication(context, UserSubscriptionEntryPoint::class.java)
            .userSubscriptionModel()
    }

    val userViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, UserViewModelEntryPoint::class.java)
            .userViewModel()
    }

    val state by userSubscriptionModel.state.collectAsState()
    val plan = state.selectPlan

    // auth se

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // ✅ Success Icon
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color(0xFF10B981).copy(alpha = 0.1f), CircleShape),
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

        // ✅ Plan Name
        Text(
            text = "You're now subscribed to the ${plan?.name ?: "Premium"}",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        // ✅ Trial Description
        plan?.trialPeriodDays?.let { trialDays ->
            Text(
                text = "Your $trialDays-day free trial is now active. Cancel anytime.",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ✅ Benefits from `features`
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "What you get:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF065F46)
                )

                Spacer(modifier = Modifier.height(12.dp))

                plan?.features?.let { features ->
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        features.forEach { feature ->
                            Text(
                                text = "✔️ $feature",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF065F46)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // ✅ Price Summary
        plan?.let {
            Text(
                text = "Price: ${it.price.toDoubleOrNull()?.toInt()} ${it.currency} / ${it.billingCycle}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // ✅ Action Buttons
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
//            Button(
//                onClick = {
//                    navController.navigateWithAnimation(Screen.PremiumOnboardingScreen.route)
//                },
//                modifier = Modifier.fillMaxWidth(),
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6))
//            ) {
//                Text("Explore Premium Features")
//            }

            OutlinedButton(
                onClick = {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Back to Home")
            }
        }
    }
}


// 6. Trial Activation Screen
@Composable
fun TrialActivationScreen() {
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
                onClick = {

                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF8B5CF6)
                )
            ) {
                Text("Start Free Trial")
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
