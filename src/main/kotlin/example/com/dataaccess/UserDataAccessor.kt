package example.com.dataaccess

import example.com.dataaccess.table.UserTable
import example.com.model.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class UserDataAccessor {
    // メッセージを追加する
    fun addUser(user: User) {//Messageデータクラス使用
        transaction {
            UserTable.insert {
                it[this.id] = user.id
                it[this.name] = user.name
                it[this.mailaddress] = user.mailaddress
            }
        }
    }

    // メッセージを取得する
    fun getUser(name: String): User? {
        var result: User? = null
        transaction {
            result = UserTable
                .select { UserTable.name eq name }//指定したidに一致するもの探す
                .map { convertToUser(it) }//Messageオブジェクトに変換
                .firstOrNull()//結果がない場合にnull返す
        }
        return result
    }

    // messagesテーブルのレコードをMessageオブジェクトに変換する
    private fun convertToUser(row: ResultRow): User {
        return User(
            id = row[UserTable.id],
            name = row[UserTable.name],
            mailaddress = row[UserTable.mailaddress]
        )
    }
}