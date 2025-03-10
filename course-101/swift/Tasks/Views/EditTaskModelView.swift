import SwiftUI

struct EditTaskModelView: View {
    //TODO: update to DittoManager
    @EnvironmentObject private var dataManager: MockDataManager
    @Environment(\.dismiss) private var dismiss
    @FocusState var titleHasFocus: Bool
    @StateObject private var viewModel: ViewModel
    
    init(taskModel: TaskModel?) {
        _viewModel = StateObject(wrappedValue: ViewModel(taskModel: taskModel))
    }
    
    var body: some View {
        NavigationView {
            Form {
                Section(header: Text("Task Details")) {
                    TextField("Title", text: $viewModel.selectedTaskModel.title)
                        .focused($titleHasFocus)
                        .onSubmit(onSubmit)
                    
                    Toggle("Is Completed", isOn: $viewModel.selectedTaskModel.done)
                }
               
                if viewModel.isExistingTask {
                    Section(header: Text("Danger Zone")) {
                        HStack {
                            Button(
                                action: {
                                    viewModel.deleteRequested.toggle()
                                },
                                label: {
                                    Text("Delete Task")
                                        .fontWeight(.bold)
                                        .foregroundColor(
                                            viewModel.deleteRequested
                                            ? .white : .red)
                                })
                            
                            Spacer()
                            
                            if viewModel.deleteRequested {
                                Image(systemName: "checkmark")
                                    .foregroundColor(.white)
                            }
                        }
                        .contentShape(Rectangle())
                    }
                    .listRowBackground(
                        viewModel.deleteRequested ? Color.red : nil)
                }
            }
            .navigationTitle(
                viewModel.isExistingTask ? "Edit Task" : "Create Task"
            )
            .navigationBarItems(
                leading: Button("Cancel") {
                    dismiss()
                },
                trailing: Button(viewModel.isExistingTask ? "Save" : "Create") {
                    onSubmit()
                }
            )
        }
        .onAppear {
            viewModel.setDataManager(dataManager)
            if !viewModel.isExistingTask {
                titleHasFocus = true
            }
        }
    }
    
    private func onSubmit() {
        Task { @MainActor in
            await viewModel.save()
            dismiss()
        }
    }
     
}

#Preview {
    EditTaskModelView(taskModel: TaskModel(title: "Get Milk", done: true))
}

extension EditTaskModelView {
    @Observable
    class ViewModel: ObservableObject {
        //TODO: update to DittoManager
        private var dataManager: MockDataManager?
        
        var isExistingTask: Bool = false
        var selectedTaskModel: TaskModel
        var deleteRequested:Bool = false
        
        init(taskModel: TaskModel? = nil) {
            self.isExistingTask = taskModel != nil
            self.selectedTaskModel = taskModel ?? TaskModel()
        }
        
        //TODO: update to DittoManager
        func setDataManager(_ manager: MockDataManager) {
            self.dataManager = manager
        }
        
        func save() async {
            if isExistingTask {
                selectedTaskModel.deleted = deleteRequested
                await dataManager?.updateTaskModel(selectedTaskModel)
            } else {
                await dataManager?.insertTaskModel(selectedTaskModel)
            }
        }
    }
}
