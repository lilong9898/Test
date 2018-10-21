package com.lilong.gradle.plugin

import com.android.build.gradle.api.ApkVariant
import org.gradle.api.Plugin
import org.gradle.api.Project

class DemoShowInfo implements Plugin<Project> {

    @Override
    void apply(Project project) {

        /** project名字*/
        project.logger.lifecycle "===project.name : ${project.name}==="

        /** project的相对路径*/
        project.logger.lifecycle "===project.path : ${project.path}==="

        /** project的build目录*/
        project.logger.lifecycle "===project.buildDir.path : ${project.buildDir.path}==="

        /** project的build目录的绝对路径*/
        project.logger.lifecycle "===project.buildDir.absolutePath : ${project.buildDir.absolutePath}==="

        /** project的build.gradle文件的绝对路径*/
        project.logger.lifecycle "===project.buildFile.absolutePath : ${project.buildFile.absolutePath}==="

        /** project的上级project的名字*/
        project.logger.lifecycle "===project.parent.name : ${project.parent.name}==="

        /** project的defaultTask名字*/
        for (taskName in project.defaultTasks){
            project.logger.lifecycle("===project.defaultTasks name : ${taskName}===")
        }

        /**
         * gradle版本，就是工程中的gradle-wrapper.properties中所指定的
         * */
        project.logger.lifecycle "===project.gradle.gradleVersion : ${project.gradle.gradleVersion}==="

        /**
         * gradle执行目录，gradle版本就是工程中的gradle-wrapper.properties中所指定的
         * 这个gradle执行目录包含gradle-xxx.zip和其解压后的可执行文件，以及gradle源代码等
         * 实际上是~/.gradle/wrapper/dists/gradle-xxx.zip-all/xxxx/gradle-xxx/目录
         * */
        project.logger.lifecycle "===project.gradle.gradleHomeDir.absolutePath : ${project.gradle.gradleHomeDir.absolutePath}==="

        /**
         *gradle用户目录，其包含上面的gradle执行目录，还包含其它的cache，编译后的gradle脚本等
         * 实际上是~/.gradle目录
         * */
        project.logger.lifecycle "===project.gradle.gradleUserHomeDir : ${project.gradle.gradleUserHomeDir}==="

        /**
         * 每次gradle build,都是因为要执行某些task
         * 所以每次build实际运行的动作都是：init->config->execute，其中execute仅包含要执行的这些task
         * 而startParamter.taskNames表示的就是这些task的名字
         * */
        for(name in project.gradle.startParameter.taskNames){
            project.logger.lifecycle "===project.gradle.startParameter.taskName : ${name}==="
        }

        /**
         * project.android.applicationVariants返回的是DomainObjectSet
         * 而其是DomainObjectCollection的子类，所以有void all(Action<? super T> var1);方法
         * all方法的每次调用，都会把后面的closure加入到actionList里，具体是通过CollectionEventRegister的registerAddAction方法加入的
         * 所以多次调用project.android.applicationVariants.all传入的closure，会在触发时依次执行
         * */
        project.android.applicationVariants.all {variant->
            project.logger.lifecycle "===project.android.applicationVariants.all operation 1"
        }

        /**
         * 输出每个applicationVariant有关的信息
         * */
        project.android.applicationVariants.all {ApkVariant variant->
            project.logger.lifecycle "===project.android.applicationVariants.all operation 2"
            project.logger.lifecycle "---------------------printing info of applicationVariant--------------"
            project.logger.lifecycle "===applicationVariant.name : ${variant.name}==="
            project.logger.lifecycle "===applicationVariant.dirName : ${variant.dirName}==="
            project.logger.lifecycle "===applicationVariant.baseName : ${variant.baseName}==="
            project.logger.lifecycle "===applicationVariant.flavorName : ${variant.flavorName}==="
            project.logger.lifecycle "===applicationVariant.buildType.name : ${variant.buildType.name}==="
            //　该applicationVariant内部的信息都在mergedFlavor里，比如
            // minSdkVersion, targetSdkVersion, versionCode, versionName, applicationId, signingConfig, buildConfigFields等
            project.logger.lifecycle "===applicationVariant.mergedFlavor : ${variant.mergedFlavor}==="

            /**
             * 可以获取到这个variant的某些task对象并配置其与其它task相互之间的依赖关系
             * 比如可以获取到assemble, preBuild, obfuscate等task
             * */
            project.logger.lifecycle "===applicationVariant.assemble : ${variant.assemble}==="

            // 添加所有applicationVariant都有的buildConfigField
            // 第三个参数最外面一定是单引号对''
            // 如果是数字则单引号对内是数字
            // 如果是字符串则单引号对内是双引号字符串
            // 同时第一个参数的类型要与第三个参数的写法匹配

            //--------------------------各种类型的buildConfigField--------------------------------------
            variant.buildConfigField "String", "BUILD_CONFIG_FIELD_TYPE_STRING", '"build_config_field_type_string"'
            variant.buildConfigField "int", "BUILD_CONFIG_FIELD_TYPE_INT", '1'
            variant.buildConfigField "float", "BUILD_CONFIG_FIELD_TYPE_FLOAT", '1.2f'
            variant.buildConfigField "double", "BUILD_CONFIG_FIELD_TYPE_DOUBLE", '1.2'
            variant.buildConfigField "long", "BUILD_CONFIG_FIELD_TYPE_LONG", '12l'
            variant.buildConfigField "boolean", "BUILD_CONFIG_FIELD_TYPE_BOOLEAN", 'true'
            variant.buildConfigField "char", "BUILD_CONFIG_FIELD_TYPE_BOOLEAN", "'a'"

            //-------------------------根据applicationVariant的情况选择性设置buildConfigField-------------
            variant.buildConfigField "String", "BUILD_CONFIG_FIELD_COMMON", '"build_config_field_common"'

            // e.g.,只给buildType.name为release的variant添加buildConfigField
            if(variant.buildType.name == "release"){
                variant.buildConfigField "String", "BUILD_CONFIG_FIELD_RELEASE_ONLY", '"build_config_field_release_only"'
            }
        }

    }

}