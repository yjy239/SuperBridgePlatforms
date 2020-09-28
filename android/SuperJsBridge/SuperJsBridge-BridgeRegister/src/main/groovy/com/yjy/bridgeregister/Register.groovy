package com.yjy.bridgeregister

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.tasks.compile.JavaCompile

class Register implements Plugin<Project>{

    @Override
    void apply(Project project) {
        // only application can register
        def isApplicationModule = project.plugins.hasPlugin(AppPlugin)

        if(isApplicationModule){
            Logger.i('Project enable register plugin')
            def android = project.extensions.getByType(AppExtension)
            android.registerTransform()
        }
    }
}