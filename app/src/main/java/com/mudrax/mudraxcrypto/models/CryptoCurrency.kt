package com.mudrax.mudraxcrypto.models

import java.io.Serializable

data class CryptoCurrency(
    val auditInfoList: List<AuditInfo>,
    val circulatingSupply: Double,
    val cmcRank: Double,
    val dateAdded: String,
    val id: Int,
    val isActive: Double,
    val isAudited: Boolean,
    val lastUpdated: String,
    val marketPairCount: Double,
    val maxSupply: Long,
    val name: String,
    val platform: Platform,
    val quotes: List<Quote>,
    val selfReportedCirculatingSupply: Double,
    val slug: String,
    val symbol: String,
    val tags: List<String>,
    val totalSupply: Double
) : Serializable{
    override fun toString(): String {
        return super.toString()
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}