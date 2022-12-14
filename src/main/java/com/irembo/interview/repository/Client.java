package com.irembo.interview.repository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "t_client")
@Table
public class Client {

    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`username`")
    private String username;

    @Column(name = "name")
    private String name;

    @Column(name = "tpm", nullable = false)
    private Long tpm;

    @Column(name = "tps", nullable = false)
    private Long tps;

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }

    public Client(){

    }

    public Client(Long id, String username, String name, Long tpm, Long tps) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.tpm = tpm;
        this.tps = tps;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTpm() {
        return tpm;
    }

    public void setTpm(Long monthlyLimit) {
        this.tpm = monthlyLimit;
    }

    public Long getTps() {
        return tps;
    }

    public void setTps(Long perSecondLimit) {
        this.tps = perSecondLimit;
    }
}
