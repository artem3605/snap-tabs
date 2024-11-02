package com.github.artem3605.snaptabs.managers

import com.intellij.openapi.vfs.VirtualFile

object ClosedFileManager {
    private val closedFiles = mutableListOf<VirtualFile>()

    fun addClosedFile(file: VirtualFile) {
        closedFiles.add(file)
    }
    fun removeClosedFile(file: VirtualFile) {
        closedFiles.remove(file)
    }
    fun getLastClosedFile(): VirtualFile? {
        return if (closedFiles.isNotEmpty()) closedFiles.removeAt(closedFiles.size - 1) else null
    }
}