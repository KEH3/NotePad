package zut.edu.cn.notepad;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {
    private static String driver = "com.mysql.jdbc.Driver";//mysql驱动
    private static String url = "jdbc:mysql://10.0.2.2:3306/notepad";//mysql数据库连接url172.20.10.2   10.0.2.2
    private static String user = "local";
    private static String password = "123456";


            private static Connection getConnection () {
                Connection conn = null;
                try {
                    Class.forName(driver);
                    conn = DriverManager.getConnection(url, user, password);

                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
                return conn;
            }

            //插入数据
            public void insertNote (String title, String content, String datetime){

                Connection conn = getConnection();
                Statement st = null;
                try {
                    st = conn.createStatement();
                    String sql = "insert into note(title,content,datetime) values(?,?,?)";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, title);
                    ps.setString(2, content);
                    ps.setString(3, datetime);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (st != null) {
                            st.close();
                            conn.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }



            public void clearNote(){
                Connection conn = getConnection();
                Statement st = null;
                try {
                    st = conn.createStatement();
                    String sql = "truncate table note";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (st != null) {
                            st.close();
                            conn.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            }

        }