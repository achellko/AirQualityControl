package app;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Wojciech Sankowski
 *
 * entity class used for adding new measurements to database
 */
@Entity
@Table(name = MeasurementNamingConfig.MEASUREMENT_TABLE_NAME)
public class Measurement {

    /**
     * id of measurement
     */
    @Id
    @Column(name = MeasurementNamingConfig.ID_COLUMN)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * foreign key for components
     */
    @Column(name = MeasurementNamingConfig.COMPONENT_ID_COLUMN)
    private String componentId;

    /**
     * foreign key for districts
     */
    @Column(name = MeasurementNamingConfig.DISTRICT_ID_COLUMN)
    private String districtId;

    /**
     * date of measurement
     * format yyyy-MM-dd HH:mm:ss
     */
    @Column(name = MeasurementNamingConfig.DATETIME_COLUMN)
    @Temporal(TemporalType.TIMESTAMP)
    private Date datetime;

    /**
     * stores value of measurement
     */
    @Column(name = MeasurementNamingConfig.VALUE_COLUMN)
    private double value;

    public Measurement(Component component) {
    }

    public Measurement(Component component, District district, Date datetime, double value) {
        this.componentId = component.toString();
        this.districtId = district.toString();
        this.datetime = datetime;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Measurement{" +
                "id=" + id +
                ", componentId='" + componentId + '\'' +
                ", districtId='" + districtId + '\'' +
                ", datetime=" + datetime +
                ", value=" + value +
                '}';
    }
}