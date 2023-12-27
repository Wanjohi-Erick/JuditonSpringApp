package shule.one.entity.payroll;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "earnings")
public class Allowances {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "school", nullable = false)
    private Integer school;

    @Size(max = 300)
    @Column(name = "Earning", length = 300)
    private String Earning;
    
    @NotNull
    @Column(name = "type", nullable = false, columnDefinition = "double default 0")
    private Integer type;
    
    @NotNull
    @Column(name = "costperunit", nullable = false, columnDefinition = "double default 0")
    private Double costperunit;

    
    @Size(max = 10)
    @Column(name = "visible", length = 10)
    private String visible;

   
    public Allowances(Integer id, Integer school, String Earning,Integer type,Double costperunit, String visible) {
        this.id = id;
        this.school = school;
        this.Earning = Earning;
        this.type = type;
        this.costperunit = costperunit;
        this.visible = visible;
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

	public String getEarning() {
		return Earning;
	}

	public void setEarning(String earning) {
		Earning = earning;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer Type) {
		this.type = Type;
	}
	
	public Double getCostperUnit() {
		return costperunit;
	}

	public void setCostperUnit(Double CostperUnit) {
		this.costperunit = CostperUnit;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String Visible) {
		visible = Visible;
	}

	
   

}