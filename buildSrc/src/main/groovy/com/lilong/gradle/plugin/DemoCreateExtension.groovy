package com.lilong.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project;

class DemoCreateExtension implements Plugin<Project>{

    @Override
    void apply(Project project) {

        //　创建一个extension，前两个参数是extension名字和extension的定义类，后面的参数是可变参数，用来给extension定义类的constructor提供参数
        // 创建好后将此extension注入project, project就会被加入demoExtension方法
        // 所以在本插件apply之前，主工程build.gradle中不能写demoExtension {....}，因为extension还未被注入，project中还没有demoExtension方法
        def demoExtension = project.extensions.create "demoExtension", DemoExtension, project
        // 之后任何时候主工程build.gradle中写了demoExtension {...}，就会配置这个extension，demoExtension中的各项数据就会被赋值
        // 所以为了保证demoExtension的各项数据不是null，要在project evalute完后再提取其数据
        // groovy的string interpolation，只能找到局部变量，也就是由def类型或者具体类型定义的变量
        // 无前缀的变量会进入script的binding里，所以string interpolation无法找到
        project.logger.lifecycle "===when apply is called : demoExtension.demoExtensionProperty = ${demoExtension.demoExtensionProperty}"
        project.afterEvaluate {
            project.logger.lifecycle "===project.afterEvaluate : demoExtension.demoExtensionProperty = ${demoExtension.demoExtensionProperty}"
        }
    }
}

class DemoExtension {

    final Project project

    DemoExtension(final Project project){
        this.project = project
        this.project.logger.lifecycle "===DemoExtension constructor called==="
    }

    String demoExtensionProperty;

    // 定义方法必须有返回值，是def类型或具体类型
    void demoExtensionMethod1(){
        this.project.logger.lifecycle "===demoExtensionMethod1 is called==="
    }

    def demoExtensionMethod2(param){
        this.project.logger.lifecycle "===demoExtensionMethod2 is called with param ${param}==="
    }
}