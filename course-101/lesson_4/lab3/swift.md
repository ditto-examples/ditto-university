# Lab 3: Updating the TaskModel to include a Soft-Delete - Swift UI iOS Application

Find the [`deleteTaskModel`](../../swift/Tasks/Data/DittoManager.swift#L387) function in the [`DittoManager.swift`](https://github.com/ditto-examples/ditto-university/blob/main/course-101/swift/Tasks/Data/DittoManager.swift) file.  This is the function that sets the deleted field to true for an existing TaskModel object that is passed in as an argument. 

The existing code should look like this:

```swift
func deleteTaskModel(_ task: TaskModel) async {
	//
	//TODO: write UPDATE DQL Statement using Soft-Delete pattern
	// https://docs.ditto.live/sdk/latest/crud/delete#soft-delete-pattern
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

This function is called when the user swipes on a given task row and taps on the `Delete` button.  When the `Delete` button is tapped, the function is called and it updates the `deleted` field in the task collection document to be set to true. 

The only one arguments needed in the `DQL UPDATE` statement: the `:_id` variable.  We can hard code the deleted field to be equal to true using the following syntax:

```sql
SET deleted = true
```

Update the `query` variable with the correct `DQL UPDATE` statement that sets the deleted field to true based on the `:_id` variable, then update the code to use the `store.execute()` method from the `dittoInstance` variable, passing the `DQL` query and `argument` which should be the `_id` field from the `task` object.

This should look very similar to the code we wrote in the `updateTaskModel` function we did in Lab 2, just with less arguments to update.

### Running the Unit Test

With this code updated, you can run the unit test associated with this function.  You can select the Task Navigator tab in Xcode and click the play button next to the `testDittoDeleteTaskModel()` function listed under the DittManagerTests group.  This will run the unit test and verify that the task collection document was marked as deleted.

You should see a message that the `Test Succeeded!`   Congratulations - another virtual high five 🙏!

### Test the Funtionality in the SwiftUI App 

 You've updated the app giving end users the ability to delete a given TaskModel objects in the Ditto Database, thus removing it from the list of Tasks in the app!   You can test this functionality by pushing a new version of the app to your simulator or physical device and swiping on an existing task and then tapping the Delete button.  
 
 Like before, the code we updated in the `registerObservers` function from the previous lab will automatically update the UI remove the tasks that you delete. 

 The app seems to be fully functional, but how do we sync information between the devices?  Let's look at how that works next. 


[Return to the lesson](../README.md) to continue.