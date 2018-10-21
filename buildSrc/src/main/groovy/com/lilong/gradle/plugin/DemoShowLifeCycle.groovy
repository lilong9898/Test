package com.lilong.gradle.plugin

import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.ProjectEvaluationListener
import org.gradle.api.ProjectState
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle;

class DemoShowLifeCycle implements Plugin<Project> {

    @Override
    void apply(Project project) {

        /**
         * apply方法会在目标工程的apply plugin: com.lilong.gradle.Demo处调用
         * 在init和config阶段，build.gradle中所有语句是按顺序执行的，
         * 也就是通过按顺序执行这些语句来完成init和config
         * execute阶段则是执行某个具体的task
         * */
        project.logger.lifecycle "===plugin apply called==="

        /**
         * 未发现有执行到
         * */
        project.beforeEvaluate {
            project.logger.lifecycle "===project.beforeEvaluate==="
        }

        /**
         init和config阶段完成，即将进入execute阶段时执行
         */
        project.afterEvaluate {
            project.logger.lifecycle "===project.afterEvaluate==="
        }


        /**
         * 在afterEvaluate的closure之前执行，因为在config阶段中，evaluate操作会评估出所有的buildVariant
         * */
        project.android.applicationVariants.all {applicationVariant ->
            project.logger.lifecycle "===project.android.applicationVariants.all variant = ${applicationVariant.name}==="
        }

        /**
         * afterEvaluate之后执行
         * */
        project.afterEvaluate {
            project.android.applicationVariants.all {applicationVariant ->
                project.logger.lifecycle "===project.afterEvaluate {project.android.applicationVariants.all variant} = ${applicationVariant.name}==="
            }
        }

        /**
         * 用configure方法来配置project
         * 注意：afterEvaluate方法会把closure加到一个监听器的集合中，所以多次调用afterEvaluate会把多个监听按照调用顺序都加入到监听器的集合里
         * 当project完成evaluate后会按监听器的集合中加入的顺序依次调用closure
         * */
        project.configure(project) {
            project.logger.lifecycle "===project.configure==="
            afterEvaluate {
                project.logger.lifecycle "===configure's after evaluate"
            }
            project.logger.lifecycle "===project.configure2==="
        }

        project.gradle.addProjectEvaluationListener(new ProjectEvaluationListener() {
            @Override
            void beforeEvaluate(Project p) {
                project.logger.lifecycle "===projectEvaluationListener:beforeEvalute"
            }

            @Override
            void afterEvaluate(Project p, ProjectState projectState) {
                project.logger.lifecycle "===projectEvaluationListener:afterEvalute"
            }
        })

        project.gradle.addBuildListener(new BuildListener() {
            @Override
            void buildStarted(Gradle gradle) {
                project.logger.lifecycle "===BuildListener : buildStarted==="
            }

            @Override
            void settingsEvaluated(Settings settings) {
                project.logger.lifecycle "===BuildListener : settingsEvaluated==="
            }

            @Override
            void projectsLoaded(Gradle gradle) {
                project.logger.lifecycle "===BuildListener : projectsLoaded==="
            }

            @Override
            void projectsEvaluated(Gradle gradle) {
                project.logger.lifecycle "===BuildListener : projectsEvaluated==="
            }

            @Override
            void buildFinished(BuildResult buildResult) {
                project.logger.lifecycle "===BuildListener : buildFinished==="
            }
        })
    }
}