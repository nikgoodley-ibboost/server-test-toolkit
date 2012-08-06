package org.test.toolkit.timerjob;

public class ClassEntry<T> {
	
	private String name;
	private String className;
	 
 	public String getName() {
		return name;
	}
 

	public void setName(String name) {
		this.name = name;
	}
 

	public String getClassName() {
		return className;
	}

 	public void setClassName(String className) {
		this.className = className;
	}
 	
 	public T  getClassInstance(){
  		try {
 	 		@SuppressWarnings("unchecked")
			Class<T> forName = (Class<T>) Class.forName(className);
			return forName.newInstance();
		} catch (InstantiationException e) {
 			e.printStackTrace();
 			throw new JobManageException(e);
		} catch (IllegalAccessException e) {
 			e.printStackTrace();
 			throw new JobManageException(e);
		} catch (ClassNotFoundException e) {
 			e.printStackTrace();
 			throw new JobManageException(e);
		}
 	}
 
	@Override
	public String toString() {
		return "ClassEntry [name=" + name + ", className=" + className + "]";
	}
	 

}
