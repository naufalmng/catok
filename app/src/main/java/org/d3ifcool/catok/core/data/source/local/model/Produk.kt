package org.d3ifcool.catok.core.data.source.local.model

data class Produk(
    var produkIdList: MutableList<Int?>? = null,
    var produkNameList: MutableList<String?>? = null,
    var produkQtyList: MutableList<Int?>? = null,
    var hargaProdukList: MutableList<Double?>? = null
)
