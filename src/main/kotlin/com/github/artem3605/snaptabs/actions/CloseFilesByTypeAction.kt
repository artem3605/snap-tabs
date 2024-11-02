package com.github.artem3605.snaptabs.actions

import com.github.artem3605.snaptabs.MyBundle
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import javax.swing.JOptionPane
import javax.swing.SwingUtilities

class CloseFilesByTypeAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project: Project = e.project ?: return
        val fileEditorManager = FileEditorManager.getInstance(project)
        SwingUtilities.invokeLater {
            val fileType = JOptionPane.showInputDialog(MyBundle.getMessage("fileTypeRequest"))
            if (fileType.isNullOrBlank()) return@invokeLater

            val openFiles = fileEditorManager.openFiles.toList()
            ApplicationManager.getApplication().invokeLater({
                openFiles.filter { it.extension == fileType }.forEach { fileEditorManager.closeFile(it) }
            }, ModalityState.defaultModalityState())
        }
    }
}