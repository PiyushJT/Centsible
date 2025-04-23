// This is Expense Table in Expense Database

package com.piyushjt.centsible

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.annotation.Keep

// These values are columns in the database
@Keep
@Entity
data class Expense(
    var title: String,
    var description: String?,
    var type: String,
    var amount: Float,
    var date: Long,
    @PrimaryKey(autoGenerate = true) var id: Int = 0
)