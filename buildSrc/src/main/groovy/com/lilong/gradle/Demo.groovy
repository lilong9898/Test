package com.lilong.gradle


import org.gradle.api.Plugin
import org.gradle.api.Project
// 插件的两大作用：
// 1. 根据原始的project dsl对project进行操作
// 2. 通过定义extension对原始的project dsl进行扩展，给project增加新的功能
class Demo implements Plugin<Project> {

    @Override
    void apply(Project project) {
//        new DemoConfiguration().apply(project)
//        new DemoShowLifeCycle().apply(project)
//        new DemoShowInfo().apply(project)
//        new DemoCreateExtension().apply(project)
//        new DemoCreateTask().apply(project)
//        new DemoAdjustApkOutput().apply(project)
    }
}
