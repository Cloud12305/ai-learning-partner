package com.digitalhorsepower.learnbuddy.vo;

import com.digitalhorsepower.learnbuddy.entity.AcademicRecord;
import com.digitalhorsepower.learnbuddy.entity.LabAttendance;
import com.digitalhorsepower.learnbuddy.entity.LibraryRecord;
import com.digitalhorsepower.learnbuddy.entity.User;
import lombok.Data;
import java.util.List;

@Data
public class AcademicProfileVO {
    private User userInfo;
    private AcademicStatsVO academicStats;
    private List<AcademicRecord> recentCourses;
    private List<LabAttendance> recentLabs;
    private List<LibraryRecord> recentBooks;
}