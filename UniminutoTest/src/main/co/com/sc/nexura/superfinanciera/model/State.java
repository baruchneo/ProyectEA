package co.com.sc.nexura.superfinanciera.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jboss.seam.annotations.Name;

/**
 * State business object. 
 * 
 * @author Alex Vicente ChacOn JimEnez (alex.chacon@software-colombia.com)
 * @since JDK 1.7.0_04
 * @version 1.0
 */
@Entity
@Table(name = "Estado")
@Name("state")
public class State implements Serializable
{
	//---------------------------------------------------------------//
	// Class constants
	//---------------------------------------------------------------//

	/**
	 * Default serial version Id.
	 */
	private static final long serialVersionUID = 1L;
	
	//---------------------------------------------------------------//
	// Class attributes
	//---------------------------------------------------------------//
	
	/**
	 * Unique state identifier
	 */
	private Long id;
	
	/**
	 * State name
	 */
	private String name;
	
	//---------------------------------------------------------------//
	// Class getters methods
	//---------------------------------------------------------------//

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(generator = "SeqEstado")
	@SequenceGenerator(name = "SeqEstado", sequenceName = "SeqEstado")
	@Column(name = "Id")
	public Long getId()
	{
		return id;
	}
	
	/**
	 * @return the name
	 */
	@Column(name = "Nombre", unique = false, nullable = false, length = 200)
	@NotNull
	@Size(min = 2, max = 200)
	public String getName()
	{
		return name;
	}
	
	//---------------------------------------------------------------//
	// Class setters methods
	//---------------------------------------------------------------//
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id)
	{
		this.id = id;
	}
}