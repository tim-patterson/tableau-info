package tableauinfo

import org.w3c.dom.Element
import org.w3c.dom.ParentNode

fun ParentNode.xpath(xpath: String): List<Element> {
    val doc = if(this is Element) this.ownerDocument!! else this
    val iter = doc.asDynamic().evaluate(xpath, this, null, js("XPathResult.ORDERED_NODE_ITERATOR_TYPE"))
    val ret = mutableListOf<Element>()

    var node = iter.iterateNext() as Element?
    while(node != null) {
        ret.add(node)
        node = iter.iterateNext() as Element?
    }
    return ret
}