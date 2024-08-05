package example.com.model

data class User(
    val id: Int,
    val name: String,
    val mailaddress: String,
)

data class UpdateMailRequest(
    val mailaddress: String
)

