# DDatJava
DD@Home in Java

## Usage

`java -jar DDatJava-[VERSION].jar`

## Config File

You can find the config file at *./conf/config.yml* after first times launching.


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