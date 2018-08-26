# file_system
## 使用方法
### 1.搭建maven环境
### 2.编译
git clone 下此项目 

cd file_system-master

mvn clean package

cd target 后将fileSystem-1.0-SNAPSHOT.jar中META-INF文件夹中MANIFEST.MF文件加上如下一行并进行保存，然后就可以通过启动fileSystem-1.0-SNAPSHOT.jar测试文件系统了

  Main-Class: view.UserShell
