package com.blackpineapple.ziptzopt.data.model

data class Contact(
        var name: String,
        var number: String,
        var message: String = "",
        var picture: String = ""
) {

    fun refactorNumber(): String { // this function only works in a Brazilian number system
        var refNum = number
        if (!refNum.startsWith("+")) { // Add country code (Brazilian)
            refNum = "+55$refNum"
        }
        if (refNum.contains(" ")) { // Checks for empty spaces between
            refNum = refNum.replace(" ", "")
        }
        if (refNum.contains("-")) { // Checks for a '-' between the numbers
            refNum = refNum.replace("-", "")
        }
        if (refNum[3] == '0') { // Checks if the first number after country code is a '0'
            refNum = refNum.substring(0, 3) + refNum.subSequence(4, refNum.length)
        }
        if (refNum.length < 14) { // Add the ninth '9' at the beginning of the number
            refNum = refNum.substring(0, 5) + "9" + refNum.substring(5, refNum.length)
        }
        return refNum
    }
}