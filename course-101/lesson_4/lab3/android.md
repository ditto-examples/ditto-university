# Lab 3: Updating the TaskModel to include a Soft-Delete - Kotlin Jetpack Compose Application

Find the [`deleteTaskModel`](../../android/app/src/main/java/live/ditto/quickstart/tasks/data/DittoManager.kt#L388) function in the [`DittoManager.kt`](https://github.com/ditto-examples/ditto-university/blob/main/course-101/android/app/src/main/java/live/ditto/quickstart/tasks/data/DittoManager.kt) file.  This is the function that sets the deleted field to true for an existing TaskModel object that is passed in as an argument. 

The existing code should look like this:

```kotlin
override suspend fun deleteTaskModel(id: String) {
	return withContext(Dispatchers.IO) {
		try {
			//
			//TODO: write UPDATE DQL Statement using Soft-Delete pattern
			// https://docs.ditto.live/sdk/latest/crud/delete#soft-delete-pattern
			//
			//UPDATE CODE HERE
			val query = ""


		} catch (e: Exception) {
			errorService.showError("Failed to archive taskModel: ${e.message}")
			Log.e(TAG, "Failed to archive taskModel:", e)
		}
	}
}
```

This function is called when the user taps on the trash icon in a given task row. When the `trash` icon is tapped, the function is called and it updates the `deleted` field in the task collection document to be set to true. 

The only one arguments needed in the `DQL UPDATE` statement: the `:_id` variable.  We can hard code the deleted field to be equal to true using the following syntax:

```sql
SET deleted = true
```

Update the Code:  update the `query` variable with the correct `DQL UPDATE` statement that sets the deleted field to true based on the `:_id` variable, then update the code to use the `store?.execute()` method from the `ditto` variable, passing the `DQL` query and `argument` which should be the `_id` field from the `task` object.  Remember to use the `?` operator to check if the `ditto` variable is not `null` before calling the `store.execute()` method.

This should look very similar to the code we wrote in the `updateTaskModel` function we did in Lab 2, just with less arguments to update.

### Running the Unit Test

With this code updated, you can run the unit test associated with this function.  You can select the `DittoManagerTests` and click the play button next to the `testDittoDeleteTaskModel()` function.  This will run the unit test and verify that the task collection documents were updated the correct data.

You should see a message that the `Test Passed!`   Congratulations - another virtual high five 🙏!  You are getting really good at this!

### Test the Funtionality in the Jetpack Compose Android App 

 You've updated the app giving end users the ability to delete a given TaskModel objects in the Ditto Database, thus removing it from the list of Tasks in the app!   You can test this functionality by pushing a new version of the app to your emulator or physical device and tapping the `trash` icon.  
 
 Like before, the code we updated in the `getTaskModels` function from the previous lab will automatically update the UI remove the tasks that you delete. 

 The app seems to be fully functional, but how do we sync information between the devices?  Let's look at how that works next. 


[Return to the lesson](../README.md) to continue.