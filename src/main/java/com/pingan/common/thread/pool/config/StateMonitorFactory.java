package com.pingan.common.thread.pool.config;

import org.apache.commons.digester.AbstractObjectCreationFactory;
import org.xml.sax.Attributes;

import com.pingan.common.thread.pool.state.AbstractStateMonitor;

/**
 * @function 监控任务工厂， 供digester调用 ， 根据threadPool.xml中配置的monitors规则创建
 * 相应的AbstractMonitorFactory实现类并返回
 * 
 * @author leslie
 */
public class StateMonitorFactory extends AbstractObjectCreationFactory {

    @Override
    public Object createObject(Attributes attributes) throws Exception {
        Class<?> clazz = Class.forName(attributes.getValue("class"));
        Object object = clazz.newInstance();
        if (object instanceof AbstractStateMonitor) {
            ((AbstractStateMonitor) object).setName(attributes.getValue("name"));
        }

        return object;
    }
}
