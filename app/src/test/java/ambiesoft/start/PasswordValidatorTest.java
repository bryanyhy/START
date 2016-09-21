package ambiesoft.start;

import org.junit.Test;

import ambiesoft.start.model.utility.PasswordValidator;

import static org.junit.Assert.assertEquals;

/**
 * Created by Bryanyhy on 21/9/2016.
 */
public class PasswordValidatorTest {

    private PasswordValidator pv = new PasswordValidator();

    @Test
    public void testIfPasswordWithoutDigitShouldReturnFalse() {
        String password = "Qweasdzxc";
        assertEquals(false, pv.validate(password));
    }

    @Test
    public void testIfPasswordWithoutLowerCaseLetterShouldReturnFalse() {
        String password = "QWEASD123";
        assertEquals(false, pv.validate(password));
    }

    @Test
    public void testIfPasswordWithoutCapitalLetterShouldReturnFalse() {
        String password = "qweasd123";
        assertEquals(false, pv.validate(password));
    }

    @Test
    public void testIfPasswordShorterThan6CharactersShouldReturnFalse() {
        String password = "1Qwe2";
        assertEquals(false, pv.validate(password));
    }

    @Test
    public void testIfPasswordLongerThan20CharactersShouldReturnFalse() {
        String password = "1234567890Qwertyuiopp";
        assertEquals(false, pv.validate(password));
    }

    @Test
    public void testIfValidPasswordShouldReturnTrue() {
        String password1 = "Qwe1as";
        String password2 = "1234Qa";
        String password3 = "1234567890Qwertyuiop";
        assertEquals(true, pv.validate(password1));
        assertEquals(true, pv.validate(password2));
        assertEquals(true, pv.validate(password3));
    }
}
