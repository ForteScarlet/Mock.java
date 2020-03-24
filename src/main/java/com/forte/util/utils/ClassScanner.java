package com.forte.util.utils;


import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * 扫描包下路径
 * 包括本地文件和jar包文件
 *
 * @author ljb
 */
public class ClassScanner {

    /**
     * 储存结果的Set集合
     */
    private Set<Class<?>> eleStrategySet = new HashSet<>();

    /**
     * 默认使用的类加载器
     */
    private ClassLoader classLoader;

    /**
     * 构造，使用当前类所在的加载器
     */
    public ClassScanner() {
        //默认使用的类加载器
        this.classLoader = ClassScanner.class.getClassLoader();
    }

    /**
     * 构造，提供一个类加载器
     *
     * @param classLoader 类加载器
     */
    public ClassScanner(ClassLoader classLoader) {
        Objects.requireNonNull(classLoader);
        this.classLoader = classLoader;
    }

    /**
     * 根据过滤规则查询
     *
     * @param classFilter class过滤规则
     * @throws ClassNotFoundException
     */
    public ClassScanner find(String packageName, Predicate<Class<?>> classFilter) throws ClassNotFoundException, IOException, URISyntaxException {
        eleStrategySet.addAll(addClass(packageName, classFilter));
        return this;
    }

    /**
     * 查询全部
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws URISyntaxException
     */
    public ClassScanner find(String packageName) throws ClassNotFoundException, IOException, URISyntaxException {
        eleStrategySet.addAll(addClass(packageName, c -> true));
        return this;
    }

    /**
     * 获取包下所有实现了superStrategy的类并加入list
     *
     * @param classFilter class过滤器
     */
    private Set<Class<?>> addClass(String packageName, Predicate<Class<?>> classFilter) throws ClassNotFoundException, URISyntaxException, IOException {
        final String path = packageName.replace(".", "/");
        URL url = classLoader.getResource(path);
        //如果路径为null，抛出异常
        if (url == null) {
            throw new RuntimeException("package url not exists: " + packageName);
        }

        //路径字符串
        String protocol = url.getProtocol();
        //如果是文件类型，使用文件扫描
        if ("file".equals(protocol)) {
            // 本地自己可见的代码
            return findClassLocal(packageName, classFilter);
            //如果是jar包类型，使用jar包扫描
        } else if ("jar".equals(protocol)) {
            // 引用jar包的代码
            return findClassJar(packageName, classFilter);
        }
        return Collections.emptySet();
    }

    /**
     * 本地查找
     */
    private Set<Class<?>> findClassLocal(final String packName, final Predicate<Class<?>> classFilter) throws URISyntaxException {
        Set<Class<?>> set = new HashSet<>();
        URI url = classLoader.getResource(packName.replace(".", "/")).toURI();

        File file = new File(url);
        final File[] files = file.listFiles();
        if (files != null) {
            for (File chiFile : files) {
                if (chiFile.isDirectory()) {
                    //如果是文件夹，递归扫描
                    set.addAll(findClassLocal(packName + "." + chiFile.getName(), classFilter));
                }
                if (chiFile.getName().endsWith(".class")) {
                    Class<?> clazz = null;
                    try {
                        clazz = classLoader.loadClass(packName + "." + chiFile.getName().replace(".class", ""));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    if (clazz != null && classFilter.test(clazz)) {
                        set.add(clazz);
                    }
                }
            }
        }

        return set;
    }

    /**
     * jar包查找
     */
    private Set<Class<?>> findClassJar(final String packName, final Predicate<Class<?>> classFilter) throws ClassNotFoundException, IOException {
        Set<Class<?>> set = new HashSet<>();
        String pathName = packName.replace(".", "/");
        JarFile jarFile;
        URL url = classLoader.getResource(pathName);
        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
        jarFile = jarURLConnection.getJarFile();

        Enumeration<JarEntry> jarEntries = jarFile.entries();
        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();
            String jarEntryName = jarEntry.getName();

            if (jarEntryName.contains(pathName) && !jarEntryName.equals(pathName + "/")) {
                //递归遍历子目录
                if (jarEntry.isDirectory()) {
                    String clazzName = jarEntry.getName().replace("/", ".");
                    int endIndex = clazzName.lastIndexOf(".");
                    String prefix = null;
                    if (endIndex > 0) {
                        prefix = clazzName.substring(0, endIndex);
                    }
                    set.addAll(findClassJar(prefix, classFilter));
                }
                if (jarEntry.getName().endsWith(".class")) {
                    Class<?> clazz = null;
                    clazz = classLoader.loadClass(jarEntry.getName().replace("/", ".").replace(".class", ""));
                    //判断，如果符合，添加
                    if (clazz != null && classFilter.test(clazz)) {
                        set.add(clazz);
                    }
                }
            }
        }
        return set;
    }

    /**
     * 获取当前扫描到的结果
     */
    public Set<Class<?>> get() {
        return new HashSet<>(this.eleStrategySet);
    }

    /**
     * 清空当前扫描结果集
     */
    public void clear(){
        this.eleStrategySet.clear();
    }

}