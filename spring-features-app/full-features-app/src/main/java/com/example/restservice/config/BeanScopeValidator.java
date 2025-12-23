package com.example.restservice.config;

import com.example.restservice.service.PrototypeService;
import com.example.restservice.service.SingletonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @className: BeanScopeValidator
 * @author: geeker
 * @date: 11/16/25 4:15 PM
 * @Version: 1.0
 * @description:
 */
//@Component
public class BeanScopeValidator implements CommandLineRunner{

    // @Autowired
    private ApplicationContext applicationContext;


    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== Spring Bean 作用域验证 ===");

        validateSingletonScope();
        validatePrototypeScope();
        validateDifferentWays();
        checkBeanDefinition();
    }

    /**
     * 验证单例作用域
     */
    private void validateSingletonScope() {
        System.out.println("\n--- 验证单例作用域 ---");

        // 方式1：多次获取同一个 Bean
        SingletonService bean1 = applicationContext.getBean(SingletonService.class);
        SingletonService bean2 = applicationContext.getBean(SingletonService.class);
        SingletonService bean3 = applicationContext.getBean(SingletonService.class);

        System.out.println("Bean1: " + bean1.getInstanceInfo());
        System.out.println("Bean2: " + bean2.getInstanceInfo());
        System.out.println("Bean3: " + bean3.getInstanceInfo());

        // 验证是否是同一个实例
        System.out.println("bean1 == bean2: " + (bean1 == bean2));
        System.out.println("bean1 == bean3: " + (bean1 == bean3));
        System.out.println("单例验证: " + (bean1 == bean2 && bean2 == bean3));
    }

    /**
     * 验证原型作用域
     */
    private void validatePrototypeScope() {
        System.out.println("\n--- 验证原型作用域 ---");

        // 方式1：多次获取同一个 Bean
        PrototypeService bean1 = applicationContext.getBean(PrototypeService.class);
        PrototypeService bean2 = applicationContext.getBean(PrototypeService.class);
        PrototypeService bean3 = applicationContext.getBean(PrototypeService.class);

        System.out.println("Bean1: " + bean1.getInstanceInfo());
        System.out.println("Bean2: " + bean2.getInstanceInfo());
        System.out.println("Bean3: " + bean3.getInstanceInfo());

        // 验证是否是不同实例
        System.out.println("bean1 == bean2: " + (bean1 == bean2));
        System.out.println("bean1 == bean3: " + (bean1 == bean3));
        System.out.println("原型验证: " + (bean1 != bean2 && bean1 != bean3));
    }

    /**
     * 验证不同的获取方式
     */
    private void validateDifferentWays() {
        System.out.println("\n--- 验证不同的获取方式 ---");

        // 方式1：通过类型获取
        SingletonService byType1 = applicationContext.getBean(SingletonService.class);
        SingletonService byType2 = applicationContext.getBean(SingletonService.class);

        // 方式2：通过名称获取
        SingletonService byName1 = (SingletonService) applicationContext.getBean("singletonService");
        SingletonService byName2 = (SingletonService) applicationContext.getBean("singletonService");

        System.out.println("按类型获取 - 相同实例: " + (byType1 == byType2));
        System.out.println("按名称获取 - 相同实例: " + (byName1 == byName2));
        System.out.println("类型 vs 名称 - 相同实例: " + (byType1 == byName1));
    }

    /**
     * 检查 Bean 定义信息
     */
    private void checkBeanDefinition() {
        System.out.println("\n--- Bean 定义信息 ---");

        String[] beanNames = applicationContext.getBeanDefinitionNames();

        for (String beanName : beanNames) {
            if (beanName.contains("Service")) {
                try {
                    BeanDefinition beanDefinition = ((BeanDefinitionRegistry) applicationContext)
                            .getBeanDefinition(beanName);

                    String scope = beanDefinition.getScope();
                    if (scope.isEmpty()) {
                        scope = "singleton"; // 默认作用域
                    }

                    System.out.println("Bean: " + beanName + ", 作用域: " + scope);
                } catch (Exception e) {
                    // 某些 Bean 可能无法获取定义信息
                }
            }
        }
    }
}
