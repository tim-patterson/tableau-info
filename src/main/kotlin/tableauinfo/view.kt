package tableauinfo

import kotlinx.html.*
import kotlinx.html.dom.*
import org.w3c.dom.Element
import kotlin.browser.document

fun renderTwbInfo(twbInfo: TwbInfo): Element {
    return document.create.div("rounded-border callout grid-x grid-margin-x") {
        h3("cell") { + twbInfo.fileName }
        hr("cell")

        h5("cell") { + "General"}
        table("unstriped kv-table") {
            tbody {
                twbInfo.id?.let { tr { td { + "Id:" }; td { + it }} }
                twbInfo.site?.let { tr { td { + "Site:" }; td { + it }} }
                tr { td { + "File Size:" }; td { + "${twbInfo.fileSize}" }}
            }
        }

        hr("cell")

        h5("cell") { + "Datasources"}
        twbInfo.dataSources.forEach { datasource ->
            div("cell callout") {
                table("unstriped kv-table") {
                    tbody {
                        datasource.caption?.let { tr { td { + "Caption:" }; td { + it }} }
                        tr { td { + "Name:" }; td { + datasource.name }}
                    }
                }
                if (datasource.tables.isNotEmpty()) {
                    h6 { + "Tables" }
                    datasource.tables.forEach {
                        pre { + it }
                    }
                }

                if (datasource.customSqls.isNotEmpty()) {
                    h6 { + "CustomSql" }
                    datasource.customSqls.forEach {
                        pre { + it }
                    }
                }
            }
        }

        hr("cell")

        h5("cell") { + "Thumbnails"}
        twbInfo.thumbnails.forEach {
            div("cell medium-3 small-6") {
                div("card") {
                    img(src = imageToDataUrl(it))
                }
            }
        }

        hr("cell")

        h5("cell") { + "External Shapes"}
        twbInfo.externalShapes.forEach { (name, content) ->
            div("cell medium-3 small-6") {
                div("card") {
                    div("card-divider") { + name }
                    img(src = imageToDataUrl(content))
                }
            }
        }

    }
}


private fun imageToDataUrl(imageB64: String): String {
    return "data:image/png;base64,$imageB64"
}