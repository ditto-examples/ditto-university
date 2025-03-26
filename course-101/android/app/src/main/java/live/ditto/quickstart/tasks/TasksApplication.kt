package live.ditto.quickstart.tasks

import android.app.Application
import live.ditto.android.DefaultAndroidDittoDependencies
import live.ditto.quickstart.tasks.data.DataManager
import live.ditto.quickstart.tasks.data.DittoManager
import live.ditto.quickstart.tasks.edit.EditScreenViewModel
import live.ditto.quickstart.tasks.list.TasksListScreenViewModel
import live.ditto.quickstart.tasks.models.DittoConfig
import live.ditto.quickstart.tasks.services.ErrorService
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import java.io.File
import java.util.UUID

class TasksApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Start Koin dependency injection
        // https://insert-koin.io/docs/reference/koin-android/start
        GlobalContext.startKoin {
            androidLogger()
            androidContext(this@TasksApplication)
            modules(registerDependencies())
        }
    }

    private fun registerDependencies() : Module {
       return module {
           // Create DittoConfig as a single instance
           single {
               DittoConfig(
                   getString(R.string.authUrl),
                   getString(R.string.websocketUrl),
                   getString(R.string.appId),
                   getString(R.string.authToken)
               )
           }

           // Create DittoManager with injected dependencies
           single<DataManager> {
               DittoManager(
                   dittoConfig = get(),     // Koin will provide the DittoConfig instance
                   androidDittoDependencies = DefaultAndroidDittoDependencies(this@TasksApplication),
                   context = get(),         // Koin will provide the Application context
                   errorService = get()
               )
           }

           // Create TasksListScreenViewModel with injected DittoManager
           viewModel { TasksListScreenViewModel(get(), this@TasksApplication) }

           // Create EditScreenViewModel with injected DittoManager
           viewModel { EditScreenViewModel(get()) }

           // add in the ErrorService which is used to display errors in the app
           single { ErrorService() }
       }
    }
}
