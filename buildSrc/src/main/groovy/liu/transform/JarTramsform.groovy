package liu.transform

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import liu.inject.PrintInject
import liu.inject.TmpInject
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Project


public class JarTramsform extends Transform {

    private Project project;

    public JarTramsform(Project project) {
        this.project = project
    }

    @Override
    String getName() {
        return "JarTramsform"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)

        transformInvocation.inputs.each {
            transformInput ->
                transformInput.directoryInputs.each {
                    directoryInput ->
                        TmpInject.inject(directoryInput.file.absolutePath,project)
                        PrintInject.injectDir(directoryInput.file.absolutePath,"liu")


                        def dest = transformInvocation.outputProvider.getContentLocation(
                                directoryInput.name,
                                directoryInput.contentTypes,
                                directoryInput.scopes,
                                Format.DIRECTORY
                        )


                        FileUtils.copyDirectory(directoryInput.file, dest)
                }


                transformInput.jarInputs.each {
                    jarInput ->

                        def jarName = jarInput.name
                        def md5Name = DigestUtils.md5Hex(jarInput.file.absolutePath)


                        if (jarName.endsWith(".jar")) {
                            jarName = jarName.substring(0, jarName.length() - 4)
                        }

                        def dest = transformInvocation.outputProvider.getContentLocation(jarName + md5Name,
                                jarInput.contentTypes,
                                jarInput.scopes,
                                Format.JAR)
                        FileUtils.copyFile(jarInput.file, dest)
//


                }
        }


    }
}
