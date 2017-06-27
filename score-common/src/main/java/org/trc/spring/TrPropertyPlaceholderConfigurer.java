package org.trc.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;

/**
 * Created by hzwzhen on 2017/6/5.
 */
public class TrPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    private Properties props;

    public TrPropertyPlaceholderConfigurer() {

    }

    public Properties getProps() {
        return props;
    }

    public void setProps(Properties props) {
        this.props = props;
    }

    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        this.props = props;
        super.processProperties(beanFactoryToProcess, props);
    }
}
