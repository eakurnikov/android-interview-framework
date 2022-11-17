package com.example.myapplication.basics

/**
 * Task: Strings comparison. What will be printed?
 */

class StringsComparison {
    val s1: String = "111"
    val s2: String = "111"
    var s3: String = "11"

    /**
     * What will be printed?
     */
    fun main() {
        println("s1 == s2: ${s1 == s2}")
        println("s1 === s2: ${s1 === s2}")

        println("s1 == s3: ${s1 == s3}")
        println("s1 === s3: ${s1 === s3}")
    }
    /**
     * true, true,
     * false, false
     */

    /**
     * And after that?
     */
    fun second() {
        s3 += "1"
        println("s1 == s3: ${s1 == s3}")
        println("s1 === s3: ${s1 === s3}")
    }
    /**
     * true, false
     */
}
