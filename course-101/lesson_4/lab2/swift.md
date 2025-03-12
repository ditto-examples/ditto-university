# Lab 2: Update an existing TaskModel - Swift UI iOS Application

Find the [`updateTaskModel`](../../swift/Tasks/Data/DittoManager.swift#L276) function in the [`DittoManager.swift`](https://github.com/ditto-examples/ditto-university/blob/main/course-101/swift/Tasks/Data/DittoManager.swift) file.  This is the function that updates an existing TaskModel object that is passed in as an argument. 

The existing code should look like this:

```swift
func updateTaskModel(_ task: TaskModel) async {
	//
	//TODO: write UPDATE DQL Statement
	// https://docs.ditto.live/dql/update#basic-update
	// https://docs.ditto.live/sdk/latest/crud/update#updating
	//
	
	//UPDATE CODE HERE
	let query = ""
	
	do {
		if let dittoInstance = ditto {
			//
			//TODO: use dittoInstance store to execute DQL with arguments
			// https://docs.ditto.live/sdk/latest/crud/update#updating
			//
			
			//UPDATE CODE HERE
		}
	} catch {
		appManager.setError(error)
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

```swift
let query = """
	UPDATE tasks SET
		title = :title,
		done = :done,
		deleted = :deleted
	WHERE _id == :_id
	"""
```
In this statement we are going to update the title, done, and deleted fields based on what the values are in the `task` object that provided in the the function as an argument. 

As before, in order to run a DQL statement, we need to use the `store.execute()` method from the `dittoInstance` variable, passing it a `DQL` query and `arguments`.  

Find the following `TODO` comments below in the code after the `let query` variable is defined.  

```swift
//
//TODO: use dittoInstance store to execute DQL with arguments
// https://docs.ditto.live/sdk/latest/crud/update#updating
//
```
Replace these comments with the code that uses the `store.execute()` method from the `dittoInstance` variable, passing the `DQL` query and `arguments` as shown below:

```swift
try await dittoInstance.store.execute(
	query: query,
	arguments: [
		"title": task.title,
		"done": task.done,
		"deleted": task.deleted,
		"_id": task._id,
	]
)
```

Notice how we are passing in the values in the arguments dictionary with the same name as the variables in the `DQL` statement.  This is important because the `DQL` statement will replace the variables with the actual values when it is executed. 

### Running the Unit Test

With this code updated, you can run the unit test associated with this function.  You can select the Task Navigator tab in Xcode and click the play button next to the `testDittoUpdateTaskModel()` function listed under the DittManagerTests group.  This will run the unit test and verify that the task collection documents were updated the correct data.

You should see a message that the `Test Succeeded!`   Congratulations - virtual high five 🙏!

## Updating the toggleComplete function

Now is your turn to write all the code needed to update the [`toggleComplete`](../../swift/Tasks/Data/DittoManager.swift#L342) function in the [`DittoManager.swift`](https://github.com/ditto-examples/ditto-university/blob/main/course-101/swift/Tasks/Data/DittoManager.swift) file.  

This function is called when the user taps the circle button in a given task in the SwiftUI app.  When the button is tapped, the function is called and it updates the `done` field in the task collection document. 

The current code in the `toggleComplete` function looks like this:

```swift
func toggleComplete(task: TaskModel) async {
	let done = !task.done
	
	//
	//TODO: write UPDATE DQL Statement
	// https://docs.ditto.live/dql/update#basic-update
	// https://docs.ditto.live/sdk/latest/crud/update#updating
	//
	
	//UPDATE CODE HERE
	let query = ""
	
	do {
		if let dittoInstance = ditto {
			//
			//TODO: use dittoInstance store to execute DQL with arguments
			// https://docs.ditto.live/sdk/latest/crud/update#updating
			//
			
			// UPDATE CODE HERE
		}
	} catch {
		appManager.setError(error)
	}
}
```

The only two arguments needed in the `DQL UPDATE` statement are the `:done` and `:_id` variables.  The `:done` variable is the value we want to update the `done` field to, and the `:_id` variable is the value we want to update the `_id` field to. 

Update the `query` variable with the correct `DQL UPDATE` and then update the code to use the `store.execute()` method from the `dittoInstance` variable, passing the `DQL` query and `arguments`.

This should look very similar to the code we wrote in the `updateTaskModel` function, just with less fields to update.

### Running the Unit Test

With this code updated, you can run the unit test associated with this function.  You can select the Task Navigator tab in Xcode and click the play button next to the `testDittoToggleComplete()` function listed under the DittManagerTests group.  This will run the unit test and verify that the task collection documents were updated the correct data.

You should see a message that the `Test Succeeded!`   Congratulations - another virtual high five 🙏!

### Test the Funtionality in the SwiftUI App 

 You've updated the app giving end users the ability to update TaskModel objects in the Ditto Database!   You can test this functionality by pushing a new version of the app to your simulator or physical device and tapping on an existing task and then updating the text in the title of the task.  You can also use the circle button to toggle the `done` field in the task collection document.  
 
 Like before, the code we updated in the `registerObservers` function from the previous lab will automatically update the UI to display the updates to the task.  We are making great progress, but we are missing the delete functionality to remove a task from the task listing in the mobile app.  Let's add that functionality next.


[Return to the lesson](../README.md) to continue.