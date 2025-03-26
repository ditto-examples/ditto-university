package live.ditto.quickstart.tasks.data

import android.content.Context
import live.ditto.android.AndroidDittoDependencies
import live.ditto.android.DefaultAndroidDittoDependencies
import java.io.File

data class CustomDirectoryAndroidDittoDependencies(
    private val androidDittoDependencies: DefaultAndroidDittoDependencies,
    private val customDir: File
) : AndroidDittoDependencies {
    override fun context(): Context {
        return androidDittoDependencies.context()
    }

    override fun ensureDirectoryExists(path: String) {
        androidDittoDependencies.ensureDirectoryExists(path)
    }

    override fun persistenceDirectory(): String {
        return customDir.path
    }
}
