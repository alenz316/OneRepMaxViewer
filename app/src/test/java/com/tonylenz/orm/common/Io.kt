package com.tonylenz.orm.common

import okio.Buffer
import okio.Source

fun String.toSource(): Source = Buffer().apply {
    write(toByteArray())
}
