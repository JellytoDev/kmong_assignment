package com.example.kmong_assignment.dummy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.transaction.Transactional;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.stream.IntStream;

@Getter
@Setter
@RequiredArgsConstructor
@Transactional
public class Dummy {
    /*
    // 1. db 연결 확인
    // 2. dummy_create
    // 2.1 pk 있는지 확인
    // 2.2 pk 및 column 가져오기
    // 2.3 column 돌면서 for 문
    // 2.4 조건 체크 후 데이터 생성
    // 2.5 fk 속성이 있을시에 해당 테이블 참조한 후 해당 키값으로부터 데이터 생성 (onetomany, manytoone)
    // 2.6 데이터 삽입
    // 3. 더미데이터만 삭제
     * unique : 아예 랜덤값으로 세팅
     * seq : 순서값 세팅
     * date : 날짜값 세팅
     * content : 랜덤 긴 문자열 세팅
     * range : 특정 범위 내의 값 세팅
     * fix : 고정값 세팅
     * str : dummy + seq값
     * choice : 주어진 값들 중 랜덤으로 세팅
     * */

    private String driver;
    private String url;
    private String user;
    private String password;
    private String schema;

    public Dummy(String driver, String url,String schema, String user, String password) {
        this.driver = driver;
        this.url = url;
        this.schema = schema;
        this.user = user;
        this.password = password;
    }


    // 테이블 정보 생성자로 가져오기
    // Map < column name, opt string >

