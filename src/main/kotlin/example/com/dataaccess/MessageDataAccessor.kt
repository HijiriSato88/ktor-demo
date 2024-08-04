package example.com.dataaccess

import example.com.dataaccess.table.MessageTable
import example.com.model.Message
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class MessageDataAccessor {
    // メッセージを追加する
    fun addMessage(message: Message) {//Messageデータクラス使用
        transaction {
            MessageTable.insert {
                it[this.id] = message.id
                it[this.message] = message.message
            }
        }
    }

    // メッセージを取得する
    fun getMessage(id: String): Message? {
        var result: Message? = null
        transaction {
            result = MessageTable
                .select { MessageTable.id eq id }//指定したidに一致するもの探す
                .map { convertToMessage(it) }//Messageオブジェクトに変換
                .firstOrNull()//結果がない場合にnull返す
        }
        return result
    }

    // messagesテーブルのレコードをMessageオブジェクトに変換する
    private fun convertToMessage(row: ResultRow): Message {
        return Message(
            id = row[MessageTable.id],
            message = row[MessageTable.message],
        )
    }
}