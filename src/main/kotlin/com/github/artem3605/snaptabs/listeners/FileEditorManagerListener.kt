package com.github.artem3605.snaptabs.listeners

import com.github.artem3605.snaptabs.managers.ClosedFileManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.vfs.VirtualFile

class FileEditorManagerListener: FileEditorManagerListener {
    override fun fileClosed(source: FileEditorManager, file: VirtualFile) {
        ClosedFileManager.addClosedFile(file)
    }

    override fun fileOpened(source: FileEditorManager, file: VirtualFile) {
        ClosedFileManager.removeClosedFile(file)
    }
}