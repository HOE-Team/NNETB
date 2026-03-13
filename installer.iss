[Setup]
AppId={{3B3B3B3B-3B3B-3B3B-3B3B-3B3B3B3B3B3B}
AppName=NNETB's Not Everything Toolbox
AppVersion=1.1.0
AppPublisher=HOE Team
AppPublisherURL=https://github.com/HOE-Team/NNETB
AppSupportURL=https://github.com/HOE-Team/NNETB
AppUpdatesURL=https://github.com/HOE-Team/NNETB
DefaultDirName={autopf}\NNETBsNotEverythingToolbox
DefaultGroupName=NNETB's Not Everything Toolbox
AllowNoIcons=yes
OutputDir=build\compose\binaries\main\installer
OutputBaseFilename=NNETBsNotEverythingToolbox-1.1.0-Setup
SetupIconFile=images\logo.ico
UninstallDisplayIcon={app}\NNETBsNotEverythingToolbox-1.1.0.exe
WizardStyle=modern
PrivilegesRequired=admin
ArchitecturesInstallIn64BitMode=x64
Compression=lzma
SolidCompression=yes
AppCopyright=Copyright(C) 2026 HOE Team. All rights reserved.

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked
Name: "quicklaunchicon"; Description: "{cm:CreateQuickLaunchIcon}"; GroupDescription: "{cm:AdditionalIcons}"; Flags: unchecked

[Files]
; Copy the entire app directory from build output
Source: "build\compose\binaries\main\exe\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs
; (Optional) Copy res directory if present
;Source: "res\*"; DestDir: "{app}\res"; Flags: ignoreversion recursesubdirs createallsubdirs
; Include installer logo so shortcuts can reference it
Source: "images\logo.ico"; DestDir: "{app}"; Flags: ignoreversion

[Icons]
Name: "{group}\NNETB's Not Everything Toolbox"; Filename: "{app}\NNETBsNotEverythingToolbox-1.1.0.exe"; WorkingDir: "{app}"; IconFilename: "{app}\logo.ico"
Name: "{group}\{cm:UninstallProgram,NNETB's Not Everything Toolbox}"; Filename: "{uninstallexe}"
Name: "{autodesktop}\NNETB's Not Everything Toolbox"; Filename: "{app}\NNETBsNotEverythingToolbox-1.1.0.exe"; Tasks: desktopicon; WorkingDir: "{app}"; IconFilename: "{app}\logo.ico"
Name: "{userappdata}\Microsoft\Internet Explorer\Quick Launch\NNETB's Not Everything Toolbox"; Filename: "{app}\NNETBsNotEverythingToolbox-1.1.0.exe"; Tasks: quicklaunchicon; WorkingDir: "{app}"; IconFilename: "{app}\logo.ico"

[Run]
Filename: "{app}\NNETBsNotEverythingToolbox-1.1.0.exe"; Description: "{cm:LaunchProgram,NNETB's Not Everything Toolbox}"; Flags: nowait postinstall skipifsilent; WorkingDir: "{app}"

[Code]
// Additional code can be added here for custom actions if needed
