package kr.co.yapp.speciallist.Helper;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by home on 2016-06-28.
 */
// 혹시나 사용할까 하여 미리 만듦
public class DBHelper {

    private static final String DATABASE_NAME = "alarmList.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;


    //DB column(유저 날짜 파일)
    private static final String SPEC_TABLE = "SPEC";

    private static final String SPEC_ID = "sId";                            // 스펙 디테일 id
    private static final String SPEC_NAME = "name";                        // 스펙 이름
    private static final String SPEC_YEAR = "year";                        // 스펙 시험년도
    private static final String SPEC_YEAR_NUMBER = "year_number";       // 스펙 시험년도 회차
    private static final String SPEC_ASSIGN_START = "assign_start";     // 접수 시작일
    private static final String SPEC_ASSIGN_END = "assign_end";          // 접수 마감일
    private static final String SPEC_TEST = "test";                        // 시험일
    private static final String SPEC_TEST_RESULT = "test_result";       // 시험결과 발표일

    private class DatabaseHelper extends SQLiteOpenHelper {

        // 생성자
        public DatabaseHelper(Context context, String name,
                              CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // 최초 DB를 만들때 한번만 호출된다.
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE "+SPEC_TABLE+" ( "+SPEC_ID+" text primary key autoincrement, "+
                    SPEC_NAME+" text not null, "+SPEC_YEAR+" integer not null, " + SPEC_YEAR_NUMBER + " integer not null, "
                    + SPEC_ASSIGN_START + " text not null, " + SPEC_ASSIGN_END+" text not null, "+ SPEC_TEST+" text not null, "+ SPEC_TEST_RESULT + " text not null);");


        }

        // 버전이 업데이트 되었을 경우 DB를 다시 만들어 준다.
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + SPEC_TABLE);
            onCreate(db);
        }
    }

    public DBHelper(Context context) {
        this.mCtx = context;
    }

    public DBHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDB.close();
    }

}