import Testing
import Foundation
@testable import Tasks

struct MockDataManagerTests {

    @Test
    func testMockPopulateTaskCollection() async throws {
        // Arrange
        let appManager = createTestAppManager()
        let mockManager = await Tasks.MockDataManager(appManager: appManager)
        
        // Act
        try await mockManager.populateTaskCollection()
        
        // Assert
        await #expect(mockManager.tasks.count == 4, "Should have 4 initial tasks")
        
        // Verify specific tasks exist
        let expectedTitles = [
            "Buy groceries",
            "Clean the kitchen",
            "Schedule dentist appointment",
            "Pay bills"
        ]
        
        // Get tasks once to avoid multiple actor-isolated calls
        let tasks = await mockManager.tasks
        
        for (index, expectedTitle) in expectedTitles.enumerated() {
            #expect(tasks[index].title == expectedTitle,
                   "Task at index \(index) should have title '\(expectedTitle)'")
        }
        
        // Verify IDs are unique
        let uniqueIds = Set(tasks.map { $0._id })
        #expect(uniqueIds.count == tasks.count,
               "All task IDs should be unique")
    }

    @Test
    func testMockInsertTaskModel() async throws {
        // Arrange
        let appManager = createTestAppManager()
        let mockManager = await Tasks.MockDataManager(appManager: appManager)
        let newTask = createInitialTask()
        
        // Get initial count
        let initialCount = await mockManager.tasks.count
        
        // Act
        await mockManager.insertTaskModel(newTask)
        
        // Assert
        let tasks = await mockManager.tasks
        #expect(tasks.count == initialCount + 1, "Should have one more task after insertion")
        
        // Verify the new task was added correctly
        if let insertedTask = tasks.first(where: { $0._id == newTask._id }) {
            #expect(insertedTask.title == newTask.title, "Task title should match")
            #expect(insertedTask.done == newTask.done, "Task done status should match")
            #expect(insertedTask.deleted == newTask.deleted, "Task deleted status should match")
        } else {
            #expect(Bool(false), "New task should be found in tasks array")
        }
    }

    @Test
    func testMockUpdateTaskModel() async throws {
        // Arrange
        let appManager = createTestAppManager()
        let mockManager = await Tasks.MockDataManager(appManager: appManager)
        let initialTask = createInitialTask()
        
        // Add initial task
        await mockManager.insertTaskModel(initialTask)
        
        // Create updated version of task
        var updatedTask = initialTask
        updatedTask.title = "Updated Title"
        updatedTask.done = true
        
        // Act
        await mockManager.updateTaskModel(updatedTask)
        
        // Assert
        let tasks = await mockManager.tasks
        #expect(tasks.count == 1, "Should still have one task after update")
        
        if let task = tasks.first {
            #expect(task._id == initialTask._id, "Task ID should remain unchanged")
            #expect(task.title == "Updated Title", "Task title should be updated")
            #expect(task.done == true, "Task done status should be updated")
        } else {
            #expect(Bool(false), "Updated task should be found in tasks array")
        }
    }
    
    @Test
    func testMockUpdateTaskModel_WhenDeleted() async throws {
        // Arrange
        let appManager = createTestAppManager()
        let mockManager = await Tasks.MockDataManager(appManager: appManager)
        let initialTask = createInitialTask()
        
        // Add initial task
        await mockManager.insertTaskModel(initialTask)
        
        // Create deleted version of task
        var deletedTask = initialTask
        deletedTask.deleted = true
        
        // Act
        await mockManager.updateTaskModel(deletedTask)
        
        // Assert
        let tasks = await mockManager.tasks
        #expect(tasks.count == 0, "Task array should be empty after deleting task")
        #expect(!tasks.contains(where: { $0._id == initialTask._id }), 
               "Deleted task should not be in array")
    }

    @Test
    func testMockToggleComplete() async throws {
        // Arrange
        let appManager = createTestAppManager()
        let mockManager = await Tasks.MockDataManager(appManager: appManager)
        let initialTask = createInitialTask()
        
        // Add initial task
        await mockManager.insertTaskModel(initialTask)
        
        // Act - Toggle complete (false -> true)
        await mockManager.toggleComplete(task: initialTask)
        
        // Assert first toggle
        var tasks = await mockManager.tasks
        if let task = tasks.first {
            #expect(task.done == true, "Task should be marked as done after first toggle")
            #expect(task._id == initialTask._id, "Task ID should remain unchanged")
            #expect(task.title == initialTask.title, "Task title should remain unchanged")
        } else {
            #expect(Bool(false), "Task should still exist after toggle")
        }
        
        // Act - Toggle complete again (true -> false)
        if let toggledTask = tasks.first {
            await mockManager.toggleComplete(task: toggledTask)
            
            // Assert second toggle
            tasks = await mockManager.tasks
            if let finalTask = tasks.first {
                #expect(finalTask.done == false, "Task should be marked as not done after second toggle")
                #expect(finalTask._id == initialTask._id, "Task ID should remain unchanged")
                #expect(finalTask.title == initialTask.title, "Task title should remain unchanged")
            } else {
                #expect(Bool(false), "Task should still exist after second toggle")
            }
        }
    }
    
    @Test
    func testMockDeleteTaskModel() async throws {
        // Arrange
        let appManager = createTestAppManager()
        let mockManager = await Tasks.MockDataManager(appManager: appManager)
        let initialTask = createInitialTask()
        
        // Add initial task
        await mockManager.insertTaskModel(initialTask)
        
        // Verify task was added
        var tasks = await mockManager.tasks
        #expect(tasks.count == 1, "Should have one task before deletion")
        #expect(tasks.first?._id == initialTask._id, "Task should be found before deletion")
        
        // Act
        await mockManager.deleteTaskModel(initialTask)
        
        // Assert
        tasks = await mockManager.tasks
        #expect(tasks.count == 0, "Task array should be empty after deletion")
        #expect(!tasks.contains(where: { $0._id == initialTask._id }), 
               "Deleted task should not be in array")
    }

    private func createInitialTask() -> Tasks.TaskModel {
        return Tasks.TaskModel(
            _id: "TEST-123-456-789",
            title: "Test Task",
            done: false,
            deleted: false
        )
    }

    private func createTestAppManager() -> Tasks.AppManager {
        let config = Tasks.DittoConfig(endpointUrl: "http://localhost:8080",
                                       appId: "test_app",
                                       authToken: "test_token")
        let appManager = Tasks.AppManager(configuration: config)
        return appManager
    }

}
