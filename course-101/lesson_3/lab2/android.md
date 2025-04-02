# Lab 2:  Register Observer - Kotlin with JetPack Compose

Kotlin and JetPack Compose use [flows](https://developer.android.com/kotlin/flow) in order emit multiple values sequentially.  This allows developers to build dynamic UI that recieve live updates from a database.

The Ditto Manager provides a getTasksModels function that returns a Flow of a List of TaskModel objects.  

```kotlin
override fun getTaskModels(): Flow<List<TaskModel>> = callbackFlow {
```

This flow is observed in the [TasksListScreenViewModel.kt](https://github.com/ditto-examples/ditto-university/blob/main/course-101/android/app/src/main/java/com/ditto/tasks/TasksListScreenViewModel.kt#L37) file.

```kotlin
 dataManager.getTaskModels()
  .collect { taskModelList ->
    _taskModels.value = taskModelList
  }
```

Our UI in the app responds to changes in the flow to update and draw the list of tasks.  We can use a Flow to update the `tasks` property when the data in the task collection changes for any reason, like when a task is added, updated, or deleted from your device or other devices that have the app installed.

Find the `getTasksModels` function in the DittoManager.kt file.  This is the place that we register the observer and emit the data to the flow.

The existing code should look like this:

```kotlin
override fun getTaskModels(): Flow<List<TaskModel>> = callbackFlow {
	try {
		//
		//TODO: - setup observer query, filter out NOT deleted
		//

		//UPDATE CODE HERE
		val query = ""

		//
		//TODO: - setup store observer with query and set array with TaskModel
		//

		//UPDATE CODE HERE

		awaitClose {
			storeObserver?.close()
			storeObserver = null
		}
	} catch (e: Exception) {
		errorService.showError("Failed to setup observer for getting taskModels: ${e.message}")
		Log.e(TAG, "Failed to setup observer for getting taskModels", e)
	}
}
```

Let's replace the TODO: comments with the correct code.

```kotlin
//
//TODO: - setup observer query, filter out NOT deleted
//

//UPDATE CODE HERE
val query = ""
```

Update the code:  Update the variable `query` to store the DQL `SELECT` statement we will use to filter out tasks that are deleted.  

```kotlin
val query = "SELECT * FROM tasks WHERE NOT deleted"
```

Note this uses a special key word `NOT` to filter out tasks that aren't deleted.  This means our app will observer the task collection and update when any task is added, or updated - as long as the delete property is set to false.  Now we we need to update the second TODO to register the observer with the query and get the data from the store that we can return as a Flow. 

We can use the registerObserver method to set up an observer within the store namespace enclosed with a query that specifies the collection to watch for changes, as well as your logic to handle the incoming changes.  Our logic is to loop through the results and send the update with the new task data as a Flow that can be collected by the UI.  

Our DittoManager class already has a variable called storeObserver, which we can use to store a reference to the registered observer. 

```kotlin
private var storeObserver: DittoStoreObserver? = null
```

Locate the following lines of code in the ``getTasksModels` function:

```kotlin
//
//TODO: - setup store observer with query and set array with TaskModel
//

//UPDATE CODE HERE
```
Update the code: replace the TODO with the following code:

```kotlin
storeObserver = ditto?.store?.registerObserver(query) { results ->
	val items = results.items.map { item ->
		TaskModel.fromMap(item.value)
	}
	trySend(items)
}
```

Let's break down the code changes:

- The first line created a store observer that:
	- listens for any changes to the tasks that match the `query` variable created in the previous step 
	- When changes occur, it runs a closure

The closure does the following:
- creates a new list of TaskModel instances from the results which are looped through using the fromMap function that is included in the TaskModel class.
- fromMap is a helper function that makes it easier to convert the Ditto results to TaskModel instances
- The code then uses the trySend method which is part of the Kotlin Coroutine Flow API to send the update with the new task data as a Flow that can be collected by the UI.

## Update the App to use the DittoManager instead of the MockDataManager


In order to test our new changes, we need to update the app to use the `DittoManager` instead of the `MockDataManager`.  This will allow us to test the app with real data from Ditto.  In order to do this, we can update the dependency injection that is used in the `TasksApplication` class to use the `DittoManager` instead of the `MockDataManager`.

Open the `TasksApplication.kt` file.  The implementation for using the DittoManager is commented out.  To start out, let's comment out the existing implementation that defines a MockDataManagerImp.

Update the code to look like this:

```kotlin
/* Create MockDataManagerImp with injected dependencies
	single<DataManager> {
		MockDataManagerImp(
			dittoConfig = get(),
			errorService = get()
		)
	}
*/
```

Next, uncomment the implementation that defines a DittoManager.  Update the code to look like this:

```kotlin
// Create DittoManager with injected dependencies
single<DataManager> {
	DittoManager(
		dittoConfig = get(),     // Koin will provide the DittoConfig instance
		androidDittoDependencies = DefaultAndroidDittoDependencies(this@TasksApplication),
		errorService = get()
	)
}
```

You can save those changes and close the TasksApplication.kt file.  

## Run the App

You can now run the app and test the new functionality.  The app should now show a list of tasks that are not deleted that we populated in the previous lab when we updated the `populateTaskCollection` function.  Note that while the app will show you a list of tasks, we haven't added the functionality to add, update, or delete tasks yet. We will do that in the next lesson. 

[Return to the lesson](../README.md) to continue.