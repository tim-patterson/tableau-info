package tableauinfo

import kotlin.js.Promise

fun <T> Promise.Companion.sequentiallyIndexed(promises: Iterable<Promise<T>>, action: (Int, T) -> Unit): Promise<Unit> {
    var chain: Promise<Unit> = Promise.resolve(Unit)
    promises.forEachIndexed { i, p ->
        chain = chain.then { p.then { action(i, it) } }.then { Unit }
    }
    return chain
}