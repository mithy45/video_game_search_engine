package fr.lernejo.fileinjector;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

class LauncherTest {

    @Test
    void main_terminates_before_5_sec() {
        assertTimeoutPreemptively(
                Duration.ofSeconds(0),
                () -> Launcher.main(new String[]{}));
    }
}
