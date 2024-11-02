package com.github.artem3605.snaptabs.managers

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import java.io.File
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

object SessionManager {
    private val projectSessions = mutableMapOf<Project, MutableMap<String, List<VirtualFile>>>()
    fun isSessionExists(project: Project, name: String): Boolean {
        return projectSessions.getOrPut(project) { loadSessionsFromFile(project) }.containsKey(name)
    }
    private fun getSessionFile(project: Project): File {
        return File(project.basePath, ".idea/sessions.dat")
    }
    fun saveSession(project: Project, name: String, files: List<VirtualFile>) {
        val sessions = projectSessions.getOrPut(project) { mutableMapOf() }
        sessions[name] = files
        ApplicationManager.getApplication().executeOnPooledThread {
            saveSessionsToFile(project, sessions)
        }
    }

    fun loadSession(project: Project, name: String, callback: (List<VirtualFile>?) -> Unit) {
        ApplicationManager.getApplication().executeOnPooledThread {
            val sessions = loadSessionsFromFile(project)
            ApplicationManager.getApplication().invokeLater {
                callback(sessions[name])
            }
        }
    }
    fun deleteSession(project: Project, name: String) {
        val sessions = projectSessions.getOrPut(project) { loadSessionsFromFile(project) }
        if (sessions.remove(name) != null) {
            ApplicationManager.getApplication().executeOnPooledThread {
                saveSessionsToFile(project, sessions)
            }
        }
    }

    fun getAllSessionNames(project: Project, callback: (List<String>) -> Unit) {
        ApplicationManager.getApplication().executeOnPooledThread {
            val sessions = loadSessionsFromFile(project)
            ApplicationManager.getApplication().invokeLater {
                callback(sessions.keys.toList())
            }
        }
    }

    private fun saveSessionsToFile(project: Project, sessions: Map<String, List<VirtualFile>>) {
        val sessionFile = getSessionFile(project)
        ObjectOutputStream(sessionFile.outputStream()).use { it.writeObject(sessions.mapValues { entry -> entry.value.map { it.url } }) }
    }

    private fun loadSessionsFromFile(project: Project): MutableMap<String, List<VirtualFile>> {
        val sessionFile = getSessionFile(project)
        val sessions = mutableMapOf<String, List<VirtualFile>>()
        if (sessionFile.exists()) {
            ObjectInputStream(sessionFile.inputStream()).use {
                val loadedSessions = it.readObject() as Map<String, List<String>>
                loadedSessions.forEach { (name, urls) ->
                    sessions[name] = urls.mapNotNull { url -> VirtualFileManager.getInstance().findFileByUrl(url) }
                }
            }
        }
        projectSessions[project] = sessions
        return sessions
    }
}