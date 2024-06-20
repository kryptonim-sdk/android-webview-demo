# Kryptonim Integration via Browser

This example demonstrates how to implement Kryptonim in your iOS app using a browser (`CustomTabsIntent`).
For integrating Kryptonim using the SDK (recommended), check [here](https://github.com/kryptonim-sdk/android).

Full API documentation can be found [here](https://www.kryptonim.com/api-documentation).

## Steps

1. Create URL with desired parameters.
2. Open CustomTabsIntent for the URL.
3. Optionally handle callback.

## Details

### Build URL

You need to build a URL with the desired parameters for the transaction. Users won't need to manually enter this information during the purchasing process, such as wallet address or fiat currency amount. All parameters can be found [here](https://www.kryptonim.com/api-documentation).

```kotlin
data class Configuration(
    val url: String,
    val amount: String? = null,
    val currency: String? = null
) {
    fun buildUrlWithParameters(): Uri {
        val uriBuilder = Uri.parse(url).buildUpon()
        amount?.let { uriBuilder.appendQueryParameter("amount", it) }
        currency?.let { uriBuilder.appendQueryParameter("currency", it) }
        return uriBuilder.build()
    }
}
```

### Build Intent
Build `CustomTabsIntent` to open Kryptonim in browser tab.
```kotlin
val customTabsIntent = CustomTabsIntent.Builder().build()
```

### Open CustomTabsIntent for URL
Next, create a composable function that opens the URL in a browser using intent described above.
```kotlin
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
        currency = "EUR"
    )
    val url = configuration.buildUrlWithParameters()
    val customTabsIntent = CustomTabsIntent.Builder().build()
    customTabsIntent.launchUrl(context, url)
}
```
Ensure you have added the necessary dependencies for `CustomTabsIntent` in your `build.gradle` file.
```kotlin
dependencies {
    implementation("androidx.browser:browser:1.3.0")
}
```

### Handle Callbacks
If you want to take action after the purchase is finished (either success or failure), you can use the `successUrl` and `failureUrl` parameters when building the URL. You need to construct these URL parameters using your app's URL scheme (details below). Kryptonim will call this URL after the purchase, and you will need to handle it in your app.

##### Register URL scheme
Add your app's URL scheme to `AndroidManifest.xml`. In the code snippet below, we use `kryptonim-demo` as the URL scheme, so your `successUrl` and `failureUrl` parameters should start with `kryptonim-demo://`. In the examples below, we use `kryptonim-demo://kryptonim.purchase.success` and `kryptonim-demo://kryptonim.purchase.failure`.

```kotlin
<activity android:name=".MainActivity">
<intent-filter>
<action android:name="android.intent.action.MAIN" />
<category android:name="android.intent.category.LAUNCHER" />
</intent-filter>
<intent-filter>
<action android:name="android.intent.action.VIEW" />
<category android:name="android.intent.category.DEFAULT" />
<category android:name="android.intent.category.BROWSABLE" />
<data android:scheme="kryptonim-demo" />
</intent-filter>
</activity>
```

##### Handle URL open
Handle the callback in your `MainActivity`.

```kotlin
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OpenKryptonimButton()
        }

        // Handle intent when the activity is launched from a URL scheme
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
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
                else -> {
                    // Handle other URLs if needed
                }
            }
        }
    }
}
```