package com.honestwalker.android.spring.context;


import android.content.Context;

import com.honestwalker.android.fastroid.R;
import com.honestwalker.android.spring.core.annotation.Autowired;
import com.honestwalker.android.spring.core.annotation.Component;
import com.honestwalker.android.spring.core.annotation.LazyInit;
import com.honestwalker.android.spring.core.annotation.Scope;
import com.honestwalker.android.spring.core.bean.ConstructorArg;
import com.honestwalker.android.spring.core.bean.PckBean;
import com.honestwalker.android.spring.core.bean.ScopeType;
import com.honestwalker.android.spring.core.bean.SpringBean;
import com.honestwalker.android.spring.core.exception.BeanInjectError;
import com.honestwalker.android.spring.core.utils.PckScanner;
import com.honestwalker.android.spring.exception.BeanRepeatException;
import com.honestwalker.androidutils.IO.LogCat;
import com.honestwalker.androidutils.IO.ObjectStreamIO;
import com.honestwalker.androidutils.StringUtil;
import com.honestwalker.androidutils.exception.ExceptionUtil;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lanzhe on 17-8-31.
 */
public class ApplicationContextUtils {

    private static String TAG = "Spring";

    private static Context appContext;

    private static ApplicationContext applicationContext;

    private static List<PckBean> scanPck = new ArrayList<>();

    public static void init(Context context, InputStream applicationContextIn) {
        applicationContext = new AndroidApplicationContext();
        ApplicationContextUtils.appContext = context;
        try {
            applicationContext.putSingletonBeanMapping("applicationContext", context);
        } catch (BeanRepeatException e) {
            ExceptionUtil.showException("Spring", e);
        }

        long start = System.currentTimeMillis();

//        AndroidBeanFactory androidBeanFactory = (AndroidBeanFactory) applicationContext.getBeanFactory();
//        boolean existApplicationContextObject = ObjectStreamIO.existsObjectStream(context.getCacheDir().getPath(), "ApplicationContext");
//        if(existApplicationContextObject) {
//            LogCat.d("Spring", "==============spring 已经缓存=============");
//            try {
//                HashMap<String, SpringBean> springBeans = (HashMap<String, SpringBean>) ObjectStreamIO.input(context.getCacheDir().getPath(), "ApplicationContext");
//                for (String beanId : springBeans.keySet()) {
//                    SpringBean springBean = springBeans.get(beanId);
//                    LogCat.d("Spring", beanId + " = " + springBean.getClassPath());
//                }
//                androidBeanFactory.setSpringBeans(springBeans);
//            } catch (Exception e) {
//                ExceptionUtil.showException("Spring", e);
//            }
//        } else {
//
//
//        }

        InputStream basicApplicationContextIn = context.getResources().openRawResource(R.raw.basic_application_context);
        try {
            LogCat.d("Spring", "读取Basic Application Context");
            readConfig(basicApplicationContextIn);
            LogCat.d("Spring", "读取App Application Context");
            readConfig(applicationContextIn);
//            HashMap<String, SpringBean> springBeanHashMap = androidBeanFactory.getSpringBeans();
//            ObjectStreamIO.output(context.getCacheDir().getPath(), springBeanHashMap, "ApplicationContext");
        } catch (Exception e) {
            ExceptionUtil.showException("Spring", e);
            throw new BeanInjectError("解析application context 失败。");
        }

        LogCat.d("Spring", "Spring 处理完毕，耗时：" + (System.currentTimeMillis() - start) + " 毫秒");

    }

