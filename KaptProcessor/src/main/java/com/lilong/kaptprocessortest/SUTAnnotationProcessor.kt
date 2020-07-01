package com.lilong.kaptprocessortest

import com.lilong.kaptannotation.SUT
import com.squareup.javapoet.*
import java.io.IOException
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind.FIELD
import javax.lang.model.element.Modifier
import javax.lang.model.element.Modifier.FINAL
import javax.lang.model.element.Modifier.PRIVATE
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.util.Elements
import javax.tools.Diagnostic.Kind
import javax.tools.Diagnostic.Kind.ERROR
import javax.tools.Diagnostic.Kind.WARNING

/**
 * AbstractProcessor 的说明
 * https://docs.oracle.com/javase/7/docs/api/javax/annotation/processing/AbstractProcessor.html
 */
class SUTAnnotationProcessor : AbstractProcessor() {

    private lateinit var elementUtils: Elements

    override fun init(processorEnv: ProcessingEnvironment) {
        super.init(processorEnv)
        elementUtils = processorEnv.elementUtils
    }

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

    private fun processAnnotatedTypeElement(typeElement: TypeElement) {

        val typeSpecBuilder = createTypeSpecBuilder(typeElement)

        for (element in typeElement.enclosedElements) {

            log(WARNING, element.toString())
            if (!element.isField() || !element.isPrivate() || element.isFinal()) {
                continue
            }

            if (element is VariableElement) {
                typeSpecBuilder.addMethod(element.buildSetterFunction())
//                val fullJavaClassName = element.asType().asTypeName().toString()
                // todo 未考虑内部类的情况
//                val packageName = getPackageName(fullJavaClassName)
//                val simpleClassName = getSimpleClassName(fullJavaClassName)
//                fileSpecBuilder.addStaticImport(packageName, simpleClassName)
            }
        }

        val typeSpec = typeSpecBuilder.build()
        val javaFile = JavaFile.builder(getPackageName(typeElement), typeSpec).build()
        try {
            javaFile.writeTo(processingEnv.filer)
        } catch (e: IOException) {
            log(WARNING, "write file fails, exception = $e")
        }
    }

    private fun createTypeSpecBuilder(typeElement: TypeElement): TypeSpec.Builder {
        val originName = typeElement.qualifiedName.toString()
        val packageName = getPackageName(typeElement)
        return TypeSpec.classBuilder("BaseTest")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
    }

    private fun Element.isField() = this.kind == FIELD

    private fun Element.isPrivate() = this.modifiers.contains(PRIVATE)

    private fun Element.isFinal() = this.modifiers.contains(FINAL)

    private fun VariableElement.buildSetterFunction(): MethodSpec {
        return MethodSpec.methodBuilder("set${capitalize(simpleName.toString())}")
                .addParameter(buildSetterFunctionParameterSpec())
                .build()
    }

    private fun VariableElement.buildSetterFunctionParameterSpec(): ParameterSpec {
//        val javaClassFullName = asType().asTypeName().toString()
//        val javaClass = Class.forName(javaClassFullName)
//        log(WARNING, javaClass.toString())
        return ParameterSpec.builder(TypeName.get(asType()), simpleName.toString()).build()
    }

    private fun Element.toTypeElementOrNull(): TypeElement? {
        if (this !is TypeElement) {
            log(ERROR, "Invalid element type, class expected", this)
            return null
        }
        return this
    }

    private fun capitalize(s: String?): String {
        if (s.isNullOrEmpty()) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            Character.toUpperCase(first) + s.substring(1)
        }
    }

    private fun getPackageName(type: TypeElement): String {
        return elementUtils.getPackageOf(type).qualifiedName.toString()
    }

    private fun log(kind: Kind, message: String, element: Element? = null) {
        processingEnv.messager.printMessage(kind, message, element)
    }

    companion object {
        private const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }
}
