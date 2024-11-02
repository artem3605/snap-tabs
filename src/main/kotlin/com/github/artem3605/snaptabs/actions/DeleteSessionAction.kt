package com.github.artem3605.snaptabs.actions

import com.github.artem3605.snaptabs.MyBundle
import com.github.artem3605.snaptabs.ui.SessionSearchDialog
import com.github.artem3605.snaptabs.managers.SessionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import javax.swing.JOptionPane
import javax.swing.SwingUtilities

class DeleteSessionAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project: Project = e.project ?: return
        SwingUtilities.invokeLater {
            SessionManager.getAllSessionNames(project) { sessions ->
                val dialog = SessionSearchDialog(sessions) { sessionName ->
                    val result = JOptionPane.showConfirmDialog(null, MyBundle.getMessage("confirmationOfDeletingMessage") + " '$sessionName'?", MyBundle.getMessage("confirmationOfDeleting"), JOptionPane.YES_NO_OPTION)
                    if (result == JOptionPane.YES_OPTION) {
                        SessionManager.deleteSession(project, sessionName)
                    }
                }
                dialog.isVisible = true
            }
        }
    }
}