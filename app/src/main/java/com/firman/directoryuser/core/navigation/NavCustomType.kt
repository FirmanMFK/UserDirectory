package com.firman.directoryuser.core.navigation

import android.os.Bundle
import androidx.navigation.NavType
import com.firman.directoryuser.feature.user.domain.model.User
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object NavCustomType {
    val UserType = object : NavType<User>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): User? {
            return bundle.getString(key)?.let { Json.decodeFromString(it) }
        }

        override fun parseValue(value: String): User {
            return Json.decodeFromString(value)
        }

        override fun serializeAsValue(value: User): String {
            return Json.encodeToString(value)
        }

        override fun put(bundle: Bundle, key: String, value: User) {
            bundle.putString(key, Json.encodeToString(value))
        }
    }
}
