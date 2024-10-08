package example.com.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import example.com.model.*
import example.com.dataaccess.UserDataAccessor
import io.ktor.server.auth.*
import io.ktor.server.request.*

fun Route.usersRoutes(userDataAccessor: UserDataAccessor) {
    route("/api/v1") {

        authenticate {
            get("/authenticated") {
                val user = call.authentication.principal<UserIdPrincipal>()
                call.respondText("authenticated id=${user?.name}")
            }
        }

        // ユーザ追加用のエンドポイント
        post("/register/user") {
            val user = call.receive<User>()
            userDataAccessor.addUser(user)
            call.respond(HttpStatusCode.OK, mapOf("status" to "Success"))
        }

        // ユーザ情報取得用のエンドポイント
        get("/{id}/user") {
            val idParam = call.parameters["id"] // ここで idParam は String? 型
            val id = idParam?.toIntOrNull()
            if (id != null) {
                val result = userDataAccessor.getUser(id) // getUser に Int を渡す
                if (result != null) {
                    call.respond(HttpStatusCode.OK, result)
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("status" to "Not Found"))
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, mapOf("status" to "Invalid ID format"))
            }
        }

        get("/all/user") {
            val users = userDataAccessor.getAllUsers() // Retrieve all users from the data accessor
            if (users.isNotEmpty()) {
                call.respond(HttpStatusCode.OK, users)
            } else {
                call.respond(HttpStatusCode.NoContent, mapOf("status" to "No Users Found"))
            }
        }

        patch("/{id}/e-mail") {
            val idParam = call.parameters["id"]
            val id = idParam?.toIntOrNull()
            if (id != null) {
                val updateRequest = call.receive<User>()
                val rowsUpdated = userDataAccessor.updateMailaddress(id, updateRequest.mailaddress)
                if (rowsUpdated > 0) {
                    call.respond(HttpStatusCode.OK, mapOf("status" to "Success"))
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("status" to "User not found"))
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, mapOf("status" to "Invalid ID format"))
            }

        }

        delete("/{id}/user") {
            val idParam = call.parameters["id"]
            val id = idParam?.toIntOrNull()
            if (id != null) {
                val rowsDeleted = userDataAccessor.deleteUser(id)
                if (rowsDeleted > 0) {
                    call.respond(HttpStatusCode.OK, mapOf("status" to "Success"))
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("status" to "User not found"))
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, mapOf("status" to "Invalid ID format"))
            }
        }

    }
}
