package live.ditto.quickstart.tasks.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.Flow
import live.ditto.quickstart.tasks.models.DittoConfig
import live.ditto.quickstart.tasks.models.TaskModel

class MockDataManagerImp(
    override val dittoConfig: DittoConfig
) : DataManager {

    override val syncEnabled: LiveData<Boolean> = MutableLiveData(true)

    override suspend fun populateTaskCollection() {
        TODO("Not yet implemented")
    }

    override suspend fun setSyncEnabled(enabled: Boolean?) {
        TODO("Not yet implemented")
    }

    override fun getTaskModels(): Flow<List<TaskModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertTaskModel(taskModel: TaskModel) {
        TODO("Not yet implemented")
    }

    override suspend fun updateTaskModel(taskModel: TaskModel) {
        TODO("Not yet implemented")
    }

    override suspend fun toggleComplete(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTaskModel(id: String) {
        TODO("Not yet implemented")
    }

}