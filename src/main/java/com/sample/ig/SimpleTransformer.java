package com.sample.ig;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.NotFoundException;

public class SimpleTransformer implements ClassFileTransformer {
	 
	  public SimpleTransformer() {
	    super();
	  }
	  

	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		
		return transformClass(classBeingRedefined, classfileBuffer) ;
	}
	private byte[] transformClass(Class<?> classToTransform, byte[] b) {
	    ClassPool pool = ClassPool.getDefault();
	    CtClass cl = null;
		try {
	      cl = pool.makeClass(new java.io.ByteArrayInputStream(b));
	      CtBehavior[] methods = cl.getDeclaredBehaviors();
	      for (int i = 0; i < methods.length; i++) {
	        if (methods[i].isEmpty() == false) {
	          changeMethod(methods[i]);
	        }
	      }
	      b = cl.toBytecode();
	    }
	    catch (Exception e) {
	      e.printStackTrace();
	    }
	    finally {
	      if (cl != null) {
	        cl.detach();
	      }
	    }
	    return b;
	  }
	private void changeMethod(CtBehavior method) throws NotFoundException, CannotCompileException {
	    if (method.getName().equals("doIt")) {
	      method.insertBefore("System.out.println(\"started method at \" + new java.util.Date());");
	      method.insertAfter("System.out.println(\"ended method at \" + new java.util.Date());");
	    }
	  }
}
