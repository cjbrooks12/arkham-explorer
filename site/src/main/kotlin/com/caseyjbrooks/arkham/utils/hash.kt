package com.caseyjbrooks.arkham.utils

import org.apache.commons.codec.digest.DigestUtils
import java.nio.file.Path
import kotlin.io.path.inputStream

val String.md5: String get() = DigestUtils.md5Hex(this.byteInputStream())

val Path.md5: String get() = DigestUtils.md5Hex(this.inputStream())
