# Lab 1: Using DQL to INSERT new TaskModel objects into the task collection - Swift UI iOS Application

Find the [`insertTaskModel`](../../swift/Tasks/Data/DittoManager.swift#L243) function in the [`DittoManager.swift`](https://github.com/ditto-examples/ditto-university/blob/main/course-101/swift/Tasks/Data/DittoManager.swift) file.  This is the function that inserts a new TaskModel object into the task collection. 

The existing code should look like this:

```swift
func insertTaskModel(_ task: TaskModel) async {
 let newTask = task.value
 //
 // TODO - write INSERT DQL Statement
 // https://docs.ditto.live/dql/insert
 // https://docs.ditto.live/sdk/latest/crud/create#creating-documents
 //
        
 //UPDATE CODE HERE
 let query = ""
        
 do {
   if let dittoInstance = ditto {
   //
   //TODO: use dittoInstance store to execute DQL with arguments
   // https://docs.ditto.live/sdk/latest/crud/create#creating-documents
   //
                
   //UPDATE CODE HERE
  }
 } catch {
   appManager.setError(error)
  }
}
```

Let's implement the functionality by updating the `query` variable with the correct DQL statement.  In a previous lab we updatd the `populateTaskCollection` function.  When we created the DQL statement, we used the `INSERT` command but with a special key word `INITIAL`.  For inserting regular documents, you don't need to use the `INITIAL` keyword.  So instead you can update the `query` variable use this string in the DQL statement:  

```sql
INSERT INTO tasks DOCUMENTS (:newTask)
```

Like before, the `:newTask` is a `DQL` variable that will be replaced with the actual task data, which we pass in as an argument.  The argument would take in the task object which is represented by the TaskModel struct, which we discussed in the previous lab.  

> [!NOTE] 
>It's extremely important that the `:newTask` variable is used in the DQL statement matches the name of the argument.  If it doesn't match, the DQL statement will not work.

As before, in order to run a DQL statement, we need to use the `store.execute()` method from the `dittoInstance` variable, passing it a `DQL` query and `arguments`.  Now replace the `TODO` comment with the code to use the `DQL` statement to insert the task into the task collection using the `dittoInstance.store.execute()` method.  It's ok to go back and look at how you implemented the `populateTaskCollection` function to see how you inserted the task object into the task collection, as this should be very similar minus the removal of the `INITIAL` keyword.  

### Running the Unit Test

With this code updated, you can run the unit test associated with this lab.  You can select the Task Navigator tab in Xcode and click the play button next to the `testDittoInsertTaskModel()` function listed under the DittManagerTests group.  This will run the unit test and verify that the task collection was populated with the correct data.

You should see a message that the `Test Succeeded!`   Congratulations - virtual high five 🙏!

### Test the Funtionality in the SwiftUI App 

 You've updated the app giving end users the ability to add new TaskModel objects into the Ditto Database!   You can test this functionality by pushing a new version of the app to your simulator or physical device and add a new task to the task collection. Once you add a new task, the code we updated in the `registerObservers` function from the previous lab will automatically update the UI to display the new task.  We are making great progress, but users need the ability to update a task.  Let's add that functionality next.

[Return to the lesson](../README.md) to continue.