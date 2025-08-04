package com.example.boostbonk.utils

import java.math.BigInteger

object Base58 {
    private val ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz"
    private val BASE = ALPHABET.length

    fun encode(input: ByteArray): String {
        var bi = input.fold(0.toBigInteger()) { acc, byte ->
            (acc shl 8) + (byte.toInt() and 0xFF).toBigInteger()
        }

        val result = StringBuilder()
        while (bi > BigInteger.ZERO) {
            val mod = (bi % BASE.toBigInteger()).toInt()
            result.insert(0, ALPHABET[mod])
            bi /= BASE.toBigInteger()
        }

        input.takeWhile { it == 0.toByte() }.forEach { result.insert(0, ALPHABET[0]) }
        return result.toString()
    }
}