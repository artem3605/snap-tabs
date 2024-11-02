package com.github.artem3605.snaptabs.ui

import com.intellij.ui.components.JBList
import javax.swing.*
import java.awt.*
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

class SessionSearchDialog(sessions: List<String>, onSelect: (String) -> Unit) : JDialog() {
    init {
        title = "Select Session"
        layout = BorderLayout(10, 10)
        val searchField = JTextField()
        val listModel = DefaultListModel<String>()
        sessions.forEach { listModel.addElement(it) }
        val sessionList = JBList(listModel)
        sessionList.selectionMode = ListSelectionModel.SINGLE_SELECTION

        searchField.addKeyListener(object : KeyAdapter() {
            override fun keyReleased(e: KeyEvent) {
                val filter = searchField.text
                listModel.clear()
                sessions.filter { it.contains(filter, ignoreCase = true) }.forEach { listModel.addElement(it) }
            }
        })

        sessionList.addListSelectionListener {
            if (!it.valueIsAdjusting) {
                val selectedSession = sessionList.selectedValue
                if (selectedSession != null) {
                    onSelect(selectedSession)
                    dispose()
                }
            }
        }

        val panel = JPanel(BorderLayout(5, 5))
        panel.add(JLabel("Search:"), BorderLayout.WEST)
        panel.add(searchField, BorderLayout.CENTER)

        add(panel, BorderLayout.NORTH)
        add(JScrollPane(sessionList), BorderLayout.CENTER)
        setSize(400, 300)
        setLocationRelativeTo(null)
    }
}