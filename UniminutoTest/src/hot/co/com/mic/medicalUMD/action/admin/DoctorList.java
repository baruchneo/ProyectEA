package co.com.mic.medicalUMD.action.admin;

import co.com.mic.medicalUMD.modelo.Doctor;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityQuery;

import java.util.Arrays;
import java.util.List;

@Name("doctorList")
public class DoctorList extends EntityQuery<Doctor>
{

    //---------------------------------------------------------------//
    // Class constants
    //---------------------------------------------------------------//

    /**
     * Default serial version Id
     */
    private static final long serialVersionUID = 1L;

    private static final String EJBQL = "select doctor from Doctor doctor";

    private static final String[] RESTRICTIONS =
            {
                    "lower(doctor.nombreDoctor) like lower(concat('%', concat(#{doctorList.doctor.nombreDoctor},'%')))",
                    "doctor.id = #{doctorList.doctor.id} ",
                    "lower(doctor.apellidoDoctor) like lower(concat('%', concat(#{doctorList.doctor.apellidoDoctor},'%')))",
                    "lower(doctor.cedulaDoctor) like lower(concat('%', concat(#{doctorList.doctor.cedulaDoctor},'%')))",
            };


    //---------------------------------------------------------------//
    // Class attributes
    //---------------------------------------------------------------//

    private Doctor doctor = new Doctor();

    //---------------------------------------------------------------//
    // Class constructors methods
    //---------------------------------------------------------------//

    public DoctorList()
    {
        setOrderColumn("doctor.id");
        setOrderDirection("asc");
        setEjbql(EJBQL);
        setRestrictionExpressionStrings(Arrays.asList(RESTRICTIONS));
        //setMaxResults(25);
    }

    //---------------------------------------------------------------//
    // Class getters methods
    //---------------------------------------------------------------//

    public Doctor getDoctor()
    {
        return doctor;
    }

    //---------------------------------------------------------------//
    // Class setters methods
    //---------------------------------------------------------------//

    /**
     * @param doctor the doctor to set
     */
    public void setDoctor(Doctor doctor)
    {
        this.doctor = doctor;
    }

    //---------------------------------------------------------------//
    // Class business methods
    //---------------------------------------------------------------//

    @Override
    public List<Doctor> getResultList()
    {
        return super.getResultList();
    }
}
