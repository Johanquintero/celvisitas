package com.celar.celvisitas.models

import com.google.gson.JsonArray
import com.google.gson.JsonObject

class VisitAllowedOrReject(
var success: Boolean,
var data: JsonObject,
var message: String
) {
    constructor() : this(success = false, JsonObject(), "",
    )
}