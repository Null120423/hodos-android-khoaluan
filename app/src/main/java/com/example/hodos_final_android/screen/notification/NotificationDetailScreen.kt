package com.example.hodos_final_android.screen.notification

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material.icons.filled.MarkEmailUnread
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.component.Loading
import com.example.hodos_final_android.component.Seprate
import com.example.hodos_final_android.di.NotificationViewEntryPoint
import com.example.hodos_final_android.model.NotificationModel
import com.example.hodos_final_android.model.PricingPlanModel
import com.example.hodos_final_android.model.RejectedPost
import com.example.hodos_final_android.model.TransactionModel
import com.example.hodos_final_android.model.UserSubscriptionModel
import dagger.hilt.android.EntryPointAccessors
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


data class PostRejectionMetadata(
    val post: RejectedPost,
    val reason: RejectionReason
)

data class RejectionReason(
    val id: String,
    val postId: String,
    val reason: String,
    val adminId: String,
    val details: String,
    val createdAt: String,
    val createdBy: String,
    val isDeleted: Boolean,
    val updatedAt: String,
    val rejectedAt: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationDetailScreen() {
    val navController = LocalNavController.current
    val id = navController.previousBackStackEntry?.savedStateHandle?.get<String>("id") ?: ""

    // Sample detailed notification data
    val context = LocalContext.current
    val notificationViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, NotificationViewEntryPoint::class.java)
            .notificationViewModel()
    }
    val dataDetail by notificationViewModel.detailState.collectAsState()
    val notification = dataDetail.data


    LaunchedEffect(Unit) {
        notificationViewModel.detail(id)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notification Details",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle mark as read */ }) {
                        if (notification != null) {
                            Icon(
                                imageVector = if (notification.isRead) Icons.Default.MarkEmailRead else Icons.Default.MarkEmailUnread,
                                contentDescription = if (notification.isRead) "Mark as unread" else "Mark as read"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->


        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Seprate(height = 10)
            if(dataDetail.isLoading) {
                Loading(title = "Loading...")
            }

            // Header Section
            if (notification != null) {
                NotificationHeader(notification)
            }

            // Transaction Details
            if (notification != null) {
                notification.metadata?.transaction?.let { transaction ->
                    TransactionDetailsCard(transaction)
                }
            }

            // Pricing Plan Details
            if (notification != null) {
                notification.metadata?.pricingPlan?.let { plan ->
                    PricingPlanCard(plan)
                }
            }

            // Subscription Details
            if (notification != null) {
                notification.metadata?.userSubscription?.let { subscription ->
                    SubscriptionDetailsCard(subscription)
                }
            }

            // QR Code Section
            if (notification != null) {
                notification.metadata?.transaction?.qrCode?.let { qrCode ->
                    QRCodeCard(qrCode)
                }
            }
            notification?.metadata?.let { metadata ->
                val post = metadata.post
                val reason = metadata.reason

                if (post != null && reason != null) {
                    PostRejectionCard(
                        PostRejectionMetadata(
                            post = post,
                            reason = reason
                        )
                    )
                }
            }


            // Metadata Section
            if (notification != null) {
                MetadataCard(notification)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun NotificationHeader(notification: NotificationModel) {
    val typeData = notification.typeData
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (typeData != null) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color(android.graphics.Color.parseColor(typeData.color)).copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getNotificationIcon(notification.type),
                            contentDescription = notification.type,
                            tint = Color(android.graphics.Color.parseColor(typeData.color)),
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = notification.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    )

                    if (typeData != null) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(android.graphics.Color.parseColor(typeData.color)).copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = typeData.name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(android.graphics.Color.parseColor(typeData.color)),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                if (!notification.isRead) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2196F3))
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = notification.message,
                fontSize = 16.sp,
                color = Color(0xFF333333),
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Sent ${formatTimestamp(notification.sentAt)}",
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )
        }
    }
}

@Composable
fun TransactionDetailsCard(transaction: TransactionModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Payment,
                    contentDescription = "Transaction",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Transaction Details",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1A1A)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TransactionDetailRow("Transaction ID", transaction.id)
            TransactionDetailRow("Amount", formatCurrency(transaction.amount, transaction.currency))
            TransactionDetailRow("Status", transaction.status.uppercase())
            TransactionDetailRow("Payment Gateway", transaction.paymentGateway)
            TransactionDetailRow("Gateway Transaction ID", transaction.gatewayTransactionId)
            TransactionDetailRow("Created By", transaction.createdByName ?: "System")
            TransactionDetailRow("Created At", formatTimestamp(transaction.createdAt))
        }
    }
}

@Composable
fun PricingPlanCard(plan: PricingPlanModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Plan",
                    tint = Color(0xFFFF9800),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = plan.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1A1A)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = plan.description,
                fontSize = 14.sp,
                color = Color(0xFF666666),
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Price",
                        fontSize = 12.sp,
                        color = Color(0xFF999999),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = formatCurrency(plan.price, plan.currency),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    )
                    Text(
                        text = "per ${plan.billingCycle}",
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                }

                Column {
                    Text(
                        text = "Trial Period",
                        fontSize = 12.sp,
                        color = Color(0xFF999999),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${plan.trialPeriodDays} days",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF4CAF50)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Features",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1A1A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            plan.features.forEach { feature ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Feature",
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = feature,
                        fontSize = 14.sp,
                        color = Color(0xFF333333)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Limits",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1A1A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                LimitItem("Max Trips", "${plan.limits.maxTripsPerMonth}/month")
                LimitItem("Collaborators", "${plan.limits.maxCollaboratorsPerTrip}/trip")
            }
        }
    }
}

