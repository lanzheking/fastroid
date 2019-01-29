package com.honestwalker.android.spring.context;


import android.content.Context;
import android.util.Log;

import com.honestwalker.android.spring.core.annotation.Autowired;
import com.honestwalker.android.spring.core.annotation.Component;
import com.honestwalker.android.spring.core.bean.Scope;
import com.honestwalker.android.spring.core.bean.SpringBean;
import com.honestwalker.android.spring.core.exception.BeanInjectError;
import com.honestwalker.android.spring.core.utils.PckScanner;
import com.honestwalker.androidutils.Application;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.StringUtil;
import com.honestwalker.androidutils.exception.ExceptionUtil;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lanzhe on 17-8-31.
 */
public class ApplicationContextUtils {

    private static Context appContext;

//    private static ApplicationContext applicationContext;

    private static InputStream applicationContextIn;

    private static List<String> scanPck = new ArrayList<>();

    public static void init(Context context, InputStream applicationContextIn) {
        ApplicationContextUtils.applicationContextIn = applicationContextIn;
        ApplicationContextUtils.appContext = context;
        ApplicationContext.putSingletonBeanMapping("applicationContext", context);
        try {
            readConfig(applicationContextIn);
        } catch (Exception e) {
            ExceptionUtil.showException("Spring", e);
            throw new BeanInjectError("解析application context 失败。");
        }
    }

    public static void init(Context context, int applicationContextResId) {
        ApplicationContextUtils.applicationContextIn = context.getResources().openRawResource(applicationContextResId);
        ApplicationContextUtils.appContext = context;

        ApplicationContext.putSingletonBeanMapping("applicationContext", context);
        try {
            readConfig(applicationContextIn);
        } catch (Exception e) {
            ExceptionUtil.showException("Spring", e);
            throw new BeanInjectError("解析application context 失败。");
        }
    }

    /**
     * 非注入对象需要调用inject，才能让该对象下的注入对象读取到Bean
     * 注入的对象自动会扫描该类下的注入对象并赋值
     * @param object
     */
    public static void inject(Object object) {

        Class currentClass = object.getClass();

        while(Object.class != currentClass) {

            Field[] fields = currentClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    if(field.get(object) != null) continue; // 已经赋值的跳过，当通过getBean获取的对象，Autowired会自动赋值，无需重新赋值
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    if(autowired == null) continue;
                    Class fieldType = field.getType();
                    Log.d("Spring", "fieldType=" + fieldType);
                    Object instance;
                    if(autowired.value() == null || "".equals(autowired.value().trim())) {
                        instance = ApplicationContext.getBean(field.getName());
                    } else {
                        instance = ApplicationContext.getBean(autowired.value());
                    }
                    if(instance == null) {
                        instance = fieldType.newInstance();
                        LogCat.d("Spring", " -- " + instance);
                    }

                    inject(instance);

                    field.set(object, instance);
                } catch (Exception e) {}
            }

            currentClass = currentClass.getSuperclass();

            LogCat.d("SCAN", "supper = " + currentClass);


        }

    }

