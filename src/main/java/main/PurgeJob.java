package main;

import lombok.SneakyThrows;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.util.ArrayList;

public class PurgeJob implements Job {

	private ArrayList<String> blockList = FileReader.getPrograms().get();

	@SneakyThrows
	public void execute(JobExecutionContext context) {
		FileReader.getPrograms().onChange(s -> blockList = s);
		final ArrayList<String> processList = IO.listProcesses();
		blockList.retainAll(processList);
		blockList.forEach(IO::killProcess);
	}
}
