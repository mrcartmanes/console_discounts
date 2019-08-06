/*
MIT License

Copyright (c) 2019 Tom Seifert

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package com.psdiscounts.domain

/**
 * An algebraic data type to provide either a [Failure][F] or a [Success][S] result.
 */
sealed class Either<out F, out S> {

    /**
     * Calls [failed] with the [failure][Failure.failure] value if result is a [Failure]
     * or [succeeded] with the [success][Success.success] value if result is a [Success]
     */
    inline fun <T> fold(failed: (F) -> T, succeeded: (S) -> T): T =
        when (this) {
            is Failure -> failed(failure)
            is Success -> succeeded(success)
        }
}

data class Failure<out F>(val failure: F) : Either<F, Nothing>()

data class Success<out S>(val success: S) : Either<Nothing, S>()

/**
 * Allows chaining of multiple calls taking as argument the [success][Success.success] value of the previous call and
 * returning an [Either].
 *
 * 1. Unwrap the result of the first call from the [Either] wrapper.
 * 2. Check if it is a [Success].
 * 3. If yes, call the next function (passed as [ifSucceeded]) with the value of the [success][Success.success]
 * property as an input parameter (chain the calls).
 * 4. If no, just pass the [Failure] through as the end result of the whole call chain.
 *
 * In case any of the calls in the chain returns a [Failure], none of the subsequent flatmapped functions is called
 * and the whole chain returns this failure.
 *
 * @param ifSucceeded next function which should be called if this is a [Success]. The [success][Success.success]
 * value will be then passed as the input parameter.
 */
inline fun <F, S1, S2> Either<F, S1>.flatMap(succeeded: (S1) -> Either<F, S2>): Either<F, S2> =
    fold({ this as Failure }, succeeded)

/**
 * Map the [Success] value of the [Either] to another value.
 *
 * You can for example map an `Success<String>` to an `Success<Int>` by
 * using the following code:
 * ```
 * val fiveString: Either<Nothing, String> = Success("5")
 * val fiveInt : Either<Nothing, Int> = fiveString.map { it.toInt() }
 * ```
 */
inline fun <F, S1, S2> Either<F, S1>.map(f: (S1) -> S2): Either<F, S2> =
    flatMap { Success(f(it)) }