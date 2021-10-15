package com.celar.celvisitas.models

import ObjetoJSON
import org.json.JSONObject


class User : ObjetoJSON {
    constructor() : super() {}
    constructor(obj: JSONObject?) : super(obj) {}

    fun setUser(u: JSONObject) {
        this.item = u
    }

    val estado: String
        get() = this.getField("estado")
    val nombreBasico: String
        get() = this.getField("nombre").split(" ").get(0)
            .toString() + " " + this.getField("apellidos").split(" ").get(0)
}


