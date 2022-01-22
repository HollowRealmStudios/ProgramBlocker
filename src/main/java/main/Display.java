package main;

import com.google.common.io.Resources;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Display extends JFrame {

	private final TextArea textArea = new TextArea();
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	@SuppressWarnings("UnstableApiUsage")
	@SneakyThrows
	public Display() {
		super("Program Blocker");
		setIconImage(ImageIO.read(Resources.getResource("icon.png")));
		setSize(600, 800);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setVisible(true);
		add(textArea);
	}

	public void addLog(String message) {
		textArea.append(String.format("[%s] %s%n", LocalTime.now().format(formatter), message));
	}
}
