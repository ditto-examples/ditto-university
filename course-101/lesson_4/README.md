# Lesson 4 🚀 DQL INSERT, UPDATE, and SOFT DELETE

Let's start this lesson with a lab using our new found knowledge of the `DQL INSERT` command.

## Lab 1: Using DQL to INSERT new TaskModel objects into the task collection

In the previous lesson, we used the DQL `INSERT` command to populate the initial task the app should include in the task collection.  In this lab, we will use the DQL `INSERT` command to insert a new task into the task collection when a user uses the UI to add a new task.

Follow the instructions based on what platform you are using:

- [Insert a new TaskModel - Swift UI iOS Application](lab1/swift.md)
- [Insert a new TaskModel - Android JetPack Compose with Kotlin Application](lab1/android.md)
- [Insert a new TaskModel - Flutter Application](lab1/flutter.md)

If you are stuck, you can refer to the `.finished` files in the [lab1](./lab1) directory with the corresponding platform name. 

## DQL (Ditto Query Language) - UPDATE Statement

As previously discussed, Ditto uses its own query language called `Ditto Query Language (DQL)` to manipulate data.  When using DQL's `UPDATE` command, you can update an existing document or several documents in a given collection based on the `WHERE` clause.  An overview of the DQL syntax is:

```sql
UPDATE your_collection_name
SET field1 = value1, field2 -> [mutator], ...
WHERE [condition]
```

> [!NOTE] 
> For a detailed guide of the `DQL UPDATE` syntax, see the [DQL UPDATE documentation](https://docs.ditto.live/dql/update).
>

We can use this syntax to update a document in the task collection when a user uses the UI to edit a task.

### Lab 2:  Update a TaskModel 

In this lab we will use the `DQL UPDATE` statement to mutate an existing document in the task collection.  

Follow the instructions based on what platform you are using:

- [Update an existing TaskModel - Swift UI iOS Application](lab2/swift.md)
- [Update an existing Collection - Android JetPack Compose with Kotlin Application](lab2/android.md)
- [Update an existing Collection - Flutter Application](lab2/flutter.md)

If you are stuck, you can refer to the `.finished` files in the [lab2](./lab2) directory with the corresponding platform name. 

## The Soft-Delete Pattern


### Lab 3:  Updating the TaskModel to include a Soft-Delete

In this lab we will create a DQL `SELECT` statement to use the observer to update the the app to show realtime updates when a task is added, updated, or deleted from any device.  Our main UI uses observers to show a list of tasks.  This feature is useful for implementing real-time updates across devices, like when one user adds, updates, or deletes a task, the other devices can see the task in realtime.      

Follow the instructions based on what platform you are using:

- [Updating the TaskModel to include a Soft-Delete - Swift UI iOS Application](lab3/swift.md)
- [Updating the TaskModel to include a Soft-Delete - Android JetPack Compose with Kotlin Application](lab3/android.md)
- [Updating the TaskModel to include a Soft-Delete - Flutter Application](lab3/flutter.md)

If you are stuck, you can refer to the `.finished` files in the [lab3](./lab3) directory with the corresponding platform name. 

## ❓ Knowledge Check 

1. What is a collection in Ditto?
   - a) A set of attachments for a document that is stored in the Ditto database 
   - b) A system for managing user authentication and permissions
   - c) A versioning system that tracks changes to documents over time
   - d) A namespace for grouping similar documents together, similar to tables in relational databases

2. What is DQL? 
   - a) A query language for managing documents in a Ditto database, similar to SQL
   - b) A programming language for building Ditto applications
   - c) A system for managing user authentication and permissions
   - d) A versioning system that tracks changes to documents over time

3. Which `SELECT` statement properly returns all the documents in the collection `animals WHERE` the `hasFur` property is set to true? 
   - a) SELECT * FROM bigCreatures THAT hasFur = true 
   - b) SELECT _id, name, hasFur FROM animals WHERE hasFur = true
   - c) SELECT * FROM animals WHERE hasFur = true
   - d) SELECT THE FURRY ONES FROM animals WHERE hasFur = true

The answer can be found in the [answer file](.answer).

## Summary

🎉 Congratulations 🙌! In this lesson, you have learned:
- Ditto Collections
- DQL INSERT and SELECT Statements
- How to react to Data Changes using Observers

## Next Steps

Now that you have got the app up and running observing data changes, you're read to implent inserting new tasks and updating them.  Let's go!

[Continue to Lesson 5 - Syncing Data Across Devices using Small Peers →](../lesson_5/README.md)