package com.lilong.kaptprocessortest

import com.lilong.kaptannotation.SUT
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic.Kind.ERROR
import javax.tools.Diagnostic.Kind.WARNING

/**
 * AbstractProcessor 的说明
 * https://docs.oracle.com/javase/7/docs/api/javax/annotation/processing/AbstractProcessor.html
 */
class SUTAnnotationProcessor : AbstractProcessor() {

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(SUT::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {

        val annotatedElements = roundEnv.getElementsAnnotatedWith(SUT::class.java)

        if (annotatedElements.isEmpty()) {
            return false
        }

        for (element in annotatedElements) {

            val typeElement = element.toTypeElementOrNull() ?: continue
            processAnnotatedTypeElement(typeElement)
        }

        return true
    }

    private fun processAnnotatedTypeElement(element: TypeElement) {
        val className = element.simpleName.toString()
        val packageName = processingEnv.elementUtils.getPackageOf(element).toString()
        processingEnv.messager.printMessage(WARNING, "className = $className, packageName = $packageName")

    }

    private fun Element.toTypeElementOrNull(): TypeElement? {
        if (this !is TypeElement) {
            processingEnv.messager.printMessage(ERROR, "Invalid element type, class expected", this)
            return null
        }
        return this
    }
}
