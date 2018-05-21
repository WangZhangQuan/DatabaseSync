package com.wzq.test;

import net.minidev.json.JSONValue;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class MemSqlTest {

    private static final String NAME = "root";
    private static final String PASS = "";
    private static final String MY_PASS = "root";

    private static final String MEM_URL = "jdbc:mysql://192.168.56.128:3306/test";
    private static final String MY_URL = "jdbc:mysql://192.168.56.128:3306/test?useUnicode=true&characterEncoding=utf8";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    Connection connection = null;

    @Before
    public void b() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        // memsql
        connection = DriverManager.getConnection(MEM_URL, NAME, PASS);
        // mysql
//        connection = DriverManager.getConnection(MY_URL, NAME, MY_PASS);
    }

    @Test
    public void t1() throws SQLException {

        PreparedStatement ps = connection.prepareStatement("INSERT INTO person(age, name, money, score)VALUES(?, ?, ?, ?)");
        String[] names = new String[] {"张", "王", "李", "冰", "鱼", "林", "零", "雨", "浩"};

        Random random = new Random(System.currentTimeMillis());

        StringBuilder stringBuilder = new StringBuilder();
        connection.setAutoCommit(false);
        long s = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            ps.setInt(1, (i + random.nextInt(30)) % (random.nextInt(100) + 1));
            stringBuilder.append(names[(i + random.nextInt(30)) % (random.nextInt(8) + 1)])
                    .append(names[(i + random.nextInt(30)) % (random.nextInt(8) + 1)])
                    .append(names[(i + random.nextInt(30)) % (random.nextInt(8) + 1)]);
            ps.setString(2, stringBuilder.toString());
            ps.setFloat(3, (i + random.nextInt(30)) / (random.nextInt(30) + 1));
            ps.setDouble(4 , (i + random.nextInt(30)) / (random.nextInt(30) + 1));
            stringBuilder.delete(0, 3);
            ps.execute();
            ps.addBatch();
            if (i % 10000 == 0) {
                ps.executeBatch();
            }
        }
        ps.executeBatch();
        connection.commit();
        long e = System.currentTimeMillis();
        ps.close();
        connection.close();

        // memsql 批量插入 驱动6 耗时58105，驱动8 耗时58479
        // memsql 单次插入 驱动6 耗时60949，驱动8 耗时59985

        // mysql 批量插入 驱动8 耗时55123
        // mysql 单次插入 驱动8 耗时314476

        System.out.println("耗时: " + (e - s));

    }

    @Test
    public void t2() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM person");
        ResultSet resultSet = ps.executeQuery();

//        ArrayList<Person> persons = new ArrayList<Person>();

        int i = 0;
        long s = System.currentTimeMillis();
        while(resultSet.next()) {
            Person person = new Person();
            person.setId(resultSet.getInt("id"));
            person.setAge(resultSet.getInt("age"));
            person.setName(resultSet.getString("name"));
            person.setMoney(resultSet.getFloat("money"));
            person.setScore(resultSet.getDouble("score"));
//            persons.add(person);
            ++i;
        }
        long e = System.currentTimeMillis();
        ps.close();
        connection.close();

//        for (Person person : persons) {
////            System.out.println(JSONValue.toJSONString(person));
//        }

        // memsql 驱动8 耗时732毫秒
        // myql 驱动8 耗时548毫秒

        System.out.println("耗时: " + (e - s));
        System.out.println("查询条数: " + i);

    }

    @Test
    public void t3() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM person WHERE name LIKE ?");
        ps.setString(1, "%冰%");

//        ArrayList<Person> persons = new ArrayList<Person>();

        int i = 0;
        long s = System.currentTimeMillis();
        ResultSet resultSet = ps.executeQuery();
        while(resultSet.next()) {
            Person person = new Person();
            person.setId(resultSet.getInt("id"));
            person.setAge(resultSet.getInt("age"));
            person.setName(resultSet.getString("name"));
            person.setMoney(resultSet.getFloat("money"));
            person.setScore(resultSet.getDouble("score"));
//            persons.add(person);
            ++i;
        }
        long e = System.currentTimeMillis();
        ps.close();
        connection.close();

//        for (Person person : persons) {
////            System.out.println(JSONValue.toJSONString(person));
//        }

        // memsql 驱动8 耗时186毫秒
        // mysql 驱动8 耗时170毫秒

        System.out.println("耗时: " + (e - s));
        System.out.println("查询条数: " + i);

    }

    @Test
    public void t4() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM person WHERE name LIKE ?");

//        ArrayList<Person> persons = new ArrayList<Person>();
        String[] names = new String[] {"%张%", "%王%", "%李%", "%冰%", "%鱼%", "%林%", "%零%", "%雨%", "%浩%"};

        Random random = new Random(System.currentTimeMillis());

        int i = 0;
        long s = System.currentTimeMillis();
        for (int j = 0; j < 10; j++) {
            ps.setString(1, names[random.nextInt(9)]);
            ResultSet resultSet = ps.executeQuery();
            while(resultSet.next()) {
                Person person = new Person();
                person.setId(resultSet.getInt("id"));
                person.setAge(resultSet.getInt("age"));
                person.setName(resultSet.getString("name"));
                person.setMoney(resultSet.getFloat("money"));
                person.setScore(resultSet.getDouble("score"));
//            persons.add(person);
                ++i;
            }
        }
        long e = System.currentTimeMillis();
        ps.close();
        connection.close();

//        for (Person person : persons) {
////            System.out.println(JSONValue.toJSONString(person));
//        }

        // memsql 驱动8 耗时未测
        // mysql 驱动8 耗时4807

        System.out.println("耗时: " + (e - s));
        System.out.println("查询条数: " + i);

    }
}