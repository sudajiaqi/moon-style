package com.github.godmoonlight.moonstyle.utils

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project


object Notifier {
    fun notify(project: Project?, content: String?, type: NotificationType) {
        NotificationGroupManager.getInstance().getNotificationGroup("Moon-style-Notification-Group")
                .createNotification(content!!, type)
                .notify(project)
    }
}
