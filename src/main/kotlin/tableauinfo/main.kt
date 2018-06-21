package tableauinfo

import org.w3c.dom.*
import org.w3c.dom.events.Event
import org.w3c.files.FileReader
import kotlin.browser.document
import kotlin.js.Promise

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
            val contentArea = document.getElementById("content-area")!!
            val promises = it.asList().map { file ->
                Promise<Element> { resolve, reject ->
                    val reader = FileReader()
                    reader.onload = {
                            resolve(processFile(file.name, file.size, reader.result as String))
                    }
                    reader.onerror = {
                        reject(Exception("Error loading file"))
                    }
                    reader.readAsText(file)
                }
            }

            Promise.sequentiallyIndexed(promises) { index, element ->
                contentArea.appendChild(element)
                if(index == 0) {
                    element.scrollIntoView(ScrollIntoViewOptions(block = ScrollLogicalPosition.START, behavior = ScrollBehavior.SMOOTH))
                }
            }
        }
    }
}

private fun processFile(fileName: String, size: Int, contents: String): Element {
    println("Processing file $fileName")
    val parser = js("new DOMParser()")
    val xmlDoc = parser.parseFromString(contents, "text/xml") as org.w3c.dom.Document

    val twbInfo = parseTwb(fileName, size, xmlDoc)
    return renderTwbInfo(twbInfo)
}