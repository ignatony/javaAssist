package com.sample.ig.test;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.ClassFile;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.ParameterAnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ClassMemberValue;

public class TestAssist {
	
	public static Class myAssist() throws NotFoundException, IOException, CannotCompileException {
		
		ClassPool pool = ClassPool.getDefault();
		CtClass cc = pool.get("com.sample.ig.Sample");
		
		//cc.setSuperclass(pool.get("com.sample.ig.Point"));
		ClassFile cf = cc.getClassFile();
		CtClass[] parameters;
		
		CtMethod mm = cc.getDeclaredMethod("hello");
		
		
	//	MethodInfo minfo = cf.getMethod("hello");    // we assume move is not overloaded.
		//CodeAttribute ca = minfo.getCodeAttribute();
		
		convertAnnotationsOnParameters(mm, "name");
		
		cc.writeFile();
		
		cf.write(new DataOutputStream(new FileOutputStream(cc.getName()+".class")));
		return null;
		
		
		
		
		/*// -- Get method template
	    CtMethod dummyMethod = liveClass.getMethods()[2];
	    // -- Get the annotation
	    //AttributeInfo parameterAttributeInfo = dummyMethod.getMethodInfo().getAttribute(ParameterAnnotationsAttribute.visibleTag);
	    AttributeInfo parameterAttributeInfo =  = AttributeInfo.copy​(new ConstPool(    java.util.Map classnames)
	    ConstPool parameterConstPool = parameterAttributeInfo.getConstPool();
	    ParameterAnnotationsAttribute parameterAtrribute = ((ParameterAnnotationsAttribute) parameterAttributeInfo);
	    Annotation[][] paramArrays = (Annotation[][]) parameterAtrribute.getAnnotations();
	    Annotation[] addAnno = paramArrays[0];
	    //-- Edit the annotation adding values
	    ((javassist.bytecode.annotation.Annotation) addAnno[0]).addMemberValue("value", new StringMemberValue("This is the value of the annotation", parameterConstPool));
	    ((javassist.bytecode.annotation.Annotation) addAnno[0]).addMemberValue("required", new BooleanMemberValue(Boolean.TRUE, parameterConstPool));
	    paramArrays[0] = addAnno;
	    parameterAtrribute.setAnnotations((javassist.bytecode.annotation.Annotation[][]) paramArrays);
		System.out.println("hello");
		return null;//cc.toClass();
*/	}

	public static void main(String arg[]) throws NotFoundException, CannotCompileException, IOException {
		myAssist();

	}
	
	private static void convertAnnotationsOnParameters(CtMethod method, String path) { 
		MethodInfo methodInfo = method.getMethodInfo();
		CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
		 LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
	       
        ConstPool parameterConstPool = attr.getConstPool();
        /**
         * param annotation
         */
        Annotation parameterAnnotation = new Annotation("QueryParam", parameterConstPool);
        ClassMemberValue parameterMemberValue = new ClassMemberValue(path, parameterConstPool);
        parameterAnnotation.addMemberValue("value", parameterMemberValue);
        /**
         * add annotation to dimensional array
         */
        LocalVariableAttribute parameterAtrribute =  attr;
        
        //ParameterAnnotationsAttribute parameterAtrribute = new ParameterAnnotationsAttribute(parameterConstPool, ParameterAnnotationsAttribute.invisibleTag);
        //LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        Annotation[][] paramArrays = new Annotation[0][0];
        /*int orderNum = 0;
        Annotation[] addAnno = paramArrays[1];
        Annotation[] newAnno = null;
        if (addAnno.length == 0) {
         newAnno = new Annotation[1];
        } else {
         newAnno = Arrays.copyOf(addAnno, addAnno.length + 1);
        }
        newAnno[addAnno.length] = parameterAnnotation;
        paramArrays[orderNum] = newAnno;*/
        ((ParameterAnnotationsAttribute) attr).setAnnotations(paramArrays);
    } 
	
	  private static boolean isPathParam(String path, String paramName) { 
	        return path.contains("{" + paramName + "}"); 
	    } 
	 
	    /*private Annotation createProducesAnnotation(String value) { 
	        StringMemberValue element = memberValueOf(value); 
	        ArrayMemberValue array = createSingleElementArrayMemberValue(String.class, element); 
	        return createAnnotation(Produces.class, array); 
	    } */
	 
	    private static Annotation[] addToArray(Annotation[] paramAnnotations, Annotation queryParam) { 
	        Annotation newParamAnnotations[] = new Annotation[paramAnnotations.length + 1]; 
	        System.arraycopy(paramAnnotations, 0, newParamAnnotations, 0, paramAnnotations.length); 
	        newParamAnnotations[newParamAnnotations.length - 1] = queryParam; 
	        return newParamAnnotations; 
	    } 
}
