package com.findme.models;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Embeddable
public class RelationshipKey implements Serializable {
    private User userFrom;
    private User userTo;

    @OneToOne()
    @JoinColumn(name = "USER_FROM")
    public User getUserFrom() {
        return userFrom;
    }

    @OneToOne()
    @JoinColumn(name = "USER_TO")
    public User getUserTo() {
        return userTo;
    }

    public void setUserFrom(User userFrom) {
        this.userFrom = userFrom;
    }

    public void setUserTo(User userTo) {
        this.userTo = userTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RelationshipKey that = (RelationshipKey) o;

        if (userFrom != null ? !userFrom.equals(that.userFrom) : that.userFrom != null)
            return false;
        return userTo != null ? userTo.equals(that.userTo) : that.userTo == null;
    }

    @Override
    public int hashCode() {
        int result = userFrom != null ? userFrom.hashCode() : 0;
        result = 31 * result + (userTo != null ? userTo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "RelationshipKey{" +
                "userFrom=" + userFrom +
                ", userTo=" + userTo +
                '}';
    }
}
