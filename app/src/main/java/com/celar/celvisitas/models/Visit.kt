package com.celar.celvisitas.models

data class Visit(
    var success: Boolean,
    var data: ArrayList<Any>,
    var message: Int
    ) {
    constructor() : this(success = false, arrayListOf<Any>(), 0,
    )
}



