package com.blackpineapple.ziptzopt.data.model

data class Contact(
        val name: String,
        val number: String,
        var message: String = "",
        var picture: String = ""
)