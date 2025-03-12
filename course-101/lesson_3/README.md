# Lesson 3 🚀 Updating the initial Data with Ditto

## Collections 

Ditto stores documents in collections. Collections are analogous to tables in relational databases. Collections allow you to group similar documents together within the same namespace.  While it is good practice for documents within a collection to share a similar structure — for instance, all car-related documents stored in the cars collection contain fields 'make', 'model', and 'year' — structural uniformity is not mandatory.

There is no limit to the number of collections you can have.  Every document must be associated with a collection, even if only one document exists within that collection.  There are no explicit steps to manage collections; Ditto implicitly creates collections when you first store a document for that collection.

In our mobile app, data is stored in a collection called `tasks`. Each task is stored as a JSON document in this collection. 

## Code Structure

The app currently uses a `MockDataManager` for all data operations. Your task will be to implement the `DittoManager` to replace the mock functionality with real Ditto features. The code includes TODO comments guiding you through each implementation step.  The mock implementation provides a clear reference for how each feature should work, making it easier to understand what needs to be replicated with Ditto.  Let's get started transforming this prototype into a production-ready app!

### The TaskModel 

The Tasks app uses a `TaskModel` to represent each task in the system. Each task has four properties:

- `_id`: A unique identifier for the task
- `title`: The description of the task
- `done`: A boolean indicating whether the task is completed
- `deleted`: A boolean used for implementing soft deletes

When a task is completed, its `done` property is set to `true`. Instead of permanently removing tasks from the database, the app uses a "soft delete" pattern - setting the `deleted` property to `true` when a task is deleted. This approach preserves the task's data for potential restoration and maintains a complete history of all tasks.

For more information about implementing soft deletes in Ditto, see the [soft delete pattern documentation](https://docs.ditto.live/sdk/latest/crud/delete#soft-delete-pattern).

## DQL (Ditto Query Language) - INSERT Statement

Ditto uses its own query language called `Ditto Query Language (DQL)` to manipulate data. Similar to SQL, `DQL` provides a familiar syntax for database operations. When using DQL's `INSERT` command, you can add new documents to a collection using JSON objects.  An overview of the DQL syntax is:

```sql
INSERT INTO your_collection_name
DOCUMENTS ([document1]),([document2]), ([document3]), ...
[ON ID CONFLICT [FAIL | DO NOTHING | DO UPDATE]]
```

> [!NOTE] 
> For a detailed guide of the `DQL INSERT` syntax, see the [document creation guide](https://docs.ditto.live/sdk/latest/crud/create#creating-documents).
>

We can use this syntax to insert a document into the task collection.  

### Lab 1:  Populate the Task Collection

Let's update the populateTaskCollection function to use the DQL `INSERT` command to populate the initial task the app should include in the task collection.  Then we can use the unit tests to verify that the task collection was populated with the correct data, which proves that the Ditto Identity was setup correctly.

Follow the instructions based on what platform you are using:

- [Populate the Task Collection - Swift UI iOS Application](lab1/swift.md)
- [Populate the Task Collection - Android JetPack Compose with Kotlin Application](lab1/android.md)
- [Populate the Task Collection - Flutter Application](lab1/flutter.md)

If you are stuck, you can refer to the `.finished` files in the [lab1](./lab1) directory with the corresponding platform name. 

## Reacting to Data Changes: Observers 

A store observer is a function that is called when any documents in a collection that match a query change.  Store observers are useful when you want to monitor changes from your local Ditto store and react to them immediately. For instance, when your end user updates their profile, asynchronously display changes in realtime.  

### DQL (Ditto Query Language) - SELECT Statement

The `SELECT` operation, once executed, retrieves documents from a collection and uses clauses like `WHERE` to specify conditions for filtering the documents to return.  It is similar to the SQL `SELECT` statement.  

```sql
SELECT *
FROM your_collection_name
[WHERE condition]
[ORDER BY orderby_expression_1, orderby_expression_2, ... [ASC|DESC]]
[LIMIT limit_value]
[OFFSET number_of_documents_to_skip]
```

> [!NOTE] 
> DQL currently only supports the SELECT * operation for document retrieval. With SELECT *, all responses result in full documents.
>

For instance, retrieve all documents in the cars collection WHERE the color property is set to the value 'black' :

```sql
SELECT * FROM cars WHERE color = 'black'
```

Store observers can use a SQL `SELECT` statement to filter the documents in a given collection to observe. 

### Lab 2:  Register Observer

In this lab we will create a DQL `SELECT` statement to use the observer to update the the app to show realtime updates when a task is added, updated, or deleted from any device.  Our main UI uses observers to show a list of tasks.  This feature is useful for implementing real-time updates across devices, like when one user adds, updates, or deletes a task, the other devices can see the task in realtime.      

Follow the instructions based on what platform you are using:

- [Register Observer - Swift UI iOS Application](lab2/swift.md)
- [Register Observer - Android JetPack Compose with Kotlin Application](lab2/android.md)
- [Register Observer - Flutter Application](lab2/flutter.md)

If you are stuck, you can refer to the `.finished` files in the [lab2](./lab2) directory with the corresponding platform name. 

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

[Continue to Lesson 4 - DQL INSERT, UPDATE, and SOFT DELETE →](../lesson_4/README.md)
