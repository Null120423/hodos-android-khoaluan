package com.example.hodos_final_android.model

data class TransactionModel(
    val id: String,
    val type: String,
    val amount: String,
    val currency: String,
    val status: String,
    val description: String,
    val paymentGateway: String,
    val gatewayTransactionId: String,
    val createdAt: String,
    val createdByName: String?,
    val qrCode: String?
)