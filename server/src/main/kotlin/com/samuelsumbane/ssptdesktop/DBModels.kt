package com.samuelsumbane.ssptdesktop


import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table


object Categories : Table("categories") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100) // Corrigido para varchar
    val isDeault = bool("is_default")
    override val primaryKey = PrimaryKey(id)
}

object Owners : Table("owners") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val telephone = varchar("telephone", 100).nullable()
    override val primaryKey = PrimaryKey(id)
}


object Products : Table("products") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val category = reference("category_id", Categories.id, onDelete = ReferenceOption.RESTRICT) // Permitir produto sem categoria
    val cost = decimal("cost", 10, 2)
    val price = decimal("price", 10, 2)
    val stock = integer("stock")
    val minStock = integer("min_stock").nullable()
    val barcode = varchar("barcode", 50)
    val owner = reference("owner", Owners.id)
    val productType = varchar("product_type", 10)
    val productRelationId = integer("product_relation_id").references(Products.id).nullable()
    override val primaryKey = PrimaryKey(id)
}

enum class ProductType(val description: String) {
    UNIT("UNITARIO"),
    PACK("EMBALAGEM")
}


object StockMovements : Table("stock_movements") {
    val id = integer("id").autoIncrement()
    val productId = reference("product_id", Products.id, onDelete = ReferenceOption.RESTRICT)
    val type = enumerationByName("type", 20, StockMovementType::class) // Usa Enum para padronizar
    val quantity = integer("quantity")
    val beforeQty = integer("before_qty")
    val afterQty = integer("after_qty")
    val costPrice = decimal("cost_price", 10, 2).nullable() // Pode ser nulo em alguns casos
    val sellPrice = decimal("sell_price", 10, 2).nullable() // Pode ser nulo em alguns casos
    val reason = text("reason").nullable()
    val datetime = long("datetime")
    val userId = reference("user_id", Users.id, onDelete = ReferenceOption.RESTRICT)
    val branch = reference("branch_id", SysBranches.id, onDelete = ReferenceOption.RESTRICT)
    override val primaryKey = PrimaryKey(id)
}

data class AddStockMovementsDC(
    val productId: Int,
    val type: StockMovementType,
    val quantity: Int,
    val beforeQty: Int,
    val afterQty: Int,
    val costPrice: Double,
    val sellPrice: Double,
    val reason: String,
    val userId: Int,
    val branchId: Int,
)



object Clients : Table("clients") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val telephone = varchar("telephone", 100)
    val total = decimal("total", 10, 2) // Corrigido nome
    override val primaryKey = PrimaryKey(id)
}

object Orders : Table("orders") {
    val id = uuid("id")
    val client_id = reference("client_id", Clients.id, onDelete = ReferenceOption.RESTRICT).nullable() // Permitir compras sem cliente
    val total = decimal("total", 10, 2)
    val orderDateTime = long("order_datetime")
    val status = varchar("status", 50)
    val userId = reference("user_id", Users.id, onDelete = ReferenceOption.RESTRICT)
    val branch = reference("branch_id", SysBranches.id, onDelete = ReferenceOption.RESTRICT)
    override val primaryKey = PrimaryKey(id)
}

object OrdersItems : Table("orders_items") {
    val id = uuid("id")
    val order_id = reference("order_id", Orders.id, onDelete = ReferenceOption.RESTRICT)
    val product_id = reference("product_id", Products.id, onDelete = ReferenceOption.RESTRICT) // Corrigido nome e referência
    val quantity = integer("quantity")
    val profit = decimal("profit", 10, 2)
    val sub_total = decimal("sub_total", 10, 2)
    override val primaryKey = PrimaryKey(id)
}

object Users : Table("users") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val email = varchar("email", 100).uniqueIndex() // Adicionado email
    val passwordHash = varchar("password_hash", 250) // Senha com hash
    val role = varchar("role", 25) // Ex: "admin", "caixa"
    val status = integer("status")
    val lastLogin = long("last_login")
    override val primaryKey = PrimaryKey(id)
}

object Suppliers : Table("suppliers") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val contact = varchar("contact", 50)
    val address = varchar("address", 100)
    override val primaryKey = PrimaryKey(id)
}

// Settings ------------>>
object SysConfigurations : Table("system_configurations") {
    val id = integer("id").autoIncrement()
    val key = varchar("key", 50).uniqueIndex()
    val stringValue = varchar("string_value", 255).nullable()
    val intValue = integer("int_value").nullable()
    val longValue = long("long_value").nullable()
    val doubleValue = double("double_value").nullable()
    val boolValue = bool("bool_value").nullable()
    val lastUpdate = long("last_update")
    override val primaryKey = PrimaryKey(id)
}

//branches
object SysBranches : Table("branches") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val address = text("address")
    override val primaryKey = PrimaryKey(id)
}

// Finaceiro Module

//payables
object payables : Table("payables") {
    val id = integer("id").autoIncrement()
    val supplier = varchar("supplier", 100)
    val description = text("description")
    val value = decimal("value", 10, 2)
    val expiration_date = long("expiration_date")
    val payment_date = long("payment_date").nullable()
    val payment_method = text("payment_method")
    val status = integer("status")
    override val primaryKey = PrimaryKey(payables.id)
}

object receivables : Table("receivables") {
    val id = integer("id").autoIncrement()
    val client = varchar("client", 100)
    val description = text("description")
    val value = decimal("value", 10, 2)
    val expiration_date = long("expiration_date")
    val received_date = long("received_date").nullable()
    val received_method = text("received_method")
    val status = integer("status")
    override val primaryKey = PrimaryKey(receivables.id)
}

object PasswordResetCodes : Table("password_reset_codes") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(Users.id)
    val code = varchar("code", 6)
    val createdAt = long("created_at")
    val expiresAt = long("expires_at")

    override val primaryKey = PrimaryKey(id)
}

object Notifications : Table("notifications") {
    val id = integer("id").autoIncrement()
    val userId = integer("userId").references(Users.id).nullable()
    val title = varchar("title", 100)
    val message = text("message")
    val type = integer("type")
    val createdAt = long("created_date")
    val read = bool("read")
    val autoDeleteAfter = long("auto_delete_after").nullable()
    override val primaryKey = PrimaryKey(Notifications.id)
}

object Logs : Table("logs") {
    val id = uuid("id").autoGenerate()
    val datetime = long("datetime")
    val level = varchar("level", 10) // INFO, WARN, ERROR, DEBUG
    val message = text("message")
    val module = varchar("module", 100).nullable()
    val user_id = reference("user_id", Users.id).nullable()
    val metadata_json = text("metadata_json").nullable()
}
// Read
//val metadata = row[Logs.metadataJson]?.let {
//    json.decodeFromString<MeuTipoMetadata>(it)
//}

enum class StockMovementType {
    Entrada, Saída, Ajustamento
}

fun returnNotificationType(type: Int): String {
    return when (type) {
        0 -> "Sucesso"
        1 -> "Informativa"
        2 -> "Aviso"
        else -> "Erro" // even 4
    }
}

fun returnPayablesStatus(statusId: Int): String {
    return when(statusId) {
        2 -> "Pago"
        3 -> "Vencido"
        else -> "Pendente"
    }
}

fun returnReceivablesStatus(statusId: Int): String {
    return when(statusId) {
        2 -> "Recebido"
        3 -> "Vencido"
        else -> "Pendente"
    }
}


enum class sysPackage(val description: String) {
    LITE("Lite"),
    PLUS("Plus"),
    PRO("Pro"),
}

enum class CustomHttpStatusCode(val value: Int) {
    EMAIL_NOT_SENT(901)
}