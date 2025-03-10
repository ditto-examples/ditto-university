# 1.3 Try Out The Sample Data 🚀


## Understanding The Data Model

In our Tasks app, data is stored in a collection called `tasks` within the Big Peer instance. Each task is stored as a JSON document in this collection. The Ditto SDK provides a unified API for:
- Reading and writing data to the Big Peer
- Synchronizing with other devices (called `Small Peers`) running the same app
- Managing offline data persistence
- Handling real-time updates

This architecture enables seamless data synchronization between:
- Your device and the cloud (Big Peer)
- Your device and other users' devices (Small Peers)
- The cloud and all connected devices



# Introduction to Ditto Learning Module with iOS and SwiftUI

Welcome to the Tasks app learning module! This hands-on project will guide you through integrating Ditto into an existing SwiftUI application, teaching you essential concepts of offline-first data synchronization. You'll experience a common real-world scenario: transforming a prototype into a production-ready application with robust data management capabilities.
## Project Structure

The app currently uses a `MockDataManager` for all data operations. Your task will be to implement the `DittoManager` class to replace the mock functionality with real Ditto features. The project includes TODO comments guiding you through each implementation step.

The mock implementation provides a clear reference for how each feature should work, making it easier to understand what needs to be replicated with Ditto.

Let's get started transforming this prototype into a production-ready app!

### The TaskModel Struct

The Tasks app uses a `TaskModel` struct to represent each task in the system. Each task has four properties:

- `_id`: A unique identifier for the task
- `title`: The description of the task
- `done`: A boolean indicating whether the task is completed
- `deleted`: A boolean used for implementing soft deletes

When a task is completed, its `done` property is set to `true`. Instead of permanently removing tasks from the database, the app uses a "soft delete" pattern - setting the `deleted` property to `true` when a task is deleted. This approach preserves the task's data for potential restoration and maintains a complete history of all tasks.

