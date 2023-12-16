package com.bookmart.bookmart.BasicTools

import kotlin.random.Random


class OTP_genrater_six_digit {

    fun generateRandomNumber(): Int {
        // Generate a random number between 100,000 (inclusive) and 999,999 (exclusive)
        return Random.nextInt(100_00, 1_000_00)
    }
}