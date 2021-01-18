[gradle-tasks]: ./doc/img/gradle-tasks.png "gradle tasks"
[modules]: ./doc/img/modules.png "modules"
[output]: ./doc/img/output.png "output"
[aar-content]: ./doc/img/aar-contents.png "aar content"
[run-app]: ./doc/img/run-app.png "run app"

# Image Drawer MDK Extension Native Android Library
Native Android Library zum bemalen von Bildern.

## Requirements
- Android Studio 4.0.1

## Project
Das Projekt besteht aus 2 Modulen **app** und **ImageEditor**. 

![alt text][modules]

**app** ist eine gesamte Android Anwendung und wird benutzt um das Projekt auf einem Gerät zu deployen. **app** benutzt und ruft das **ImageEditor** Modul auf um einen Zugriff von außerhalb auf die library zu simulieren ohne dafür einen MDK Client bauen oder nutzen zu müssen. **app** ist sozusagen wie der TypeScript part der MDK Extension später. 

**ImageEditor** ist das Modul für die native library welche zuständig ist für das laden, anzeigen und bemalen von Bildern. Der gebaute Output wird in der MDK Extension verwendet.

**Gradle Scripts** enthält die gradle scripts um die Module zu bauen, es gibt ein gradle script  für das komplette Projekt, das app Module und für die ImageDrawer Library. Gradle scripts definieren unterstütze Versionen und weitere Abhängigkeiten die von den Modulen genutzt werden.  
Das Script für das ImageEditor Modul muss später auch in der MDK Extension beigelegt werden, damit MDK einen Client bauen kann.

**app** kann über die Toolbar auf ein Gerät oder Simulator deployed werden mit dem "Play" ▶️ Button (stelle sicher das das app module ausgewählt ist neben dem Hammer Symbol)

![alt text][run-app]

## Build
Der Build kann ausgelöst werden mit Gradle Tasks. Diese sind auf der rechten Seite der IDE unter ```Gradle/ImageDrawer/Tasks/``` zu finden. Ein Doppel klick auf ```Gradle/ImageDrawer/Tasks/build/build``` wird alle Module als _.aar_ library bauen. Bevor ein build ausgeführt wird ist es ratsam auch einen ```cleanBuildCache``` auszuführen

![alt text][gradle-tasks]

Der Output kann im Projekt gefunden werden unter ```/ImageDrawer/ImageEditor/build/outputs/aar/ImageEditor-release.aar``` bzw ```/ImageEditor-debug.aar```

![alt text][output]

Der Inhalt einer .aar kann auch über Android Studio angezeigt werden. Dafür muss die gebaute .aar library in Android Studios Editor bereich gedragt und dropt werden.

![alt text][aar-content]