import Foundation


/// A document in the `tasks` collection
struct TaskModel {
    let _id: String
    var title: String
    var done: Bool = false
    var deleted: Bool = false
}

extension TaskModel {
    /// Convenience initializer returns instance from `QueryResultItem.value`
    init(_ value: [String: Any?]) {
        self._id = value["_id"] as! String
        self.title = value["title"] as? String ?? ""
        self.done = value["done"] as? Bool ?? false
        self.deleted = value["deleted"] as? Bool ?? false
    }
}

extension TaskModel {
    /// Returns properties as key/value pairs for DQL INSERT query
    var value: [String: Any?] {
        [
            "_id": _id,
            "title": title,
            "done": done,
            "deleted": deleted,
        ]
    }
}

// MARK: - Identifiable
extension TaskModel: Identifiable {
    
    /// Required for SwiftUI List view
    var id: String {
        return _id
    }
    
}

// MARK: - Equatable
extension TaskModel: Equatable {
    /// Required for ContentView List animation
    static func == (lhs: Self, rhs: Self) -> Bool {
        lhs.id == rhs.id
    }
}

// MARK: - Codable
extension TaskModel: Codable {
    /// Returns optional instance decoded from `QueryResultItem.jsonString()`
    init?(_ json: String) {
        do {
            self = try JSONDecoder().decode(Self.self, from: Data(json.utf8))
        } catch {
            print("ERROR:", error.localizedDescription)
            return nil
        }
    }
}

// MARK: - Preview support
extension TaskModel {
    
    /// Convenience initializer with defaults for previews and instances generated for new tasks
    init(
        title: String = "", done: Bool = false, deleted: Bool = false
    ) {
        self._id = UUID().uuidString
        self.title = title
        self.done = done
        self.deleted = deleted
    }
}
