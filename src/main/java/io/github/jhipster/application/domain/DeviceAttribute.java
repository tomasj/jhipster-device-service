package io.github.jhipster.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.Objects;

import io.github.jhipster.application.domain.enumeration.DeviceAttributeType;

/**
 * A DeviceAttribute.
 */
@Entity
@Table(name = "device_attribute")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "deviceattribute")
public class DeviceAttribute implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private DeviceAttributeType type;

    @Column(name = "jhi_value")
    private String value;

    @ManyToOne
    @JsonIgnoreProperties("deviceAttributes")
    private Device device;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public DeviceAttribute name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeviceAttributeType getType() {
        return type;
    }

    public DeviceAttribute type(DeviceAttributeType type) {
        this.type = type;
        return this;
    }

    public void setType(DeviceAttributeType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public DeviceAttribute value(String value) {
        this.value = value;
        return this;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Device getDevice() {
        return device;
    }

    public DeviceAttribute device(Device device) {
        this.device = device;
        return this;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceAttribute)) {
            return false;
        }
        return id != null && id.equals(((DeviceAttribute) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DeviceAttribute{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", value='" + getValue() + "'" +
            "}";
    }
}
