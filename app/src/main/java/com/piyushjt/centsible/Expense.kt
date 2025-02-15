// This is Expense Table in Expense Database

package com.piyushjt.centsible

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable


// These values are columns in the database
@Entity
data class Expense(
    var title: String,
    var description: String?,
    var type: String,
    var amount: Float,
    var date: Long,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)