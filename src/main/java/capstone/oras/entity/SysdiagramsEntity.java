package capstone.oras.entity;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "sysdiagrams", schema = "dbo", catalog = "ORAS")
public class SysdiagramsEntity {
    private Object name;
    private Integer principalId;
    private Integer diagramId;
    private Integer version;
    private byte[] definition;

    @Basic
    @Column(name = "name")
    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    @Basic
    @Column(name = "principal_id")
    public Integer getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(Integer principalId) {
        this.principalId = principalId;
    }

    @Id
    @Column(name = "diagram_id")
    public Integer getDiagramId() {
        return diagramId;
    }

    public void setDiagramId(Integer diagramId) {
        this.diagramId = diagramId;
    }

    @Basic
    @Column(name = "version")
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Basic
    @Column(name = "definition")
    public byte[] getDefinition() {
        return definition;
    }

    public void setDefinition(byte[] definition) {
        this.definition = definition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SysdiagramsEntity that = (SysdiagramsEntity) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(principalId, that.principalId) &&
                Objects.equals(diagramId, that.diagramId) &&
                Objects.equals(version, that.version) &&
                Arrays.equals(definition, that.definition);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(name, principalId, diagramId, version);
        result = 31 * result + Arrays.hashCode(definition);
        return result;
    }
}
