package gui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArchivistTest {

    @Test
    void main() {
        String[] input1 = new String[1];
        input1[0] = "-h";

        String[] input2 = new String[1];
        input2[0] = "-v";
        Archivist.main(input1);
        Archivist.main(input2);
    }
}