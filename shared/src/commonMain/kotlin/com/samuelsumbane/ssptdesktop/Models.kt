package com.samuelsumbane.ssptdesktop


// @Serializable
data class CategoryItem(
    val id: Int,
    val name: String,
    val isDefault: Boolean,
)

// @Serializable
data class ProductItem(
    val id: Int,
    val name: String,
    val type: String,
    val productRelationId: Int?,
    val productRelationName: String,
    val cost: Double,
    val price: Double,
    val stock: Int,
    val minStock: Int?,
    val categoryId: Int,
    val categoryName: String,
    val barcode: String,
    val ownerId: Int,
    val ownerName: String,
)

// @Serializable
data class OwnerItem(
    val id: Int,
    val name: String,
    val telephone: String?,
)

// @Serializable
data class ClientItem(
    val id: Int?,
    val name: String,
    val telephone: String
)

// @Serializable
data class OrderItem(
    // @Serializable(with = UUIDSerializer::class) val id: UUID,
    val clientId: Int?,
    val clientName: String?,
    val total: Double,
    val orderDateTime: String?,
    val status: String,
    val userId: Int,
    val userName: String,
    val branchName: String
)


// @Serializable
data class OrderItemsItem(
    // @Serializable(with = UUIDSerializer::class) val id: UUID?,
    // @Serializable(with = UUIDSerializer::class) val orderId: UUID,
    val productId: Int,
    val productName: String?,
    val quantity: Int,
    val subTotal: Double,
    val profit: Double,
    val ownerName: String,
)

// @Serializable
data class SaleReportItem(
    val productName: String,
    val quantity: Int,
    val subTotal: Double,
    val profit: Double,
    val status: String,
    val ownerName: String,
    val userId: Int,
    val userName: String,
    val datetime: String?,
)


// @Serializable
data class OrderItemDraft(
    val clientId: Int?,
    val total: Double,
    val status: String,
    val reason: String,
    val userId: Int,
    val branchId: Int,
)


// @Serializable
data class OrderItemsItemDrafts(
    val productId: Int,
    val quantity: Int,
    val costPrice: Double,
    val sellPrice: Double,
    val subTotal: Double,
    val profit: Double,
)

// @Serializable
data class SaleItem(
    val order: OrderItemDraft,
    val orderItems: List<OrderItemsItemDrafts>
)


// @Serializable
data class UserItem(
    val id: Int,
    val name: String,
    val email: String,
    val passwordHash: String,
    val role: String,
    val status: String,
    val lastLogin: String
)


// @Serializable
data class UserItemDraft(
    val name: String,
    val email: String,
    val role: String,
)


// @Serializable
data class SupplierItem(
    val id: Int?,
    val name: String,
    val contact: String,
    val address: String
)


// @Serializable
data class ChangeProductPriceDraft(
    val productId: Int,
    val newPrice: Double
)

// @Serializable
data class IncreaseProductStockDraft(
    val productId: Int,
    val cost: Double,
    val price: Double,
    val newStock: Int,
    val reason: String,
    val userId: Int,
    val branchId: Int,
)

// @Serializable
data class ProductNameAndCategory(
    val productId: Int,
    val productName: String,
    val categoryId: Int,
    val barcode: String,
)

// @Serializable
data class StockItem(
    val productName: String,
    val type: String,
    val quantity: Int,
    val beforeQty: Int,
    val afterQty: Int,
    val cost: Double?,
    val price: Double?,
    val reason: String,
    val ownerName: String,
    val datetime: String,
    val userId: Int,
    val userName: String,
    val branchName: String,
)

// @Serializable
data class SysConfigItem(
    val id: Int,
    val key: String,
    val stringValue: String?,
    val intValue: Int?,
    val doubleValue: Double?,
    val boolValue: Boolean?,
    val lastUpdate: String
)

//// @Serializable
//data class SysConfigDraft(
//    val id: Int,
//    val key: String,
//    val value: String
//)

// @Serializable
data class UserSession(
    val data: UserItem
)

// @Serializable
data class ChangeStatusDC(
    val status: Int,
    val userId: Int,
)

// @Serializable
data class ChangeRoleDC(
    val role: String,
    val userId: Int,
)

// @Serializable
data class BranchItem(
    val id: Int,
    val name: String,
    val address: String,
)

// @Serializable
data class LoginRequest(val email: String, val password: String)

// @Serializable
data class PasswordDraft(
    val userId: Int,
    val hashedPassword: String?,
    val newPassword: String
)


// @Serializable
data class VerifyPasswordDC(
    val actualPassword: String,
    val hashedPassword: String,
)

// @Serializable
data class VerifyResetCodeDraft(
    val userId: Int,
    val inputCode: String,
)


//object UUIDSerializer : KSerializer<UUID> {
//    override val descriptor: SerialDescriptor =
//        PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)
//
//    override fun serialize(encoder: Encoder, value: UUID) {
//        encoder.encodeString(value.toString())
//    }
//
//    override fun deserialize(decoder: Decoder): UUID {
//        return UUID.fromString(decoder.decodeString())
//    }
//}


//

// @Serializable
data class PayableDraft(
    val supplier: String,
    val description: String,
    val value: Double,
    val expiration_date: String,
    val payment_method: String,
)

// @Serializable
data class IdAndStatus(
    val id: Int,
    val status: Int,
)

// @Serializable
data class PayableItem(
    val id: String,
    val supplier: String,
    val description: String,
    val value: Double,
    val expiration_date: String,
    val payment_date: String,
    val payment_method: String,
    val status: String,
)

// @Serializable
data class ReceivableDraft(
    val client: String,
    val description: String,
    val value: Double,
    val expiration_date: String,
    val received_method: String,
)

// @Serializable
data class ReceivableItem(
    val id: String,
    val client: String,
    val description: String,
    val value: Double,
    val expiration_data: String,
    val received_data: String,
    val received_method: String,
    val status: String,
)

// @Serializable
data class NotificationDraft(
    val userId: Int?,
    val title: String,
    val message: String,
    val type: Int,
    val autoDeleteAfter: Long?,
)

// @Serializable
data class NotificationItem(
    val id: Int,
    val userName: String?,
    val title: String,
    val message: String,
    val type: String,
    val createdAt: String,
    val read: Boolean,
    val userId: Int?,
)

// @Serializable
data class IdAndReadState(
    val id: Int,
    val isRead: Boolean,
)

// @Serializable
data class UpdateUserPersonalData(
    val userId: Int,
    val userName: String,
    val userEmail: String,
)

// @Serializable
data class LogItem(
    val id: String,
    val datetime: String,
    val level: String,
    val message: String,
    val module: String?,
    val userName : String?,
    val metadataJson: String?,
)

// @Serializable
data class LogDraft(
    val level: String,
    val message: String,
    val module: String?,
    val userId: Int?,
    val metadataJson: String?,
)

enum class LogLevel(val description: String) {
    INFO("INFO"),
    WARN("WARN"),
    DEBUG("DEBUG"),
    ERROR("ERROR"),
}

// @Serializable
data class LogMetadata(
    val productId: Int?,
    val quantity: Int?,
    val userAgent: Int?,
    val ip: String?
)

// @Serializable
data class UserLogMetadata(
    val ip: String?,
    val method: String = "Web"
)

// @Serializable
data class SellMetadata(
    val value: Double,
//    val mode: String
)

// @Serializable
data class ErrorMetadata(
    val error: String?,
)

//data class SendEmailDC(
//    val receiver: String,
//    val subject: String,
//    val content: String,
//)

// @Serializable
data class EmailDc(
    val email: String
)
