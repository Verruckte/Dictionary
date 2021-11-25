package com.project.model.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Meanings(
    @Expose val translation: Translation?,

    @Expose
    @field:SerializedName("imageUrl")
    val imageUrl: String?
)