//    public static ApplicationContext getApplicationContext() {
//        if(applicationContext == null) {
//            applicationContext = new ApplicationContext();
//        } else {
//            return applicationContext;
//        }
//        try {
//            readConfig(applicationContextIn);
//        } catch (Exception e) {
//            ExceptionUtil.showException("Spring", e);
//            throw new BeanInjectError("解析application context 失败。");
//        }
//        return applicationContext;
//    }

    private final static String NODE_COMPONENT_SCAN = "component-scan";
    private final static String NODE_BEAN = "bean";
    private final static String NODE_PROPERTY = "property";

    private final static String ATTR_BASE_PACKAGE = "base-package";

    private static void readConfig(InputStream applicationContextIn) throws JDOMException, IOException {
        SAXBuilder sb = new SAXBuilder();
        Document doc = sb.build(applicationContextIn);//读入指定文件
        Element root = doc.getRootElement();//获得根节点
        List<Element> list = root.getChildren();//将根节点下的所有子节点放入List中
        for (Element element : list) {
            String nodeName = element.getName();
            if(NODE_COMPONENT_SCAN.equals(nodeName)) {
                componentScanNode(element);
            } else if(NODE_BEAN.equals(nodeName)) {
                beanNode(element);
            }
        }
        for (String pck : scanPck) {
            scan(pck);
        }
    }

    private static void componentScanNode(Element element) {
        String basePackage = element.getAttribute(ATTR_BASE_PACKAGE).getValue();
        addScanPck(basePackage);
    }

    private static void beanNode(Element element) {
        Attribute scapeAttr = element.getAttribute("scope");
        String scope = scapeAttr == null ? Scope.prototype.toString() : scapeAttr.getValue();
        String classPath = element.getAttribute("class").getValue();
        String name = null;
        if(element.getAttribute("name") != null && !"".equals(element.getAttribute("name").getValue().trim())) {
            name = element.getAttribute("name").getValue().trim();
        }
        if(name == null) {
            name = getBeanNameByClass(classPath);
        }
        SpringBean springBean = new SpringBean();
        springBean.setClassPath(classPath);
        springBean.setScope(Scope.singleton.toString().equals(scope) ? Scope.singleton : Scope.prototype);

        List<Element> propertyList = element.getChildren();
        if(propertyList != null && propertyList.size() > 0) {
            LogCat.d("Spring", "bean=====" + name);
            for (int i = 0; i < propertyList.size(); i++) {
                Element propertyElement = propertyList.get(i);
                if(!NODE_PROPERTY.equals(propertyElement.getName())) continue;
                String propertyName = propertyElement.getAttribute("name").getValue();
                if(hasAttribute(propertyElement, "value")) {
                    String propertyValue = propertyElement.getAttribute("value").getValue();
                    springBean.getFieldType().put(propertyName, FieldType.value);
                    LogCat.d("Spring" , "value=======" + propertyValue);
                    springBean.getFieldValueMapping().put(propertyName, propertyValue);
                } else if(hasAttribute(propertyElement, "ref")) {
                    String propertyRef = propertyElement.getAttribute("ref").getValue();
                    springBean.getFieldType().put(propertyName, FieldType.ref);
                    LogCat.d("Spring" , "ref=======" + propertyRef);
                    springBean.getFieldValueMapping().put(propertyName, propertyRef);
                }
            }
        } else {
            LogCat.d("Spring", name + " 不包含 property");
        }

        putSingleInstanceBean(name, springBean);
    }

    private static boolean hasAttribute(Element element, String attributeName) {
        try {
            Object value = element.getAttribute(attributeName);
            return value != null;
        } catch (Exception e) {
            return false;
        }
    }

    private static void addScanPck(String pck) {
        if(scanPck.contains(pck)) return;
        LogCat.d("SPRING", "添加 " + pck + " 的扫描");
        scanPck.add(pck);
    }

    private static void putSingleInstanceBean(String beanName, SpringBean springBean) {
//        if(ApplicationContext.containsBean(beanName)) {
//            LogCat.e("SPRING", beanName + " 重复的bean name");
//            throw new BeanInjectError("Bean name \"" + beanName + "\" has exists.");
//        }
        LogCat.d("SPRING", "添加bean : " + beanName + "  " + springBean.getClassPath());
        ApplicationContext.putBeanMapping(beanName, springBean);
    }

    /**
     * 首字母小写
     * @param classPath
     * @return
     */
    private static String getBeanNameByClass(String classPath) {
        String name = "";
        if(classPath.indexOf(".") > -1) {
            name = classPath.substring(classPath.lastIndexOf(".") + 1);
        } else {
            name = classPath;
        }
        name = name.substring(0, 1).toLowerCase() + name.substring(1);
        return name;
    }

    private static void scan(String pck) {
        List<String> classes = PckScanner.getClassName(appContext, pck, true);
        for (String aClassName : classes) {
            try {
                Class aClass = Class.forName(aClassName);
                Component component = (Component) aClass.getAnnotation(Component.class);
                String name =  component == null ? getBeanNameByClass(aClass.getName()) : component.value();
                if(StringUtil.isEmptyOrNull(name)) {
                    name = getBeanNameByClass(aClass.getName());
                }
                SpringBean springBean = new SpringBean();
                springBean.setScope(component == null ? Scope.prototype : component.scope());
                springBean.setClassPath(aClass.getName());

                if(!ApplicationContext.containsBean(name)) {
                    putSingleInstanceBean(name, springBean);
                }

            } catch (Exception e){
                ExceptionUtil.showException(e);
                throw new BeanInjectError("解析 " + aClassName + " 错误");
            }
        }
    }

}
