package live.ditto.quickstart.tasks.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import live.ditto.quickstart.tasks.data.DataManager
import live.ditto.quickstart.tasks.models.TaskModel

class TasksListScreenViewModel(
    val dataManager: DataManager,
) : ViewModel() {

    private val _taskModels = MutableStateFlow<List<TaskModel>>(emptyList())
    val taskModels: StateFlow<List<TaskModel>> = _taskModels.asStateFlow()

    init {
        viewModelScope.launch {
            dataManager.populateTaskCollection()
            dataManager.getTaskModels()
                .collect { taskModelList ->
                    _taskModels.value = taskModelList
                }
            //todo need to fix getting default value
            dataManager.setSyncEnabled(null)
        }
    }

    fun setSyncEnabled(enabled: Boolean) {
        viewModelScope.launch {
            dataManager.setSyncEnabled(enabled)
        }
    }

    fun toggle(id: String) {
        viewModelScope.launch {
            dataManager.toggleComplete(id)
        }
    }

    fun delete(id: String) {
        viewModelScope.launch {
            dataManager.deleteTaskModel(id)
        }
    }
}
