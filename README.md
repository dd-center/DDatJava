# DDatJava

DD@Home in Java

## Usage

### Plain Launch

use `java -jar DDatJava-[VERSION].jar`

### Developer Use

There are many use way in [DDCore](src/main/kotlin/moe/vtbs/DDCore.kt) class.  
If you think launch like the `Plain Launch`, see [Bootstrap.kt](src/main/kotlin/moe/vtbs/Bootstrap.kt) file.

#### For Gradle

1. clone this project to an empty folder with Terminal.  
   such as `git clone https://github.com/dd-center/DDatJava.git`
2. run `gradlew publishToMavenLocal`  
   if you're using PowerShell, use `.\gradlew publishToMavenLocal`
3. go to you project and open the `build.gradle`.
4. add `mavenLocal()` in your `repositories` scope.  
   add `implementation("moe.vtbs:dd-home-api:1.1.0.0")`
5. sync your project.

#### For Maven

1. `For Gradle`'s step 1~2
2. go to you project and open the `pom.xml`.
3. add this:

```xml

<dependencies>
    <!--Other...-->
    <dependency>
        <groupId>moe.vtbs</groupId>
        <artifactId>dd-home-api</artifactId>
        <version>1.1.0.0</version>
    </dependency>
    <!--Other...-->
</dependencies>
```

4. sync your project.

## Config File

You can find the config file at `./conf/config.yml` after first times launching.

```yaml
#应用程序设置
app:
    #操作间隔，单位为秒
    #默认值为 120
    interval: 120
    #昵称
    nickname: "<unset>"
```

`interval` is in second(s), not in millisecond(ms).
