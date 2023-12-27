package shule.one.entity.payroll;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "deductions")
public class Deductions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "school", nullable = false)
    private Integer school;

    @Size(max = 300)
    @Column(name = "Deduction", length = 300)
    private String Deduction;
    
    @NotNull
    @Column(name = "type", nullable = false, columnDefinition = "double default 0")
    private Integer type;
    
    @NotNull
    @Column(name = "calculationtype", nullable = false, columnDefinition = "double default 0")
    private Integer calculationtype;
    
    @NotNull
    @Column(name = "costperunit", nullable = false, columnDefinition = "double default 0")
    private Double costperunit;

    @Size(max = 10)
    @Column(name = "visible", length = 10)
    private String visible;

   
    public Deductions(Integer id, Integer school, String Deduction,Integer type,Integer calculationtype,Double costperunit, String visible) {
        this.id = id;
        this.school = school;
        this.Deduction = Deduction;
        this.type = type;
        this.calculationtype = calculationtype;
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

	public String getDeduction() {
		return Deduction;
	}

	public void setDeduction(String deduction) {
		Deduction = deduction;
	}
	
	public Integer getType() {
		return type;
	}

	public void setType(Integer Type) {
		this.type = Type;
	}
	
	public Integer getCalculationType() {
		return calculationtype;
	}

	public void setCalculationType(Integer Type) {
		this.calculationtype = Type;
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