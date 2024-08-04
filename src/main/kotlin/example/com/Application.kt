package example.com

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.* // CORS機能をインポート
import io.ktor.server.routing.* // ルーティングをインポート
import io.ktor.http.*
import io.ktor.serialization.gson.*
import org.jetbrains.exposed.sql.Database
import example.com.dataaccess.MessageDataAccessor
import example.com.routes.*
import example.com.routes.messagesRoutes

fun main(args: Array<String>): Unit = io.ktor.server.jetty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module() {

    install(ContentNegotiation) {
        gson {
        }
    }

    /*フロントエンドと通信する場合に必要
    install(CORS) {
        allowMethod(HttpMethod.Options) // allowMethodを使用
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization) // allowHeaderを使用
        allowHeader(HttpHeaders.ContentType)
        allowCredentials = true
        anyHost() // 本番環境では特定のホストに置き換えることを検討してください
    }
     */

    // データベースのホスト名 (環境変数 `DATABASE_HOSTNAME` から取得)
    val databaseHostName = System.getenv("DATABASE_HOSTNAME") ?: "localhost"

    Database.connect(
        url = "jdbc:mysql://$databaseHostName:3307/mydatabase",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root",
        password = "password" // パスワードを指定
    )

    routing {
        messagesRoutes(MessageDataAccessor())
    }
}
