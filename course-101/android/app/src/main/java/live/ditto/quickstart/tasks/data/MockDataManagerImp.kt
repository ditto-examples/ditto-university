package live.ditto.quickstart.tasks.data

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    private val _taskModelsFlow = MutableStateFlow<List<TaskModel>>(emptyList())

    override fun getTaskModels(): Flow<List<TaskModel>> = _taskModelsFlow.asStateFlow()

    override suspend fun populateTaskCollection() {
        if (_taskModelsFlow.value.isEmpty()) {
            val initialTasks = listOf(
                TaskModel("50191411-4C46-4940-8B72-5F8017A04FA7", "Buy groceries"),
                TaskModel("6DA283DA-8CFE-4526-A6FA-D385089364E5", "Clean the kitchen"),
                TaskModel("5303DDF8-0E72-4FEB-9E82-4B007E5797F0", "Schedule dentist appointment"),
                TaskModel("38411F1B-6B49-4346-90C3-0B16CE97E174", "Pay bills")
            )
            _taskModelsFlow.value = initialTasks
        }
    }

    override suspend fun setSyncEnabled(enabled: Boolean) {
        //do work to set sync enabled
        Log.d(TAG, "Mock Sync Enabled: $enabled")
    }

    override suspend fun insertTaskModel(taskModel: TaskModel) {
        _taskModelsFlow.update { currentList ->
            currentList + taskModel
        }
    }

    override suspend fun updateTaskModel(taskModel: TaskModel) {
        _taskModelsFlow.update { currentList ->
            currentList.map {
                if (it._id == taskModel._id) {
                    taskModel
                } else {
                    it
                }
            }.filter {
                if (taskModel._id == it._id) !taskModel.deleted else true
            }
        }
    }

    override suspend fun toggleComplete(id: String) {
        _taskModelsFlow.update { currentList ->
            currentList.map { task ->
                if (task._id == id) {
                    task.copy(done = !task.done)
                } else {
                    task
                }
            }
        }
    }

    override suspend fun deleteTaskModel(id: String) {
        _taskModelsFlow.update { currentList ->
            currentList.filterNot { it._id == id }
        }
    }
}