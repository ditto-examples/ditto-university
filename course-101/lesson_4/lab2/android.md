# Lab 2: Update an existing TaskModel - Kotlin JetPack Compose Android Application 

Find the [`updateTaskModel`](../../android/app/src/main/java/live/ditto/quickstart/tasks/data/DittoManager.kt#L299) function in the [`DittoManager.kt`](https://github.com/ditto-examples/ditto-university/blob/main/course-101/android/app/src/main/java/live/ditto/quickstart/tasks/data/DittoManager.kt) file.  This is the function that updates an existing TaskModel object that is passed in as an argument. 

The existing code should look like this:

```kotlin
override suspend fun updateTaskModel(taskModel: TaskModel) {
	return withContext(Dispatchers.IO) {
		try {
			//
			//TODO: write UPDATE DQL Statement
			// https://docs.ditto.live/dql/update#basic-update
			// https://docs.ditto.live/sdk/latest/crud/update#updating
			//

			//UPDATE CODE HERE
			val query = ""

			//
			//TODO: use dittoInstance store to execute DQL with arguments
			// https://docs.ditto.live/sdk/latest/crud/update#updating
			//

			//UPDATE CODE HERE

		} catch (e: Exception) {
			errorService.showError("Failed to update taskModel: ${e.message}")
			Log.e(TAG, "Failed to update taskModel:", e)
		}
	}
}
```
Let's implement the functionality by updating the `query` variable with the correct `DQL UPDATE` statement.  The UPDATE command in DQL works very similar to the `UPDATE` command in SQL.  We need to tell the UPDATE command which collection to update, and then we need to specify the fields in the document we want to update and the values we want to update them to.  The syntax looks like this:

```sql
UPDATE your_collection_name
SET
  color = :color,
  numberOfWidgets = :numberOfWidgets 
WHERE _id = :id
```

The `:color`, `:numberOfWidgets` and `:id` are a `DQL` variables that will be replaced with the actual task data, which we pass in as an argument.  

Let's update the code:  update the `query` variable with the correct `DQL UPDATE` string:

```kotlin
val query = """
	UPDATE tasks
	SET title = :title,
		done = :done,
		deleted = :deleted
	WHERE _id = :_id 
	"""
```
In this statement we are going to update the title, done, and deleted fields based on what the values are in the `TaskModel` object that provided in the the function as an argument. 

As before, in order to run a DQL statement, we need to use the `store?.execute()` method from the `ditto` variable, passing it a `DQL` query and `arguments`. Remember to use the `?` operator to check if the `ditto` variable is not `null` before calling the `store.execute()` method.

Next, find the following `TODO` comments below in the code after the `var query` variable is defined.  

```kotlin
//
//TODO: use dittoInstance store to execute DQL with arguments
// https://docs.ditto.live/sdk/latest/crud/update#updating
//

//UPDATE CODE HERE
```

Replace these comments with the code that uses the `store?.execute()` method from the `ditto` variable, passing the `DQL` query and `arguments` as shown below:

```kotlin
ditto?.store?.execute(
	query,
	mapOf(
		"title" to taskModel.title,
		"done" to taskModel.done,
		"deleted" to taskModel.deleted,
		"_id" to taskModel._id
	)
)
```

Notice how we are passing in the values in the arguments dictionary with the same name as the variables in the `DQL` statement.  This is important because the `DQL` statement will replace the variables with the actual values when it is executed. 

### Running the Unit Test

With this code updated, you can run the unit test associated with this function.  You can select the `DittoManagerTests` and click the play button next to the `testDittoUpdateTaskModel()` function.  This will run the unit test and verify that the task collection documents were updated the correct data.

You should see a message that the `Test Passed!`   Congratulations - virtual high five 🙏!

## Updating the toggleComplete function

Now is your turn to write all the code needed to update the [`toggleComplete`](../../android/app/src/main/java/live/ditto/quickstart/tasks/data/DittoManager.kt#L341) function in the [`DittoManager.kt`](https://github.com/ditto-examples/ditto-university/blob/main/course-101/android/app/src/main/java/live/ditto/quickstart/tasks/data/DittoManager.kt) file.  

This function is called when the user taps the circle button in a given task in the Jetpack Compose Android app.  When the button is tapped, the function is called and it updates the `done` field in the task collection document. 

The current code in the `toggleComplete` function looks like this:

```kotlin
override suspend fun toggleComplete(taskModel: TaskModel) {
	return withContext(Dispatchers.IO) {
		try {
				//
				//TODO: write UPDATE DQL Statement
				// https://docs.ditto.live/dql/update#basic-update
				// https://docs.ditto.live/sdk/latest/crud/update#updating
				//

				//UPDATE CODE HERE
				val query = ""

				//
				//TODO: use dittoInstance store to execute DQL with arguments
				// https://docs.ditto.live/sdk/latest/crud/update#updating
				//

				// UPDATE CODE HERE
			
		} catch (e: Exception) {
			errorService.showError("Failed to update taskModel: ${e.message}")
			Log.e(TAG, "Failed to update taskModel:", e)
		}
	}
}
```

The only two arguments needed in the `DQL UPDATE` statement are the `:done` and `:_id` variables.  The `:done` variable is the value we want to update the `done` field to, and the `:_id` variable is the value we want to update the `_id` field to. 

Update the `query` variable with the correct `DQL UPDATE` and then update the code to use the `store?.execute()` method from the `ditto` variable, passing the `DQL` query and `arguments`.  Remember to use the `?` operator to check if the `ditto` variable is not `null` before calling the `store?.execute()` method.

This should look very similar to the code we wrote in the `updateTaskModel` function, just with less fields to update.

### Running the Unit Test

With this code updated, you can run the unit test associated with this function.  You can select the `DittoManagerTests` and click the play button next to the `testDittoToggleComplete()` function.  This will run the unit test and verify that the task collection documents were updated the correct data.

You should see a message that the `Test Succeeded!`   Congratulations - another virtual high five 🙏!  You are getting really good at this!

### Test the Funtionality in the SwiftUI App 

 You've updated the app giving end users the ability to update TaskModel objects in the Ditto Database!   You can test this functionality by pushing a new version of the app to your emulator or physical device and tapping on an existing task and then updating the text in the title of the task.  You can also use the circle button to toggle the `done` field in the task collection document.  
 
 Like before, the code we updated in the `getTaskModels` function from the previous lab will automatically update the UI to display the updates to the task.  We are making great progress, but we are missing the delete functionality to remove a task from the task listing in the mobile app.  Let's add that functionality next.


[Return to the lesson](../README.md) to continue.