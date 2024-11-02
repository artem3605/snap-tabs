package com.github.artem3605.snaptabs.actions

import com.github.artem3605.snaptabs.managers.SessionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import javax.swing.JOptionPane
import javax.swing.SwingUtilities

class SaveSessionAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project: Project = e.project ?: return
        val fileEditorManager = FileEditorManager.getInstance(project)
        SwingUtilities.invokeLater {
            val openFiles = fileEditorManager.openFiles.toList()
            val sessionName = JOptionPane.showInputDialog("Enter the name for the session:")
            if (sessionName.isNullOrBlank()) return@invokeLater
            if (SessionManager.isSessionExists(project, sessionName)) {
                val result = JOptionPane.showConfirmDialog(null, "Session with the same name already exists. Do you want to overwrite it?", "Warning", JOptionPane.YES_NO_OPTION)
                if (result == JOptionPane.NO_OPTION) return@invokeLater
            }
            SessionManager.saveSession(project, sessionName, openFiles)
        }
    }
}