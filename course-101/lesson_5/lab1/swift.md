# Lab 1: Validate Sync Process - Swift UI iOS Application


Find the [`startSync`](../../swift/Tasks/Data/DittoManager.swift#L195) function in the [`DittoManager.swift`](https://github.com/ditto-examples/ditto-university/blob/main/course-101/swift/Tasks/Data/DittoManager.swift#L195) file.  This is the function that starts the sync process and creates a subscription query.  

The existing code should look like this:

```swift
private func startSync() throws {
	do {
		if let dittoInstance = ditto {
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
	} catch {
		appManager.setError(error)
		throw error
	}
}
```

The first update we need to make is to tell Ditto we are ready to start syncing information.  A ditto instance provides a `startSync` method that will start the sync process. 

Update the first TODO with the code to start the sync process:

```swift
try dittoInstance.startSync()
```

Now we can setup a subscription query to tell Ditto what data we want to synchronize.  In our subscription query, we want to select all the data from the `tasks` collection.  

## Coding Tasks:  

- Update the second TODO comment with a new variable `subscriptionQuery` that defines a DQL SELECT statement to select all the data from the `tasks` collection, without any filters (no WHERE clause).  
- Call the `dittoInstance.sync.registerSubscription(query: subscriptionQuery)` method to create the subscription. 

### Validate the app still builds and runs 

With this code updated, you can build and run the app to validate it still builds and runs. Note that you should not see any errors in the console. We will validate that the subscription query working in the next lab using the Ditto Portal!  

[Return to the lesson](../README.md) to continue.