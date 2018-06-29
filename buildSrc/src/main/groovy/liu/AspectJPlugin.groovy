package liu

import com.android.builder.model.AndroidProject
import org.gradle.api.Plugin
import org.gradle.api.Project

import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main


class AspectJPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        project.dependencies {
            api 'org.aspectj:aspectjrt:1.8.13'
//            api 'org.aspectj:aspectjtools:1.8.13'
        }
        handleAspectJ(project)


    }

    void handleAspectJ(Project project) {
        def log = project.logger
        project.android.applicationVariants.all {
            variant ->
                log.debug("this in")
                variant.javaCompile.doLast {
                    String[] args = ["-showWeaveInfo",
                                     "-1.8",
                                     "-inpath", javaCompile.destinationDir.toString(),
                                     "-aspectpath",javaCompile.destinationDir.toString(),
                                     "-d", javaCompile.destinationDir.toString(),
                                     "-classpath", javaCompile.classpath.asPath,
                                     "-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)]

                    MessageHandler handler = new MessageHandler(true)
                    new Main().run(args, handler)
                    for (IMessage message : handler.getMessages(null, true)) {
                        switch (message.getKind()) {
                            case IMessage.ABORT:
                            case IMessage.ERROR:
                            case IMessage.FAIL:
                                log.error message.message, message.thrown
                                break
                            case IMessage.WARNING:
                                log.warn message.message, message.thrown
                                break
                            case IMessage.INFO:
                                log.info message.message, message.thrown
                                break
                            case IMessage.DEBUG:
                                log.debug message.message, message.thrown
                                break
                        }
                    }
                }




        }
    }

}
