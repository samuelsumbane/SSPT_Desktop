package com.samuelsumbane.infrastructure.repositories.tokkens


object TokenStore {
    val activeTokens = mutableSetOf<String>()

    fun addToken(token: String) {
        activeTokens.add(token)
    }

    fun removeToken(token: String) {
        activeTokens.remove(token)
    }

    fun isTokenActive(token: String): Boolean {
        return token in activeTokens
    }

    fun clearAll() {
        activeTokens.clear()
    }
}
