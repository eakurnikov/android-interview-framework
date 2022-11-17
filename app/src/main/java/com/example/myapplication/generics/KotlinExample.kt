package com.example.myapplication.generics

/**
 * Task: generics. Will it compile in Kotlin? Why?
 */

fun main() {
    val integers: List<Int> = arrayListOf()
    val numbers: List<Number> = integers
}
