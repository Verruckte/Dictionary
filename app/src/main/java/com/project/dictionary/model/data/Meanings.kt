package com.project.dictionary.model.data

import com.google.gson.annotations.Expose

class Meanings(
    @Expose val translation: Translation?,
    @Expose val imageUrl: String?
)