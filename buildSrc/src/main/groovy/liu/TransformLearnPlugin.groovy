package liu

import com.android.build.gradle.AppExtension
import liu.inject.PrintInject
import liu.transform.JarTramsform
import org.gradle.api.Plugin
import org.gradle.api.Project;

public class TransformLearnPlugin implements Plugin<Project>{
    @Override
    void apply(Project project) {


        def android =  project.extensions.findByType(AppExtension)
        android.registerTransform(new JarTramsform(project))




    }
}
