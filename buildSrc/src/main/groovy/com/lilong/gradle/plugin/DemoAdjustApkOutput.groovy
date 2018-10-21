package com.lilong.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class DemoAdjustApkOutput implements Plugin<Project>{

    @Override
    void apply(Project project) {

        /** 输出每个applicationVariant的outputFile(生成文件，实际上就是生成的apk)的信息*/
        project.android.applicationVariants.all {variant->
            project.logger.lifecycle "===applicationVariant name : ${variant.name}"
            // variant.outputs -> List<ApkVariantOutputImpl_Decorated>
            project.logger.lifecycle "===applicationVariant.outputs : ${variant.outputs}"
            variant.outputs.each {output->
                // variant.outputs[i] -> ApkVariantOutputImpl_Decorated
                project.logger.lifecycle "===applicationVariant.outputs[i] : ${output}"
                // variant.outputs[i].outputFile -> File
                project.logger.lifecycle "===applicationVariant.outputs[i].outputFile : ${output.outputFile}"
            }
        }

        // 下面代码让module/build/outputs/apk/中的apk名字中加个_adjusted后缀
        project.logger.lifecycle "===change outputFile of each applicationVariant"
        project.android.applicationVariants.all {variant->
            variant.outputs.each {output->
                File originalOutputFile = output.outputFile
                project.logger.lifecycle "===originalOutputFile.parent : ${originalOutputFile.parent}"
                project.logger.lifecycle "===originalOutputFile.name : ${originalOutputFile.name}"
                File adjustedOutputFile = new File(originalOutputFile.parent, originalOutputFile.name.split("\\.")[0] + "_adjusted" + ".apk");
                output.outputFile = adjustedOutputFile;
                project.copy {
                    // apk原样拷贝到另一个目录
                    from adjustedOutputFile.absolutePath
                    into adjustedOutputFile.parent + "_copied"
                    // apk重命名后拷贝到同一个目录
                    // 方法参数中包括闭包和其它参数时，其它参数的括号不能省略
                    // rename的操作要写在from方法的闭包参数里，写在into方法的闭包参数里无效
                    from (adjustedOutputFile.absolutePath) {
                        // rename 方法的闭包参数的返回值为重命名后的文件名
                        rename {
                            return "renamed.apk"
                        }
                    }
                    into (adjustedOutputFile.parent)
                }
            }
        }
    }

}