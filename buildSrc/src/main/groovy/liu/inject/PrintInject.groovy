package liu.inject

import javassist.CtClass
import javassist.CtConstructor
import org.apache.commons.io.FileUtils;

import java.io.File;

import javassist.ClassPool;
import javassist.NotFoundException;

public class PrintInject {

    private static ClassPool pool = ClassPool.getDefault();
    private static final String injectStr = "System.out.println(\"I inject\");";


    private static eachFile(String path,String originPath,String packageName){
        File dir = new File(path)
        if (dir.isDirectory()){
            dir.listFiles().each {
                eachFile(it.absolutePath,originPath,packageName)
            }
        }else{
            String filePath = dir.absolutePath
            if (filePath.endsWith(".class") && !filePath.contains('R$')
                    && !filePath.contains('R.class') && !filePath.contains("BuildConfig.class")) {


                int index = filePath.indexOf(packageName)

                boolean isMyPackage = index != -1

                if (isMyPackage) {
                    //.class -> 6位
                    int end = filePath.length() - 6
                    String className = filePath.substring(index, end)
                            .replace('\\', '.').replace('/', '.')
                    CtClass c = pool.getCtClass(className)


                    if (c.isFrozen()) {
                        c.defrost()
                    }


                    if (c.isAnnotation() || c.isInterface()){
                        return
                    }

                    CtConstructor[] cts = c.getDeclaredConstructors()

                    if (cts == null || cts.length == 0) {

                        CtConstructor constructor = new CtConstructor(new CtClass[0], c)

                        constructor.setBody("{\n" + injectStr +
                                "\n}\n")
                        c.addConstructor(constructor)

                    } else {
                        cts[0].insertAfter(injectStr)
                    }
                    c.writeFile(filePath.substring(0,index))
                    c.detach()
                }


            }
        }
    }


    public static void injectDir(String path, String packageName) {

//        if (path.endsWith(".jar")) {
//            injectJar(path)
//            return
//        }
        println("path >>> : " + path)

        try {

            eachFile(path,path,packageName)


            println("path >>> : ok " )


        } catch (NotFoundException e) {
            e.printStackTrace();
        }


    }


}
