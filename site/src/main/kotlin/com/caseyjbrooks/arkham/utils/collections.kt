package com.caseyjbrooks.arkham.utils

data class Tuple1<T1>(val t1: T1)
data class Tuple2<T1, T2>(val t1: T1, val t2: T2)
data class Tuple3<T1, T2, T3>(val t1: T1, val t2: T2, val t3: T3)

fun <T1> List<*>.destruct1(): Tuple1<T1> {
    return Tuple1(this[0] as T1)
}

fun <T1, T2> List<*>.destruct2(): Tuple2<T1, T2> {
    return Tuple2(this[0] as T1, this[1] as T2)
}

fun <T1, T2, T3> List<*>.destruct3(): Tuple3<T1, T2, T3> {
    return Tuple3(this[0] as T1, this[1] as T2, this[2] as T3)
}
