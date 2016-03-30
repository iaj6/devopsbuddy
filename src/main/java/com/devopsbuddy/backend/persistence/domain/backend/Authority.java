package com.devopsbuddy.backend.persistence.domain.backend;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by tedonema on 30/03/2016.
 */
public class Authority implements GrantedAuthority {

    private final String authority;


    public Authority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Authority authority1 = (Authority) o;

        return authority != null ? authority.equals(authority1.authority) : authority1.authority == null;

    }

    @Override
    public int hashCode() {
        return authority != null ? authority.hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Authority{");
        sb.append("authority='").append(authority).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
