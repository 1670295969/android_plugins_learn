# autoregisteractivity2manifest
# 练习gradle插件编写 
# 主要功能是
1. 自动配置Activity到AndroidManifest.xml 中, 这样就不会因忘记配置而造成崩溃的问题
 如果activity 已经注册到 AndroidManifest.xml 中就不会再次注册
2. aspectj plugin 加入 仅支持 *.java
3. 学习自定义transform 并使用javassist 对class文件进行代码插入