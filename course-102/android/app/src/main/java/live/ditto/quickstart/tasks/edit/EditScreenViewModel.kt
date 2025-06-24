package live.ditto.quickstart.tasks.edit

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import live.ditto.quickstart.tasks.data.DataManager
import live.ditto.quickstart.tasks.models.TaskModel

class EditScreenViewModel(
    private val dataManager: DataManager
) : ViewModel() {

    private var task:TaskModel? = null

    var title = mutableStateOf("")
    var done = mutableStateOf(false)
    var canDelete = mutableStateOf(false)


    fun setupWithTask(taskJson: String?) {
        canDelete.value = (taskJson != null)
        val json: String = taskJson ?: return
        task = TaskModel.fromJson(json)
        task?.let {
            title.value = it.title
            done.value = it.done
        }
    }

    fun save() {
        viewModelScope.launch {
            if (task == null) {
                val taskModel = TaskModel(
                    title = title.value,
                    done = done.value,
                    deleted = false
                )
                dataManager.insertTaskModel(taskModel)
            } else {
                task?.let {
                    //update the task before saving it to the database
                    it.title = title.value
                    it.deleted = false
                    it.done = done.value

                    dataManager.updateTaskModel(it)
                }
            }
        }
    }

    fun delete() {
        viewModelScope.launch {
            task?.let {
                dataManager.deleteTaskModel(it._id)
            }
        }
    }
}
