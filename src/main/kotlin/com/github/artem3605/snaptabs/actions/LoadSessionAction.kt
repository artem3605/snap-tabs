package com.github.artem3605.snaptabs.actions

import com.github.artem3605.snaptabs.managers.SessionManager
import com.github.artem3605.snaptabs.ui.SessionSearchDialog
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import javax.swing.SwingUtilities

class LoadSessionAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project: Project = e.project ?: return
        val fileEditorManager = FileEditorManager.getInstance(project)
        SwingUtilities.invokeLater {
            SessionManager.getAllSessionNames(project) { sessions ->
                val dialog = SessionSearchDialog(sessions) { sessionName ->
                    val openFiles = fileEditorManager.openFiles.toList()
                    SessionManager.loadSession(project, sessionName) { filesToOpen ->
                        if (filesToOpen != null) {
                            ApplicationManager.getApplication().invokeLater({
                                filesToOpen.forEach { fileEditorManager.openFile(it, true) }
                                openFiles.forEach { if (!filesToOpen.contains(it)) fileEditorManager.closeFile(it) }
                            }, ModalityState.defaultModalityState())
                        }
                    }
                }
                dialog.isVisible = true
            }
        }
    }
}