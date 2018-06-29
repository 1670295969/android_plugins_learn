package liu.inject

import javassist.ClassPool
import javassist.CtClass
import javassist.CtMethod
import org.gradle.api.Project;

public class TmpInject {



    private final static ClassPool pool = ClassPool.getDefault()


    public static void inject(String path ,Project project){

        pool.appendClassPath(path)

        project.android.bootClasspath.each{
            System.out.println(it)
        }


        pool.appendClassPath(project.android.bootClasspath[0].toString())
        pool.importPackage("android.os.Bundle")
        File dir = new File(path)

        if (dir.isDirectory()){
            dir.eachFileRecurse {
                file->
                    String filePath = file.absolutePath
                    if (file.getName().equals("AptActivity.class")){
                        CtClass ctClass = pool.getCtClass("liu.com.autoregisteravtivity.AptActivity")

                        if (ctClass.isFrozen()){
                            ctClass.defrost()
                        }

//                        CtMethod ctBundle = pool.getCtClass("android.os.Bundle")
                        CtMethod ctMethod = ctClass.getDeclaredMethod("onCreate")

                        String injectStr = "android.widget.Toast.makeText(this,\"inject code\",android.widget.Toast.LENGTH_SHORT).show();"

                        ctMethod.insertAfter(injectStr)

                        ctClass.writeFile(path)
                        ctClass.detach()






                    }
            }
        }



    }




}
