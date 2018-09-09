package com.sample.ig.test;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;

public class MyTest {
	
	public static void create() throws ClassNotFoundException, CannotCompileException {
		ClassPool pool = ClassPool.getDefault();
		CtClass cc = pool.makeClass("com.sample.ig.Sample");
		create_keyMethod(cc);
		
	}
	 private  static void create_keyMethod(CtClass ctClass) throws CannotCompileException, ClassNotFoundException { 
	       // CtField idField = EnhancerUtility.getFieldAnnotatedWithId(ctClass); 
	       ConstPool constPool = ctClass.getClassFile().getConstPool();
	       CtField idField = CtField.make("public int id = 0;", ctClass);
	       
	        String code = String.format("public Object _key(int id){return %s;}", idField.getName()); 
	        final CtMethod _key = CtMethod.make(code, ctClass); 
	        ctClass.addMethod(_key); 
	 
	        Annotation annotation = new Annotation(Override.class.getName(), constPool); 
	        AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag); 
	        attr.addAnnotation(annotation); 
	 
	        _key.getMethodInfo().addAttribute(attr); 
	    }
	public static void main(String[] args) throws ClassNotFoundException, CannotCompileException {
		create();
	}

}
