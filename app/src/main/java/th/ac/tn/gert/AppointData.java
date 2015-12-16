package th.ac.tn.gert;

/**
 * Created by danupon on 16/12/2558.
 */
public class AppointData {
    private String _ap_id, _ap_personal_id, _ap_date, _ap_time, _ap_subject,_ap_detail,_admin_name;
    private int _id;

    public AppointData(int id, String ap_id, String ap_personal_id, String ap_date, String ap_time, String ap_subject,String ap_detail,String admin_name)
    {
        _id = id;
        _ap_id = ap_id;
        _ap_personal_id = ap_personal_id;
        _ap_date = ap_date;
        _ap_time = ap_time;
        _ap_subject = ap_subject;
        _ap_detail = ap_detail;
        _admin_name = admin_name;
    }

    public int getId() { return _id; }
    public String getApId() { return _ap_id; }
    public String getApPersonalId() { return _ap_personal_id; }
    public String getApDate() { return _ap_date; }
    public String getAptime() { return _ap_time; }
    public String getApSubject() { return _ap_subject; }
    public String getApDetail() { return _ap_detail; }
    public String getAdminName() { return _admin_name; }
}
