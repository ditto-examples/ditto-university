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

The soft-delete pattern is a way to flag data as inactive while retaining it for various requirements, such as archival evidence, reference integrity, prevention of potential data loss due to end-user error, and so on.  

Our app is already using the soft-delete pattern.  When we registered our observer, we made sure to filter out only documents where the done is set to false:

```swift
let observerQuery = "SELECT * FROM tasks WHERE NOT deleted"
```

This means that if a user deletes a task, the task is not removed from the database, but instead the `deleted` field is set to true.  This allows us to retain the task in the database for future reference, while also allowing us to filter out tasks that have been deleted from the UI.

### Lab 3:  Updating the TaskModel to include a Soft-Delete

In this lab we will update the code to use the soft-delete pattern to delete a task.  When a user deletes a task, the `deleted` field is set to true.  This allows us to retain the task in the database for future reference, while also allowing us to filter out tasks that have been deleted from the UI.

Follow the instructions based on what platform you are using:

- [Updating the TaskModel to include a Soft-Delete - Swift UI iOS Application](lab3/swift.md)
- [Updating the TaskModel to include a Soft-Delete - Android JetPack Compose with Kotlin Application](lab3/android.md)
- [Updating the TaskModel to include a Soft-Delete - Flutter Application](lab3/flutter.md)

If you are stuck, you can refer to the `.finished` files in the [lab3](./lab3) directory with the corresponding platform name. 

## ❓ Knowledge Check 

1. Which `UPDATE` statement properly updates the `animal` collection with all documents that the field animalType equal bear and sets the hybernate field to true?
   - a) UPDATE animals LET hybernate = true WHERE animalType = "winnie the pooh"
   - b) UPDATE animals SET hybernate = true WHERE animalType = "bear"
   - c) UPDATE mammals SET hybernate = true WHERE type = "bear"
   - d) UPDATE animals LET hybernate = !hybernate WHERE animalType = "bear"

2. What is the purpose of the soft-delete pattern?
   - a) To temporarily store deleted data in a separate backup collection for 30 days before permanent deletion
   - b) To compress deleted documents to save storage space while maintaining data integrity
   - c) To automatically trim the document size by removing all the data minus the deleted field 
   - d) To mark documents as deleted instead of removing them, preserving data for potential restoration while maintaining history

The answer can be found in the [answer file](.answer).

## Summary

🎉 Congratulations 🙌! In this lesson, you have learned:
- How to using DQL to INSERT new TaskModel objects into the task collection
- How to use DQL to UPDATE an existing TaskModel object in the task collection
- How to use the soft-delete pattern to delete a TaskModel object from the task collection
- How the registerObserver function uses the soft-delete pattern to filter out deleted tasks from the UI

## 📚 References

- [DQL UPDATE](https://docs.ditto.live/dql/update)
- [Updating Documents](https://docs.ditto.live/sdk/latest/crud/update)
- [Soft-Delete Pattern](https://docs.ditto.live/sdk/latest/crud/delete#soft-delete-pattern)

## Next Steps

Now that you have got the app up and running observing data changes, you are ready to look at syncing data between multiple devices using small peers and Ditto's Peer-to-Peer Syncing feature.  Let's go!

[Continue to Lesson 5 - Syncing Data Across Devices using Small Peers →](../lesson_5/README.md)