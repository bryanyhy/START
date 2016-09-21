package ambiesoft.start;

import org.junit.Test;
import org.mockito.Mockito;

import ambiesoft.start.presenter.fragment.EditProfileFragmentPresenter;

import static org.junit.Assert.assertEquals;

/**
 * Created by Bryanyhy on 21/9/2016.
 */
public class EditProfileFragmentPresenterTest {

    private EditProfileFragmentPresenter epfp;

    @Test
    public void testIfNotEmptyOrBlankUsernameReturnTrue() {
        epfp = Mockito.spy(new EditProfileFragmentPresenter());
        Mockito.when(epfp.getUsername()).thenReturn("Bruno");
        assertEquals(true, epfp.validInput(epfp.getUsername()));
    }

    @Test
    public void testIfEmptyUsernameReturnFalse() {
        epfp = Mockito.spy(new EditProfileFragmentPresenter());
        Mockito.when(epfp.getUsername()).thenReturn("");
        assertEquals(false, epfp.validInput(epfp.getUsername()));
    }

    @Test
    public void testIfUsernameWithSpaceOnlyReturnFalse() {
        epfp = Mockito.spy(new EditProfileFragmentPresenter());
        Mockito.when(epfp.getUsername()).thenReturn("     ");
        assertEquals(false, epfp.validInput(epfp.getUsername()));
    }

}
