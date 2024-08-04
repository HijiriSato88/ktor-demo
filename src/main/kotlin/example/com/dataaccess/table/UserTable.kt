package example.com.dataaccess.table

import org.jetbrains.exposed.sql.Table

object UserTable : Table("users") {//usersテーブル
    val id = integer("id").autoIncrement() // 自動インクリメント
    val name = varchar("name", 255)
    val mailaddress = varchar("mailaddress", 255)
    override val primaryKey = PrimaryKey(id)
}