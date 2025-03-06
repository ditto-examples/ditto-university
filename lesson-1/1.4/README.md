# 1.4 - Create Your First Ditto Document

> [!NOTE] 
>This is a proof of concept for the Ditto iOS Introduction to Ditto Learning Module.  It is a simple to-do list app that uses Ditto to store and sync the task data.  This is not production ready code and is only meant to be used as a reference for the POC of Ditto University learning modules. 

## Intro to DQL

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

## Now Update the App

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

## TODO - view the change on the portal

Now that we've successfully populated our task collection, let's verify the data made it to the Ditto Cloud by viewing it in the portal:

1. Log into the [Ditto Portal](https://portal.ditto.live)
2. Navigate to your app
3. Click on the "Collections" tab
4. Select the "tasks" collection

You should see the initial tasks we just created listed in the portal. This confirms that our data is successfully syncing to the Ditto Cloud!

## Next Step: Subscriptions and Observers

Now that we have our data persisting in Ditto, let's learn how to keep our UI in sync with real-time changes using [Subscriptions and Observers](../1.5/README.md).
