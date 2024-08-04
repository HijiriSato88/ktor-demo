package example.com.dataaccess

import example.com.dataaccess.table.UserTable
import example.com.model.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class UserDataAccessor {
    // ユーザ追加
    fun addUser(user: User) {//Userデータクラス使用
        transaction {
            UserTable.insert {
                it[this.id] = user.id
                it[this.name] = user.name
                it[this.mailaddress] = user.mailaddress
            }
        }
    }

    // ユーザ情報を取得する
    fun getUser(name: String): User? {
        var result: User? = null
        transaction {
            result = UserTable
                .select { UserTable.name eq name }//指定したnameに一致するもの探す
                .map { convertToUser(it) }//Userオブジェクトに変換
                .firstOrNull()//結果がない場合にnull返す
        }
        return result
    }

    // usersテーブルのレコードをUserオブジェクトに変換する
    private fun convertToUser(row: ResultRow): User {
        return User(
            id = row[UserTable.id],
            name = row[UserTable.name],
            mailaddress = row[UserTable.mailaddress]
        )
    }
}