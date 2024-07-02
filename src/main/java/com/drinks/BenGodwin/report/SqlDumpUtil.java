package com.drinks.BenGodwin.report;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class SqlDumpUtil {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${backup.directory}")
    private String backupDirectory;

    public void createSqlDump(String tableName) {
        if (!ensureBackupDirectoryExists()) {
            log.error("Backup directory does not exist and could not be created: {}", backupDirectory);
            return;
        }

        String databaseName = getDatabaseNameFromUrl(dbUrl);
        String backupFilePath = backupDirectory + "/" + tableName + ".sql";

        try {
            String[] command = {
                    "mysqldump",
                    "--protocol=tcp",
                    "--host=localhost",
                    "--port=45645",
                    "--user=" + dbUsername,
                    "--password=" + dbPassword,
                    "--single-transaction",
                    "--quick",
                    databaseName,
                    tableName,
                    "--result-file=" + backupFilePath
            };
            log.info("Running command: {}", String.join(" ", command));

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            processBuilder.environment().put("MYSQL_PWD", dbPassword);

            Process process = processBuilder.start();
            int processComplete = process.waitFor();

            if (processComplete == 0) {
                log.info("SQL dump for table {} created successfully at {}", tableName, backupFilePath);
            } else {
                log.error("Failed to create SQL dump for table {} at {}. Process exited with code {}", tableName, backupFilePath, processComplete);
                logProcessErrorStream(process);
            }
        } catch (IOException | InterruptedException e) {
            log.error("Error creating SQL dump for table {}", tableName, e);
        }
    }

    private boolean ensureBackupDirectoryExists() {
        File directory = new File(backupDirectory);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                log.info("Backup directory created: {}", backupDirectory);
                return true;
            } else {
                log.error("Failed to create backup directory: {}", backupDirectory);
                return false;
            }
        }
        return true;
    }

    private String getDatabaseNameFromUrl(String dbUrl) {
        // Extract database name from the JDBC URL
        String[] parts = dbUrl.split("/");
        return parts[parts.length - 1].split("\\?")[0];
    }

    private void logProcessErrorStream(Process process) {
        try (var reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                log.error(line);
            }
        } catch (IOException e) {
            log.error("Error reading process error stream", e);
        }
    }
}