package example.com

import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.* // CORS機能をインポート
import io.ktor.server.routing.* // ルーティングをインポート
import io.ktor.http.*
import io.ktor.serialization.gson.*
import org.jetbrains.exposed.sql.Database
import example.com.dataaccess.UserDataAccessor
import example.com.routes.*
import example.com.routes.usersRoutes

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module() {

    install(ContentNegotiation) {
        gson {
        }
    }

    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowCredentials = true
        anyHost() // 本番環境では特定のホストに置き換えることを検討してください
    }

    // データベースのホスト名と接続情報を環境変数から取得
    val databaseHostName = System.getenv("DATABASE_HOSTNAME") ?: "localhost"
    val databasePassword = System.getenv("MYSQL_ROOT_PASSWORD") ?: "password"
    val databaseName = System.getenv("MYSQL_DATABASE") ?: "mydatabase"

    Database.connect(
        url = "jdbc:mysql://$databaseHostName:3307/$databaseName",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root",
        password = databasePassword // 環境変数からパスワードを指定
    )

    routing {
        usersRoutes(UserDataAccessor())
    }
}
