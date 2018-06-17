package tableauinfo

import org.w3c.dom.Document

data class TwbInfo(
        val fileName: String,
        val fileSize: Int,
        val id: String?,
        val site: String?,
        val dataSources: List<DataSource>,
        val thumbnails: List<String>,
        val externalShapes: List<Pair<String, String>>
)

data class DataSource(
        val caption: String?,
        val name: String,
        val customSqls: List<String>,
        val tables: List<String>
)

fun parseTwb(fileName: String, fileSize: Int, xmlDoc: Document): TwbInfo {

    val repositoryLocation = xmlDoc.xpath("/workbook/repository-location").firstOrNull()
    val thumbnails = xmlDoc.xpath("/workbook/thumbnails/thumbnail").mapNotNull { it.textContent }
    val externalShapes = xmlDoc.xpath("/workbook/external/shapes/shape")
            .map { it.getAttribute("name")!! to it.textContent!! }

    val dataSources = xmlDoc.xpath("/workbook/datasources/datasource").map { datasource ->

        val customSqls = datasource.xpath(".//relation[@name=\"custom_sql_query\"]").mapNotNull { it.textContent }
        val tables = datasource.xpath(".//relation[@type=\"table\"]").mapNotNull { it.getAttribute("table") }

        DataSource(
                caption = datasource.getAttribute("caption"),
                name = datasource.getAttribute("name")!!,
                customSqls = customSqls,
                tables = tables
        )
    }

    return TwbInfo(
            fileName = fileName,
            fileSize = fileSize,
            id = repositoryLocation?.getAttribute("id"),
            site = repositoryLocation?.getAttribute("site"),
            dataSources = dataSources,
            thumbnails = thumbnails,
            externalShapes = externalShapes
    )
}