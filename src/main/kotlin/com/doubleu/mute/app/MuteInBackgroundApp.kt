package com.doubleu.mute.app

import com.doubleu.mute.cmd.Cmdow
import com.doubleu.mute.cmd.Nircmd
import com.doubleu.mute.view.MainView
import javafx.application.Platform
import javafx.scene.image.Image
import javafx.stage.Stage
import tornadofx.*
import java.awt.SystemTray
import java.awt.TrayIcon
import java.lang.Thread.sleep
import java.nio.file.Files

class MuteInBackgroundApp : App(MainView::class) {
    val systemTray = SystemTray.getSystemTray()!!
    lateinit var mainView: MainView
    lateinit var stage: Stage
    lateinit var trayIcon: TrayIcon

    override fun start(stage: Stage) {
        super.start(stage)
        this.stage = stage
        mainView = find(MainView::class)
        trayIcon = initTrayIcon()
        addStageIcon(Image("icon/512.png"))
        stage.iconifiedProperty().onChange {
            if (it) minToTray()
        }
        if (Properties.startMinimized)
            stage.isIconified = true
        WindowWorker.start()
    }

    private fun initTrayIcon(): TrayIcon {
        val trayIcon = TrayIcon(javax.imageio.ImageIO.read(resources.stream("/icon/512.png")), mainView.title)
        with(trayIcon) {
            isImageAutoSize = true
            setOnMouseClicked(fxThread = true, clickCount = 2) {
                restoreFromTray()
            }
            menu(mainView.title) {
                item("Show...") {
                    setOnAction(fxThread = true) {
                        restoreFromTray()
                    }
                }
                item("Exit") {
                    setOnAction(fxThread = true) {
                        stop()
                    }
                }
            }
        }
        return trayIcon
    }

    private fun restoreFromTray() {
        Platform.setImplicitExit(true)
        systemTray.remove(trayIcon)
        stage.isIconified = false
        stage.show()
        stage.toFront()
    }

    private fun minToTray() {
        Platform.setImplicitExit(false)
        systemTray.add(trayIcon)
        stage.close()
    }

    override fun stop() {
        Properties.save()
        WindowWorker.run = false
        sleep(WindowWorker.sleepTime)
        Files.deleteIfExists(Nircmd.path)
        Files.deleteIfExists(Cmdow.path)
        Platform.exit()
        System.exit(0)
    }
}