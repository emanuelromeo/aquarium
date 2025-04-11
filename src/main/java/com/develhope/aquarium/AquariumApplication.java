package com.develhope.aquarium;

import com.develhope.aquarium.services.AquariumService;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
public class AquariumApplication implements ApplicationRunner {

	@Autowired
	private AquariumService aquariumService;

	public static void main(String[] args) {
		SpringApplication.run(AquariumApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {

		TimerTask updateStats = new TimerTask() {
			public void run() {
				aquariumService.updateStats();
			}
		};

		TimerTask updateFishesAge = new TimerTask() {
			public void run() {
				aquariumService.updateFishesAge();
			}
		};

		Timer updateTimer = new Timer("Stats Timer");
		Timer agingTimer = new Timer("Aging Timer");

		// Update stats any minute
		long updateDelay = 1000 * 60;
		long updatePeriod = 1000 * 60;
		updateTimer.scheduleAtFixedRate(updateStats, updateDelay, updatePeriod);

		// Update age any day
		long agingDelay = 1000 * 60 * 60 * 24;
		long agingPeriod = 1000 * 60 * 60 * 24;
		agingTimer.scheduleAtFixedRate(updateFishesAge, agingDelay, agingPeriod);
	}
}
