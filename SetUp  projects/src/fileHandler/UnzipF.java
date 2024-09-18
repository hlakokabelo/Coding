package fileHandler;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class UnzipF {
	public static void unzip(final ZipFile source, final File destination) throws IOException {
		for (final ZipEntry entry : Collections.list(source.entries())) {
			unzip(source, entry, destination);
		}
	}

	private static void unzip(final ZipFile source, final ZipEntry entry, final File destination) throws IOException {
		if (!entry.isDirectory()) {
			final File resource = new File(destination, entry.getName());
			if (!resource.getCanonicalPath().startsWith(destination.getCanonicalPath() + File.separator)) {
				throw new IOException("Entry is outside of the target dir: " + entry);
			}

			final File folder = resource.getParentFile();
			if (!folder.exists()) {
				if (!folder.mkdirs()) {
					throw new IOException();
				}
			}

			try (final BufferedInputStream input = new BufferedInputStream(source.getInputStream(entry));
					final BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(resource))) {
				output.write(input.readAllBytes());
				output.flush();
			}
		}
	}

	public static void unzip(final String file) throws IOException {
		final File source = new File(file);
		unzip(new ZipFile(source),
				new File(source.getParent(), source.getName().substring(0, source.getName().lastIndexOf('.'))));
	}

	public static void unzip(final String source, final String destination) throws IOException {
		unzip(new File(source), new File(destination));
	}

	public static void unzip(final File source, final File destination) throws IOException {
		unzip(new ZipFile(source), destination);
	}
}
