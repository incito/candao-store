package com.candao.www.interceptor;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.beans.factory.annotation.Autowired;

import com.candao.common.utils.Constant;
import com.candao.common.utils.DateUtils;
import com.candao.common.utils.PropertiesUtils;
import com.candao.www.data.model.User;
import com.candao.www.utils.SessionUtils;
import com.candao.www.webroom.model.SynSqlObject;
import com.candao.www.webroom.service.impl.BranchServiceThread;
import com.candao.www.webroom.zookeeper.CuratorTools;
import com.candao.www.webroom.zookeeper.ZkDqQueuer;
 
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
        //@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,RowBounds.class, 	ResultHandler.class }) 
        })
public class MybatisInterceptor implements Interceptor {
	
	
	 private Log log = LogFactory.getLog(MybatisInterceptor.class);
 
    private Properties properties;
    
//    @Autowired
//    ZkDqQueuer  queuer;
  
 
 
    public Object intercept(Invocation invocation) throws Throwable {
    	
    	//只有总店下发数据
	   MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }
        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();
        Object object = invocation.proceed();
//        String sql = getSql(configuration,boundSql);
        //判断当前的表名称做处理
//        setSqlToMQ(sql);
        
        
//    	SynSqlObject synObject = new SynSqlObject();
//    	synObject.setBranchid(PropertiesUtils.getValue("current_branch_id"));
//    	synObject.setGenerattime(DateUtils.dateToString(new Date()));
//    	synObject.setSqltext(sql);
//    	synObject.setId(UUID.randomUUID().toString());
//    	synObject.setStatus(0);
//		new BranchServiceThread(synObject);
		
        return object;
//        return null;
      
    }
    
  
    /**
     * set sql string to MQ
     * 云端下发数据到分店
     * @author tom_zhao
     * @param sql
     */
    private void setSqlToMQ(String sql){
//    	TbUser user = SessionUtils.getCurrentUser();
//    	String branchCode ;//= user.getBranchCode();
    	//TODO transfer sql to branch according branch code and role
    	
    	if("N".equals(PropertiesUtils.getValue("isbranch"))){
    		
//    		SessionUtils.getCurrentUser().getAccount();
//    		String branchId = PropertiesUtils.getValue("current_branch_id");
//    		String branchId = String.valueOf(SessionUtils.getSession(true).getAttribute("_BRANCH_ID"));
//    		String branchId = "";
//    		SynSqlObject object = new SynSqlObject();
//    		object.setBranchid(branchId);
//    		object.setGenerattime(DateUtils.dateToString(new Date()));
//    		object.setId(IdentifierUtils.getId().generate().toString());
//    		object.setStatus(0);
////    		object.setSql(sql); 
//    		object.setSqltext(sql);
    		
    		User user = (User)SessionUtils.get(Constant.CURRENT_USER);

//    		try {
//    			queuer.createrOrUpdate("/root/n_b_"+user.getAccount(), sql);
//			} catch (Exception e) {
//				log.error("同步数据错误 =["+e.getMessage()+"]");
//			}
//    		writeSqlFile(sql);
//    		new BranchServiceThread(object);
    		
 
    	}else{
    		//TODO insert t_syn_sql set status = 0
//    		writeSqlFile(sql);
    	}
    	
    }
    
    public void writeSqlFile(String sql){
    	SynSqlObject object = new SynSqlObject();
		object.setBranchid(PropertiesUtils.getValue("current_branch_id"));
		object.setGenerattime(DateUtils.dateToString(new Date()));
		object.setSqltext(sql);
		object.setId(UUID.randomUUID().toString());
		object.setStatus(0);
//		tSynSqlMapper.insert(object);
		new BranchServiceThread(object,0);
    }
 
    public static String getSql(Configuration configuration, BoundSql boundSql) {
    	return showSql(configuration, boundSql);
    }
 
    private static String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "null";
            }
 
        }
        return value;
    }
 
    public static String showSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));
 
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
                    }
                }
            }
        }
        return sql;
    }
 
    public Object plugin(Object target) {
    	
    	 if (target instanceof Executor) {  
             return Plugin.wrap(target, this);  
         } else {  
             return target;  
         }  
    	 
    }

	@Override
	public void setProperties(Properties properties) {
		 this.properties = properties;
	}
}