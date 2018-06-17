package tableauinfo

import kotlin.browser.document

fun main(args: Array<String>) {
    println("initializing")
    val dropArea = document.getElementById("drop-area")!!

    println("initialization done")
}

// Require styles
external fun require(name: String): dynamic
val foundationStyles = require("../node_modules/foundation-sites/dist/css/foundation.min.css")
val appStyles = require("../resources/main/styles.css")
