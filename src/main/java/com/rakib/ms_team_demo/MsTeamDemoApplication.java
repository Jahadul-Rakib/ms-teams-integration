package com.rakib.ms_team_demo;

import com.rakib.ms_team_demo.teams.TeamApiIntegration;
import com.rakib.ms_team_demo.teams.TeamsHook;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MsTeamDemoApplication implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(MsTeamDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        TeamApiIntegration.sendMessage("Hello");
        TeamsHook.createWebHook("title", "message");

    }
}
