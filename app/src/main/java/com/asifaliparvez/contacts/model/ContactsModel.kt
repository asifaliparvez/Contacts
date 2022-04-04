package com.asifaliparvez.contacts.model

data class ContactsModel(
    var name:String,
    var id:String,
    var photo:String? = null,
    var lookUpKey:String
)
