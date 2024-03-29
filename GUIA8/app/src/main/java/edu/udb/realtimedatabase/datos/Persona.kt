package edu.udb.realtimedatabase.datos

import java.util.*
import kotlin.collections.HashMap

class Persona {
    fun key(key: String?) {
    }

    var dui: String? = null
    var nombre: String? = null
    var birthday: String? = null
    var genero: String? = null
    var peso: String? = null
    var altura: String? = null
    var key: String? = null
    var per: MutableMap<String, Boolean> = HashMap()

    constructor() {}
    constructor(dui: String?, nombre: String?,birthday: String?,genero: String?,peso:String?,altura:String?) {
        this.dui = dui
        this.nombre = nombre
        this.birthday = birthday
        this.genero = genero
        this.peso = peso
        this.altura = altura
    }

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "dui" to dui,
            "nombre" to nombre,
            "birthday" to birthday,
            "genero" to genero,
            "peso" to peso,
            "altura" to altura,
            "key" to key,
            "per" to per
        )
    }
}