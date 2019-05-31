package io.github.jhipster.application.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Device.
 */
@Entity
@Table(name = "device")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "device")
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "device")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<DeviceAttribute> attributes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("devices")
    private Device device;

    @OneToMany(mappedBy = "device")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Device> parents = new HashSet<>();

    @OneToOne(mappedBy = "device")
    @JsonIgnore
    private DeviceType type;

    @ManyToMany(mappedBy = "devices")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<DeviceGroup> groups = new HashSet<>();

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

    public Device name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<DeviceAttribute> getAttributes() {
        return attributes;
    }

    public Device attributes(Set<DeviceAttribute> deviceAttributes) {
        this.attributes = deviceAttributes;
        return this;
    }

    public Device addAttributes(DeviceAttribute deviceAttribute) {
        this.attributes.add(deviceAttribute);
        deviceAttribute.setDevice(this);
        return this;
    }

    public Device removeAttributes(DeviceAttribute deviceAttribute) {
        this.attributes.remove(deviceAttribute);
        deviceAttribute.setDevice(null);
        return this;
    }

    public void setAttributes(Set<DeviceAttribute> deviceAttributes) {
        this.attributes = deviceAttributes;
    }

    public Device getDevice() {
        return device;
    }

    public Device device(Device device) {
        this.device = device;
        return this;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Set<Device> getParents() {
        return parents;
    }

    public Device parents(Set<Device> devices) {
        this.parents = devices;
        return this;
    }

    public Device addParent(Device device) {
        this.parents.add(device);
        device.setDevice(this);
        return this;
    }

    public Device removeParent(Device device) {
        this.parents.remove(device);
        device.setDevice(null);
        return this;
    }

    public void setParents(Set<Device> devices) {
        this.parents = devices;
    }

    public DeviceType getType() {
        return type;
    }

    public Device type(DeviceType deviceType) {
        this.type = deviceType;
        return this;
    }

    public void setType(DeviceType deviceType) {
        this.type = deviceType;
    }

    public Set<DeviceGroup> getGroups() {
        return groups;
    }

    public Device groups(Set<DeviceGroup> deviceGroups) {
        this.groups = deviceGroups;
        return this;
    }

    public Device addGroups(DeviceGroup deviceGroup) {
        this.groups.add(deviceGroup);
        deviceGroup.getDevices().add(this);
        return this;
    }

    public Device removeGroups(DeviceGroup deviceGroup) {
        this.groups.remove(deviceGroup);
        deviceGroup.getDevices().remove(this);
        return this;
    }

    public void setGroups(Set<DeviceGroup> deviceGroups) {
        this.groups = deviceGroups;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Device)) {
            return false;
        }
        return id != null && id.equals(((Device) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Device{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
