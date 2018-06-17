package tableauinfo

import org.w3c.dom.DragEvent
import org.w3c.dom.Element
import org.w3c.dom.ScrollIntoViewOptions
import org.w3c.dom.asList
import org.w3c.dom.events.Event
import org.w3c.files.FileReader
import kotlin.browser.document
import kotlin.browser.window

// Require styles
external fun require(name: String): dynamic
val foundationStyles = require("../node_modules/foundation-sites/dist/css/foundation.min.css")
val appStyles = require("../resources/main/styles.css")


fun main(args: Array<String>) {
    document.addEventListener("DOMContentLoaded", {
        println("initializing")

        val dropArea = document.getElementById("drop-area")!!

        initializeDropEvents(dropArea)

        println("initialization done")
    })
}


private fun initializeDropEvents(dropArea: Element) {
    // Stop the browser just opening the file
    listOf("dragenter", "dragleave", "dragover", "drop").forEach {
        dropArea.addEventListener(it, { event ->
            event.preventDefault()
            event.stopPropagation()
        })
    }
    dropArea.addEventListener("drop", ::handleDrop)
}

private fun handleDrop(event: Event) {
    if (event is DragEvent) {
        event.dataTransfer?.files?.let {
            it.asList().forEachIndexed { idx, file ->
                val reader = FileReader()
                reader.onloadend = {
                    processFile(file.name, file.size, reader.result as String, idx ==0)
                }
                reader.readAsText(file)
            }
        }
    }
}

private fun processFile(fileName: String, size: Int, contents: String, scrollTo: Boolean) {
    val contentArea = document.getElementById("content-area")!!

    println("Processing file $fileName")
    val parser = js("new DOMParser()")
    val xmlDoc = parser.parseFromString(contents, "text/xml") as org.w3c.dom.Document

    val twbInfo = parseTwb(fileName, size, xmlDoc)
    val view = renderTwbInfo(twbInfo)
    contentArea.appendChild(view)
    
    if (scrollTo) { view.scrollIntoView(true) }
}