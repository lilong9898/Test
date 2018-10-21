package com.lilong.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class DemoCreateTask implements Plugin<Project>{

    @Override
    void apply(Project project) {
        // 给project添加最简单的task，下面两种方法是一样的
        project.task "task1CreatedByDemoCreateTask"
        project.tasks.create "task2CreatedByDemoCreateTask"
    }
}