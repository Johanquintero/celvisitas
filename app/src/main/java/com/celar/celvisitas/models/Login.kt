package com.celar.celvisitas.models

import com.google.gson.JsonObject

data class Login(var success: Boolean, var data : JsonObject,var message: String)
