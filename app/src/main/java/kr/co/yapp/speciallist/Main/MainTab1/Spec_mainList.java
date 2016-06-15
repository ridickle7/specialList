package kr.co.yapp.speciallist.Main.MainTab1;

/**
 * Created by home on 2016-06-15.
 */
public class Spec_mainList {
    String spec_id;                       // 스펙 아이디
    String spec_detail_id;               // 스펙 세부 아이디
    String spec_panbyel;                 // 시험까지인지, 시험등록까지인지 확인해주는 플래그
    int deadLine;                        // 해당 플래그까지 남은 날짜
    String spec_name;                    // 스펙이름
    String spec_flag;                    // 필기, 실기
    String spec_year;                    // 스펙 년도
    String spec_year_number;            // 스펙 회차
    String spec_assign_start_date;     // 신청 시작기간
    String spec_assign_end_date;       // 신청 마감기간
    String spec_hour_second;            // 시험등록, 시험결과 확인인 경우 시간을 보여준다.

    public String getSpec_id() {
        return spec_id;
    }

    public void setSpec_id(String spec_id) {
        this.spec_id = spec_id;
    }

    public String getSpec_detail_id() {
        return spec_detail_id;
    }

    public void setSpec_detail_id(String spec_detail_id) {
        this.spec_detail_id = spec_detail_id;
    }

    public String getSpec_panbyel() {
        return spec_panbyel;
    }

    public void setSpec_panbyel(String spec_panbyel) {
        this.spec_panbyel = spec_panbyel;
    }

    public int getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(int deadLine) {
        this.deadLine = deadLine;
    }

    public String getSpec_name() {
        return spec_name;
    }

    public void setSpec_name(String spec_name) {
        this.spec_name = spec_name;
    }

    public String getSpec_flag() {
        return spec_flag;
    }

    public void setSpec_flag(String spec_flag) {
        this.spec_flag = spec_flag;
    }

    public String getSpec_assign_start_date() {
        return spec_assign_start_date;
    }

    public void setSpec_assign_start_date(String setSpec_assign_start_date) {
        this.spec_assign_start_date = setSpec_assign_start_date;
    }

    public String getSpec_assign_end_date() {
        return spec_assign_end_date;
    }

    public void setSpec_assign_end_date(String setSpec_assign_end_date) {
        this.spec_assign_end_date = setSpec_assign_end_date;
    }

    public String getSpec_hour_second() {
        return spec_hour_second;
    }

    public void setSpec_hour_second(String spec_hour_second) {
        this.spec_hour_second = spec_hour_second;
    }

    public String getSpec_year() {
        return spec_year;
    }

    public void setSpec_year(String spec_year) {
        this.spec_year = spec_year;
    }

    public String getSpec_year_number() {
        return spec_year_number;
    }

    public void setSpec_year_number(String spec_year_number) {
        this.spec_year_number = spec_year_number;
    }
}

