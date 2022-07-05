# DDatJava

DD@Home in Java

## Usage

### Plain Launch

use `java -jar DDatJava-<verison>.jar`

### Developer Use

There are many use way in [DDCore](src/main/kotlin/moe/vtbs/DDCore.kt) class.  
If you think launch like the `Plain Launch`, see [Bootstrap.kt](src/main/kotlin/moe/vtbs/Bootstrap.kt) file.

#### 1. For Gradle

1. Clone this project to an empty folder with Terminal.  
   Such as `git clone https://github.com/dd-center/DDatJava.git`
2. Run `gradlew publishToMavenLocal`  
   If you're using PowerShell, use `.\gradlew publishToMavenLocal`
3. Go to your project and open the `build.gradle`.
4. Add `mavenLocal()` in your `repositories` scope.  
   Add `implementation("moe.vtbs:dd-home-api:<version>")`
5. Sync your project.

#### 2. For Maven

1. `For Gradle`'s step 1~2
2. Go to your project and open the `pom.xml`.
3. Add this:

```xml

<dependencies>
    <!--Other...-->
    <dependency>
        <groupId>moe.vtbs</groupId>
        <artifactId>dd-home-api</artifactId>
        <version>VERSION</version>
    </dependency>
    <!--Other...-->
</dependencies>
```

4. Sync your project.

#### 3. Use the jar file

1. Run `gradlew jar`
2. Copy the `.\build\libs\DDatJava-<version>.jar` to your project.

**If you want to use the `DDatJava-<version>-api.jar`and the following steps are also required:**

3. Add they to classpath:
    - com.google.code.gson:gson:2.9.0
    - org.slf4j:slf4j-log4j12:1.7.36
    - commons-io:commons-io:2.11.0
    - org.yaml:snakeyaml:1.30
    - com.squareup.okhttp3:okhttp:4.10.0
    - org.jetbrains.kotlin:kotlin-stdlib:1.7.0
    - org.jetbrains.kotlin:kotlin-reflect:1.7.0
    - org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.2

## Config File

You can find the config file at `./conf/config.yml` after first times launching.

```yaml
#Vtbs网站任务分发设置
distribution:
    #操作间隔，单位为秒
    #默认值为 120
    interval: 120
    #昵称
    nickname: <unset>
#Language. Such as: zh-cn, en-us
#If you want to use another language, please place the "<languageName>.ini" file under Classpath://lang/
language: zh-cn
```

`distribution.interval` is in second(s), not in millisecond(ms).  
`distribution.nickname` is a custom nickname, it will ask you to fill in on first boot.