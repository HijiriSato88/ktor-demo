package example.com.model

import org.eclipse.jetty.util.security.Password

data class User(
    val id: Int,
    val name: String,
    val mailaddress: String,
    val password: String
)

