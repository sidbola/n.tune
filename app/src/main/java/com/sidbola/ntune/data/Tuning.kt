package com.sidbola.ntune.data

import java.util.Arrays

data class Tuning (
    val name: String,
    val notes: Array<Note>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tuning

        if (name != other.name) return false
        if (!Arrays.equals(notes, other.notes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + Arrays.hashCode(notes)
        return result
    }
}