    public void createDummy(Map<String, String> opt, String tableName, int dummySize) {
        Connection con = null;
        //ResultSet rs = null;
        PreparedStatement psmt = null;

        try {

            // db 연결 설정
            String url = this.url;
            String user = this.user;
            String password = this.password;
            String driver = this.driver;
            String schema = this.schema;

            if (driver == "h2") {
                // h2 연결
                System.out.println("h2 connect");
                Class.forName("org.h2.Driver");
            } else if (driver == "mariadb") {
                // mariadb 연결
                System.out.println("mariadb connect");
                Class.forName("org.mariadb.jdbc.Driver");
            }

            System.out.println("DB 연결 시도중");

            try {

                // db 연결
                con = DriverManager.getConnection(url, user, password);
                DatabaseMetaData dbmd = con.getMetaData();
                //String tableName = "BOARD";


                // 테이블 존재하는지 확인
                ResultSet tableRs = isTableExist(dbmd, tableName);

                if (!tableRs.next()) throw new SQLException();

                // 칼럼 정보 가져오기
                // (1) PK
                ResultSet pkRs = getPK(dbmd,schema, tableName);

                String PK = null;
                String FK = null;
                List<Map<String, String>> columns = new ArrayList<>();

                while (pkRs.next()) {
                    PK = pkRs.getString("COLUMN_NAME");
                }

                // (2) FK
                ResultSet fkRs = getFK(dbmd,schema, tableName);
                while (fkRs.next()) {
                    FK = fkRs.getString("FKCOLUMN_NAME");

                    //System.out.println("FK = " + fkRs.getString("FKCOLUMN_NAME"));
                }

                String sqlInit = "PUBLIC." + tableName + "(";

                // (3) Column
                ResultSet columnRs = getColumn(dbmd, tableName);
                while (columnRs.next()) {
                    Map column = new HashMap();
                    String name = columnRs.getString("COLUMN_NAME");
                    //&& name != FK
                    if (!PK.equals(name)) {
                        //System.out.println("name = " + name+" "+PK +(PK.equals(name)));
                        column.put("COLUMN_NAME", name);
                        sqlInit += name + ",";
                        String type = columnRs.getString("TYPE_NAME");
                        column.put("TYPE_NAME", type);
                        columns.add(column);
                    }
                }
                sqlInit = sqlInit.replaceFirst(".$", "");
                sqlInit += ")";
                //System.out.println("sqlInit = " + sqlInit);

                HashMap<String, ArrayList<Object>> datas = new HashMap<>();

                //column 더미 데이터 세팅
                for (Map<String, String> column : columns) {
                    String optValue = opt.getOrDefault(column.get("COLUMN_NAME"), "str");

                    ArrayList<Object> dmData = new ArrayList<>();

                    // options 검출
                    if (optValue.contains("date")) {
                        System.out.println("date option 실행 : "+driver);


                        for (int i = 0; i < dummySize; i++) {
                            String data = "";
                            if (driver.equals("h2")) {
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String myDate = df.format(new Date());
                                data = "to_timestamp('" + myDate + "', 'yyyy-mm-dd hh24:mi:ss')";
                            } else if (driver.equals("mariadb")) {
                                SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
                                String myDate = df.format(new Date());
                                data = "timestamp('" + myDate + "')";
                            }
                            System.out.println("data = " + data);
                            dmData.add(data);
                        }

                    } else if (optValue.contains("str")) {

                        for (int i = 0; i < dummySize; i++) {
                            String data = "'dummy" + i + "'";
                            dmData.add(data);
                        }

                    } else if (optValue.contains("content")) {

                        for (int i = 0; i < dummySize; i++) {
                            String data = "' " + i + " this is dummy content. What are you dreaming of? and What are you doing to achieve that dream?" + i + "'";
                            dmData.add(data);
                        }

                    } else if (optValue.contains("unique")) {
                        for (int i = 0; i < dummySize; i++) {
                            String data = "'" + UUID.randomUUID() + "'";
                            dmData.add(data);
                        }

                    } else if (optValue.contains("choice")) {
                        String[] opts = optValue.split(" ");
                        ArrayList<String> choiceKeywords = new ArrayList<>();

                        for (int i = 1; i < opts.length; i++) {
                            choiceKeywords.add(opts[i]);
                        }

                        for (int i = 0; i < dummySize; i++) {
                            int index = (int) (Math.random() * choiceKeywords.size());
                            Object data = "'" + choiceKeywords.get(index) + "'";
                            dmData.add(data);
                        }

                    } else if (optValue.contains("fix")) {
                        String[] opts = optValue.split(" ");

                        for (int i = 0; i < dummySize; i++) {
                            Object data = "'" + opts[1] + "'";
                            dmData.add(data);
                        }

                    } else if (optValue.contains("range")) {
                        String[] opts = optValue.split(" ");
                        int lt = Integer.valueOf(opts[1]);
                        int rt = Integer.valueOf(opts[2]);

                        ArrayList<Object> rangeValues = IntStream.range(lt, rt + 1).collect(ArrayList::new, List::add, List::addAll);

                        for (int i = 0; i < dummySize; i++) {
                            int index = (int) (Math.random() * rangeValues.size());
                            Object data = "'" + rangeValues.get(index) + "'";
                            dmData.add(data);
                        }


                    } else if (optValue.contains("seq")) {

                        ArrayList<Object> seqValues = IntStream.range(1, dummySize + 1).collect(ArrayList::new, List::add, List::addAll);

                        for (int i = 0; i < dummySize; i++) {
                            Object data = "'" + seqValues.get(i) + "'";
                            dmData.add(data);
                        }
                    } else if (optValue.contains("fk")) {
                        String[] opts = optValue.split(" ");
                        String fkTableName = opts[1];
                        String fkColumnName, fkType;

                        System.out.println("fkTableName = " + fkTableName);

                        DatabaseMetaData fk_dbmd = con.getMetaData();
                        ResultSet FK_tableRs = isTableExist(fk_dbmd, fkTableName);

                        if (!FK_tableRs.next()) throw new SQLException("FK 테이블이 존재하지 않습니다");

                        ResultSet FK_pkRs = getPK(fk_dbmd,schema, fkTableName);

                        String FK_PK = "";
                        while (FK_pkRs.next()) {
                            FK_PK = FK_pkRs.getString("COLUMN_NAME");
                            System.out.println("FK_pkRs = " + FK_PK);
                        }

                        switch (opts.length) {
                            case 3:
                                //fkColumnName = opts[2];
                                fkType = opts[2];
                                break;
                            default:
                                //fkColumnName = FK_PK;
                                fkType = "manytoone";
                                break;
                        }


                        Statement stmt = con.createStatement();
                        System.out.println("Statement객체 생성 성공");

                        // h2는 database 생략하면 기본 public database로 가는듯?
                        // mariadb의 경우는 무조건 database 이름을 테이블 앞에 적어줘야 함
                        ResultSet FK_PK_ResultSet = stmt.executeQuery("select " + FK_PK + " from PUBLIC." + fkTableName);
                        //
                        ArrayList<Integer> FK_PK_result = new ArrayList<>();
                        //int cnt=0;
                        while (FK_PK_ResultSet.next()) {
                            FK_PK_result.add(FK_PK_ResultSet.getInt(FK_PK));
                        }


                        if (fkType.equals("onetoone") && FK_PK_result.size() < dummySize) throw new SQLException();

                        for (int i = 0; i < dummySize; i++) {
                            int index = (int) (Math.random() * FK_PK_result.size());
                            Object data = FK_PK_result.get(index);
                            if (fkType.equals("onetoone")) {
                                FK_PK_result.remove(index);
                            }
                            dmData.add(data);
                        }

                    }

                    //onetoone queue 구조 사용할 것

                    datas.put(column.get("COLUMN_NAME"), dmData);
                }


                for (int i = 0; i < dummySize; i++) {
                    String sql = "INSERT INTO " + sqlInit + " VALUES ";
                    String values = "(";

                    for (Map<String, String> column : columns) {
                        System.out.println("column = " + column.get("COLUMN_NAME"));
                        System.out.println("datas.get(column.get(\"COLUMN_NAME\")) = " + datas.get(column.get("COLUMN_NAME")));
                        values += datas.get(column.get("COLUMN_NAME")).get(i) + ",";
                    }
                    values = values.replaceFirst(".$", "");
                    values += ")";

                    sql += values;

                    System.out.println("values = " + values);
                    System.out.println("sql = " + sql);

                    try {
                        Statement stmt = con.createStatement();
                        stmt.execute(sql);

                        System.out.println("쿼리성공");
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("쿼리실패 : ");
                    }
                }

                // pk auto increment, unique user, 아니면 일반인지 판별
                // 실제 테이블 foreign key 이름 판별

                System.out.println("columns = " + columns);
                System.out.println("PK = " + PK);
                System.out.println("FK = " + FK);


            } catch (SQLException e) {
                System.out.println("DB계정불일치");
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            System.out.println("DB연결실패");
            e.printStackTrace();
        }
    }

    public void deleteDummy(String table) throws SQLException {
        String url = this.url;
        String user = this.user;
        String password = this.password;
        String schema = this.schema;

        Connection con = DriverManager.getConnection(url, user, password);
        String sql = "DELETE FROM " + schema + "." + table;
        PreparedStatement ps = con.prepareStatement(sql);
        ps.execute();
    }

    private static ResultSet isTableExist(DatabaseMetaData dbmd, String tableName) throws SQLException {
        ResultSet rs = dbmd.getTables(null, "PUBLIC", tableName, null);

        return rs;
    }



    private static ResultSet getPK(DatabaseMetaData dbmd,String schema, String tableName) throws SQLException {
        ResultSet rs = dbmd.getPrimaryKeys(null, schema, tableName);

        return rs;
    }

    private static ResultSet getFK(DatabaseMetaData dbmd,String schema, String tableName) throws SQLException {
        ResultSet rs = dbmd.getImportedKeys(null, "PUBLIC", tableName);

        return rs;
    }

    private static ResultSet getBoardByCategory(Connection con) throws SQLException {
        //pstmt = con.prepareStatement("select * from Example where c_no = ? and c_name = ?");
        String sql = "select * from board where category_id = ?";
        PreparedStatement pstat = con.prepareStatement(sql);

        pstat.setString(1, "1");
        ResultSet rs = pstat.executeQuery();

        return rs;
    }

    private static ResultSet getColumn(DatabaseMetaData dbmd, String tableName) throws SQLException {
        ResultSet rs = dbmd.getColumns(null, "PUBLIC", tableName, null);

        //while(rs.next()) {
        //    String name = rs.getString("COLUMN_NAME");
        //    int position = rs.getInt("ORDINAL_POSITION");
        //    String type = rs.getString("TYPE_NAME");
        //    int length = rs.getInt("CHAR_OCTET_LENGTH");
        //    int precision = rs.getInt("COLUMN_SIZE");
        //    int scale = rs.getInt("DECIMAL_DIGITS");
        //    boolean nullable = rs.getInt("NULLABLE") == 0 ? false : true;
        //
        //    System.out.println("name = " + name);
        //}

        return rs;
    }

    public static void test() {

        Dummy dummy = new Dummy("h2", "jdbc:h2:tcp://localhost/~/dummy/test","PUBLIC", "sa", "1234");

        //Map<String, String> option = new HashMap<>();
        //option.put("BOARD_NAME", "str");
        //option.put("BOARD_DATE", "date");
        //option.put("CATEGORY_ID", "fk CATEGORY");
        ////option.put("BOARD_CONTENT", "content");
        //dummy.createDummy(option,"BOARD",20);

        //Map<String, String> option2 = new HashMap<>();
        //option2.put("APP_UNIQUE_ID", "unique");
        //option2.put("APP_CONTENT", "content");
        //option2.put("APP_ACTIVE", "choice active inactive");
        //option2.put("APP_STATUS", "fix public");
        //option2.put("APP_SPEED", "range 5 12");
        //option2.put("APP_SEQ", "seq");
        //
        //dummy.createDummy(option2,"APP",20);

        // mariadb 연결
        //org.mariadb.jdbc.Driver
        //Class.forName("org.mariadb.jdbc.Driver");
        //String url = "jdbc:mariadb://localhost";
        //String user = "season";
        //String password = "season123!@";
        //String driver = "mariadb";
        //String tableName = this.tableName;
        //System.out.println("DB정상연결");

        // h2 연결
        //String url = "jdbc:h2:tcp://localhost/~/dummy/test";
        //String user = "sa";
        //String password = "1234";
        //String driver = "h2";
        //Class.forName("org.h2.Driver");
        //System.out.println("DB정상연결");

        //getBoardByCategory(con);
        //getTables(dbmd);
        //getPK(dbmd);
        //getColumn(dbmd);
        //insertData(con);
        //crossRef(dbmd);
    }

}