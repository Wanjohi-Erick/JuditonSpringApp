package shule.one.entity.payroll;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "payrolls")
public class Payroll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "school", nullable = false)
    private Integer school;

    @Size(max = 20)
    @Column(name = "date", length = 20)
    private String date;

   
    public Payroll(Integer id, Integer school, String Date) {
        this.id = id;
        this.school = school;
        this.date = Date;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSchool() {
		return school;
	}

	public void setSchool(Integer school) {
		this.school = school;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String Date) {
		date = Date;
	}
	
	

}