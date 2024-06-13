package Classes;

import java.util.Date;

public class Questions {
    private int id ;
    private int owner ;
    private String title ;
    private String description ;
    private int[] approver ;
    private Date createdDate ;

    public Questions(){}
    public Questions(int owner, String title, String description) {
        this.owner = owner;
        this.title = title;
        this.description = description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setApprover(int[] approver) {
        this.approver = approver;
    }

    public int getId() {
        return id;
    }

    public int getOwner() {
        return owner;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int[] getApprover() {
        return approver;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
