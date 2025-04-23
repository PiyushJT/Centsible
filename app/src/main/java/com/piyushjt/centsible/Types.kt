package com.piyushjt.centsible

import androidx.annotation.Keep

@Keep
enum class Types (val type: String) {
    MISC("misc"),
    FOOD("food"),
    GROCERIES("grocery"),
    TRAVEL("travel"),
    ENT("ent"),
    SHOPPING("shopping"),
    SKILL("skill"),
    BILL("bills"),
    EMI("emi"),
    MEDICINE("medicine"),
    INVESTMENT("investment")

}