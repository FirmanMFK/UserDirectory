package com.firman.directoryuser.feature.user.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CityDto(
    @SerialName("id") val id: String? = null,
    @SerialName("name") val name: String? = null
)
