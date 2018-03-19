package com.doubleu.mute.view

import com.doubleu.mute.app.Properties
import com.doubleu.mute.model.MutedWindow
import com.doubleu.mute.model.Window
import com.doubleu.mute.model.WindowStorage
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.Pos
import javafx.stage.FileChooser
import tornadofx.*
import java.awt.Desktop
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class MainView : View("Mute In Background") {

    override val root = tabpane {
        tab("Apps") {
            isClosable = false
            hbox {
                paddingAll = 10
                spacing = 20.0
                vbox {
                    label("All apps")
                    tableview(WindowStorage.windows) {
                        column("App", Window::imageProperty)
                        column("Title", Window::captionProperty)
                        columnResizePolicy = SmartResize.POLICY
                        onUserSelect(clickCount = 2) {
                            val w = MutedWindow(it.image, it.caption)
                            with(WindowStorage.muted) {
                                if (!contains(w)) {
                                    add(w)
                                }
                            }
                        }
                        contextmenu {
                            item("Mute") {
                                setOnAction {
                                    selectedItem?.let {
                                        val w = MutedWindow(it.image, it.caption)
                                        with(WindowStorage.muted) {
                                            if (!contains(w)) {
                                                add(w)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        itemsProperty().onChange {
                            smartResize()
                            FX.primaryStage.sizeToScene()
                        }
                    }
                }
                vbox {
                    label("Muted when in Background")
                    tableview(WindowStorage.muted) {
                        column("App", MutedWindow::image)
                        column("Title", MutedWindow::caption)
                        columnResizePolicy = SmartResize.POLICY
                        onUserSelect(clickCount = 2) {
                            WindowStorage.muted.remove(it)
                        }
                        contextmenu {
                            item("Unmute") {
                                setOnAction {
                                    selectedItem?.let {
                                        WindowStorage.muted.remove(it)
                                    }
                                }
                            }
                        }
                        itemsProperty().onChange {
                            smartResize()
                            FX.primaryStage.sizeToScene()
                        }
                    }
                }
            }
        }
        tab("Options") {
            isClosable = false
            vbox {
                alignment = Pos.CENTER
                spacing = 10.0
                hbox {
                    alignment = Pos.CENTER
                    spacing = 5.0
                    label("Path to executable")
                    textfield(Properties.pathToExecutableProperty)
                    button("...") {
                        setOnAction {
                            val files = chooseFile("Path to executable",
                                    mode = FileChooserMode.Single,
                                    filters = arrayOf(FileChooser.ExtensionFilter("Executable", "*.exe")),
                                    owner = FX.primaryStage) {
                                initialDirectory = File(".")
                            }
                            if (files.isNotEmpty())
                                Properties.pathToExecutable = files[0].absolutePath
                        }
                    }
                }
                val error = SimpleStringProperty()
                val msg = "Requires admin rights.\n" +
                        "You can always create the link yourself.\n" +
                        "Create a link to the MuteInBackground.exe in the autostart folder.\n" +
                        "The button to the left will open that folder."
                hbox {
                    alignment = Pos.CENTER
                    spacing = 5.0
                    val autostart = "${System.getProperty("user.home")}\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup\\"
                    button("Create Run On Startup link") {
                        enableWhen { Properties.pathToExecutableProperty.isNotEmpty }
                        tooltip(msg)
                        setOnAction {
                            val execPath = Paths.get(Properties.pathToExecutable)
                            if (!Files.exists(Paths.get("$autostart${execPath.fileName}")))
                                try {
                                    Files.createSymbolicLink(Paths.get("$autostart${execPath.fileName}"), execPath)
                                } catch (e: Exception) {
                                    error.value = msg
                                }
                        }
                    }
                    button("...") {
                        setOnAction { Desktop.getDesktop().open(File(autostart)) }
                    }
                }
                label(error) {
                    visibleWhen { error.isNotEmpty }
                }
                checkbox("Start Minimized", property = Properties.startMinimizedProperty) {
                    alignment = Pos.CENTER
                }
            }
        }
    }
}