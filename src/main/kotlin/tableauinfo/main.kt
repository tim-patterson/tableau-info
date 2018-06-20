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
                        resolve(processFile(file.name, file.size, ""))
                    }
                    reader.readAsText(file)
                }
            }

            // We actually want to generate a chain of promises here
            // that do the resolving of the file processing promises.
            // This ensures that items get added in the same order that they're
            // "dropped"
            var chain: Promise<Any> = Promise.resolve(Unit)
            promises.forEachIndexed { index, p ->
                chain = chain.then { p.then {
                    contentArea.appendChild(it)
                    if(index == 0) {
                        it.scrollIntoView(ScrollIntoViewOptions(block = ScrollLogicalPosition.START, behavior = ScrollBehavior.SMOOTH))
                    }
                } }
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