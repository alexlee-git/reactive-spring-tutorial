package max.lab.rst.s01;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import max.lab.rst.domain.Book;
import max.lab.rst.domain.InMemoryDataSource;

public class C01FunctionalProgramming101 {

    // 返回一个包含每种类别中最贵的书的列表, 非函数式编程
    public static List<Book> getMostExpensiveBooksByCategory() {
        var map = new HashMap<String, Book>();
        // 获取所有的数据
        for (Book book : InMemoryDataSource.books) {
            // 根据类别获取数据,每个类别就保存一份
            var aBook = map.get(book.getCategory());
            // 如果该类别下面的书籍被找到了久比较后,存放更贵的那一本
            if (aBook != null) {
                if (book.getPrice().compareTo(aBook.getPrice()) > 0) {
                    map.put(book.getCategory(), book);
                }
            } else {
                // 如果没有在当前的类别中找到书籍,那么直接不用比较将本书放进去
                map.put(book.getCategory(), book);
            }
        }
        return (new ArrayList<>(map.values()));
    }

    // 返回一个包含每种类别中最贵的书的列表, 函数式编程
    // 使用Java8的StreamAPI
    public static List<Book> getMostExpensiveBooksByCategoryFunctional() {
        var map = Stream.of(InMemoryDataSource.books)
                // 通过book数组生成steam
                .collect(Collectors.groupingBy(Book::getCategory))
                // collect转换为集合或者聚合的元素,键为string,值为list
                // 使用book的getCategory进行分组,那么key就是category的返回值,value就是分好组的list

                //// .collect(Collectors.groupingByConcurrent(Book::getCategory)) // 通过修改一行代码就可以编程并行代码
                .entrySet()
                // 转换为set了,每个set的元素是一个hashmap

                .stream()
                // 将上面的hashmap的set转换为steam()
                //// .parallelStream() // 通过修改一行代码就可以编程并行代码

                // 遍历元素set中的每一个元素,将其转换为一个Stream(book)类型
                .map(e -> e.getValue()
                        // 拿到数组
                        .stream()
                        // 将数组转换为steam
                        //// .parallelStream() // 通过修改一行代码就可以编程并行代码
                        .sorted(Comparator.comparing(Book::getPrice).reversed())
                        // 将其排序
                        .findFirst()
                        // 那倒排序后最大的一个
                        .get())
                // 得到它
                .collect(Collectors.toList());
        // 将这个stream<Book>的set转换为list?
        // steam最重要转换为java类型

        System.out.println(map);
        return new ArrayList<>();
    }

    public static void main(String[] args) {
        var books1 = getMostExpensiveBooksByCategory();
        books1.stream().forEach(System.out::println);

        System.out.printf("%n%n");

        var books2 = getMostExpensiveBooksByCategoryFunctional();
        books2.stream().forEach(System.out::println);
    }

}