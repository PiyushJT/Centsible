// This is Expense Table in Expense Database

package com.piyushjt.centsible

import androidx.room.Entity
import androidx.room.PrimaryKey


// These values are columns in the database
@Entity
data class Expense(
    val title : String,
    val description : String?,
    val type : String,
    val amount: Float,
    val date: Long,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)