package com.etocrm.memcached.util;

import com.etocrm.memcached.pojo.ErrorCode;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedDecodeException;
import net.rubyeye.xmemcached.exception.MemcachedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.concurrent.TimeoutException;

/**
 * @author xing.liu
*memcachedb支持命令有：get/set/add/replace/delete/incr/decr
 *没有失效时间的概念，因为是存到db数据库中,永不失效
 * 2019年10月12日
 */
@Configuration
public class MemcachedbClient {

	private  Logger  logger=LoggerFactory.getLogger(MemcachedbClient.class);
	
	
	@Autowired
    private MemcachedClient memcachedClient;
	
	
	
	/**
	 * @param key 永久存在
	 * @param value
	 * @return
	 */
	public ErrorCode set(String key, Object value) {
		boolean set=false;
		ErrorCode blank = StringUtils.isBlank(key,value);
		if(blank!=ErrorCode.SUCCESS) {
			return blank;
		}
		value = StringUtils.valueFormat(key,value);
	        try {
	        	set = memcachedClient.set(key,0, value);
	        	if (!set){
	        		logger.error("未知异常！");
				}
	        	return ErrorCode.SUCCESS;
			} catch (Exception e) {
				getException(e);
				return ErrorCode.CONNECTION_ERROR;
			}

	 }
	
	 /**
     * 查询key
     * 如果没有对应 key 返回null
     * @param key
     * @return
     */
    public Object get(String key) {
		ErrorCode blank = StringUtils.isBlank(key);
		if(blank!=ErrorCode.SUCCESS) {
			return blank;
		}
		try {
    		return   memcachedClient.get(key)==null?ErrorCode.KEY_NOTEXITS:memcachedClient.get(key);

		} catch (Exception e) { 
			getException(e);
			return ErrorCode.CONNECTION_ERROR;
	   }
    	
    }
    
    /**
     * 获取多个key的值
     * @param key
     * @return
     */
    public Object get(Collection<String> key) {
    	if(key==null || key.size()==0) {
        	return ErrorCode.NULL_KEY;
        }
		try {
			
    		return   memcachedClient.get(key)==null?ErrorCode.KEY_NOTEXITS:memcachedClient.get(key);
		} catch (Exception e) {
			getException(e);
			return ErrorCode.CONNECTION_ERROR;
	   }
    	
    }
    
    
   
    
    
    /**
     * 如果该key存在，数据不会更新
     * @param key
     * @param value
     * @return
     */
    public ErrorCode add(String key,Object value){
    	boolean add=false;
		ErrorCode blank = StringUtils.isBlank(key,value);
		if(blank!=ErrorCode.SUCCESS) {
			return blank;
		}
		value = StringUtils.valueFormat(key,value);
    	try {
			 add = memcachedClient.add(key,0,  value);
			 if (add){
			 	return ErrorCode.SUCCESS;
			 }
			 return ErrorCode.KEY_EXITS;
    	} catch (Exception e) {
    		getException(e);
			return ErrorCode.CONNECTION_ERROR;
		}
    	
    }
    
    
    
    /**
     * 替换已存在的 key(键) 的 value(数据值)。
     * @param key
     * @param value
     * @return
     */
    public  ErrorCode  replaceData(String key,Object value){
    	boolean add=false;
		ErrorCode blank = StringUtils.isBlank(key,value);
		if(blank!=ErrorCode.SUCCESS) {
			return blank;
		}
		value = StringUtils.valueFormat(key,value);
    	try {
			 add = memcachedClient.replace(key,0 ,value);
			 if (add){
			 	return ErrorCode.SUCCESS;
			 }
			 return ErrorCode.KEY_NOTEXITS;
    	} catch (Exception e) {	
    		getException(e);
			return ErrorCode.CONNECTION_ERROR;
		}
    	
    }
    
    /**
     * 向已存在 key(键) 的 value(数据值) 后面追加数据
     * @param key
     * @param value
     * @return
     */
    public  ErrorCode  append (String key,Object value){
		ErrorCode blank = StringUtils.isBlank(key,value);
		if(blank!=ErrorCode.SUCCESS) {
			return blank;
		}
		value = StringUtils.valueFormat(key,value);
    	try {
			return memcachedClient.append(key, value)?ErrorCode.SUCCESS : ErrorCode.KEY_NOTEXITS;
    	} catch (Exception e) {
    		getException(e);
			return ErrorCode.CONNECTION_ERROR; 
		}
    	
    }
    /**
     * 向已存在 key(键) 的 value(数据值) 后面追加数据
     * @param key
     * @param value
     * @return
     */
    public  ErrorCode  prepend (String key,Object value){
		ErrorCode blank = StringUtils.isBlank(key,value);
		if(blank!=ErrorCode.SUCCESS) {
			return blank;
		}
		value = StringUtils.valueFormat(key,value);
    	try {
			return memcachedClient.prepend(key, value)?ErrorCode.SUCCESS : ErrorCode.KEY_NOTEXITS;
    	} catch (Exception e) {
    		getException(e);
			return ErrorCode.CONNECTION_ERROR; 
		}
    	
    }
    
   

    /**
     * 自减,自减
     * delta>0自增，否则自减
     * @param key
     * @param delta
     * @return
     */
    public ErrorCode incrOrDecr (String key,Long delta,Long  initValue){
		ErrorCode blank = StringUtils.isBlankAndNumber(key,delta);
	   if(blank!=ErrorCode.SUCCESS) {
		    return blank;
	   }
    	try {
    		if(delta<0) {
        		long abs = Math.abs(delta);
        		//自减
        		 memcachedClient.decr(key, abs, initValue);
        		
        	}else if(delta>0) {
        		  memcachedClient.incr(key, delta, initValue);
        	}
    	} catch (Exception e) {
    		getException(e);
			return ErrorCode.CONNECTION_ERROR;
		}
		return ErrorCode.SUCCESS;
    	
    }
    
    /**
    * 删除
    * @param key 
    */  
   public ErrorCode delete(String key) {
	   ErrorCode blank = StringUtils.isBlank(key);
	   if(blank!=ErrorCode.SUCCESS) {
		    return blank;
	   }

       try {
       		memcachedClient.delete(key);
            return ErrorCode.SUCCESS;
       } catch (Exception e) {
       		logger.info("--------抛出异常。");
       		getException(e);
			return ErrorCode.CONNECTION_ERROR;
       }  
   }


	/**
	 * 异常日志打印
	 * @param e
	 * @return
	 */
	private  String  getException  (Exception  e){
		if(e instanceof TimeoutException) {
			  logger.error("timeoutException", e);
			  return "timeoutException";
		}else if(e instanceof InterruptedException) {
			  logger.error("InterruptedException", e);
			  return "InterruptedException";
		}else if(e instanceof MemcachedException) {
			  logger.error("MemcachedbException", e);
			  return "MemcachedbException";
		}else if(e instanceof MemcachedDecodeException) {
			logger.error("MemcachedDecodeException", e);
			return "MemcachedDecodeException";
		} else {
			  logger.error("error", e);
			  return "error";
		}
	}



}
