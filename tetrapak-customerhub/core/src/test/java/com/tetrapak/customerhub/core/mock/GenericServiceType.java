package com.tetrapak.customerhub.core.mock;

 public class GenericServiceType<T> {
    private Class<T> clazzType;
    private T ObjectType;
    public GenericServiceType(){
        
    }
 
    public void setClazzType(Class<T> clazzType) {
        this.clazzType = clazzType;
    }
    public Class<T> getClazzType() {
        return this.clazzType;
    }
    public T get(){
        return this.ObjectType;
    }
    public void set(T ObjectType){
        this.ObjectType = ObjectType;
    }
 
        
    }