package riscv;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegisterNamingConverterTest {

    @Test
    void toAbi() {
        assertEquals("zero", RegisterNamingConverter.toAbi(0));
        assertEquals("t1", RegisterNamingConverter.toAbi(6));
        assertEquals("t3", RegisterNamingConverter.toAbi(28));
        assertEquals("a3", RegisterNamingConverter.toAbi(13) );
        assertEquals("s0", RegisterNamingConverter.toAbi(8));
        assertThrows(AssertionError.class, () -> RegisterNamingConverter.toAbi(33));
        assertThrows(AssertionError.class, () -> RegisterNamingConverter.toAbi(-1));
    }

    @Test
    void CSRtoAbi() {
        assertEquals("fflags", RegisterNamingConverter.CSRtoAbi(0x001));
        assertEquals("time", RegisterNamingConverter.CSRtoAbi(0xC01));
        assertEquals("timeh", RegisterNamingConverter.CSRtoAbi(0xC81));
        assertThrows(AssertionError.class, () -> RegisterNamingConverter.toAbi(33));
        assertThrows(AssertionError.class, () -> RegisterNamingConverter.toAbi(-1));
    }

    @Test
    void compressedToAbi() {
        for (int i = 0; i < 8; i++) {
            assertEquals("x" + (i + 8), RegisterNamingConverter.compressedToAbi(i));
        }
        assertThrows(AssertionError.class, () -> RegisterNamingConverter.compressedToAbi(9));
        assertThrows(AssertionError.class, () -> RegisterNamingConverter.compressedToAbi(11));
        assertThrows(AssertionError.class, () -> RegisterNamingConverter.compressedToAbi(-1));
    }
}