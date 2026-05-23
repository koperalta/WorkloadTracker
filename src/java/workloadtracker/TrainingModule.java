package workloadtracker;

public class TrainingModule {
    private int moduleId;
    private int adminId;
    private String title;

    public TrainingModule() {}

    public int getModuleId() { return moduleId; }
    public void setModuleId(int moduleId) { this.moduleId = moduleId; }

    public int getAdminId() { return adminId; }
    public void setAdminId(int adminId) { this.adminId = adminId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}