package tableauinfo

import kotlinx.html.*
import kotlinx.html.dom.*
import org.w3c.dom.Element
import kotlin.browser.document

fun renderTwbInfo(twbInfo: TwbInfo): Element {
    return document.create.div("panel") {
        p {
            +"Filename: ${twbInfo.fileName}"
            +"Filesize: ${twbInfo.fileSize}"
            twbInfo.thumbnails.forEach {
                img(src = imageToDataUrl(it))
            }
        }
    }
}


private fun imageToDataUrl(imageB64: String): String {
    return "data:image/png;base64,$imageB64"
}