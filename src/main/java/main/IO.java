package main;

import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class IO {

	@SneakyThrows
	public static void killProcess(String process) {
		Main.display.addLog(String.format("Killing process %s", process));
		Runtime.getRuntime().exec(String.format("TASKKILL /IM %s", process)).waitFor();
	}

	@SneakyThrows
	public static ArrayList<String> listProcesses() {
		final Process process = Runtime.getRuntime().exec("TASKLIST /V /FO CSV /FI \"Status eq Running\" /NH");
		return new ArrayList<>(Arrays.stream(IOUtils.toString(process.getInputStream())
						.split("\n"))
				.map(s -> s.split(","))
				.map(strings -> strings[0])
				.map(s -> s.substring(1, s.length() - 1))
				.toList());
	}
}
