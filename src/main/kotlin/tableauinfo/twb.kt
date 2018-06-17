package tableauinfo

import org.w3c.dom.Document

data class TwbInfo(
        val fileName: String,
        val fileSize: Int,
        val thumbnails: List<String>,
        val externalShapes: List<Pair<String, String>>
)

fun parseTwb(fileName: String, fileSize: Int, xmlDoc: Document): TwbInfo {

    val thumbnails = xmlDoc.xpath("//thumbnails/thumbnail").mapNotNull { it.textContent }

    return TwbInfo(
            fileName,
            fileSize,
            thumbnails,
            listOf()
    )
}