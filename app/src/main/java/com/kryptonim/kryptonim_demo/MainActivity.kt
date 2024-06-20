package com.kryptonim.kryptonim_demo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.kryptonim.kryptonim_demo.ui.theme.KryptonimDemoTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KryptonimDemoTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    KryptonimButton()
                }
            }
        }
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }
}

private fun handleIntent(intent: Intent) {
    intent.data?.let { uri ->
        when (uri.toString()) {
            "kryptonim-demo://kryptonim.purchase.success" -> {
                println("SUCCESS")
            }
            "kryptonim-demo://kryptonim.purchase.failure" -> {
                println("FAILURE")
            }
        }
    }
}

@Composable
fun KryptonimButton() {
    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button({ openKryptonim(context) }) {
            Text(text = "Open Kryptonim")
        }
    }
}

private fun openKryptonim(context: Context) {
    val configuration = Configuration(
        url = "https://intg-kryptonim.devone.cc/iframe-form",
        amount = "0.5",
        currency = "EUR",
        successUrl = "kryptonim-demo://kryptonim.purchase.success",
        failureUrl = "kryptonim-demo://kryptonim.purchase.failure"
    )
    val url = configuration.buildUrlWithParameters()
    val customTabsIntent = CustomTabsIntent.Builder().build()
    customTabsIntent.launchUrl(context, url)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KryptonimDemoTheme {
        KryptonimButton()
    }
}