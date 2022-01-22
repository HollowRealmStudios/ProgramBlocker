package main;

import lombok.SneakyThrows;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import javax.swing.*;
import java.util.Arrays;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

public class Main {

	public static final Display display = new Display();

	@SneakyThrows
	public static void main(String[] args) {
		Thread.setDefaultUncaughtExceptionHandler((t, e) ->
				JOptionPane.showMessageDialog(null, String.format("%s%n%s", e.getMessage(), Arrays.toString(e.getStackTrace())), "Message Blocker Error", JOptionPane.ERROR_MESSAGE));
		final Scheduler scheduler = new StdSchedulerFactory().getScheduler();
		FileReader.setupReader();
		rescheduleJob(scheduler, FileReader.getHours().get());
		FileReader.getHours().onChange(s -> rescheduleJob(scheduler, s));
		scheduler.start();
	}

	@SneakyThrows
	private static void rescheduleJob(Scheduler scheduler, String s) {
		scheduler.clear();
		final Trigger trigger = createTrigger(s);
		final JobDetail job = createJob();
		scheduler.scheduleJob(job, trigger);
	}

	@SneakyThrows
	private static Trigger createTrigger(String blockHours) {
		final CronExpression cronExpression = new CronExpression(String.format("0 * %s * * ?", blockHours));
		return newTrigger().withIdentity("trigger", "group").withSchedule(CronScheduleBuilder.cronSchedule(cronExpression)).build();
	}

	private static JobDetail createJob() {
		return newJob(PurgeJob.class).withIdentity("job1", "group1").build();
	}
}
