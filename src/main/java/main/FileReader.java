package main;

import io.methvin.watcher.DirectoryChangeEvent;
import io.methvin.watcher.DirectoryWatcher;
import lombok.Getter;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class FileReader {

	private static final String userHomeDir = System.getProperty("user.home");
	private static final Path programPath = Path.of(userHomeDir).resolve("Documents").resolve("My Games").resolve("ProgramBlocker");
	@Getter
	private static final Observable<ArrayList<String>> programs = new Observable<>(new ArrayList<>());
	@Getter
	private static final Observable<String> hours = new Observable<>(null);

	@SneakyThrows
	public static void setupReader() {
		Files.createDirectories(programPath);
		if (!Files.exists(programPath.resolve("blockTimes.txt")))
			Files.createFile(programPath.resolve("blockTimes.txt"));
		if (!Files.exists(programPath.resolve("blockedExes.txt")))
			Files.createFile(programPath.resolve("blockedExes.txt"));
		hours.set(readHours());
		programs.set(readApplications());
		setupWatcher().watchAsync();
	}

	@SneakyThrows
	private static DirectoryWatcher setupWatcher() {
		return DirectoryWatcher
				.builder()
				.path(programPath)
				.listener((event) -> {
					if (event.eventType() != DirectoryChangeEvent.EventType.MODIFY) return;
					if (event.path().equals(programPath.resolve("blockTimes.txt"))) {
						Main.display.addLog("Rereading hours");
						hours.set(readHours());
					} else if (event.path().equals(programPath.resolve("blockedExes.txt"))) {
						Main.display.addLog("Rereading applications");
						programs.set(readApplications());
					}
				})
				.build();
	}

	@SneakyThrows
	public static String readHours() {
		final Path path = programPath.resolve("blockTimes.txt");
		Main.display.addLog(String.format("Reading hours from %s", path));
		return Files.readString(path);
	}

	@SneakyThrows
	private static ArrayList<String> readApplications() {
		final Path path = programPath.resolve("blockedExes.txt");
		Main.display.addLog(String.format("Reading blocked applications from %s", path));
		return new ArrayList<>(Files.readAllLines(path).stream().map(String::trim).toList());
	}
}