@Composable
fun SubscriptionDetailsCard(subscription: UserSubscriptionModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Subscriptions,
                    contentDescription = "Subscription",
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Subscription Details",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1A1A)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SubscriptionStatusItem("Status", subscription.status.uppercase(), getStatusColor(subscription.status))
                SubscriptionStatusItem("Auto Renew", if (subscription.autoRenew) "ON" else "OFF", if (subscription.autoRenew) Color(0xFF4CAF50) else Color(0xFFFF5722))
            }

            Spacer(modifier = Modifier.height(16.dp))

            TransactionDetailRow("Subscription ID", subscription.id)
            TransactionDetailRow("Start Date", formatDate(subscription.startDate))
            subscription.nextPaymentDate?.let { formatDate(it) }?.let {
                TransactionDetailRow("Next Payment",
                    it
                )
            }
            // Post Rejection Details

            TransactionDetailRow("Current Period Ends", formatDate(subscription.currentPeriodEndDate))
            TransactionDetailRow("Trial", if (subscription.isTrial) "Yes" else "No")
        }
    }
}

@Composable
fun QRCodeCard(qrCodeUrl: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.QrCode,
                    contentDescription = "QR Code",
                    tint = Color(0xFF9C27B0),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Payment QR Code",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1A1A)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AsyncImage(
                model = qrCodeUrl,
                contentDescription = "QR Code for payment",
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Scan this QR code to complete the payment",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MetadataCard(notification: NotificationModel) {
    val typeData = notification.typeData
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Metadata",
                    tint = Color(0xFF607D8B),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Additional Information",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1A1A)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            TransactionDetailRow("Notification ID", notification.id)
            if (typeData != null) {
                TransactionDetailRow("Type", typeData.description)
            }
            TransactionDetailRow("Created At", formatTimestamp(notification.createdAt))
            TransactionDetailRow("Sent At", formatTimestamp(notification.sentAt))
        }
    }
}

@Composable
fun TransactionDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color(0xFF666666),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = Color(0xFF1A1A1A),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End
        )
    }
}

@Composable
fun LimitItem(title: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            color = Color(0xFF999999),
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1A1A1A)
        )
    }
}

@Composable
fun SubscriptionStatusItem(title: String, value: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            color = Color(0xFF999999),
            fontWeight = FontWeight.Medium
        )
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = color.copy(alpha = 0.1f)
        ) {
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = color,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )
        }
    }
}

fun formatCurrency(amount: String, currency: String): String {
    return try {
        val number = amount.toDouble()
        val formatter = NumberFormat.getNumberInstance(Locale.getDefault())
        "${formatter.format(number)} $currency"
    } catch (e: Exception) {
        "$amount $currency"
    }
}

fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: Date())
    } catch (e: Exception) {
        dateString
    }
}

fun getStatusColor(status: String): Color {
    return when (status.lowercase()) {
        "active" -> Color(0xFF4CAF50)
        "pending" -> Color(0xFFFF9800)
        "expired" -> Color(0xFFFF5722)
        "cancelled" -> Color(0xFF9E9E9E)
        else -> Color(0xFF607D8B)
    }
}

@Composable
fun PostRejectionCard(postRejection: PostRejectionMetadata) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Cancel,
                    contentDescription = "Post Rejected",
                    tint = Color(0xFFE91E63),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Post Rejection Details",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1A1A)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Post thumbnail
            AsyncImage(
                model = postRejection.post.thumbnail,
                contentDescription = "Post thumbnail",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Post details
            TransactionDetailRow("Post Title", postRejection.post.title)
            postRejection.post.status?.let { TransactionDetailRow("Status", it.uppercase()) }
            TransactionDetailRow("Created At", formatTimestamp(postRejection.post.createdAt.toString()))
            TransactionDetailRow("Comments", postRejection.post.commentCount.toString())

            Spacer(modifier = Modifier.height(16.dp))

            // Post content preview
            Text(
                text = "Content Preview",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1A1A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF5F5F5)
            ) {
                Text(
                    text = postRejection.post.content,
                    fontSize = 14.sp,
                    color = Color(0xFF333333),
                    modifier = Modifier.padding(12.dp),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Rejection reason
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFE91E63).copy(alpha = 0.1f),
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxSize()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Rejection reason",
                            tint = Color(0xFFE91E63),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Rejection Reason",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFFE91E63)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Reason: ${postRejection.reason.reason.uppercase()}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1A1A1A)
                    )

                    if (postRejection.reason.details.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Details: ${postRejection.reason.details}",
                            fontSize = 14.sp,
                            color = Color(0xFF666666)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Rejected on ${formatTimestamp(postRejection.reason.rejectedAt)}",
                        fontSize = 12.sp,
                        color = Color(0xFF999999)
                    )
                }
            }
        }
    }
}