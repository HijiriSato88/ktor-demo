package example.com.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import example.com.model.*
import example.com.dataaccess.UserDataAccessor
import io.ktor.server.request.*

fun Route.usersRoutes(userDataAccessor: UserDataAccessor) {
    route("/api/v1/users") {
        // ユーザ追加用のエンドポイント
        post("/register") {
            val user = call.receive<User>()
            userDataAccessor.addUser(user)
            call.respond(HttpStatusCode.OK, mapOf("status" to "SUCCESS"))
        }

        // ユーザ情報取得用のエンドポイント
        get("/{name}") {
            val name = call.parameters["name"] ?:""
            val result = userDataAccessor.getUser(name)
            if (result != null) {
                call.respond(HttpStatusCode.OK, result)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("status" to "Not Found"))
            }
        }
    }
}
