package ambiesoft.start;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.Assert.*;

import ambiesoft.start.presenter.fragment.CreatePerformanceFragmentPresenter;
import ambiesoft.start.view.fragment.CreatePerformanceFragment;

/**
 * Created by Bryanyhy on 2/9/2016.
 */
public class CreatePerformanceFragmentPresenterTest {

    private CreatePerformanceFragmentPresenter cpfp;

    @Test
    public void testIfAllValidInputReturnTrue() {
        cpfp = Mockito.spy(new CreatePerformanceFragmentPresenter());
        Mockito.when(cpfp.getName()).thenReturn("Band show");
        Mockito.when(cpfp.getDesc()).thenReturn("Best show in CBD!");
        Mockito.when(cpfp.getSelectedCategory()).thenReturn("Instrument");
        Mockito.when(cpfp.getSelectedAddress()).thenReturn("322 Little Bourke Street, Melbourne Victoria 3000, Australia");
        Mockito.when(cpfp.getSelectedLat()).thenReturn(-37.813243);
        Mockito.when(cpfp.getSelectedLng()).thenReturn(144.962762);
        Mockito.when(cpfp.getSelectedDate()).thenReturn("02/09/2016");
        Mockito.when(cpfp.getSelectedSTime()).thenReturn("21:24");
        Mockito.when(cpfp.getSelectedDuration()).thenReturn(30);
        assertEquals(true, cpfp.validInput());
    }

    @Test
    public void testIfNoNameInputReturnFalse() {
        cpfp = Mockito.spy(new CreatePerformanceFragmentPresenter());
        Mockito.when(cpfp.getName()).thenReturn(null);
        Mockito.when(cpfp.getDesc()).thenReturn("Best show in CBD!");
        Mockito.when(cpfp.getSelectedCategory()).thenReturn("Instrument");
        Mockito.when(cpfp.getSelectedAddress()).thenReturn("322 Little Bourke Street, Melbourne Victoria 3000, Australia");
        Mockito.when(cpfp.getSelectedLat()).thenReturn(-37.813243);
        Mockito.when(cpfp.getSelectedLng()).thenReturn(144.962762);
        Mockito.when(cpfp.getSelectedDate()).thenReturn("02/09/2016");
        Mockito.when(cpfp.getSelectedSTime()).thenReturn("21:24");
        Mockito.when(cpfp.getSelectedDuration()).thenReturn(30);
        assertEquals(false, cpfp.validInput());
    }

    @Test
    public void testIfEmptyNameInputReturnFalse() {
        cpfp = Mockito.spy(new CreatePerformanceFragmentPresenter());
        Mockito.when(cpfp.getName()).thenReturn("   ");
        Mockito.when(cpfp.getDesc()).thenReturn("Best show in CBD!");
        Mockito.when(cpfp.getSelectedCategory()).thenReturn("Instrument");
        Mockito.when(cpfp.getSelectedAddress()).thenReturn("322 Little Bourke Street, Melbourne Victoria 3000, Australia");
        Mockito.when(cpfp.getSelectedLat()).thenReturn(-37.813243);
        Mockito.when(cpfp.getSelectedLng()).thenReturn(144.962762);
        Mockito.when(cpfp.getSelectedDate()).thenReturn("02/09/2016");
        Mockito.when(cpfp.getSelectedSTime()).thenReturn("21:24");
        Mockito.when(cpfp.getSelectedDuration()).thenReturn(30);
        assertEquals(false, cpfp.validInput());
    }

    @Test
    public void testIfNoDescInputReturnFalse() {
        cpfp = Mockito.spy(new CreatePerformanceFragmentPresenter());
        Mockito.when(cpfp.getName()).thenReturn("Band show");
        Mockito.when(cpfp.getDesc()).thenReturn(null);
        Mockito.when(cpfp.getSelectedCategory()).thenReturn("Instrument");
        Mockito.when(cpfp.getSelectedAddress()).thenReturn("322 Little Bourke Street, Melbourne Victoria 3000, Australia");
        Mockito.when(cpfp.getSelectedLat()).thenReturn(-37.813243);
        Mockito.when(cpfp.getSelectedLng()).thenReturn(144.962762);
        Mockito.when(cpfp.getSelectedDate()).thenReturn("02/09/2016");
        Mockito.when(cpfp.getSelectedSTime()).thenReturn("21:24");
        Mockito.when(cpfp.getSelectedDuration()).thenReturn(30);
        assertEquals(false, cpfp.validInput());
    }

