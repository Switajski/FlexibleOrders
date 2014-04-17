package de.switajski.priebes.flexibleorders.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.springframework.format.annotation.DateTimeFormat;

import de.switajski.priebes.flexibleorders.json.JsonDateDeserializer;
import de.switajski.priebes.flexibleorders.json.JsonDateSerializer;

@JsonAutoDetect
@MappedSuperclass
public abstract class GenericEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@NotNull
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(style = "M-")
	protected Date created = new Date();

	@Version
	@Column(name = "version")
	private Integer version;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@JsonSerialize(using = JsonDateSerializer.class)
	public Date getCreated() {
		return this.created;
	}

	@JsonDeserialize(using = JsonDateDeserializer.class)
	public void setCreated(Date created) {
		this.created = created;
	}

	// TODO: implement equals method - suggested from
	// http://notesonjava.wordpress.com/2008/11/03/managing-the-bidirectional-relationship/
	// public boolean equals(Object object) {
	// if (object == this)
	// return true;
	// if ((object == null) || !(object instanceof A))
	// return false;
	//
	// final A a = (A)object;
	//
	// if (id != null && a.getId() != null) {
	// return id.equals(a.getId());
	// }
	// return false;
	// }
}
