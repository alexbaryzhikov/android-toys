package com.alexbaryzhikov.dirlist

data class FileDirItem(
        val path: String,
        var isDirectory: Boolean = false,
        var size: Long = 0L,
        var lastModified: String = "") : Comparable<FileDirItem> {

    companion object {
        var sorting = 0
    }

    val name = path.substringAfterLast('/', path)

    val extension: String
        get() = if (isDirectory) name else name.substringAfterLast('.', "")

    val mimeType: String
        get() = MIME_TYPES[extension.toLowerCase()] ?: ""

    override fun compareTo(other: FileDirItem) = when {
        isDirectory && !other.isDirectory -> -1
        !isDirectory && other.isDirectory -> 1
        else -> when {
            sorting and SORT_BY_DATE_MODIFIED != 0 -> lastModified.compareTo(other.lastModified)
            sorting and SORT_BY_SIZE != 0 -> size.compareTo(other.size)
            sorting and SORT_BY_EXTENSION != 0 -> extension.toLowerCase().compareTo(other.extension.toLowerCase())
            else -> name.toLowerCase().compareTo(other.name.toLowerCase())
        }.let {
            it * (if (sorting and SORT_DESCENDING == 0) 1 else -1)
        }
    }
}
