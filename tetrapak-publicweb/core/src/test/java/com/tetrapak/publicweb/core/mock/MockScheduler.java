package com.tetrapak.publicweb.core.mock;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.sling.commons.scheduler.ScheduleOptions;
import org.apache.sling.commons.scheduler.Scheduler;

/**
 * The quartz based implementation of the scheduler.
 * @scr.component metatype="no" immediate="true"
 * @scr.service interface="org.apache.sling.commons.scheduler.Scheduler"
 * @scr.reference name="job" interface="org.apache.sling.commons.scheduler.Job" cardinality="0..n" policy="dynamic"
 * @scr.reference name="task" interface="java.lang.Runnable" cardinality="0..n" policy="dynamic"
 */
public class MockScheduler implements Scheduler {
    
    Runnable task;
    
    public MockScheduler(Runnable task) {
        this.task = task;
    }
    
    @Override
    public ScheduleOptions AT(Date arg0) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public ScheduleOptions AT(Date arg0, int arg1, long arg2) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public ScheduleOptions EXPR(String arg0) {
        // TODO Auto-generated method stub
        return new MockScheduleOptions();
    }
    
    @Override
    public ScheduleOptions NOW() {
        // TODO Auto-generated method stub
        return new MockScheduleOptions();
    }
    
    @Override
    public ScheduleOptions NOW(int arg0, long arg1) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void addJob(String arg0, Object arg1, Map<String, Serializable> arg2, String arg3, boolean arg4)
            throws Exception {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void addPeriodicJob(String arg0, Object arg1, Map<String, Serializable> arg2, long arg3, boolean arg4)
            throws Exception {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void addPeriodicJob(String arg0, Object arg1, Map<String, Serializable> arg2, long arg3, boolean arg4,
            boolean arg5) throws Exception {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void fireJob(Object arg0, Map<String, Serializable> arg1) throws Exception {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public boolean fireJob(Object arg0, Map<String, Serializable> arg1, int arg2, long arg3) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public void fireJobAt(String arg0, Object arg1, Map<String, Serializable> arg2, Date arg3) throws Exception {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public boolean fireJobAt(String arg0, Object arg1, Map<String, Serializable> arg2, Date arg3, int arg4, long arg5) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public void removeJob(String arg0) throws NoSuchElementException {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public boolean schedule(Object arg0, ScheduleOptions arg1) {
        // TODO Auto-generated method stub
        task.run();
        return true;
    }
    
    @Override
    public boolean unschedule(String arg0) {
        return true;
    }
    
}
