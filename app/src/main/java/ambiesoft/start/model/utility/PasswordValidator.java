package ambiesoft.start.model.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Bryanyhy on 13/9/2016.
 */
public class PasswordValidator {
    private Pattern pattern;
    private Matcher matcher;

    private static final String PASSWORD_PATTERN =
            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,20})";

    public PasswordValidator(){
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }

    // validate if password is matching the regular expression
    public boolean validate(final String password){

        matcher = pattern.matcher(password);
        return matcher.matches();

    }

}
