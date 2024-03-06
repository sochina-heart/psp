package com.sochina.test.pattern.singleton;

/**
 * 单例模式
 */
public class Singleton {

    /**
     * 懒汉式，线程不安全
     * 是否lazy初始化：是
     * 是否多线程安全：否
     */
    /*private static Singleton instance;

    private Singleton(){

    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }*/

    /**
     * 懒汉式，线程安全
     * 是否lazy初始化：是
     * 是否多线程安全：是
     * 具备很好的lazy loading，能够在多线程中很好工作，但工作效率很低，大多数情况下不需要同步
     */
    /*private static Singleton instance;

    private Singleton() {}

    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }*/

    /**
     * 饿汉式。线程安全
     * 是否lazy初始化：否
     * 是否多线程安全：是
     * 优点：在类加载的时候完成了实例化，避免了线程同步带来的问题
     * 缺点：因为在类加载的时候完成了实例化，因此，没有达到懒加载的效果，无论该实例是否会用到，但都会被加载
     */
    /*private static Singleton instance = new Singleton();

    private Singleton() {}

    public static Singleton getInstance() {
        return instance;
    }*/

   /* private static Singleton instance = null;

    private Singleton(){}

    static {
        instance = new Singleton();
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }*/

    /**
     * 双检锁/双重校验锁
     * 是否lazy初始化：是
     * 是否多线程安全：是
     */
    /*private volatile static Singleton instance;

    private Singleton () {}

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }*/

    /**
     * 内部类
     * 是否lazy初始化：是
     * 是否多线程安全：是
     * 对静态域使用延迟初始化，应使用这种方式，不使用双检锁的方式
     * 内部类和饿汉式都采用类加载机制保证实例初始化时，只有一个线程，避免多线程同步问题
     * singleton在类被加载时就会被实例化，而内部类，在被需要时，才需要通过调用getInstance方法，才能装载singletonDemo
     */
    /*private static class SingletonDemo {
        private static final Singleton INSTANCE = new Singleton();
    }

    private Singleton() {

    }

    public static final Singleton getInstance() {
        return SingletonDemo.INSTANCE;
    }*/
    private Singleton() {
    }

    static enum SingletonDemo2 {
        INSTANCE;

        private Singleton singleton;

        private SingletonDemo2() {
            singleton = new Singleton();
        }

        public Singleton getInstance() {
            return singleton;
        }
    }

    public static Singleton getInstance() {
        return SingletonDemo2.INSTANCE.getInstance();
    }
}
