package com.guru.recognizetext.utils

import android.content.Context
import android.widget.Toast


fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun <R : Any> safeLetAll(vararg params: Any?, block: (List<Any?>) -> R): R? {
    return if (params.all { it != null }) {
        block(params.toList())
    } else {
        null
    }
}

data class Person(
    var id: Long? = null,
    var name: String? = null,
    var lastName: String? = null,
    var gender: Boolean? = null
)

data class User(
    var id: Long = 0L,
    var name: String = "",
    var lastName: String = "",
    var gender: Boolean = false
)

class A {
    val person = Person()

    fun getUser() {
        val userDetails: User? = null
        person.id?.let {
            userDetails?.id = it
        }
        person.name?.let {
            userDetails?.name = it
        }
        person.lastName?.let {
            userDetails?.lastName = it
        }
        person.gender?.let {
            userDetails?.gender = it
        }
    }
}

class B {
    val person = Person(id = 101,"g","l",true)
    val newUserDetails = User()

    fun getNewUserDetails() {
        safeLetAll(person.id, person.name, person.lastName, person.gender) { result ->
            newUserDetails.id = result[0] as Long
            newUserDetails.name = result[1] as String
            newUserDetails.lastName = result[2] as String
            newUserDetails.gender = result[3] as Boolean
        }

        System.out.println(newUserDetails)
    }
}

fun main() {
    val b = B()
    b.getNewUserDetails()
}


//how can handle multiple null checks at ones using extension function