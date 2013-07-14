/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oracletrace;

import java.sql.*;
import oracle.jdbc.driver.*;
import java.util.*;

public class OracleTrace {

    private static String url;
    final private static String server = "localhost";
    final private static String port = "1521";
    final private static String sid = "XE";
    final private static String username = "system";
    final private static String password = "656932";

    public static void main(String[] args) throws ClassNotFoundException {

        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection connection = null;
        Locale.setDefault(Locale.ENGLISH);
        SessionTrace trace = new SessionTrace();

        try {
            // create a database connection
            url = "jdbc:oracle:thin:@" + server + ":" + port + ":" + sid;
            connection = DriverManager.getConnection(url, username, password);
            trace.turnOnSessionTrace(connection);
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("select * from emp");
            while (rs.next()) {
                System.out.println("Number = " + rs.getString("empno"));
                System.out.println("Name = " + rs.getString("ename"));
            }
            trace.turnOffSessionTrace(connection);
        } catch (SQLException e) {
            // if the error message is "out of memory", 
            // it probably means no database file is found
            System.err.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // connection close failed.
                System.err.println(e);
            }
        }
    }
}

class SessionTrace {

    private int serialNum;
    private int sessionId;
    private String pathTraceFile;

    void turnOnSessionTrace(Connection conn) throws SQLException {
        /*  String sql =
         "SELECT dbms_debug_jdwp.current_session_id sid, "
         + "dbms_debug_jdwp.current_session_serial serial#  "
         + "from dual";
         */
        String sql =
                "SELECT s.sid, s.serial#, "
                + "pa.value || '/' || LOWER(SYS_CONTEXT('userenv','instance_name')) || '_ora_' || p.spid || '.trc' AS trace_file "
                + "FROM   v$session s, v$process p, v$parameter pa "
                + "WHERE  pa.name = 'user_dump_dest' AND s.paddr = p.addr "
                + "AND s.audsid = SYS_CONTEXT('USERENV', 'SESSIONID')";
        String turnOnSQL =
                "begin DBMS_MONITOR.SESSION_TRACE_ENABLE (session_id => ?, "
                + "serial_num => ?, waits => TRUE, binds => TRUE); end;";

        Statement stmt = conn.createStatement();
        ResultSet rset = stmt.executeQuery(sql);
        rset.next();
        sessionId = rset.getInt(1);
        serialNum = rset.getInt(2);
        pathTraceFile = rset.getString(3);

        System.out.println(pathTraceFile);

        System.out.println(
                String.format("Session Details : session_id=%s, serial_num=%s",
                sessionId, serialNum));

        OracleCallableStatement csmt = (OracleCallableStatement) conn.prepareCall(turnOnSQL);
        csmt.setInt(1, sessionId);
        csmt.setInt(2, serialNum);
        csmt.execute();

        stmt.close();
        rset.close();
        csmt.close();
    }

    void turnOffSessionTrace(Connection conn) throws SQLException {
        String turnOffSQL =
                "begin DBMS_MONITOR.SESSION_TRACE_DISABLE (session_id => ?, serial_num => ?); end;";

        OracleCallableStatement csmt = (OracleCallableStatement) conn.prepareCall(turnOffSQL);
        csmt.setInt(1, sessionId);
        csmt.setInt(2, serialNum);
        csmt.execute();
    }
}