For more information about implementing soft deletes in Ditto, see the [soft delete pattern documentation](https://docs.ditto.live/sdk/latest/crud/delete#soft-delete-pattern).

## Building and Running the iOS Application

To see the app in action running with mock data, let's build and run the app by following these steps:

1. Log into the Ditto portal at <https://portal.ditto.live/>.  Make note of the app ID and online playground token.
2. Launch Xcode and open the `/Tasks/Tasks.xcodeproj` project.
3. Open the `dittoConfig.plist` file and update your app ID and online playground token with the values from the Ditto portal.
4. Navigate to the project **Signing & Capabilities** tab and modify the **Team** and **Bundle Identifier** settings to your Apple developer account credentials to provision building to your device.
5. In Xcode, select a connected iOS device or iOS Simulator as the destination.
6. Choose the **Product > Run** menu item.

The app will build and run on the selected device or emulator.  You can add, edit, and delete tasks in the app.  But if you close the app and open it up, all your changes will be lost due to the mock data.  Let's start fixing that by replacing the mock data with a real Ditto database.



This all seems great, but how can we test this?  Let's update the populateTaskCollection function to use Ditto to populate the initial task the app should include in the task collection.  Then we can use the unit tests to verify that the task collection was populated with the correct data, which proves that the Ditto Identity was setup correctly.

### Step 2: Populate the Task Collection

Find the `populateTaskCollection()` function.  This is the place to populate the task collection with data.

```swift
func populateTaskCollection() async throws {
	
	let initialTasks: [TaskModel] = [
		TaskModel(
			_id: "50191411-4C46-4940-8B72-5F8017A04FA7",
			title: "Buy groceries"),
		TaskModel(
			_id: "6DA283DA-8CFE-4526-A6FA-D385089364E5",
			title: "Clean the kitchen"),
		TaskModel(
			_id: "5303DDF8-0E72-4FEB-9E82-4B007E5797F0",
			title: "Schedule dentist appointment"),
		TaskModel(
			_id: "38411F1B-6B49-4346-90C3-0B16CE97E174",
			title: "Pay bills"),
	]
	
	for task in initialTasks {
		do {
			if let dittoInstance = ditto {
				//
				//TODO: add tasks into the ditto collection using INSERT statment
				// https://docs.ditto.live/sdk/latest/crud/write#inserting-documents
				//
				
				//UPDATE CODE HERE
			}
		} catch {
			appManager.setError(error)
		}
	}
}
```

This function initializes the app with some starter data, making it an ideal place to demonstrate using the Ditto SDK for inserting documents into the task collection. Let's implement the functionality by updating the `TODO` comment.

Ditto uses its own query language called `Ditto Query Language (DQL)` to manipulate data. Similar to SQL, `DQL` provides a familiar syntax for database operations. For a detailed explanation of DQL syntax, see the [document creation guide](https://docs.ditto.live/sdk/latest/crud/create#creating-documents).

The following `DQL` statement can be used to insert a document into the task collection:
```sql
INSERT INTO tasks INITIAL DOCUMENTS (:task)
```
Note this uses a special key word `INITIAL DOCUMENTS` which tells Ditto to insert the documents into the collection only if they don't already exist.  

The `:task` is a `DQL` variable that will be replaced with the actual task data, which we pass in as an argument.  The argument would take in the task object which is represented by the TaskModel struct, which we discussed earlier.

The arguments syntax would look like this:
```swift
"task": 
  [
      "_id": task._id,
      "title": task.title,
      "done": task.done,
      "deleted": task.deleted,
  ]
```

> [!NOTE] 
>It's extremely important that the `:task` variable is used in the DQL statement matches the name of the argument.  If it doesn't match, the DQL statement will not work.

In order to run a DQL statement, we need to use the `store.execute()` method from the `Ditto` instance, passing it a `DQL` query and `arguments`.  Now replace the `TODO` comment with the code to use the `DQL` statement to insert the task into the task collection.

```swift
try await dittoInstance.store.execute(
  query: "INSERT INTO tasks INITIAL DOCUMENTS (:task)",
  arguments: [
    "task":
     [
         "_id": task._id,
         "title": task.title,
         "done": task.done,
         "deleted": task.deleted,
     ]
  ]
)
```

All DQL statements are executed through the `store` property and the execute method of the `Ditto` instance.

With this code now replaced, we can run the unit test to verify that the following was setup correctly:
- The `dittoConfig.plist` was updated with the app ID and online playground token
- The ditto instance was initialized `initialize()` function the with the app ID and online playground token
- The `populateTaskCollection()`  function was updated with the DQL statement to insert the task into the task collection.

To run the unit tests, you can select the Task Navigator tab in Xcode and click the play button next to the `populateTaskCollection()` function listed under the DittManagerTests group.  This will run the unit test and verify that the task collection was populated with the correct data.

![Unit Tests](./assets/unit-tests.gif)

You should see a message that the `Test Succeeded!`   Congratulations - virtual high five 🙏! You've done your first major task of replacing the mock data with a real Ditto database.  Let's move on to the next step of adding offline-first data persistence.

### Step 3: Register Observers for the Task Collection 

An observer is a function that is called when any documents in a collection that match a query change.  Store observers are useful when you want to monitor changes from your local Ditto store and react to them immediately. For instance, when your end user updates their profile, asynchronously display changes in realtime.  This feature is useful for implementing real-time updates across devices, like when one user adds a task, the other user sees the task in realtime.

The Ditto Manager provides a `@Published` property `tasks` which is an array of `TaskModel` objects.  Our UI in the app responds to changes in this property to update and draw the list of tasks.  We can use the observer to update the `tasks` property when the data in the task collection changes for any reason, like when a task is added, updated, or deleted from your device or other devices that have the app installed.



## Run the App

You can now run the app and test the new ditto functionality.  You can add, update, and delete tasks from your device and other devices that have the app installed.  You can also see the changes in realtime as they are made.

## Conclusion

Congratulations - you have completed the Tasks app learning module! 🎉 You've now replaced the mock data with a real Ditto database and implemented offline-first data persistence.  You can now add, update, and delete tasks from your device and other devices that have the app installed.  You can also see the changes in realtime as they are made.
