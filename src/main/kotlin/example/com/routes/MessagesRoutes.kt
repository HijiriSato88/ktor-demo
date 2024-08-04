package example.com.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import example.com.model.*
import example.com.dataaccess.MessageDataAccessor
import io.ktor.server.request.*

fun Route.messagesRoutes(messageDataAccessor: MessageDataAccessor) {
    route("/api/v1/messages") {
        // メッセージ投稿用のエンドポイント
        post("/register") {
            val message = call.receive<Message>()
            messageDataAccessor.addMessage(message)
            call.respond(HttpStatusCode.OK, mapOf("status" to "SUCCESS"))
        }

        // メッセージ取得用のエンドポイント
        get("/{id}") {
            val messageId = call.parameters["id"] ?: ""
            val result = messageDataAccessor.getMessage(messageId)
            if (result != null) {
                call.respond(HttpStatusCode.OK, result)
            } else {
                call.respond(HttpStatusCode.NotFound, mapOf("status" to "Not Found"))
            }
        }
    }
}
