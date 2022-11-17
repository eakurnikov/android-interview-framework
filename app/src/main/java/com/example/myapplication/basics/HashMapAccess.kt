package com.example.myapplication.basics

/**
 * Task: fix the access to the value in hashmap. What will be the execution result? How to see 1 1?
 */

// 0 - remove "data"
data class Person(
    val name: String,
    var age: Int
) {
//  override equals/hashcode
    override fun equals(other: Any?): Boolean {
        other ?: return false
        if (this === other) return true
        if (other !is Person) return false
        return this.name == other.name
    }

//  1
    private val hash: Int by lazy { name.hashCode() * 31 + age }
    override fun hashCode(): Int = hash

//    2
//    override fun hashCode(): Int = name.hashCode()
}

fun main() {
    val person = Person("Alice", 20)
    val hashmap = hashMapOf(person to 1)

    println(hashmap[person])

    person.age = 21

    println(hashmap[person])
}
