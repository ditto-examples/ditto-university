# Lab 1: Populate the Task Collection

Find the [`populateTaskCollection()`](../../swift/Tasks/Data/DittoManager.swift#L109) function in the [`DittoManager.swift`](https://github.com/ditto-examples/ditto-university/blob/main/course-101/swift/Tasks/Data/DittoManager.swift) file.  This is the place to populate the task collection with data.  

The existing code should look like this:

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

![Unit Tests](../../assets/unit-tests.gif)

You should see a message that the `Test Succeeded!`   Congratulations - virtual high five 🙏! You've done your first major task of replacing the mock data with a real Ditto database.  Let's move on to the next step of adding offline-first data persistence.  

[Return to the lesson](../README.md) to continue.