    @Test
    public void testIfEmptyDescInputReturnFalse() {
        cpfp = Mockito.spy(new CreatePerformanceFragmentPresenter());
        Mockito.when(cpfp.getName()).thenReturn("Band show");
        Mockito.when(cpfp.getDesc()).thenReturn("     ");
        Mockito.when(cpfp.getSelectedCategory()).thenReturn("Instrument");
        Mockito.when(cpfp.getSelectedAddress()).thenReturn("322 Little Bourke Street, Melbourne Victoria 3000, Australia");
        Mockito.when(cpfp.getSelectedLat()).thenReturn(-37.813243);
        Mockito.when(cpfp.getSelectedLng()).thenReturn(144.962762);
        Mockito.when(cpfp.getSelectedDate()).thenReturn("02/09/2016");
        Mockito.when(cpfp.getSelectedSTime()).thenReturn("21:24");
        Mockito.when(cpfp.getSelectedDuration()).thenReturn(30);
        assertEquals(false, cpfp.validInput());
    }

    @Test
    public void testIfNoDateInputReturnFalse() {
        cpfp = Mockito.spy(new CreatePerformanceFragmentPresenter());
        Mockito.when(cpfp.getName()).thenReturn("Band show");
        Mockito.when(cpfp.getDesc()).thenReturn("Best show in CBD!");
        Mockito.when(cpfp.getSelectedCategory()).thenReturn("Instrument");
        Mockito.when(cpfp.getSelectedAddress()).thenReturn("322 Little Bourke Street, Melbourne Victoria 3000, Australia");
        Mockito.when(cpfp.getSelectedLat()).thenReturn(-37.813243);
        Mockito.when(cpfp.getSelectedLng()).thenReturn(144.962762);
        Mockito.when(cpfp.getSelectedDate()).thenReturn(null);
        Mockito.when(cpfp.getSelectedSTime()).thenReturn("21:24");
        Mockito.when(cpfp.getSelectedDuration()).thenReturn(30);
        assertEquals(false, cpfp.validInput());
    }

    @Test
    public void testIfNoStartTimeInputReturnFalse() {
        cpfp = Mockito.spy(new CreatePerformanceFragmentPresenter());
        Mockito.when(cpfp.getName()).thenReturn("Band show");
        Mockito.when(cpfp.getDesc()).thenReturn("Best show in CBD!");
        Mockito.when(cpfp.getSelectedCategory()).thenReturn("Instrument");
        Mockito.when(cpfp.getSelectedAddress()).thenReturn("322 Little Bourke Street, Melbourne Victoria 3000, Australia");
        Mockito.when(cpfp.getSelectedLat()).thenReturn(-37.813243);
        Mockito.when(cpfp.getSelectedLng()).thenReturn(144.962762);
        Mockito.when(cpfp.getSelectedDate()).thenReturn("02/09/2016");
        Mockito.when(cpfp.getSelectedSTime()).thenReturn(null);
        Mockito.when(cpfp.getSelectedDuration()).thenReturn(30);
        assertEquals(false, cpfp.validInput());
    }

    @Test
    public void testIfNoDurationInputReturnFalse() {
        cpfp = Mockito.spy(new CreatePerformanceFragmentPresenter());
        Mockito.when(cpfp.getName()).thenReturn("Band show");
        Mockito.when(cpfp.getDesc()).thenReturn("Best show in CBD!");
        Mockito.when(cpfp.getSelectedCategory()).thenReturn("Instrument");
        Mockito.when(cpfp.getSelectedAddress()).thenReturn("322 Little Bourke Street, Melbourne Victoria 3000, Australia");
        Mockito.when(cpfp.getSelectedLat()).thenReturn(-37.813243);
        Mockito.when(cpfp.getSelectedLng()).thenReturn(144.962762);
        Mockito.when(cpfp.getSelectedDate()).thenReturn("02/09/2016");
        Mockito.when(cpfp.getSelectedSTime()).thenReturn("21:24");
        Mockito.when(cpfp.getSelectedDuration()).thenReturn(0);
        assertEquals(false, cpfp.validInput());
    }

    @Test
    public void testIfNoLocationInputReturnFalse() {
        cpfp = Mockito.spy(new CreatePerformanceFragmentPresenter());
        Mockito.when(cpfp.getName()).thenReturn("Band show");
        Mockito.when(cpfp.getDesc()).thenReturn("Best show in CBD!");
        Mockito.when(cpfp.getSelectedCategory()).thenReturn("Instrument");
        Mockito.when(cpfp.getSelectedAddress()).thenReturn("");
        Mockito.when(cpfp.getSelectedLat()).thenReturn(null);
        Mockito.when(cpfp.getSelectedLng()).thenReturn(null);
        Mockito.when(cpfp.getSelectedDate()).thenReturn("02/09/2016");
        Mockito.when(cpfp.getSelectedSTime()).thenReturn("21:24");
        Mockito.when(cpfp.getSelectedDuration()).thenReturn(30);
        assertEquals(false, cpfp.validInput());
    }


}
