package com.github.artem3605.snaptabs.actions

import com.github.artem3605.snaptabs.managers.ClosedFileManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project

class ReopenLastClosedFileAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project: Project = e.project ?: return
        val fileEditorManager = FileEditorManager.getInstance(project)
        val lastClosedFile = ClosedFileManager.getLastClosedFile()
        if (lastClosedFile != null) {
            fileEditorManager.openFile(lastClosedFile, true)
        }
    }
}