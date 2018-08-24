package org.icddrb.dhis.android.sdk.utils.support.expression;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.Validate;
import org.icddrb.dhis.android.sdk.persistence.models.DataElement;

public class Expression implements Serializable {
    public static final String EXP_CLOSE = "}";
    public static final String EXP_OPEN = "#{";
    public static final String PAR_CLOSE = ")";
    public static final String PAR_OPEN = "(";
    public static final String SEPARATOR = ".";
    private Set<DataElement> dataElementsInExpression = new HashSet();
    private String description;
    private transient String explodedExpression;
    private String expression;
    private int id;
    private MissingValueStrategy missingValueStrategy = MissingValueStrategy.SKIP_IF_ALL_VALUES_MISSING;

    public Expression(String expression, String description, Set<DataElement> dataElementsInExpression) {
        this.expression = expression;
        this.description = description;
        this.dataElementsInExpression = dataElementsInExpression;
    }

    public String getExplodedExpressionFallback() {
        return this.explodedExpression != null ? this.explodedExpression : this.expression;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Expression other = (Expression) obj;
        if (this.description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!this.description.equals(other.description)) {
            return false;
        }
        if (this.expression == null) {
            if (other.expression != null) {
                return false;
            }
            return true;
        } else if (this.expression.equals(other.expression)) {
            return true;
        } else {
            return false;
        }
    }

    public int hashCode() {
        int i = 0;
        int hashCode = ((this.description == null ? 0 : this.description.hashCode()) + 31) * 31;
        if (this.expression != null) {
            i = this.expression.hashCode();
        }
        return hashCode + i;
    }

    public String toString() {
        return "Expression{id=" + this.id + ", expression='" + this.expression + '\'' + ", explodedExpression='" + this.explodedExpression + '\'' + ", description='" + this.description + '\'' + ", dataElementsInExpression=" + this.dataElementsInExpression.size() + '}';
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExpression() {
        return this.expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public Set<DataElement> getDataElementsInExpression() {
        return this.dataElementsInExpression;
    }

    public void setDataElementsInExpression(Set<DataElement> dataElementsInExpression) {
        this.dataElementsInExpression = dataElementsInExpression;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MissingValueStrategy getMissingValueStrategy() {
        return this.missingValueStrategy;
    }

    public void setMissingValueStrategy(MissingValueStrategy missingValueStrategy) {
        this.missingValueStrategy = missingValueStrategy;
    }

    public String getExplodedExpression() {
        return this.explodedExpression;
    }

    public void setExplodedExpression(String explodedExpression) {
        this.explodedExpression = explodedExpression;
    }

    public void mergeWith(Expression other) {
        Set set;
        Validate.notNull(other);
        this.expression = other.getExpression() == null ? this.expression : other.getExpression();
        this.description = other.getDescription() == null ? this.description : other.getDescription();
        this.missingValueStrategy = other.getMissingValueStrategy() == null ? this.missingValueStrategy : other.getMissingValueStrategy();
        if (other.getDataElementsInExpression() == null) {
            set = this.dataElementsInExpression;
        } else {
            set = new HashSet(other.getDataElementsInExpression());
        }
        this.dataElementsInExpression = set;
    }
}
