package org.d3ifcool.catok.core.data.source.local.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class Produk(
    var produkIdList: MutableList<Int?>? = null,
    var produkNameList: MutableList<String?>? = null,
    var produkQtyList: MutableList<Int?>? = null,
    var produkSatuanPerList: MutableList<String?>? = null,
    var hargaProdukList: MutableList<Double?>? = null
): Parcelable
