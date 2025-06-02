//
//  TaskRowView.swift
//  Tasks
//
//  Created by Aaron LaBeau on 2/24/25.
//

import SwiftUI

struct TaskModelRowView: View {
    let task: TaskModel
    
    var onToggle: ((_ task: TaskModel) -> Void)?
    var onClickEdit: ((_ task: TaskModel) -> Void)?
    
    var body: some View {
        HStack {
            Image(systemName: task.done ? "circle.fill" : "circle")
                .renderingMode(.template)
                .foregroundColor(.accentColor)
                .frame(minWidth: 32)
                .onTapGesture {
                    onToggle?(task)
                }
            if task.done {
                Text(task.title)
                    .strikethrough()
            } else {
                Text(task.title)
            }
        }
        .contentShape(Rectangle())
        .onTapGesture {
            onClickEdit?(task)
        }
    }
}

struct TaskModelRowView_Previews: PreviewProvider {
    static var previews: some View {
        List {
            TaskModelRowView(task: TaskModel(title: "Get Milk", done: true))
            TaskModelRowView(task: TaskModel(title: "Do Homework"))
            TaskModelRowView(task: TaskModel(title: "Take out trash", done: true))
        }
    }
}
