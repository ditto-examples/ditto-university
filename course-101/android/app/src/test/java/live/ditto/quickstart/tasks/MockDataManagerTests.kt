package live.ditto.quickstart.tasks

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import live.ditto.quickstart.tasks.models.DittoConfig
import live.ditto.quickstart.tasks.services.ErrorService
import live.ditto.quickstart.tasks.data.MockDataManagerImp
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import live.ditto.quickstart.tasks.models.TaskModel
import org.junit.Test
import org.junit.Assert.*

/**
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class MockDataManagerTests {

    @Test
    fun testMockPopulateTaskCollection() = runTest {
        //arrange
        val mockManager = MockDataManagerImp(createTestDittoConfig(), createErrorService())
        val expectedTitles = listOf(
            "Buy groceries",
            "Clean the kitchen",
            "Schedule dentist appointment",
            "Pay bills"
        )

        //act
        mockManager.populateTaskCollection()
        advanceUntilIdle()

        //assert
        val tasks = mockManager.getTaskModels().first()
        assertEquals(4, tasks.size)
        assertEquals(expectedTitles, tasks.map { it.title })
    }

    @Test
    fun testMockInsertTaskModel() = runTest {
        //arrange
        val mockManager = MockDataManagerImp(createTestDittoConfig(), createErrorService())
        val newTask = createInitialTaskModel()
        val initalCount = mockManager.getTaskModels().first().size

        //act
        mockManager.insertTaskModel(newTask)

        //assert
        val tasks = mockManager.getTaskModels().first()
        val newCount = tasks.size
        val insertedTask = tasks.find { it._id == newTask._id }

        assertEquals(initalCount + 1, newCount)
        assertNotNull(insertedTask)
        assertEquals(newTask, insertedTask)
    }

    @Test
    fun testMockUpdateTaskModel() = runTest {
        //arrange
        val mockManager = MockDataManagerImp(createTestDittoConfig(), createErrorService())
        val newTask = createInitialTaskModel()
        mockManager.insertTaskModel(newTask)
        advanceUntilIdle()

        //act
        val updatedTask = newTask.copy(title = "Updated Task", done = true)
        mockManager.updateTaskModel(updatedTask)
        advanceUntilIdle()

        //assert
        val tasks = mockManager.getTaskModels().first()
        val newCount = tasks.size
        val insertedUpdatedTask = tasks.find { it._id == newTask._id }
        assertEquals(1, newCount)
        assertNotNull(insertedUpdatedTask)
        assertEquals(updatedTask, insertedUpdatedTask)
    }

    @Test
    fun testMockUpdateTaskModel_WhenDeleted() = runTest {
        //arrange
        val mockManager = MockDataManagerImp(createTestDittoConfig(), createErrorService())
        val newTask = createInitialTaskModel()
        mockManager.insertTaskModel(newTask)
        advanceUntilIdle()

        //act
        newTask.deleted = true
        mockManager.updateTaskModel(newTask)
        advanceUntilIdle()

        //assert
        val tasks = mockManager.getTaskModels().first()
        val newCount = tasks.size
        assertEquals(0, newCount)
    }

    @Test
    fun testMockToggleComplete() = runTest {
        //arrange
        val mockManager = MockDataManagerImp(createTestDittoConfig(), createErrorService())
        val newTask = createInitialTaskModel()
        mockManager.insertTaskModel(newTask)
        advanceUntilIdle()

        //act
        mockManager.toggleComplete(newTask._id)
        advanceUntilIdle()

        //assert
        val tasks = mockManager.getTaskModels().first()
        val newCount = tasks.size
        val insertedUpdatedTask = tasks.find { it._id == newTask._id }
        assertEquals(1, newCount)
        assertNotNull(insertedUpdatedTask)
        assertEquals(insertedUpdatedTask?.done, true)
    }

    @Test
    fun testMockDeleteTaskModel () = runTest {
        //arrange
        val mockManager = MockDataManagerImp(createTestDittoConfig(), createErrorService())
        val newTask = createInitialTaskModel()
        mockManager.insertTaskModel(newTask)
        advanceUntilIdle()

        //act
        mockManager.deleteTaskModel(newTask._id)
        advanceUntilIdle()

        //assert
        val tasks = mockManager.getTaskModels().first()
        val newCount = tasks.size
        assertEquals(0, newCount)
    }

    private fun createInitialTaskModel() : TaskModel {
        return TaskModel(
            _id = "TEST-123-456-789",
            title = "Test Task",
            done = false,
            deleted = false)
    }

    private fun createErrorService() : ErrorService {
        return ErrorService()
    }

    private fun createTestDittoConfig() : DittoConfig {
        return DittoConfig("", "", "test_app", "test_token")
    }
}