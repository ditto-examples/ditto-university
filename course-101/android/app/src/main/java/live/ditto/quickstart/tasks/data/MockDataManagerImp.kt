package live.ditto.quickstart.tasks.data

import android.util.Log
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import live.ditto.quickstart.tasks.models.DittoConfig
import live.ditto.quickstart.tasks.models.TaskModel
import live.ditto.quickstart.tasks.services.ErrorService

class MockDataManagerImp(
    override val dittoConfig: DittoConfig,
    private val errorService: ErrorService
) : DataManager {

    companion object {
        private const val TAG = "MockDataManagerImp"
    }

    private val taskModels: MutableList<TaskModel> = mutableListOf()

    override suspend fun populateTaskCollection() {
        if (taskModels.isEmpty()) {
            taskModels.addAll(
                0, listOf(
                    TaskModel("50191411-4C46-4940-8B72-5F8017A04FA7", "Buy groceries"),
                    TaskModel("6DA283DA-8CFE-4526-A6FA-D385089364E5", "Clean the kitchen"),
                    TaskModel(
                        "5303DDF8-0E72-4FEB-9E82-4B007E5797F0",
                        "Schedule dentist appointment"
                    ),
                    TaskModel("38411F1B-6B49-4346-90C3-0B16CE97E174", "Pay bills")
                )
            )
        }
    }

    override suspend fun setSyncEnabled(enabled: Boolean) {
        //do work to set sync enabled
    }

    override fun getTaskModels(): Flow<List<TaskModel>> = callbackFlow {
        try {
            trySend(taskModels)
        } catch (e: Exception) {
            errorService.showError("Failed to setup observer for getting taskModels: ${e.message}")
            Log.e(TAG, "Failed to setup observer for getting taskModels", e)
        }
        awaitClose {
            // Cleanup code here if needed
        }
    }

    override suspend fun insertTaskModel(taskModel: TaskModel) {
        taskModels.add(taskModel)
    }

    override suspend fun updateTaskModel(taskModel: TaskModel) {
        taskModels.find { it._id == taskModel._id }?.let {
            taskModels.remove(it)
        }
        if (!taskModel.deleted) {
            taskModels.add(taskModel)
        }
    }

    override suspend fun toggleComplete(id: String) {
        taskModels.find { it._id == id }?.let {
            it.done = !it.done
        }
    }

    override suspend fun deleteTaskModel(id: String) {
        taskModels.find { it._id == id }?.let {
            taskModels.remove(it)
        }
    }
}