package live.ditto.quickstart.tasks

import android.content.Context
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import live.ditto.quickstart.tasks.models.DittoConfig
import live.ditto.quickstart.tasks.services.ErrorService
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import live.ditto.android.DefaultAndroidDittoDependencies
import live.ditto.quickstart.tasks.data.CustomDirectoryAndroidDittoDependencies
import live.ditto.quickstart.tasks.data.DittoManager
import live.ditto.quickstart.tasks.models.TaskModel
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import java.util.UUID
import java.io.File

/**
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DittoManagerTests {

    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("live.ditto.quickstart.tasks", appContext.packageName)
    }

    @Test
    fun testDittoPopulateTaskCollection() = runTest {
        val dittoManager = createDittoManagerForTests()
        try {
            //arrange
            val expectedTitles = listOf(
                "Buy groceries",
                "Clean the kitchen",
                "Schedule dentist appointment",
                "Pay bills"
            )

            //act
            dittoManager.populateTaskCollection()
            advanceUntilIdle()
            val tasks = dittoManager.getTaskModels().first()

            //assert
            assertEquals(expectedTitles.size, tasks.size)
            assertEquals(expectedTitles, tasks.map { it.title })
        } finally {
            cleanUpCollection(dittoManager)
        }
    }

    private fun createInitialTaskModel() : TaskModel {
        return TaskModel(
            _id = UUID.randomUUID().toString(),
            title = "Test Task",
            done = false,
            deleted = false)
    }

    private fun createDittoManagerForTests() : DittoManager {
        // Create a temporary directory for testing
        val tempDir = File.createTempFile("ditto_test_${UUID.randomUUID()}", null)
        tempDir.delete()
        tempDir.mkdirs()

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        val dittoConfig = DittoConfig(
            authUrl = appContext.getString(R.string.authUrl),
            websocketUrl = appContext.getString(R.string.websocketUrl),
            appId = appContext.getString(R.string.appId),
            authToken = appContext.getString(R.string.authToken)
        )
        
        val customDependencies = CustomDirectoryAndroidDittoDependencies(
            DefaultAndroidDittoDependencies(appContext),
            tempDir
        )

        return DittoManager(
            dittoConfig = dittoConfig,
            context = appContext,
            androidDittoDependencies = customDependencies,
            errorService = createErrorService()
        )
    }

    private fun createErrorService() : ErrorService {
        return ErrorService()
    }

    private suspend fun cleanUpCollection(dittoManager: DittoManager) {
        try {
            dittoManager.stopSync()
            dittoManager.ditto?.store?.execute("EVICT FROM tasks")

            //remove the directory and all files
            val directoryPath = dittoManager.ditto?.persistenceDirectory
            directoryPath?.let { path ->
                val directory = File(path)
                if (directory.exists()) {
                    directory.listFiles()?.forEach { file ->
                        try {
                            file.delete()
                        } catch (e: Exception) {
                            Log.e("DittoManagerTests", "Failed to delete file: ${file.absolutePath}", e)
                        }
                    }
                    try {
                        directory.delete()
                    } catch (e: Exception) {
                        Log.e("DittoManagerTests", "Failed to delete directory: ${directory.absolutePath}", e)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("DittoManagerTests", "Failed to clean up collection", e)
        } finally {
            dittoManager.ditto = null
        }
    }

}