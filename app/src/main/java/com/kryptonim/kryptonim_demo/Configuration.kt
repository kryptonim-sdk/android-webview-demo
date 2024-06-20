package com.kryptonim.kryptonim_demo

import android.net.Uri

data class Configuration(
    val url: String,
    val amount: String? = null,
    val currency: String? = null,
    val successUrl: String? = null,
    val failureUrl: String? = null
) {
    fun buildUrlWithParameters(): Uri {
        val uriBuilder = Uri.parse(url).buildUpon()
        amount?.let { uriBuilder.appendQueryParameter("amount", it) }
        currency?.let { uriBuilder.appendQueryParameter("currency", it) }
        successUrl?.let { uriBuilder.appendQueryParameter("successUrl", it) }
        failureUrl?.let { uriBuilder.appendQueryParameter("failureUrl", it) }
        return uriBuilder.build()
    }
}
