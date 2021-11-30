package com.celar.celvisitas.models

data class Visit(
    var success: Boolean,
    var data: ArrayList<Any>,
    var message: String
//    var couerier_drops: [Drops]!
    ) {
    constructor() : this(success = false, arrayListOf<Any>(), "",
    )
}