    public static void init(Context context, int applicationContextResId) {
        InputStream applicationContextIn = context.getResources().openRawResource(applicationContextResId);
        init(context, applicationContextIn);
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
                    Object instance;
                    if(autowired.value() == null || "".equals(autowired.value().trim())) {
                        instance = applicationContext.getBean(field.getName());
                    } else {
                        instance = applicationContext.getBean(autowired.value());
                    }
                    if(instance == null) {
                        instance = fieldType.newInstance();
                    }
                    inject(instance);
                    field.set(object, instance);
                } catch (Exception e) {}
            }
            currentClass = currentClass.getSuperclass();
        }

    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    private final static String NODE_COMPONENT_SCAN = "component-scan";
    private final static String NODE_BEAN = "bean";
    private final static String NODE_PROPERTY = "property";
    private final static String NODE_CONSTRUCTOR_ARG = "constructor-arg";
    private final static String ATTR_BASE_PACKAGE = "base-package";
    private final static String ATTR_LAZY_INIT = "lazy-init";
    private final static String ATTR_SCOPE = "scope";

    private static void readConfig(InputStream applicationContextIn) throws JDOMException, IOException {
        LogCat.d("Spring", "--------------readConfig");
        SAXBuilder sb = new SAXBuilder();
        Document doc = sb.build(applicationContextIn);//读入指定文件
        Element root = doc.getRootElement();//获得根节点
        List<Element> list = root.getChildren();//将根节点下的所有子节点放入List中
        for (Element element : list) {
            String nodeName = element.getName();
            if(NODE_COMPONENT_SCAN.equals(nodeName)) {
                LogCat.d(TAG, "包节点 " + element.getAttributeValue(ATTR_BASE_PACKAGE));
                componentScanNode(element);
            } else if(NODE_BEAN.equals(nodeName)) {
                LogCat.d(TAG, "bean节点 " + element.getAttributeValue("class"));
                beanNode(element);
            }
        }
        for (PckBean pck : scanPck) {
            scan(pck);
        }
    }

    private static void componentScanNode(Element element) {
        PckBean pckBean = new PckBean();
        String basePackage = element.getAttribute(ATTR_BASE_PACKAGE).getValue();
        pckBean.setPck(basePackage);
        addScanPck(pckBean);
    }

    private static void beanNode(Element element) {
        Attribute scapeAttr = element.getAttribute(ATTR_SCOPE);
        Attribute lazyInitAttr = element.getAttribute(ATTR_LAZY_INIT);

        String xmlScopeValue = scapeAttr == null ?
                               ScopeType.prototype.toString() :
                               scapeAttr.getValue();

        String classPath = element.getAttribute("class").getValue();
        String id = null;
        if(element.getAttribute("id") != null &&
                !"".equals(element.getAttribute("id").getValue().trim())) {
            id = element.getAttribute("id").getValue().trim();
        }
        if(id == null) {
            id = getBeanIdByClass(classPath);
        }
        SpringBean springBean = new SpringBean();
        springBean.setClassPath(classPath);
        // bean 可以通过xml或者注解设置 scope， 以注解优先， 同时存在注解的scope生效
        ScopeType mScopeType = null;
        try {
            Class beanClass = Class.forName(classPath);
            Scope annoScopeValue = (Scope) beanClass.getAnnotation(Scope.class);
            if(annoScopeValue != null) {
                mScopeType = annoScopeValue.value();
            }

            boolean lazyInit = true;
            LazyInit lazyInitAnno = (LazyInit) beanClass.getAnnotation(LazyInit.class);
            if(lazyInitAnno != null) {
                lazyInit = lazyInitAnno.value();
            } else if(lazyInitAttr != null) {
                try {
                    lazyInit = lazyInitAttr.getBooleanValue();
                } catch (Exception e) {}
            }
            springBean.setLazyInit(lazyInit);

        } catch (Exception e) {
        }

        if(mScopeType == null) {
            mScopeType = ScopeType.singleton.toString().equals(xmlScopeValue) ?
                         ScopeType.singleton :
                         ScopeType.prototype;
        }
        springBean.setScope(mScopeType);

        List<Element> propertyList = element.getChildren();
        if(propertyList != null && propertyList.size() > 0) {
            LogCat.d("Spring", "bean=====" + id);
            for (int i = 0; i < propertyList.size(); i++) {
                Element propertyElement = propertyList.get(i);

                if(NODE_PROPERTY.equals(propertyElement.getName())) {
                    String propertyName = propertyElement.getAttribute("id").getValue();
                    if(hasAttribute(propertyElement, "value")) {
                        String propertyValue = propertyElement.getAttribute("value").getValue();
                        springBean.getFieldType().put(propertyName, FieldType.value);
                        LogCat.d("Spring" , "   设置属性 " + propertyName + "========" + propertyValue);
                        springBean.getFieldValueMapping().put(propertyName, propertyValue);
                    } else if(hasAttribute(propertyElement, "ref")) {
                        String propertyRef = propertyElement.getAttribute("ref").getValue();
                        springBean.getFieldType().put(propertyName, FieldType.ref);
                        LogCat.d("Spring" , "   设置属性 " + propertyName + " ref =======" + propertyRef);
                        springBean.getFieldValueMapping().put(propertyName, propertyRef);
                    }
                } else if(NODE_CONSTRUCTOR_ARG.equals(propertyElement.getName())) {
                    if(hasAttribute(propertyElement, "value")) {
                        String propertyValue = propertyElement.getAttribute("value").getValue();
                        String propertyType = propertyElement.getAttribute("type").getValue();
                        ConstructorArg constructorArg;
                        try {
                            constructorArg = new ConstructorArg(propertyValue, Class.forName(propertyType), FieldType.value);
                        } catch (Exception e) {
                            constructorArg = new ConstructorArg(propertyValue, Object.class, FieldType.value);
                        }
                        springBean.getConstructorArgMapping().put(i + "", constructorArg);
                    } else if(hasAttribute(propertyElement, "ref")) {
                        String propertyRef = propertyElement.getAttribute("ref").getValue();
                        ConstructorArg constructorArg = new ConstructorArg(propertyRef, null, FieldType.ref);
                        springBean.getConstructorArgMapping().put(i + "", constructorArg);
                    }
                }
            }
        } else {
            LogCat.d("Spring", id + " 不包含 property");
        }

        LogCat.d("Spring", id + " springBean.isLazyInit() " + springBean.isLazyInit());
        if(!springBean.isLazyInit()) {
            try {
                Object bean = Class.forName(springBean.getClassPath());
                applicationContext.putSingletonBeanMapping(id, bean);
                LogCat.d("Spring", "添加bean : " + id + "  " + springBean.getClassPath() + " bean=" + bean);
                inject(bean);
            } catch (ClassNotFoundException e) {
                ExceptionUtil.showException("Spring", e);
            } catch (BeanRepeatException e) {
                ExceptionUtil.showException("Spring", e);
            }
        } else {
            LogCat.d("Spring", "添加bean : " + id + "  " + springBean.getClassPath());
            try {
                applicationContext.putBeanMapping(id, springBean);
            } catch (BeanRepeatException e) {
                ExceptionUtil.showException("Spring", e);
            }
        }
    }

    private static boolean hasAttribute(Element element, String attributeName) {
        try {
            Object value = element.getAttribute(attributeName);
            return value != null;
        } catch (Exception e) {
            return false;
        }
    }

    private static void addScanPck(PckBean pck) {
        if(scanPck.contains(pck)) return;
        LogCat.d(TAG, "添加 " + pck + " 的扫描");
        scanPck.add(pck);
    }

    /**
     * 首字母小写
     * @param classPath
     * @return
     */
    private static String getBeanIdByClass(String classPath) {
        String id = "";
        if(classPath.indexOf(".") > -1) {
            id = classPath.substring(classPath.lastIndexOf(".") + 1);
        } else {
            id = classPath;
        }
        id = id.substring(0, 1).toLowerCase() + id.substring(1);
        return id;
    }

    /**
     * 扫描注入包下的bean
     * @param pck
     */
    private static void scan(PckBean pck) {
        LogCat.d("Spring", "扫描 " + pck.getPck());
        List<String> classes = PckScanner.getClassName(appContext, pck.getPck());
        for (String aClassName : classes) {
            try {
                Class aClass = Class.forName(aClassName);
                Component component = (Component) aClass.getAnnotation(Component.class);
                Scope scope = (Scope) aClass.getAnnotation(Scope.class);
                LazyInit lazyInit = (LazyInit) aClass.getAnnotation(LazyInit.class);
                String id =  component == null ? getBeanIdByClass(aClass.getName()) : component.value();
                if(StringUtil.isEmptyOrNull(id)) {
                    id = getBeanIdByClass(aClass.getName());
                }
                SpringBean springBean = new SpringBean();
                springBean.setScope(scope == null ? ScopeType.prototype : scope.value());
                springBean.setClassPath(aClass.getName());
                springBean.setLazyInit(lazyInit == null ? true : lazyInit.value());

                if(!springBean.isLazyInit()) { // 非懒加载，启动app就实例化，作为单例存放
                    springBean.setScope(ScopeType.singleton);

                    Object bean = aClass.newInstance();

                    inject(bean);
                    applicationContext.putSingletonBeanMapping(id, bean);
                } else {
                    applicationContext.putBeanMapping(id, springBean);
                }

            } catch (BeanRepeatException e){
                ExceptionUtil.showException("Spring", e);
                ExceptionUtil.showException(e);
            } catch (Exception e){
                throw new BeanInjectError(e.getMessage() + "\r\n解析 " + aClassName);
            }
        }
    }

}
