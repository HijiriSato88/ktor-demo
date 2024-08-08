package example.com.dataaccess

import example.com.dataaccess.table.UserTable
import example.com.model.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
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
    fun getUser(id:Int): User? {
        var result: User? = null
        transaction {
            result = UserTable
                .select { UserTable.id eq id }//指定したidに一致するもの探す
                .map { convertToUser(it) }//Query型をUserオブジェクトに変換
                .firstOrNull()//結果がない場合にnull返す
        }
        return result
    }

    fun getAllUsers(): List<User> {
        var result: List<User> = listOf()
        transaction {
            result = UserTable
                .selectAll()
                .map { convertToUser(it) }
        }
        return result
    }

    fun updateMailaddress(id: Int, newMailAddress: String): Int {
        return transaction {
            UserTable.update({ UserTable.id eq id }) {
                it[this.mailaddress] = newMailAddress
            }
        }
    }

    fun deleteUser(id: Int): Int{
        return transaction {
            UserTable.deleteWhere { UserTable.id eq id }
        }
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