package com.lilong.javanewfeaturetest;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * java 8 新特性：Stream API{@link Stream}, android sdk >=24 才支持
 *
 * (1)只有执行了{@link Stream#collect(Collector)}方法后，才开始执行遍历操作，所以这个方法叫terminal方法
 * (2)其他方法比如{@link Stream#map(Function)}等，只是标记了遍历中要执行的动作，等待terminal方法启动遍历后执行，所以都叫intermediate方法
 * (3)intermediate方法中对原集合的影响，分为按值传递（原集合元素为基本类型）和按引用传递（原集合元素非基本类型）两种情况，前者对原集合无影响，后者有可能有影响
 * (4)terminal方法返回的集合跟原集合是不同的对象
 */

public class StreamTest {

    public static void main(String[] args) {

        // 不用流式编程，可以用foreach实现简单遍历
        // Iterable的foreach对象是函数式接口Consumer的实现对象，所以这里可以写成lambda表达式
        System.out.println("--------------------forEach-----------------------");
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, -6);
        numbers.forEach(number -> {
            number = number * number;
            System.out.println(number);
        });

        System.out.println("------------------forEach changes original data ? -----------------");
        // foreach方法遍历的集合元素是基本类型，则参数的lambda表达式中改变元素值，对原集合的元素无影响（按值传递）
        List<Integer> numbersOriginal = Arrays.asList(1, 2, 3, 4);
        numbersOriginal.forEach(number -> number = number * number);
        Util.display(numbersOriginal);

        // foreach方法遍历的集合元素是类的对象，则参数lambda表达式中改变对象属性，对原集合的元素有影响（按引用传递）
        List<Entity> entitiesOriginal = Arrays.asList(new Entity("a", 1), new Entity("a", 2), new Entity("a", 3));
        entitiesOriginal.forEach(element -> element.number = 8);
        Util.display(entitiesOriginal);

        System.out.println("-----------------stream + map changes original data ? -------------------");
        // map方法遍历的集合元素是基本类型，则参数lambda中改变元素值，对原集合的元素无影响（按值传递）
        List<Integer> numbersOriginal2 = Arrays.asList(1, 2, 3, 4);
        List<Integer> numbersMapped2 = numbersOriginal2.stream().map(number -> {number = number * number; return number;}).collect(Collectors.toList());
        Util.display(numbersOriginal2);
        Util.displayObj(numbersOriginal2, numbersMapped2);
        System.out.println(numbersOriginal2 == numbersMapped2);

        // map方法遍历的集合元素是类的对象，则参数lambda中改变对象属性，对原集合的元素有影响（按引用传递）
        List<Entity> entitiesOriginal2 = Arrays.asList(new Entity("a", 1), new Entity("a", 2), new Entity("a", 3));
        List<Entity> entitiesMapped2 = entitiesOriginal2.stream().map(element -> {element.number = 9; return element;}).collect(Collectors.toList());
        Util.display(entitiesOriginal2);
        Util.displayObj(entitiesOriginal2, entitiesMapped2);
        System.out.println(entitiesOriginal2 == entitiesMapped2);

        List<String> strings = Arrays.asList(null, "abc", "", "bc", "efg", "abcd", "", "jkl");
        System.out.println("--------------------stream + filter----------------");
        // 用流式编程
        // stream的filter方法参数是函数式接口Predicate的实现对象，所以这里可以写成lambda表达式
        // 删除空字符串和null
        List<String> filtered = strings.stream().filter(element -> element != null && !element.equals("")).collect(Collectors.toList());
        filtered.forEach(element -> System.out.println(element));

        System.out.println("--------------------stream + map---------------------");
        // 用流式编程
        // stream的map方法参数是函数式接口Function的实现对象，所以可写成lambda
        List<String> mapped = strings.stream().map(element -> element + "suffix").collect(Collectors.toList());
        mapped.forEach(element -> System.out.println(element));

        System.out.println("--------------------stream + limit--------------------");
        // 用流式编程
        // stream的limit方法可以截取流中的指定个数的前几个元素
        List<String> limited = strings.stream().limit(2).collect(Collectors.toList());
        limited.forEach(element -> System.out.println(element));

        System.out.println("-------------------stream + sorted--------------------");
        // 用流式编程
        // stream的sorted方法可以对流中元素进行排序，相当于调用string的compareTo方法，所以不能有null的
        List<String> sorted = strings.stream().filter(element -> element != null).sorted().collect(Collectors.toList());
        sorted.forEach(element -> System.out.println(element));

        System.out.println("-------------------stream + sorted(自定义comparator)");
        // 用流式编程，按字符串长度排序
        List<String> sortedBySelfDefinedComparator = strings.stream().filter(element -> element != null).sorted((element1, element2) -> element1.length() - element2.length()).collect(Collectors.toList());
        sortedBySelfDefinedComparator.forEach(element -> System.out.println(element));

        // 并行stream，与默认的串行stream对比速度
        // 并行stream是指将stream中的数据分割成几小块，每个小块的处理都由单独线程进行，最后将所有小块的结果合并起来
        // stream的默认的处理方式是串行，由一个线程处理
        // 并行和串行的速度不一定哪个更快，取决于数据源的分割/合并开销
        List<Integer> unorderedNumbers = Arrays.asList(3, 1, 2, 7, 4, 3, 90);
        System.out.println("------------------stream + sorted (sequential vs parallel)");
        long timeSortSequential = System.currentTimeMillis();
        List<Integer> sortedSequential = unorderedNumbers.stream().sorted().collect(Collectors.toList());
        System.out.println("sequential stream time : " + (System.currentTimeMillis() - timeSortSequential) + " ms");
        sortedSequential.forEach(element -> System.out.println(element));
        long timeSortParallel = System.currentTimeMillis();
        List<Integer> sortedParallel = unorderedNumbers.parallelStream().sorted().collect(Collectors.toList());
        System.out.println("parallel stream time : " + (System.currentTimeMillis() - timeSortParallel) + " ms");
        sortedParallel.forEach(element -> System.out.println(element));

        System.out.println("--------------------Collector test---------------------");
        // 按name对entities进行分类
        List<Entity> entities = Arrays.asList(new Entity("a", 1), new Entity("a", 2), new Entity("b", 1));
        Map<String, List<Entity>> collectedEntities = entities.stream().collect(Collectors.groupingBy(Entity :: getName));
        Util.display(collectedEntities);
    }

    static class Entity {

        public String name = "";
        public int number = 0;
        public Entity(String name, int number){
            this.name = name;
            this.number = number;
        }

        public String getName(){
            return name;
        }

        public int getNumber(){
            return number;
        }

        @Override
        public String toString() {
            return "entity [name = " + name + ", number = " + number + "]";
        }
    }
}
