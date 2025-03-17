package live.ditto.quickstart.tasks.edit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import live.ditto.quickstart.tasks.data.DataManager
import live.ditto.quickstart.tasks.models.TaskModel

class EditScreenViewModel(
    private val dataManager: DataManager
) : ViewModel() {

    private var _id: String? = null
    var title = MutableLiveData<String>("")
    var done = MutableLiveData<Boolean>(false)
    var canDelete = MutableLiveData<Boolean>(false)


    fun setupWithTask(id: String?) {
        canDelete.postValue(id != null)
        val taskId: String = id ?: return
        viewModelScope.launch {
            val task = dataManager.getTaskModel(taskId)
            task?.let {
                _id = task._id
                title.postValue(task.title)
                done.postValue(task.done)
            }
        }
    }

    fun save() {
        viewModelScope.launch {
            if (_id == null) {
                val taskModel = TaskModel(
                    title = title.value ?: "",
                    done = done.value ?: false,
                    deleted = false
                )
                dataManager.insertTaskModel(taskModel)
            } else {
                // Update tasks into the ditto collection using DQL UPDATE statement
                // https://docs.ditto.live/sdk/latest/crud/update#updating
                _id?.let { id ->
                    val taskModel = TaskModel(
                        _id = id,
                        title = title.value ?: "",
                        done = done.value ?: false,
                        deleted = false
                    )
                    dataManager.updateTaskModel(taskModel)
                }
            }
        }
    }

    fun delete() {
        viewModelScope.launch {
            _id?.let { id ->
                dataManager.deleteTaskModel(id)
            }
        }
    }
}
