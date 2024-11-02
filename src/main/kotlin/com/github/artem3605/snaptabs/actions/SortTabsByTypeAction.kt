package com.github.artem3605.snaptabs.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import io.ktor.util.*

class SortTabsByTypeAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project: Project = e.project ?: return
        val fileEditorManager = FileEditorManager.getInstance(project)
        val openFiles = fileEditorManager.openFiles.toList()
        for (file in openFiles) println(file.extension)
        val sortedFiles = openFiles.sortedBy { it.extension.toString().toLowerCasePreservingASCIIRules()}
        openFiles.forEach { fileEditorManager.closeFile(it) }
        sortedFiles.forEach { fileEditorManager.openFile(it, true)}
    }
}