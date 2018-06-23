package liu

import liu.ext.ActivityRegisterExtension
import org.gradle.api.Plugin
import org.gradle.api.Project


class AutoRegisterActivity implements Plugin<Project> {


    public static final String DISPATCHER_EXTENSION_NAME = "activityRegister"

    ActivityRegisterService registerService = new ActivityRegisterService()
    ActivityRegisterExtension activityRegister

    @Override
    void apply(Project project) {

        project.extensions.create(DISPATCHER_EXTENSION_NAME,ActivityRegisterExtension)

        this.activityRegister = project.extensions.getByType(ActivityRegisterExtension)



        project.afterEvaluate{
            System.out.println(">>>>>>" + activityRegister.theme)
            System.out.println(">>>>>>" + activityRegister.screenOrientation)
            System.out.println(">>>>>>" + activityRegister.manifestFilePath)
            registerService.register(project)
        }



    }
}