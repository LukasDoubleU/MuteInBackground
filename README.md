# MuteInBackground
JavaFX Application that mutes Applications when they are in the background.

# How to
1. Download from Github [link](https://github.com/LukasDoubleU/MuteInBackground/releases/download/v1.1/MuteInBackground.zip)
2. Unzip
3. Run MuteInBackground.exe

# Why?
Some apps have an option to mute themselves when they are in the background.  
Others don't.  
For the latter there is this tool to save you a couple clicks.  

# What it does
This tool will list your running apps.  
You chose which of them you want to mute.  
The apps you selected will be muted when they are in the background  
and unmuted when they regain focus.  
The app has an option to run on startup, and to be started minimized (in the tray),  
so "MuteInBackground" will itself be running in the background.  

# Technical Details
Only works with Windows, tested only with Windows 10.  
Preferences will be saved to your user home.  
Temp files are being created (and deleted) to interact with CMD-utility.  

# Credits
• [Cmdow](https://ritchielawrence.github.io/cmdow/) - for finding open apps  
• [Nircmd](http://nircmd.nirsoft.net/) - for muting / unmuting apps  
• [TornadoFX](https://tornadofx.io/) - JavaFX Framework for Kotlin  
