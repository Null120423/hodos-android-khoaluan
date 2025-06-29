package com.example.hodos_final_android.screen.profile


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.hodos_final_android.helper.formatDate
import com.example.hodos_final_android.helper.formatPrice
import com.example.hodos_final_android.model.PricingPlanModel
import com.example.hodos_final_android.model.User

@Composable
fun SubscriptionManagementCard(currentUser: User, planData: PricingPlanModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF10B981).copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ManageAccounts,
                        contentDescription = null,
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(20.dp)
                    )
                }

                Column {
                    Text(
                        text = "Subscription Management",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                    Text(
                        text = "Premium plan details and settings",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Subscription Status Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = when (currentUser.subscriptionStatus) {
                        "active" -> Color(0xFFF0FDF4)
                        "cancelled" -> Color(0xFFFEF3C7)
                        "expired" -> Color(0xFFFEE2E2)
                        else -> Color(0xFFF9FAFB)
                    }
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = when (currentUser.subscriptionStatus) {
                            "active" -> Icons.Default.CheckCircle
                            "cancelled" -> Icons.Default.Warning
                            "expired" -> Icons.Default.Error
                            else -> Icons.Default.Info
                        },
                        contentDescription = null,
                        tint = when (currentUser.subscriptionStatus) {
                            "active" -> Color(0xFF10B981)
                            "cancelled" -> Color(0xFFF59E0B)
                            "expired" -> Color(0xFFEF4444)
                            else -> Color(0xFF6B7280)
                        },
                        modifier = Modifier.size(24.dp)
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = when (currentUser.subscriptionStatus) {
                                "active" -> "Subscription is active"
                                "cancelled" -> "Subscription will expire"
                                "expired" -> "Subscription expired"
                                else -> "Unknown status"
                            },
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = when (currentUser.subscriptionStatus) {
                                "active" -> Color(0xFF065F46)
                                "cancelled" -> Color(0xFF92400E)
                                "expired" -> Color(0xFF991B1B)
                                else -> Color(0xFF374151)
                            }
                        )

                        Text(
                            text = when (currentUser.subscriptionStatus) {
                                "active" -> if (currentUser.isAutoRenew)
                                    "Auto-renews on ${formatDate(currentUser.subscriptionEndDate)}"
                                else "Expires on ${formatDate(currentUser.subscriptionEndDate)}"
                                "cancelled" -> "On ${formatDate(currentUser.subscriptionEndDate)}"
                                "expired" -> "On ${formatDate(currentUser.subscriptionEndDate)}"
                                else -> "Please check subscription status"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = when (currentUser.subscriptionStatus) {
                                "active" -> Color(0xFF065F46).copy(alpha = 0.8f)
                                "cancelled" -> Color(0xFF92400E).copy(alpha = 0.8f)
                                "expired" -> Color(0xFF991B1B).copy(alpha = 0.8f)
                                else -> Color(0xFF6B7280)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Subscription Details Section
            Text(
                text = "Subscription Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1F2937)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                SubscriptionDetailItem(
                    icon = Icons.Default.Star,
                    iconColor = Color(0xFF8B5CF6),
                    iconBackground = Color(0xFF8B5CF6).copy(alpha = 0.1f),
                    label = "Plan Name",
                    value = planData.name,
                    subtitle = "Premium Plan"
                )

                SubscriptionDetailItem(
                    icon = Icons.Default.AttachMoney,
                    iconColor = Color(0xFF10B981),
                    iconBackground = Color(0xFF10B981).copy(alpha = 0.1f),
                    label = "Plan Price",
                    value = formatPrice(planData.price, planData.currency),
                    subtitle = if (planData.billingCycle == "monthly") "Billed monthly" else "Billed annually"
                )

                SubscriptionDetailItem(
                    icon = Icons.Default.CalendarMonth,
                    iconColor = Color(0xFF3B82F6),
                    iconBackground = Color(0xFF3B82F6).copy(alpha = 0.1f),
                    label = "Billing Cycle",
                    value = formatDate(currentUser.subscriptionEndDate),
                    subtitle = if (currentUser.isAutoRenew) "Auto-renew enabled" else "Auto-renew disabled"
                )

                SubscriptionDetailItem(
                    icon = if (currentUser.subscriptionStatus == "active") Icons.Default.Verified else Icons.Default.Schedule,
                    iconColor = if (currentUser.subscriptionStatus == "active") Color(0xFF10B981) else Color(0xFFF59E0B),
                    iconBackground = if (currentUser.subscriptionStatus == "active") Color(0xFF10B981).copy(alpha = 0.1f) else Color(0xFFF59E0B).copy(alpha = 0.1f),
                    label = "Status",
                    value = when (currentUser.subscriptionStatus) {
                        "active" -> "Active"
                        "cancelled" -> "Cancelled"
                        "expired" -> "Expired"
                        else -> "Unknown"
                    },
                    subtitle = when (currentUser.subscriptionStatus) {
                        "active" -> "All features available"
                        "cancelled" -> "Usable until expiration"
                        "expired" -> "Renew to continue"
                        else -> "Contact support"
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(color = Color(0xFFF3F4F6), thickness = 1.dp)

            Spacer(modifier = Modifier.height(24.dp))

            // Quick Actions
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1F2937)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (currentUser.subscriptionStatus == "active") {
                    if (currentUser.isAutoRenew) {
                        ActionButton(
                            onClick = { },
                            icon = Icons.Default.Cancel,
                            title = "Disable Auto-Renew",
                            subtitle = "Will expire on ${formatDate(currentUser.subscriptionEndDate)}",
                            buttonColor = Color(0xFFFEE2E2),
                            iconColor = Color(0xFFDC2626),
                            textColor = Color(0xFF991B1B),
                            isDestructive = true
                        )
                    } else {
                        ActionButton(
                            onClick = { },
                            icon = Icons.Default.Autorenew,
                            title = "Enable Auto-Renew",
                            subtitle = "Will auto-renew on ${formatDate(currentUser.subscriptionEndDate)}",
                            buttonColor = Color(0xFFF0FDF4),
                            iconColor = Color(0xFF10B981),
                            textColor = Color(0xFF065F46),
                            isDestructive = false
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Footer Note
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF6B7280),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Changes will take effect in the next billing cycle",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6B7280)
                    )
                }
            }
        }
    }
}
@Composable
fun SubscriptionDetailItem(
    icon: ImageVector,
    iconColor: Color,
    iconBackground: Color,
    label: String,
    value: String,
    subtitle: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(iconBackground, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF6B7280),
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF1F2937),
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF9CA3AF)
            )
        }
    }
}
@Composable
fun ActionButton(
    onClick: () -> Unit,
    icon: ImageVector,
    title: String,
    subtitle: String,
    buttonColor: Color,
    iconColor: Color,
    textColor: Color,
    isDestructive: Boolean
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = buttonColor),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = textColor,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = textColor.copy(alpha = 0.8f)
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = textColor.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
