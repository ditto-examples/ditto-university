package live.ditto.quickstart.tasks.models

import androidx.annotation.Keep
@Keep
data class DittoConfig(
    val endpointUrl: String,
    val appId: String,
    val authToken: String)
