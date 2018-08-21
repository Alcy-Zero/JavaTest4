package com.hand;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.Reader;

public class MainApp {
    private static SqlSessionFactory sqlSessionFactory;
    private static Reader reader;

    static {
        try {
            reader = Resources.getResourceAsReader("applicationContext-mybatis.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //throw new RuntimeException("Error!!!");

        String ip = System.getenv("MYSQL_IP");
        String port = System.getenv("MYSQL_PORT");
        String dbname = System.getenv("DBNAME");
        String username = System.getenv("USERNAME");
        String password = System.getenv("MYSQL_ROOT_PASSWORD");

        String title = System.getenv("TITLE");
        // title = "Moon";
        String description = System.getenv("DESCRIPTION");
        //String description = "A quiet movie";
        int language_id = Integer.parseInt(System.getenv("LANGUAGE_ID"));
        //int language_id = 1;

        //System.out.println("1.dbname:" + dbname);
        //System.out.println("2.username:" + username);
        //System.out.println("3.password:" + password);
        //System.out.println("4.ip:" + ip);
        //System.out.println("5.port:" + port);
        //System.out.println();

        ApplicationContext acx = null;
        SqlSession session = null;
        try {
            acx = new ClassPathXmlApplicationContext("applicationContext.xml");
            ((ClassPathXmlApplicationContext) acx).start();
            System.out.println("------Insert Film Data--------");

            session = sqlSessionFactory.openSession();
            Film film = new Film();
            film.setLanguage_id(language_id);
            film.setTitle(title);
            film.setDescription(description);
            System.out.println("Film Title:\n" + film.getTitle());
            System.out.println("Film Description:\n" + film.getDescription());
            System.out.println("Film LanguageId:\n" + film.getLanguage_id());
            int result = session.insert("com.hand.FilmMapper.insertFilm", film);
            session.commit();
            if (result > 0) {
                System.out.println("---------Insert Success....---------");
            } else {
                System.out.println("---------Insert Error....---------");
            }

            ((ClassPathXmlApplicationContext) acx).stop();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
