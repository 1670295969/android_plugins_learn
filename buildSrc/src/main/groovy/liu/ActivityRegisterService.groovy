package liu

import com.android.build.gradle.AppExtension
import groovy.xml.MarkupBuilder
import liu.ext.ActivityRegisterExtension
import org.gradle.api.Project


class ActivityRegisterService {

    def static final NAME = "android:name"
    def static final SCREENORIENTATION = "android:screenOrientation"
    def static final THEME = "android:theme"

//

    private String rootDirPath
    private ArrayList<String> classFile = new ArrayList<String>()

    private ActivityRegisterExtension activityRegister

    void register(Project project) {
        this.activityRegister = project.extensions.getByType(ActivityRegisterExtension)
        rootDirPath = project.rootDir.absolutePath

        def android = project.extensions.getByType(AppExtension)

        //查找 AndroidManifest.xml 文件
        def manifestFile = ""
        android.sourceSets.each {
            j ->
                if (j.manifest.srcFile.exists()) {
                    println(">>>>-->>>>" + j.manifest.srcFile.absolutePath)
                    manifestFile = j.manifest.srcFile.absolutePath
                }


                j.java.each {
                    it.srcDirs.each {
                        fs ->
                            handleFile(fs)
                    }

                }

        }


        handleManifestFile(new File(manifestFile))

        println(">>>>-->>>>" + rootDirPath)
        println(">>>>-->>>>" + classFile)
    }

    private void handleFile(File file) {
        def abspath = file.absolutePath
        handleJava(abspath, file)

    }

    private void handleJava(String abspath, File file) {
        if (file.isDirectory()) {

            file.listFiles()
                    .each {
                handleJava(abspath, it)
            }

        } else {
            String absPathFile = file.absolutePath
            if (absPathFile.endsWith("Activity.java") || absPathFile.endsWith("Activity.kt")) {
                absPathFile = absPathFile.replace(".java", "")
                absPathFile = absPathFile.replace(".kt", "")
                def activity = absPathFile.replace(abspath + "\\", "").replace("\\", ".")
                if (!classFile.contains(activity))
                    classFile.add(activity)
            }
        }
    }

    private void handleManifestFile(File file) {
        def xml = new XmlSlurper().parse(file)
        HashSet<String> activitySet = new HashSet<String>()
        System.out.println("handleManifestFile-->" + file.absolutePath)
        String packageName = (xml.@"package")

        System.out.println("packageName-->" + packageName)
        xml.application.activity.each {
            String activityName = it.@"android:name"
            System.out.println("activityName-->" + activityName)
            if (activityName.startsWith(".")) {

                activityName = packageName.toString() + activityName.toString()
                System.out.println("activityName-->" + activityName)
            }

            activitySet.add(activityName)
        }
        System.out.println("activitySet-->" + activitySet)


        def writer = new StringWriter()
        def newManifest = new MarkupBuilder(writer)

        newManifest.application {
            classFile.each {
                if (!activitySet.contains(it)) {
                    if (activityRegister.theme == null || activityRegister.theme.isEmpty()) {
                        activity("$NAME": it.toString(),
                                "$SCREENORIENTATION": activityRegister.screenOrientation.toString(),
                        )
                    } else {
                        activity("$NAME": it,
                                "$SCREENORIENTATION": activityRegister.screenOrientation.toString(),
                                "$THEME": activityRegister.theme.toString())
                    }

                }

            }
        }

        String activityManifest = writer.toString()
                .replace("<application>", "")
                .replace("</application>", "")
                .replace("<application />", "")

        System.out.println(">>>" + activityManifest)
        String newManifestContent = file.getText("UTF-8")
        int index = newManifestContent.lastIndexOf("</application>")
        newManifestContent = newManifestContent.substring(0, index) + activityManifest + newManifestContent.substring(index)
        file.write(newManifestContent, 'UTF-8')
        //file重新定向
        if (activityRegister != null && activityRegister.manifestFilePath != null) {
            new File("$rootDirPath/${activityRegister.manifestFilePath}/AndroidManifest.xml").write(newManifestContent, "UTF-8")
        }


    }

}
