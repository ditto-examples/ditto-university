# Lab 1: Populate the Task Collection

Find the [`populateTaskCollection()`](../../android/app/src/main/java/live/ditto/quickstart/tasks/data/DittoManager.kt#L75) function in the [`DittoManager.kt`](https://github.com/ditto-examples/ditto-university/blob/main/course-101/android/app/src/main/java/live/ditto/quickstart/tasks/data/DittoManager.kt) file.  This is the place to populate the task collection with data.  

The existing code should look like this:

```kotlin
override suspend fun populateTaskCollection() {
  return withContext(Dispatchers.IO) {
    val tasks = listOf(
      TaskModel("50191411-4C46-4940-8B72-5F8017A04FA7", "Buy groceries"),
      TaskModel("6DA283DA-8CFE-4526-A6FA-D385089364E5", "Clean the kitchen"),
      TaskModel("5303DDF8-0E72-4FEB-9E82-4B007E5797F0", "Schedule dentist appointment"),
      TaskModel("38411F1B-6B49-4346-90C3-0B16CE97E174", "Pay bills")
            )
      tasks.forEach { task ->
        try {
          //
          //TODO: add tasks into the ditto collection using INSERT statment
          // https://docs.ditto.live/sdk/latest/crud/write#inserting-documents
          //

          //UPDATE CODE HERE
        } catch (e: Exception) {
          errorService.showError("Unable to insert initial documents: ${e.message}")
          Log.e(TAG, "Unable to insert initial document", e)
        }
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
```kotlin
"task" to mapOf(
  "_id" to task._id,
  "title" to task.title,
  "done" to task.done,
  "deleted" to task.deleted,
)
```

> [!NOTE] 
>It's extremely important that the `:task` variable is used in the DQL statement matches the name of the argument.  If it doesn't match, the DQL statement will not work.

In order to run a DQL statement, we need to use the `store.execute()` method from the `Ditto` instance, passing it a `DQL` query and `arguments`.  Now replace the `TODO` comment with the code to use the `DQL` statement to insert the task into the task collection.

```kotlin
ditto?.store?.execute(
  "INSERT INTO tasks INITIAL DOCUMENTS (:task)",
    mapOf(
      "task" to mapOf(
      "_id" to task._id,
      "title" to task.title,
      "done" to task.done,
      "deleted" to task.deleted,
    )
  )
)
```

All DQL statements are executed through the `store` property and the execute method of the `Ditto` instance.

With this code now replaced, we can run the unit test to verify that the following was setup correctly:
- The `dittoConfig.xml` was updated with the App ID, Online Playground Authentication Token, Auth URL, and Websocket URL
- The ditto instance was initialized `init` function the with the App ID, Online Playground Authentication Token, Auth URL, and Websocket URL
- The `populateTaskCollection()`  function was updated with the DQL statement to insert the task into the task collection.

The `populateTaskCollection` function has an integration test that can be used to validate that your code is working properly.  To run the integration tests, you can open up the `DittoManagerTests` class from the `live.ditto.quickstart.tasks` androidTest folder. Locate the `testDittoPopulateTaskCollection` function and then use the play icon on the left to select `Run testDittoPopulateTaskCollection` menu that appears.  This will run the unit test and verify that the task collection was populated with the correct data.

![Unit Tests](../../assets/android-unit-tests.gif)

You should see a message that the `Test Passed!`   Congratulations - virtual high five 🙏! You've done your first major task of replacing the mock data with a real Ditto database.  Let's move on to the next step of adding offline-first data persistence.  

[Return to the lesson](../README.md) to continue.
