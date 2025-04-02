# Lab 1: Validate Sync Process - Kotlin with Jetpack Compose Application 
Find the [`startSync`](../../android/app/src/main/java/live/ditto/quickstart/tasks/data/DittoManager.kt#L234) function in the [`DittoManager.kt`](https://github.com/ditto-examples/ditto-university/blob/main/course-101/android/app/src/main/java/live/ditto/quickstart/tasks/data/DittoManager.kt#L234) file.  This is the function that starts the sync process and creates a subscription query.  

The existing code should look like this:

```kotlin
 private suspend fun startSync() {
        return withContext(Dispatchers.IO) {
            try {
                ditto?.let {
                    //
                    //TODO: implement the startSync
                    // https://docs.ditto.live/sdk/latest/install-guides/swift#integrating-and-initializing-sync
                    //

                    //UPDATE CODE HERE

                    //
                    //TODO: implement the set subscription
                    // https://docs.ditto.live/sdk/latest/sync/syncing-data#creating-subscriptions
                    //

                    //UPDATE CODE HERE
                }
            } catch (e: Exception) {
                errorService.showError("Failed to start ditto sync: ${e.message}")
                Log.e(TAG, "Failed to start ditto sync", e)
            }
        }
    }
```

The first update we need to make is to tell Ditto we are ready to start syncing information.  A ditto instance provides a `startSync` method that will start the sync process. 

Update the first TODO with the code to start the sync process:

```kotlin
it.startSync()
```

Now we can setup a subscription query to tell Ditto what data we want to synchronize.  In our subscription query, we want to select all the data from the `tasks` collection.  

## Coding Tasks:  

- Update the second TODO comment with a new variable `subscriptionQuery` that defines a DQL SELECT statement to select all the data from the `tasks` collection, without any filters (no WHERE clause).  
- Call the `it.sync.registerSubscription(query: subscriptionQuery)` method to create the subscription.  The `it` variable is the ditto instance since we are checking it for null:

```kotlin
ditto?.let {
```

### Validate the app still builds and runs 

With this code updated, you can build and run the app to validate it still builds and runs. Note that you should not see any errors in the console. We will validate that the subscription query working in the next lab using the Ditto Portal!  

[Return to the lesson](../README.md) to continue.