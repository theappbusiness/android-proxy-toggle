package common

// Automatic version code generated from VERSION_NAME
@Suppress("MagicNumber")
fun versionCode(versionName: String?): Int {
    return versionName?.split('.')?.map { it.toInt() }?.let {
        val versionMajor = it.getOrNull(0) ?: 0
        val versionMinor = it.getOrNull(1) ?: 0
        val versionPatch = it.getOrNull(2) ?: 0
        versionMajor * 10000 + versionMinor * 100 + versionPatch
    } ?: error("VersionName is null!")
}
