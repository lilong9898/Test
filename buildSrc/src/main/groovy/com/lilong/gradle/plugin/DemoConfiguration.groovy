package com.lilong.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration

/**
 * gradle dependency resolution有关内容，重点是configuration的概念和围绕它的操作
 * configuration是一类dependency，比如compile, provided这些都是内置的configuration
 * 可以自定义configuration并设置它的属性：
 * (1) 方法1: project.getConfigurations().create
 * (2) 方法2: 在configuration{}块中直接写自定义configuration的名字
 * configuration{} 中可以配置configuration，这种写法本质上是调用了project的configuration方法，参数是个闭包，闭包的delegate是ConfigurationContainer
 * */
class DemoConfiguration implements Plugin<Project> {

    @Override
    void apply(Project project) {

        // 自定义configuration : customCompile，继承自compile
        Configuration customCompile1 = project.getConfigurations().create("customCompile1");
        // 设置customCompile为解析依赖时考虑依赖传递
        customCompile1.setTransitive(true)

    }
}
